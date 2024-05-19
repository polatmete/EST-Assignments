package zest;

import java.util.List;

public class MessageProcessor {

    private final MessageService messageService;
    private final MessageListener messageListener;

    public MessageProcessor(MessageService messageService, MessageListener messageListener) {
        this.messageService = messageService;
        this.messageListener = messageListener;
    }

    private void notifyListener(String receiver, String content) {
        messageListener.logSentMessage(receiver, content);
    }

    public void processMessages(List<Message> messages) {
        if (messages == null) {
            throw new IllegalArgumentException("no messages provided");
        }
        for (Message message : messages) {
            messageService.sendMessage(message.getReceiver(), message.getContent());
            notifyListener(message.getReceiver(), message.getContent());
        }
    }
}
