package New.Shared;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

@SuppressWarnings("Duplicates")
public class Connector {
    // JMS server URL, default broker URL is tcp://localhost:61616
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    // Queue name
    private static String subject = "JCG_QUEUE";

    Session session;
    Destination destination;

    MessageConsumer consumer;

    // Listener to notify when a message has been received
    private IManualListener listener;

    public Connector(String queueName, IManualListener listener) {
        try {
            subject = queueName;
            this.listener = listener;
            connect();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws JMSException {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");

        // Getting JMS connection from the server and starting it
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Creating a non transactional session to send/receive JMS message
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //Destination represents here our queue 'JCG_QUEUE' on the JMS server.
        //The queue will be created automatically on the server.
        destination = session.createQueue(subject);

        // MessageConsumer is used for receiving (consuming) messages
        consumer = session.createConsumer(destination);

        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message msg) {
                try {
                    receiveGenericMessage(msg);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void sendMessage(CustomMessage message) throws JMSException {
        // MessageProducer is used for sending messages to the queue.
        MessageProducer producer = session.createProducer(destination);

        // We will send a small text message saying 'Hello World!!!'
        ObjectMessage oMessage = session.createObjectMessage(message);

        // Here we are sending our message!
        producer.send(oMessage);

        System.out.println("Sent message: " + message.toString());
    }

    public void receiveGenericMessage(Message msg) throws JMSException {
        // Here we receive the message.
        Message message = msg;
        System.out.println("Message received OEI: " + msg.toString());

        // Now to determine what kind of message this is
        if (message instanceof ObjectMessage) {
            ObjectMessage oMessage = (ObjectMessage) message;
            CustomMessage customMessage = (CustomMessage) oMessage.getObject();
            System.out.println("Type: " + customMessage.type.toString());
            switch (customMessage.type) {
                case Undefined:
                    // ...
                    break;

                case ProductSearch:
                    // Message is a MessageProductSearch object
                    listener.onProductSearchReceived((MessageProductSearch) customMessage);
                    break;

                case ProductResult:
                    // Message is a MessageProductResult object
                    listener.onProductResultReceived((MessageProductResult) customMessage);
                    break;
            }
        }
    }
}
