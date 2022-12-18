package org.example.ws;

import lombok.AllArgsConstructor;
import org.example.gen.*;
import org.example.model.Room;
import org.example.service.RoomService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
@AllArgsConstructor
public class RoomEndpoint {

    private static final String NAMESPACE_URI = "http://example.org/gen";

    private final RoomService roomService;

    /**
     * Эндпоинт для получения списка комнат на сервере
     * @return - ответ со списком id комнат
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getRoomsRequest")
    @ResponsePayload
    public GetRoomsResponse getRooms() {
        GetRoomsResponse response = new GetRoomsResponse();
        RoomList roomList = new RoomList();
        roomList.getRooms().addAll(roomService.getList());
        System.out.println(roomList.getRooms());
        response.setRoomList(roomList);
        return response;
    }

    /**
     * Эндпоинт для создания комнаты
     * @param request - запрос со соложностью поля
     * @return - ответ с id комнаты
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createRoomRequest")
    @ResponsePayload
    public CreateRoomResponse createRoom(@RequestPayload CreateRoomRequest request) {
        CreateRoomResponse response = new CreateRoomResponse();
        response.setRoomId(roomService.createRoom(request.getCreateRoomDto().getDifficulty()));
        return response;
    }

    /**
     * Эндпоинт для получения состояния
     * @param request - запрос с указанным id комнаты
     * @return - ответ с текущим состоянием комнаты
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "fetchRoomRequest")
    @ResponsePayload
    public FetchRoomResponse fetchRoom(@RequestPayload FetchRoomRequest request) {
        FetchRoomResponse response = new FetchRoomResponse();
        Room room = roomService.getRoom(request.getRoomId());
        RoomState roomState = new RoomState();
        roomState.setDifficulty(room.getField().getDifficulty());
        roomState.setField(room.getField().fieldString());
        response.setState(roomState);
        return response;
    }



}