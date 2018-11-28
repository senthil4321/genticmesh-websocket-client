package com.genticmesh.websocket.example.websocket_bridge;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpointConfig;
public class Config extends ClientEndpointConfig.Configurator{
	
	private String token = "";
	Config(String token)
	{
		this.token  = token;
		
	}
    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
    	headers.put("Authorization", Arrays.asList("Bearer " + token));
    }
}
