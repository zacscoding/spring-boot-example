package raftdemo.count;

import com.alipay.remoting.exception.CodecException;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CounterStateMachine extends StateMachineAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CounterStateMachine.class);

    /**
     * counter value
     */
    private AtomicLong value = new AtomicLong(0);

    @Override
    public void onApply(Iterator iter) {
        // Traverse the log
        while (iter.hasNext()) {
            long delta = 0;

            IncrementAndAddClosure closure = null;
            // If the done callback is not null, it must be called after the log entry application. If it is not null, the current load is the leader.
            if (iter.done() != null) {
                // If the current node is the leader, the delta value can be directly obtained from IncrementAndAddClosure to avoid deserialization.
                closure = (IncrementAndAddClosure) iter.done();
                delta = closure.getRequest().getDelta();
            } else {
                // If another node applies this log entry, it needs to deserialize IncrementAndGetRequest to get the delta value.
                ByteBuffer data = iter.getData();
//                try {
//                    IncrementAndGetRequest request = Codecs.getSerializer(Codecs.Hessian2).decode(data.array(),
//                        IncrementAndGetRequest.class.getName());
//                    delta = request.getDelta();
//                } catch (CodecException e) {
//                    logger.error("Fail to decode IncrementAndGetRequest", e);
//                }
            }
            long prev = this.value.get();
            // Update the state machine.
            long updated = value.addAndGet(delta);
            // Be sure to call done after the update and return the response to the client.
            if (closure != null) {
                closure.getResponse().setValue(updated);
                closure.getResponse().setSuccess(true);
                closure.run(Status.OK());
            }
            logger.info("Added value={} by delta={} at logIndex={}", prev, delta, iter.getIndex());
            iter.next();
        }
    }
}
