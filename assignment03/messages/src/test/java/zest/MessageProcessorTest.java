package zest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;

public class MessageProcessorTest {
    private MessageService mockMessageService;
    private MessageProcessor messageProcessor;

    @BeforeEach
    void init() {
        mockMessageService = Mockito.mock(MessageService.class);
        messageProcessor = new MessageProcessor(mockMessageService);
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

        verify(mockMessageService, times(1)).sendMessage(anyString(), anyString());
    }

    @Test
    void multipleMessages() {
        List<Message> messages = List.of(
                new Message("Bob", "Alice", "Hey Alice!"),
                new Message("Alice", "Bob", "Hello Bob!")
        );
        messageProcessor.processMessages(messages);

        verify(mockMessageService, times(2)).sendMessage(anyString(), anyString());
    }

    @Test
    void duplicateMessages() {
        List<Message> messages = List.of(
                new Message("Bob", "Alice", "Hey Alice!"),
                new Message("Bob", "Alice", "Hey Alice!")
        );
        messageProcessor.processMessages(messages);

        verify(mockMessageService, times(2)).sendMessage(anyString(), anyString());
    }
}
