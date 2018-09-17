package demo.rpc2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import javax.jms.Message;

/**
 * @author zacconding
 * @Date 2018-09-17
 * @GitHub : https://github.com/zacscoding
 */
public class ReplyMessage {

    private CountDownLatch countDownLatch;
    private Semaphore semaphore;
    private Message message;

    public ReplyMessage() {
        // this.semaphore = new Semaphore(0);
        this.countDownLatch = new CountDownLatch(1);
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
}
