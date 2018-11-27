package demo.events;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zacconding
 * @Date 2018-11-27
 * @GitHub : https://github.com/zacscoding
 */
public class Publisher {

    private List<Subscriber> subscribers;

    public Publisher() {
        this.subscribers = new LinkedList<>();
    }

    public void register(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void fire(String message) {
        for (Subscriber subscriber : subscribers) {
            subscriber.receive(message);
        }
    }
}
