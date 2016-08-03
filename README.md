# WildFly Swarm JAX-RS & Message Driven Bean

This examples uses JAX-RS resource implementations and deploys
them through a user-provided `main()`method.

It also configures a JMS server with two JMS destinations:
Queue with name "sample-queue"
Topic with name "sample-topic"

The `SampleTopicMDB`consumes from the topic.

## Project `pomx.xml`

The project is a normal maven project with `jar` packaging, not `war`.

``` xml
<packaging>jar</packaging>
```

The project adds a `<plugin>` to configure `wildfly-swarm-plugin` to
create the runnable `.jar`.

``` xml
<plugin>
  <groupId>org.wildfly.swarm</groupId>
  <artifactId>wildfly-swarm-plugin</artifactId>
  <version>${version.wildfly-swarm}</version>
  <configuration>
    <mainClass>sample.Main</mainClass>
  </configuration>
  <executions>
    <execution>
      <goals>
        <goal>package</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

To define the needed parts of WildFly Swarm, some dependencies are added

``` xml
<dependency>
  <groupId>org.wildfly.swarm</groupId>
  <artifactId>weld-jaxrs</artifactId>
  <version>${version.wildfly-swarm}</version>
</dependency>
<dependency>
  <groupId>org.wildfly.swarm</groupId>
  <artifactId>messaging</artifactId>
  <version>${version.wildfly-swarm}</version>
</dependency>
<dependency>
  <groupId>org.wildfly.swarm</groupId>
  <artifactId>ejb</artifactId>
  <version>${version.wildfly-swarm}</version>
</dependency>
```

## Project `main()`

This project supplies a `main()` in order to configure the messaging
subsystem and deploy all the pieces of the application.

You can run it:

* mvn package && java -jar ./target/wildfly-swarm-example-messaging-mdb-swarm.jar
* mvn wildfly-swarm:run
* In your IDE run the `org.wildfly.swarm.examples.messaging.mdb.Main` class

## Use

    http://localhost:8080/

The MDB will log the received message to the console:

``` sh
2016-08-03 08:23:16,922 INFO  [sample.SampleTopicMDB] (Thread-117 (ActiveMQ-client-global-threads-1211290936)) received: Test Message
```
