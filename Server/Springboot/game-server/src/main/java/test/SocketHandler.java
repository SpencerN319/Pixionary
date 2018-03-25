package test;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;




@Component
public class SocketHandler extends TextWebSocketHandler {
	
	CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<WebSocketSession>();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException 
	{

		System.out.println("Socket attempting to connect");
		for(WebSocketSession webSocketSession : sessions) {
		String value =	message.getPayload();
			webSocketSession.sendMessage(new TextMessage("Hello " + value + " !"));
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		//the messages will be broadcasted to all users.
		sessions.add(session);
	}
	
}
