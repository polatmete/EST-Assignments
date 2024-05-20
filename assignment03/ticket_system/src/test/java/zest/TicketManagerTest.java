package zest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TicketManagerTest {

    private NotificationService notificationService;
    private LogService logService;
    private TicketRepository ticketRepository;
    private TicketManager ticketManager;

    @BeforeEach
    void init() {
        notificationService = mock(NotificationService.class);
        logService = mock(LogService.class);
        ticketRepository = mock(TicketRepository.class);
        ticketManager = new TicketManager(notificationService, logService, ticketRepository);
    }

    @Test
    void testCreateTicket() {
        Ticket ticket = new Ticket("customer@example.com", "Issue description", TicketPriority.NORMAL);

        ticketManager.createTicket(ticket);

        verify(logService).logTicketCreation(ticket);
        verify(notificationService).notifyCustomer(eq(ticket.getCustomerEmail()), anyString());
        verify(ticketRepository).save(ticket);
    }

    @Test
    void testCreateTicketWithNullTicket() {
        assertThrows(IllegalArgumentException.class, () -> ticketManager.createTicket(null));

        verify(logService, never()).logTicketCreation(any(Ticket.class));
        verify(notificationService, never()).notifyCustomer(anyString(), anyString());
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void testCreateTicketWithNotificationFailure() {
        Ticket ticket = new Ticket("customer@example.com", "Issue description", TicketPriority.NORMAL);
        doThrow(new RuntimeException("Notification failure")).when(notificationService).notifyCustomer(anyString(), anyString());

        ticketManager.createTicket(ticket);

        verify(logService).logTicketCreation(ticket);
        verify(notificationService).notifyCustomer(eq(ticket.getCustomerEmail()), anyString());
        verify(ticketRepository).save(ticket);
    }

    @Test
    void testCreateTicketWithLoggingFailure() {
        Ticket ticket = new Ticket("customer@example.com", "Issue description", TicketPriority.NORMAL);
        doThrow(new RuntimeException("Logging failure")).when(logService).logTicketCreation(any(Ticket.class));

        ticketManager.createTicket(ticket);

        verify(logService).logTicketCreation(ticket);
        verify(notificationService).notifyCustomer(eq(ticket.getCustomerEmail()), anyString());
        verify(ticketRepository).save(ticket);
    }
}
