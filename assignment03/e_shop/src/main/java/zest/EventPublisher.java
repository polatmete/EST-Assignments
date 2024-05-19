package zest;

import java.util.ArrayList;
import java.util.List;

public class EventPublisher {
    private final MessageListener messageListener;
    private List<EventListener> listeners = new ArrayList<>();

    public EventPublisher(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    private void notifyListener(String orderId, Double amount) {
        messageListener.logSentMessage(orderId, amount);
    }

    public void publishOrderToAllListeners(Order order) {
        for (EventListener listener : listeners) {
            listener.onOrderPlaced(order);
            notifyListener(order.getOrderId(), order.getAmount());
        }
    }
}
