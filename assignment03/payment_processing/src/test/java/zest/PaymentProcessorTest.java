package zest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class PaymentProcessorTest {

    private PaymentProcessor processorUnderTest;
    private List<AuditService> mockAuditServices;

    @BeforeEach
    void init() {
        // Initialize the mock audit services
        mockAuditServices = new ArrayList<>();
        mockAuditServices.add(mock(AuditService.class));

        // Initialize the necessary services with mocks
        EventPublisher eventPublisher = new EventPublisher();
        TransactionService mockTransactionService = mock(TransactionService.class);
        FraudDetectionService mockFraudDetectionService = mock(FraudDetectionService.class);

        // Set up the behavior for the fraud detection service
        setupFraudDetectionService(mockFraudDetectionService);

        // Subscribe audit services to the event publisher
        for (AuditService auditService : mockAuditServices) {
            eventPublisher.subscribe(auditService);
        }

        // Initialize the payment processor with the necessary services
        processorUnderTest = new PaymentProcessor(eventPublisher, mockTransactionService, mockFraudDetectionService);
    }

    private void setupFraudDetectionService(FraudDetectionService fraudDetectionService) {
        // Transactions with positive IDs are considered valid
        when(fraudDetectionService.evaluateTransaction(argThat(
                transaction -> transaction != null && transaction.getId() > 0)))
                .thenReturn(true);

        // Transactions with non-positive IDs are considered invalid
        when(fraudDetectionService.evaluateTransaction(argThat(
                transaction -> transaction != null && transaction.getId() <= 0)))
                .thenReturn(false);
    }

    @Test
    void shouldInvokeOnTransactionCompleteForValidTransactions() {
        // Create valid transactions
        Transaction validTransaction1 = new Transaction(1);
        Transaction validTransaction2 = new Transaction(2);

        // Process the valid transactions
        processorUnderTest.processPayment(validTransaction1);
        processorUnderTest.processPayment(validTransaction2);

        // Verify the audit services are called twice
        for (AuditService auditService : mockAuditServices) {
            verify(auditService, times(2)).onTransactionComplete(any(Transaction.class));
        }
    }

    @Test
    void shouldNotInvokeOnTransactionCompleteForInvalidTransactions() {
        // Create invalid transactions
        Transaction invalidTransaction1 = new Transaction(0);
        Transaction invalidTransaction2 = new Transaction(-1);

        // Process the invalid transactions
        processorUnderTest.processPayment(invalidTransaction1);
        processorUnderTest.processPayment(invalidTransaction2);

        // Verify the audit services are not called
        for (AuditService auditService : mockAuditServices) {
            verify(auditService, never()).onTransactionComplete(any(Transaction.class));
        }
    }

    @Test
    void shouldInvokeOnTransactionCompleteForMixedTransactions() {
        // Create mixed transactions
        Transaction validTransaction = new Transaction(1);
        Transaction invalidTransaction = new Transaction(0);

        // Process the mixed transactions
        processorUnderTest.processPayment(validTransaction);
        processorUnderTest.processPayment(invalidTransaction);

        // Verify the audit services are called once
        for (AuditService auditService : mockAuditServices) {
            verify(auditService, times(1)).onTransactionComplete(any(Transaction.class));
        }
    }

    @Test
    void shouldCaptureArgumentsOfCompletedTransactions() {
        // Create transactions
        Transaction validTransaction1 = new Transaction(123);
        Transaction validTransaction2 = new Transaction(1234);
        Transaction invalidTransaction1 = new Transaction(-321);
        Transaction invalidTransaction2 = new Transaction(-4321);
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        // Process transactions
        processorUnderTest.processPayment(validTransaction1);
        processorUnderTest.processPayment(validTransaction2);
        processorUnderTest.processPayment(invalidTransaction1);
        processorUnderTest.processPayment(invalidTransaction2);

        // Capture and assert the valid transactions
        for (AuditService auditService : mockAuditServices) {
            verify(auditService, times(2)).onTransactionComplete(transactionCaptor.capture());

            List<Transaction> capturedTransactions = transactionCaptor.getAllValues();
            assertEquals(2, capturedTransactions.size());
            assertEquals(validTransaction1.getId(), capturedTransactions.get(0).getId());
            assertEquals(validTransaction2.getId(), capturedTransactions.get(1).getId());
        }
    }

    @Test
    void shouldReturnProcessedValidTransactions() {
        // Create valid transactions
        Transaction validTransaction1 = new Transaction(567);
        Transaction validTransaction2 = new Transaction(5678);

        // Process transactions and get the returned transactions
        Transaction returnedTransaction1 = processorUnderTest.processPayment(validTransaction1);
        Transaction returnedTransaction2 = processorUnderTest.processPayment(validTransaction2);

        // Assert the returned transactions are as expected
        assertNotNull(returnedTransaction1);
        assertEquals(validTransaction1.getId(), returnedTransaction1.getId());
        assertNotNull(returnedTransaction2);
        assertEquals(validTransaction2.getId(), returnedTransaction2.getId());
    }

    @Test
    void shouldReturnMixedProcessedTransactions() {
        // Create a valid and an invalid transaction
        Transaction validTransaction = new Transaction(890);
        Transaction invalidTransaction = new Transaction(-98);

        // Process transactions and get the returned transactions
        Transaction returnedValidTransaction = processorUnderTest.processPayment(validTransaction);
        Transaction returnedInvalidTransaction = processorUnderTest.processPayment(invalidTransaction);

        // Assert the valid transaction is returned and the invalid transaction is null
        assertEquals(validTransaction.getId(), returnedValidTransaction.getId());
        assertNull(returnedInvalidTransaction);
    }
}
