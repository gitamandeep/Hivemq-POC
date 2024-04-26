package org.example;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;

import java.util.Scanner;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;
public class Main {
    public static void main(String[] args) {
        System.out.println("Connecting to server ....");
        final String host = "bdf87bc530f0XXXXXXXX.s1.eu.hivemq.cloud";
        final String username = "<usrnme>";
        final String password = "<pswd>";

        /**
         * Building the client with ssl.
         */
        final Mqtt5BlockingClient client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(host)
                .serverPort(8884)
                .sslWithDefaultConfig()
                .webSocketConfig()
                .serverPath("mqtt")
                .applyWebSocketConfig()
                .buildBlocking();

        /**
         * Connect securely with username, password.
         */
        client.connectWith()
                .simpleAuth()
                .username(username)
                .password(UTF_8.encode(password))
                .applySimpleAuth()
                .send();

        System.out.println("Connected successfully");

        /**
         * Subscribe to the topic "my/test/topic" with qos = 2 and print the received message.
         */
        client.subscribeWith()
                .topicFilter("<Topic>")
                .qos(MqttQos.EXACTLY_ONCE)
                .send();

        /**
         * Set a callback that is called when a message is received (using the async API style).
         * Then disconnect the client after a message was received.
         */
        client.toAsync().publishes(ALL, publish -> {
            System.out.println("Received message: " + publish.getTopic() + " -> " + UTF_8.decode(publish.getPayload().get()));

//            client.disconnect();
        });

        /**
         * Publish "Hello" to the topic "my/test/topic" with qos = 2.
         */
        Scanner message = new Scanner(System.in);
        while(true) {
            client.publishWith()
                    .topic("<topic>")
                    .payload(UTF_8.encode(message.nextLine()))
                    .qos(MqttQos.EXACTLY_ONCE)
                    .send();
        }
    }
}