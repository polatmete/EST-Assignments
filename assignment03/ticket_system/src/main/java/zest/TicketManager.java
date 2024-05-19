package zest;

// TicketManager class to handle ticket creation and interaction with services
public class TicketManager {
    private final NotificationService notificationService;
    private final LogService logService;
    private final TicketRepository ticketRepository;

    public TicketManager(NotificationService notificationService, LogService logService, TicketRepository ticketRepository) {
        this.notificationService = notificationService;
        this.logService = logService;
        this.ticketRepository = ticketRepository;
    }

    public void createTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }
        // Log the ticket creation
        try {
            logService.logTicketCreation(ticket);
        } catch (Exception e) {
            System.out.println("Error logging ticket creation: " + e.getMessage());
        }

        // Notify the customer
        try {
            notificationService.notifyCustomer(ticket.getCustomerEmail(), "Thank you for your request. Your support ticket has been created and will be processed shortly.");
        } catch (Exception e) {
            System.out.println("Error notifying customer: " + e.getMessage());
        }

        // Save the ticket to the database
        saveTicket(ticket);
    }

    // Method to save ticket to a database
    private void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }
}
