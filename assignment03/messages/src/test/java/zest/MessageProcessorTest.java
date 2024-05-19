package zest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class MessageProcessorTest {
    private MessageService mockMessageService;
    private StubMessageListener stubListener;
    private MessageProcessor messageProcessor;
    private ArgumentCaptor<String> receiverCaptor;
    private ArgumentCaptor<String> contentCaptor;

    @BeforeEach
    void init() {
        mockMessageService = Mockito.mock(MessageService.class);
        stubListener = new StubMessageListener();
        messageProcessor = new MessageProcessor(mockMessageService, stubListener);
        receiverCaptor = ArgumentCaptor.forClass(String.class);
        contentCaptor = ArgumentCaptor.forClass(String.class);
    }

    @Test
    void nullList() {
        assertThrows(IllegalArgumentException.class, () -> messageProcessor.processMessages(null));
        verify(mockMessageService, never()).sendMessage(any(), any());  // w/o ArgumentCaptor any() can be used
    }

    @Test
    void emptyList() {
        List<Message> messages = List.of();
        messageProcessor.processMessages(messages);
        verify(mockMessageService, never()).sendMessage(any(), any());  // w/o ArgumentCaptor any() can be used
    }

    @Test
    void singleMessage() {
        List<Message> messages = List.of(
                new Message("Bob", "Alice", "Hey Alice!")
        );
        messageProcessor.processMessages(messages);
        verifyReceiverContentSingleMessage(messages);
    }

    @Test
    void multipleMessages() {
        List<Message> messages = List.of(
                new Message("Bob", "Alice", "Hey Alice!"),
                new Message("Alice", "Bob", "Hello Bob!")
        );
        messageProcessor.processMessages(messages);
        verifyReceiverContentTwoPlusMessages(2, messages);
    }

    @Test
    void duplicateMessages() {
        List<Message> messages = List.of(
                new Message("Bob", "Alice", "Hey Alice!"),
                new Message("Bob", "Alice", "Hey Alice!")
        );
        messageProcessor.processMessages(messages);
        verifyReceiverContentTwoPlusMessages(2, messages);
    }

    private void verifyReceiverContentSingleMessage(List<Message> messages) {
        // B. ArgumentCaptor
        verify(mockMessageService, times(1)).sendMessage(receiverCaptor.capture(), contentCaptor.capture());
        String receiver = receiverCaptor.getValue();
        String content = contentCaptor.getValue();
        assertEquals(messages.get(0).getReceiver(), receiver);
        assertEquals(messages.get(0).getContent(), content);

        // C. Observability
        assertEquals(1, stubListener.loggedMessages.size());
        assertEquals(messages.get(0).getReceiver(), stubListener.loggedMessages.get(0).getReceiver());
        assertEquals(messages.get(0).getContent(), stubListener.loggedMessages.get(0).getContent());
    }

    private void verifyReceiverContentTwoPlusMessages(int numberMessages, List<Message> messages) {
        // B. ArgumentCaptor
        verify(mockMessageService, times(numberMessages)).sendMessage(receiverCaptor.capture(), contentCaptor.capture());
        List<String> receiver = receiverCaptor.getAllValues();
        List<String> content = contentCaptor.getAllValues();
        for (int i = 0; i < messages.size(); i++) {
            assertEquals(messages.get(i).getReceiver(), receiver.get(i));
            assertEquals(messages.get(i).getContent(), content.get(i));
        }

        // C. Observability
        assertEquals(numberMessages, stubListener.loggedMessages.size());
        for (int i = 0; i < messages.size(); i++) {
            assertEquals(messages.get(i).getReceiver(), stubListener.loggedMessages.get(i).getReceiver());
            assertEquals(messages.get(i).getContent(), stubListener.loggedMessages.get(i).getContent());
        }
    }

    private static class StubMessageListener implements MessageListener {
        List<Message> loggedMessages = new ArrayList<>();
        @Override
        public void logSentMessage(String receiver, String content) {
            loggedMessages.add(new Message("", receiver, content));
        }
    }
}