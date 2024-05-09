package zest;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EShopTest {
    // Setup an order
    private String orderId = "Big order";
    private Double amount = 100.0;
    private Order completeOrder = new Order(orderId, amount);

    // Setup the event publisher
    private EventPublisher publisher = new EventPublisher();

    // Setup mocked services
    private EmailNotificationService emailService = mock(EmailNotificationService.class);
    private InventoryManager inventoryManager = mock(InventoryManager.class);

    @Test
    void correctNoOfIncovations() {
        // Setup subscriptions and place order
        publisher.subscribe(emailService);
        publisher.subscribe(inventoryManager);
        publisher.publishOrderToAllListeners(completeOrder);

        // Verify that the onOrderPlaced was called for both services
        verify(emailService, times(1)).onOrderPlaced(completeOrder);
        verify(inventoryManager, times(1)).onOrderPlaced(completeOrder);
    }

    @Test
    void correctContentOfOrders() {
        // Setup subscriptions and place order
        publisher.subscribe(emailService);
        publisher.subscribe(inventoryManager);
        publisher.publishOrderToAllListeners(completeOrder);

        // Verify that the onOrderPlaced was called for both services
        verify(emailService, times(1)).onOrderPlaced(completeOrder);
        verify(inventoryManager, times(1)).onOrderPlaced(completeOrder);

        // Verify that the content of each calls matches the expected order
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
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
    void correctContentOfInvocations() {
        /**
         * Instead of using `ArgumentCaptor`, you could increase the observability of one or more classes to achieve the same goal.
         * Implement the necessary code for increasing the observability and write additional test(s) to
         * test whether the content of the messages is as expected.
         */
    }


}