package sample;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
//import org.wildfly.swarm.ejb.remote.EJBRemoteFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.management.ManagementFraction;
import org.wildfly.swarm.messaging.MessagingFraction;
import org.wildfly.swarm.remoting.RemotingFraction;

/**
 * @author Markus Eisele
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        container.fraction(MessagingFraction.createDefaultFraction()
                .defaultServer((s) -> {
                    s.jmsQueue("sample-queue");
                    s.jmsTopic("sample-topic");
                })
        )
                .fraction(new RemotingFraction())
                .fraction(new ManagementFraction()
                        .securityRealm("ApplicationRealm", (realm) -> {
                            realm.inMemoryAuthentication((authn) -> {
                                authn.add("admin", "password", true);
                            });

                        }));

        container.start();

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage(Main.class.getPackage());

        container.deploy(deployment);
    }
}
