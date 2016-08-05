package sample;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * JAX-RS to send a message to the sample-queue.
 *
 * @author Markus Eisele
 */
@ApplicationScoped
@Path("/")
public class SampleResource {

    public static final String MY_TOPIC = "/jms/topic/sample-topic";
    public static final String MY_QUEUE = "/jms/queue/sample-queue";

    private static final String TEST_MESSAGE = "Test Message";

    @Inject
    private JMSContext context;

    @Resource(lookup = MY_TOPIC)
    private Topic topic;

    @GET
    @Produces("text/plain")
    public String get() {
        context.createProducer().send(topic, TEST_MESSAGE);
        return "Send " + TEST_MESSAGE + "to mdb!";
    }

}
