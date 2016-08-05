package sample;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;

/**
 * Simple Singleton which sends messages to a queue.
 *
 * @author Markus Eisele
 */
@Singleton
public class SampleQueueTimer {

    private static final Logger LOG = Logger.getLogger(SampleQueueTimer.class.getName());

    @Inject
    JMSContext context;

    @Resource(mappedName = "java:" + SampleResource.MY_QUEUE)
    Queue myQueue;

    @Schedule(second = "*/1", minute = "*", hour = "*", persistent = false)
    public void doWork() {
        String message = "Test " + System.currentTimeMillis();
        LOG.log(Level.INFO, "Send: {0}", message);
        context.createProducer().send(myQueue, message);
    }

}
