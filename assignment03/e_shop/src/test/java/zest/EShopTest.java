package zest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EShopTest {
    private EventPublisher publisher;
    private TestMessageListener testListener;
    private EmailNotificationService mockEmailService;
    private InventoryManager mockInventoryManager;
    private ArgumentCaptor<Order> orderCaptor;

    @BeforeEach
    void init() {
        // Setup the event publisher
        testListener = new TestMessageListener();
        publisher = new EventPublisher(testListener);

        // Setup mocked services
        mockEmailService = mock(EmailNotificationService.class);
        mockInventoryManager = mock(InventoryManager.class);

        // Setup ArgumentCaptors
        orderCaptor = ArgumentCaptor.forClass(Order.class);

        // Setup subscriptions and place order
        publisher.subscribe(mockEmailService);
        publisher.subscribe(mockInventoryManager);
    }

    @Test
    void nullOrder() {
        Order order = new Order("Big Order", 100.0);
        assertThrows(NullPointerException.class, () -> publisher.publishOrderToAllListeners(null));
        verify(mockEmailService, never()).onOrderPlaced(order);
        verify(mockInventoryManager, never()).onOrderPlaced(order);
    }

    @Test
    void verifiyNumberOfInvocations() {
        Order order = new Order("Big Order", 100.0);
        publisher.publishOrderToAllListeners(order);

        // Verify that the onOrderPlaced was called for both services
        verify(mockEmailService, times(1)).onOrderPlaced(order);
        verify(mockInventoryManager, times(1)).onOrderPlaced(order);
    }

    @Test
    void verifySingleOrder() {
        Order order = new Order("Big Order", 100.0);
        publisher.publishOrderToAllListeners(order);

        verifyReceiverContentSingleOrder(order);
    }

    @Test
    void verifyMultipleOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order("Big Order", 100.0));
        orders.add(new Order("Second Order", 200.0));
        orders.add(new Order("Third Order", 300.0));

        for (int i = 0; i < orders.size(); i++) {
            publisher.publishOrderToAllListeners(orders.get(i));
        }

        verifyReceiverContentMultipleOrders(3, orders);
    }

    void verifyReceiverContentSingleOrder(Order order) {
        // B. ArgumentCaptor
        verify(mockEmailService).onOrderPlaced(orderCaptor.capture());
        verify(mockInventoryManager).onOrderPlaced(orderCaptor.capture());

        Order capturedMessageService = orderCaptor.getAllValues().get(0);
        Order capturedInventoryManager = orderCaptor.getAllValues().get(1);

        assertEquals(order.getOrderId(), capturedMessageService.getOrderId());
        assertEquals(order.getAmount(), capturedMessageService.getAmount());
        assertEquals(order.getOrderId(), capturedInventoryManager.getOrderId());
        assertEquals(order.getAmount(), capturedInventoryManager.getAmount());

        // C. Observability
        assertEquals(2, testListener.loggedMessages.size());
        for (int i = 0; i < testListener.loggedMessages.size(); i++) {
            assertEquals(order.getOrderId(), testListener.loggedMessages.get(i).getOrderId());
            assertEquals(order.getAmount(), testListener.loggedMessages.get(i).getAmount());
        }
    }

    void verifyReceiverContentMultipleOrders(int numberOfOrders, ArrayList<Order> orders) {
        // B. ArgumentCaptor
        verify(mockEmailService, times(orders.size())).onOrderPlaced(orderCaptor.capture());
        verify(mockInventoryManager, times(orders.size())).onOrderPlaced(orderCaptor.capture());

        List<Order> capturedOrders = orderCaptor.getAllValues();

        for (int i = 0; i < orders.size(); i++) {
            assertEquals(orders.get(i).getOrderId(), capturedOrders.get(i).getOrderId());
            assertEquals(orders.get(i).getAmount(), capturedOrders.get(i).getAmount());
            assertEquals(orders.get(i).getOrderId(), capturedOrders.get(i + numberOfOrders).getOrderId());
            assertEquals(orders.get(i).getAmount(), capturedOrders.get(i + numberOfOrders).getAmount());
        }

        // C. Observability
        assertEquals(numberOfOrders * 2, testListener.loggedMessages.size());

        /**
         * Every order is two times in messages. One from the messageService and once from the inventoryManager.
         * We therefore need to compare the first two messages to the first order, the second two messages to the
         * second order and so on. That is why we divide the iterator for the orders by 2.
         */
        for (int i = 0; i < testListener.loggedMessages.size(); i++) {
            assertEquals(orders.get(i/2).getOrderId(), testListener.loggedMessages.get(i).getOrderId());
            assertEquals(orders.get(i/2).getAmount(), testListener.loggedMessages.get(i).getAmount());
        }
    }

    private static class TestMessageListener implements MessageListener {
        List<Order> loggedMessages = new ArrayList<>();
        @Override
        public void logSentMessage(String orderId, Double amount) {
            loggedMessages.add(new Order(orderId, amount));
        }
    }

}