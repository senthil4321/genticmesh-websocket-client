package com.genticmesh.websocket.example.websocket_bridge;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;


public class ClientEndpoint extends Endpoint  {

    Session clientEndpoint = null;


    @OnOpen
    public void onOpen(Session session) {
        System.out.println("ClientEndpoint : on Socket Open");
        this.clientEndpoint = session;
    }
	@Override
	public void onOpen(Session session, EndpointConfig config) {
		
		System.out.println("ClientEndpoint: on Socket Open ");
        this.clientEndpoint = session;
	}
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("ClientEndpoint: closing websocket");
        this.clientEndpoint = null;
    }

    @OnMessage
    public void onMessage(String message) {
    	System.out.println("ClientEndpoint: String message: " + message);
    }
    
    @OnMessage
    public void onMessage(byte[] message) {
    	System.out.println("ClientEndpoint: Binary message: " + new String(message));    	
    }

}

