//package raftdemo.count;
//
//import com.alipay.remoting.AsyncContext;
//import com.alipay.remoting.BizContext;
//import com.alipay.remoting.exception.CodecException;
//import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
//import com.alipay.sofa.jraft.Closure;
//import com.alipay.sofa.jraft.Status;
//import com.alipay.sofa.jraft.entity.Task;
//import java.nio.ByteBuffer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class IncrementAndGetRequestProcessor extends AsyncUserProcessor<IncrementAndGetRequest> {
//
//    private static final Logger logger = LoggerFactory.getLogger(IncrementAndGetRequestProcessor.class);
//
//    private CounterServer counterServer;
//
//    public IncrementAndGetRequestProcessor(CounterServer counterServer) {
//        this.counterServer = counterServer;
//    }
//
//    @Override
//    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, IncrementAndGetRequest request) {
//　　　　　// Generate a redirect request if the node is not the leader.
//
////        if (!counterServer.getFsm().isLeader()) {
////            asyncCtx.sendResponse(counterServer.redirect());
////            return;
////        }
//
//        // Create a response callback.
//        ValueResponse response = new ValueResponse();
//        IncrementAndAddClosure closure = new IncrementAndAddClosure(counterServer, request, response, new Closure() {
//            @Override
//            public void run(Status status) {
//                // Responses
//                if (!status.isOk()) {
//                    // Return the error message if the request fails.
//                    response.setErrorMsg(status.getErrorMsg());
//                    response.setSuccess(false);
//                }
//                // Return the ValueResponse if the request is successful.
//                asyncCtx.sendResponse(response);
//            }
//        });
//
////        try {
////            // Create a task.
////            Task task = new Task();
////            task.setDone(closure); // Set the callback
////            // Serialize the request by using Codecs.Hessian2, and fill in the serialized data in the data field.
////
////            task.setData(ByteBuffer.wrap(Codecs.getSerializer(Codecs.Hessian2).encode(request)));
////
////            // Submit the task to the Raft group.
////            counterServer.getNode().apply(task);
////        } catch (CodecException e) {
////            // Serialization exception
////            logger.error("Fail to encode IncrementAndGetRequest", e);
////            ValueResponse responseObject = response;
////            responseObject.setSuccess(false);
////            responseObject.setErrorMsg(e.getMessage());
////            asyncCtx.sendResponse(responseObject);
////        }
//    }
//
//    @Override
//    public String interest() {
//        return IncrementAndGetRequest.class.getName();
//    }
//}
