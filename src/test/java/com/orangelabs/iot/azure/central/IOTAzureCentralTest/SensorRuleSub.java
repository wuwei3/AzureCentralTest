package com.orangelabs.iot.azure.central.IOTAzureCentralTest;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.MessageHandlerOptions;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.TopicClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;

public class SensorRuleSub {
	
    static final Gson GSON = new Gson();
	
    static String connectionString = "Endpoint=sb://pubandsub.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=P0auldM+1ZE+TmhnjhsC/A5bCRVh9MYWyDFapLXw+Gg=";

	public static void main(String[] args) throws Exception{
		SubscriptionClient subscription1Client = new SubscriptionClient(new ConnectionStringBuilder(connectionString, "sensor-storealldata/subscriptions/sensor-alldata-sub"), ReceiveMode.PEEKLOCK);

		ExecutorService executorService = Executors.newFixedThreadPool(100);
		
        registerMessageHandlerOnClient(subscription1Client, executorService);
        System.out.println("wna bi le2");
        
        // {"applicationId":"110b00bb-4997-4ca5-b584-f527ff17839b","messageSource":"telemetry","deviceId":"vml2e9ggnj","schema":"default@v1","templateId":"dtmi:ijurr4mr1h:kjfk3pku4i","enqueuedTime":"2021-02-05T07:35:27.085Z","telemetry":{"tempslong":200},"messageProperties":{"$.cdid":"vml2e9ggnj"},"enrichments":{}}


        // {"applicationId":"110b00bb-4997-4ca5-b584-f527ff17839b","messageSource":"telemetry","deviceId":"2i1myoivl8u","schema":"default@v1","templateId":"dtmi:modelDefinition:exkxevuee:kjfk3pku4i","enqueuedTime":"2021-02-05T08:15:33.486Z","telemetry":{"sensorProperty":{"co2":"10","humidity":"28","id":"eb24637a-e671-4054-8572-997568eef8ed","insertTime":"2021-02-05 16:15:31","pollution":"14","temperature":145}},"messageProperties":{"$.cdid":"2i1myoivl8u"},"enrichments":{}}

        
	}
	
	static void registerMessageHandlerOnClient(SubscriptionClient receiveClient, ExecutorService executorService) throws Exception {

        // register the RegisterMessageHandler callback
        IMessageHandler messageHandler = new IMessageHandler() {
            // callback invoked when the message handler loop has obtained a message
            public CompletableFuture<Void> onMessageAsync(IMessage message) {
            	
                // receives message is passed to callback
//                if (message.getLabel() != null &&
//                        message.getContentType() != null &&
//                        message.getLabel().contentEquals("message") &&
//                        message.getContentType().contentEquals("application/json")) {

                    byte[] body = message.getBody();
//                    Map scientist = GSON.fromJson(new String(body, UTF_8), Map.class);
                    
                    try {
						String str = new String(body, "UTF-8");
						System.out.println(str);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
//
//                    System.out.printf(
//                            "\n\t\t\t\t%s Message received: \n\t\t\t\t\t\tMessageId = %s, \n\t\t\t\t\t\tSequenceNumber = %s, \n\t\t\t\t\t\tEnqueuedTimeUtc = %s," +
//                                    "\n\t\t\t\t\t\tExpiresAtUtc = %s, \n\t\t\t\t\t\tContentType = \"%s\",  \n\t\t\t\t\t\tContent: [ firstName = %s, name = %s ]\n",
//                            receiveClient.getEntityPath(),
//                            message.getMessageId(),
//                            message.getSequenceNumber(),
//                            message.getEnqueuedTimeUtc(),
//                            message.getExpiresAtUtc(),
//                            message.getContentType(),
//                            scientist != null ? scientist.get("firstName") : "",
//                            scientist != null ? scientist.get("name") : "");
                //}
                return receiveClient.completeAsync(message.getLockToken());
            }

            public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
                System.out.printf(exceptionPhase + "-" + throwable.getMessage());
            }
        };


        receiveClient.registerMessageHandler(
                    messageHandler,
                    // callback invoked when the message handler has an exception to report
                // 1 concurrent call, messages are auto-completed, auto-renew duration
                new MessageHandlerOptions(100, false, Duration.ofMinutes(5)), executorService);

    }

}
