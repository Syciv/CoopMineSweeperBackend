package org.example.config;

import lombok.AllArgsConstructor;
import org.example.handler.RoomSocketHandler;
import org.example.service.RoomService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocket
@EnableScheduling
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final RoomService roomService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/room/*")
                .addInterceptors(auctionInterceptor());
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new RoomSocketHandler(roomService);
    }

    @Bean
    public HandshakeInterceptor auctionInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes){

                String path = request.getURI().getPath();
                String roomId = path.substring(path.lastIndexOf('/') + 1);
                attributes.put("roomId", roomId);
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
            }
        };
    }



}