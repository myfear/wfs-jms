package sample;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.config.messaging.activemq.server.Role;
import org.wildfly.swarm.config.messaging.activemq.server.SecuritySetting;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.management.ManagementFraction;
import org.wildfly.swarm.messaging.MessagingFraction;
import org.wildfly.swarm.naming.NamingFraction;

/**
 * @author Markus Eisele
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Container container = new Container();

        container.fraction(MessagingFraction.createDefaultFraction()
                .defaultServer((s) -> {
                    s.securitySetting(new SecuritySetting("#")
                                              .role(new Role("guest")
                                                            .consume(true)
                                                            .send(true)
                                                            .createNonDurableQueue(true)
                                                            .deleteNonDurableQueue(true)
                                              )
                    );
                    s.enableRemote();
                    s.jmsQueue("sample-queue");
                    s.remoteJmsTopic("sample-topic");
                })
        )
                .fraction(new NamingFraction().remoteNamingService())
                .fraction(new ManagementFraction()
                        .securityRealm("ApplicationRealm", (realm) -> {
                            realm.inMemoryAuthentication((authn) -> {
                                authn.add("admin", "password", true);
                            });
                            realm.inMemoryAuthorization((authz) -> {
                               authz.add("admin", "guest");
                            });
                        }));

        container.start();

        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
        deployment.addPackage(Main.class.getPackage());

        container.deploy(deployment);
    }
}
