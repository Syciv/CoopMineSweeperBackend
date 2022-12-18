package org.example.handler;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.dto.MessageDto;
import org.example.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.Session;
import java.util.*;

@Component
@AllArgsConstructor
public class RoomSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {

    private static final Logger logger = LoggerFactory.getLogger(RoomSocketHandler.class);

    private final Map<String, Set<WebSocketSession>> roomSessionsMap = new HashMap<>();

    private final RoomService roomService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String roomId = (String) session.getAttributes().get("roomId");
        if(Objects.nonNull(roomService.getRoom(roomId))) {
            // Добавление клиента в комнату
            if (!roomSessionsMap.containsKey(roomId)) {
                roomSessionsMap.put(roomId, new HashSet<>());
            }
            roomSessionsMap.get(roomId).add(session);
        }
        else {
            throw new RuntimeException("Комнаты не существует");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Соединение закрыто: {}", status);
        String roomId = (String) session.getAttributes().get("roomId");
        roomSessionsMap.get(roomId).remove(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String request = message.getPayload();
        String roomId = (String) session.getAttributes().get("roomId");

        roomService.setValue(roomId, new Gson().fromJson(message.getPayload(), MessageDto.class));
        logger.info("Получено сообщение: {}", request);

        // Отправка сообщения всем клиетам в этой комнате
        sendToAll(roomId, message, session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.info("Ошибка: {}", exception.getMessage());
    }

    @Override
    public List<String> getSubProtocols() {
        return Collections.singletonList("subprotocol.demo.websocket");
    }

    @SneakyThrows
    private void sendToAll(String roomId, TextMessage message, WebSocketSession sender) {
        for(WebSocketSession session : roomSessionsMap.get(roomId)){
            if(!sender.equals(session)) {
                session.sendMessage(message);
            }
        }
    }
}
