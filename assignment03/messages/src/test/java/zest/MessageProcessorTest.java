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
    private TestMessageListener testListener;
    private MessageProcessor messageProcessor;
    private ArgumentCaptor<String> receiverCaptor;
    private ArgumentCaptor<String> contentCaptor;

    @BeforeEach
    void init() {
        mockMessageService = Mockito.mock(MessageService.class);
        testListener = new TestMessageListener();
        messageProcessor = new MessageProcessor(mockMessageService, testListener);
        receiverCaptor = ArgumentCaptor.forClass(String.class);
        contentCaptor = ArgumentCaptor.forClass(String.class);
    }

    @Test
    void nullList() {
        assertThrows(NullPointerException.class, () -> messageProcessor.processMessages(null));
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

    void verifyReceiverContentSingleMessage(List<Message> messages) {
        // B. ArgumentCaptor
        verify(mockMessageService, times(1)).sendMessage(receiverCaptor.capture(), contentCaptor.capture());
        String receiver = receiverCaptor.getValue();
        String content = contentCaptor.getValue();
        assertEquals(messages.get(0).getReceiver(), receiver);
        assertEquals(messages.get(0).getContent(), content);

        // C. Observability
        assertEquals(1, testListener.loggedMessages.size());
        assertEquals(messages.get(0).getReceiver(), testListener.loggedMessages.get(0).getReceiver());
        assertEquals(messages.get(0).getContent(), testListener.loggedMessages.get(0).getContent());
    }

    void verifyReceiverContentTwoPlusMessages(int numberMessages, List<Message> messages) {
        // B. ArgumentCaptor
        verify(mockMessageService, times(numberMessages)).sendMessage(receiverCaptor.capture(), contentCaptor.capture());
        List<String> receiver = receiverCaptor.getAllValues();
        List<String> content = contentCaptor.getAllValues();
        for (int i = 0; i < messages.size(); i++) {
            assertEquals(messages.get(i).getReceiver(), receiver.get(i));
            assertEquals(messages.get(i).getContent(), content.get(i));
        }

        // C. Observability
        assertEquals(numberMessages, testListener.loggedMessages.size());
        for (int i = 0; i < messages.size(); i++) {
            assertEquals(messages.get(i).getReceiver(), testListener.loggedMessages.get(i).getReceiver());
            assertEquals(messages.get(i).getContent(), testListener.loggedMessages.get(i).getContent());
        }
    }

    private static class TestMessageListener implements MessageListener {
        List<Message> loggedMessages = new ArrayList<>();
        @Override
        public void logSentMessage(String receiver, String content) {
            loggedMessages.add(new Message("", receiver, content));
        }
    }
}