package zest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EShopTest {
    // Setup an order
    String orderId = "Big order";
    Double amount = 100.0;
    Order completeOrder = new Order(orderId, amount);

    // Setup variables
    private EventPublisher publisher;
    private EmailNotificationService emailService;
    private InventoryManager inventoryManager;
    private ArgumentCaptor<Order> orderCaptor;

    @BeforeEach
    void init() {
        // Setup the event publisher
        publisher = new EventPublisher();

        // Setup mocked services
        emailService = mock(EmailNotificationService.class);
        inventoryManager = mock(InventoryManager.class);

        // Setup ArgumentCaptors
        orderCaptor = ArgumentCaptor.forClass(Order.class);

        // Setup subscriptions and place order
        publisher.subscribe(emailService);
        publisher.subscribe(inventoryManager);
    }

    @Test
    void nullOrder() {
        publisher.publishOrderToAllListeners(null);
        verify(emailService, never()).onOrderPlaced(completeOrder);
        verify(inventoryManager, never()).onOrderPlaced(completeOrder);
    }

    @Test
    void publisherCallsCorrectly() {
        publisher.publishOrderToAllListeners(completeOrder);

        // Verify that the onOrderPlaced was called for both services
        verify(emailService, times(1)).onOrderPlaced(completeOrder);
        verify(inventoryManager, times(1)).onOrderPlaced(completeOrder);
    }

    @Test
    void wholeOrderPlacement() {
        publisher.publishOrderToAllListeners(completeOrder);

        // Verify that the content of each calls matches the expected order
        verify(emailService).onOrderPlaced(orderCaptor.capture());
        verify(inventoryManager).onOrderPlaced(orderCaptor.capture());

        // Retrieve the captured orders
        Order capturedService1 = orderCaptor.getAllValues().get(0);
        Order capturedService2 = orderCaptor.getAllValues().get(1);

        // Assert that the captured orders are equal to the original order
        assertEquals(completeOrder, capturedService1);
        assertEquals(completeOrder, capturedService2);
    }

    @Test
    void contentOfOrderPlacements() {
        /**
         * Instead of using `ArgumentCaptor`, you could increase the observability of one or more classes to achieve the same goal.
         * Implement the necessary code for increasing the observability and write additional test(s) to
         * test whether the content of the messages is as expected.
         */
    }


}