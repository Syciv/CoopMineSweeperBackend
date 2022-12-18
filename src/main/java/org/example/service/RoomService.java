package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dto.MessageDto;
import org.example.gen.RoomList;
import org.example.model.Field;
import org.example.model.Room;
import org.example.repository.SudokuRepository;
import org.jooq.codegen.maven.example.tables.pojos.GameField;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class RoomService {

    private final Map<String, Room> rooms = new HashMap<>();

    private final SudokuRepository sudokuRepository;

    /**
     * Создание комнаты в памяти
     * @param difficulty - сложность поля в комнате
     * @return - id комнаты
     */
    public String createRoom(Integer difficulty){
        Room room = new Room();
        List<GameField> gameFields = sudokuRepository.fetchFieldsByDifficulty(difficulty);
        GameField gameField = gameFields.get(new Random().nextInt(gameFields.size()));
        room.setField(new Field(gameField.getUnsolved(), gameField.getSolved(), difficulty));
        room.setRoomId(generateId());
        System.out.println("Создана комната " + room.getRoomId());
        rooms.put(room.getRoomId(), room);
        return room.getRoomId();
    }

    /**
     * Отобраджение комнат в консоли
     */
    public void showRooms(){
        for(Room room : rooms.values()){
            System.out.println(room.getRoomId());
            room.getField().showField();
        }
    }

    /**
     * Получение комнаты по id
     * @param roomId - id комнаты
     * @return - комната
     */
    public Room getRoom(String roomId){
        return rooms.get(roomId);
    }

    /**
     * Установка значения в поле
     * @param roomId - id комнаты
     * @param messageDto - полученное сообщение с координатами и значением
     */
    public void setValue(String roomId, MessageDto messageDto){
        rooms.get(roomId).getField().setValue(messageDto.getHeight(), messageDto.getWidth(), messageDto.getValue());
    }

    /**
     * Генерация случайного id комнаты
     * @return - id комнаты
     */
    private String generateId(){
        Random random = new Random();
        Integer idLength = 6;
        char[] text = new char[idLength];
        for (int i = 0; i < idLength; i++)
        {
            String chars = "ABCDEF1234567890";
            text[i] = chars.charAt(random.nextInt(chars.length()));
        }
        return new String(text);
    }

    /**
     * Получение списка существующих комнат
     * @return - список id комнат
     */
    public List<String> getList() {
        List<String> roomIds = rooms.values().stream().map(Room::getRoomId).toList();
        return roomIds;
    }
}
