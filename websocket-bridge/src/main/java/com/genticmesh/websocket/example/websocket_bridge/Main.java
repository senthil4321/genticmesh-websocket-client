package com.genticmesh.websocket.example.websocket_bridge;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.MessageHandler;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

//https://www.javatips.net/api/javax.websocket.messagehandler
public class Main {

	private static final String LOCALHOST = "localhost";

	private static final String EVENTBUS_WEBSOCKET = "ws://%s/api/v1/eventbus/websocket";

	private static String TOKEN = "";

	public static void main(String[] args) throws URISyntaxException {

		String serverPath = LOCALHOST;
		String JWTToken = TOKEN;
		if (args.length >= 1 && args[0] != null) {
			serverPath = args[0];
		}

		if (args.length >= 2 && args[1] != null) {
			JWTToken = args[1];
		}

		String URL = String.format(EVENTBUS_WEBSOCKET, serverPath);

		System.out.println("URL    :" + URL);
		System.out.println("TOKEN  :" + JWTToken);

		Configurator config = new Config(JWTToken);
		ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create().configurator(config).build();

		WebSocketContainer container = ContainerProvider.getWebSocketContainer();

		URI endpointURI = new URI(URL);

		try {
			Session session = container.connectToServer(ClientEndpoint.class, clientEndpointConfig, endpointURI);

			session.addMessageHandler(String.class, new MessageHandler.Whole<String>() {
				public void onMessage(String text) {
					System.out.println("String message: " + text);
				}
			});

			session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

				@Override
				public void onMessage(ByteBuffer buffer) {
					System.out.println("Binary message: " + new String(buffer.array()));
				}
			});

			session.addMessageHandler(new MessageHandler.Whole<PongMessage>() {

				@Override
				public void onMessage(PongMessage pongMessage) {
					StringBuffer pong = new StringBuffer();
					pong.append("Pong message: ").append(new String(pongMessage.getApplicationData().array()));
					System.out.println(pong.toString());

				}
			});

			String data1 = new String("{\"type\":\"register\",\"address\":\"mesh.node.updated\"}");
			session.getBasicRemote().sendText(data1);

			Thread t = new Thread(() -> {
				System.out.println("Starting Heart Beat Thread");

				while (true) {
					String data = new String("{\"type\":\"ping\"}");

					try {
						System.out.println("Sending Heart Beat Event");
						session.getBasicRemote().sendText(data);

					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			});
			t.start();

		} catch (DeploymentException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
