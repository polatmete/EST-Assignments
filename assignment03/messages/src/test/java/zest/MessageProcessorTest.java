package zest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

public class MessageProcessorTest {
    private MessageService mockMessageService;
    private MessageProcessor messageProcessor;
    private ArgumentCaptor<String> receiverCaptor;
    private ArgumentCaptor<String> contentCaptor;

    @BeforeEach
    void init() {
        mockMessageService = Mockito.mock(MessageService.class);
        messageProcessor = new MessageProcessor(mockMessageService);
        receiverCaptor = ArgumentCaptor.forClass(String.class);
        contentCaptor = ArgumentCaptor.forClass(String.class);
    }

    @Test
    void nullList() {
        assertThrows(NullPointerException.class, () -> messageProcessor.processMessages(null));
        verify(mockMessageService, never()).sendMessage(anyString(), anyString());
    }

    @Test
    void emptyList() {
        List<Message> messages = List.of();
        messageProcessor.processMessages(messages);

        verify(mockMessageService, never()).sendMessage(anyString(), anyString());
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
        verify(mockMessageService, times(1)).sendMessage(receiverCaptor.capture(), contentCaptor.capture());

        String receiver = receiverCaptor.getValue();
        String content = contentCaptor.getValue();

        assertEquals(messages.get(0).getReceiver(), receiver);
        assertEquals(messages.get(0).getContent(), content);
    }

    void verifyReceiverContentTwoPlusMessages(int numberMessages, List<Message> messages) {
        verify(mockMessageService, times(numberMessages)).sendMessage(receiverCaptor.capture(), contentCaptor.capture());

        List<String> receiver = receiverCaptor.getAllValues();
        List<String> content = contentCaptor.getAllValues();

        for (int i = 0; i < messages.size(); i++) {
            assertEquals(messages.get(i).getReceiver(), receiver.get(i));
            assertEquals(messages.get(i).getContent(), content.get(i));
        }
    }
}