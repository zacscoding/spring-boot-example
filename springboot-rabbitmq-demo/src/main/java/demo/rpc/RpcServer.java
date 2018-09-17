package demo.rpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author zacconding
 * @Date 2018-08-08
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class RpcServer {

    @RabbitListener(queues = "rpc.requests")
    public int fibonacci(int n) {
        log.info("RabbitListener : {}", Thread.currentThread().getName());
        // int result = (n < 0) ? -1 : fib(n);
        int result = n;

        StringBuilder sb = new StringBuilder("\n// ==================================================\n")
            .append("[[ RPC SERVER ]]\n")
            .append("Thread : ").append(Thread.currentThread().getName()).append("(").append(Thread.currentThread().getId()).append(")\n")
            .append("Receive param : ").append(n).append("\n")
            .append("==> Result : ").append(result).append("\n")
            .append("===================================================== //\n");

        log.info(sb.toString());

        return result;
    }

    private int fib(int n) {
        return (n == 0) ? 0 : n == 1 ? 1 : (fib(n - 1) + fib(n - 2));
    }
}
