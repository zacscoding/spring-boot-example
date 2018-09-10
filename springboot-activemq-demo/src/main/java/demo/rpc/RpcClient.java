//package demo.rpc;
//
//import java.util.UUID;
//import javax.jms.Destination;
//import javax.jms.JMSException;
//import javax.jms.MessageConsumer;
//import javax.jms.Session;
//import javax.jms.TextMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Profile;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.core.SessionCallback;
//import org.springframework.jms.support.destination.DestinationResolver;
//import org.springframework.stereotype.Component;
//
///**
// * @author zacconding
// * @Date 2018-09-10
// * @GitHub : https://github.com/zacscoding
// */
//@Profile("rpc")
//@Slf4j
//@Component
//public class RpcClient {
//
//    private JmsTemplate jmsTemplate;
//
//    @Autowired
//    private RpcClient(JmsTemplate jmsTemplate) {
//        this.jmsTemplate = jmsTemplate;
//    }
//
//    /*public JsonRpcResponse request(JsonRpcRequest request) {
//
//    }*/
//
//    private static class RpcProducerConsumer implements SessionCallback<JsonRpcResponse> {
//
//        private DestinationResolver destinationResolver;
//
//        @Override
//        public JsonRpcResponse doInJms(Session session) throws JMSException {
//            /*MessageConsumer consumer = null;
//            MessageProducer producer = null;
//            try {
//                final String correlationId = UUID.randomUUID().toString();
//                final Destination requestQueue =
//                    destinationResolver.resolveDestinationName( session, queue+".request", false );
//                final Destination replyQueue =
//                    destinationResolver.resolveDestinationName( session, queue+".response", false );
//                // Create the consumer first!
//                consumer = session.createConsumer( replyQueue, "JMSCorrelationID = '" + correlationId + "'" );
//                final TextMessage textMessage = session.createTextMessage( msg );
//                textMessage.setJMSCorrelationID( correlationId );
//                textMessage.setJMSReplyTo( replyQueue );
//                // Send the request second!
//                producer = session.createProducer( requestQueue );
//                producer.send( requestQueue, textMessage );
//                // Block on receiving the response with a timeout
//                return consumer.receive( TIMEOUT );
//            }
//            finally {
//                // Don't forget to close your resources
//                JmsUtils.closeMessageConsumer( consumer );
//                JmsUtils.closeMessageProducer( producer );
//            }*/
//            try {
//                final String correlationId = UUID.randomUUID().toString();
//                final Destination requestQueue = destinationResolver.resolveDestinationName(session, "json-rpc-request", false);
//                final Destination replyQueue = destinationResolver.resolveDestinationName(session, "json-rpc-response", false);
//
//                MessageConsumer consumer = session.createConsumer( replyQueue, "JMSCorrelationID = '" + correlationId + "'" );
//                final TextMessage textMessage = session.createTextMessage(msg);
//
//
//
//            }
//
//            return null;
//        }
//    }
//}