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

    public void showRooms(){
        for(Room room : rooms.values()){
            System.out.println(room.getRoomId());
            room.getField().showField();
        }
    }

    public Room getRoom(String roomId){
        return rooms.get(roomId);
    }

    public void setValue(String roomId, MessageDto messageDto){
        rooms.get(roomId).getField().setValue(messageDto.getHeight(), messageDto.getWidth(), messageDto.getValue());
    }

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


    public List<String> getList() {
        List<String> roomIds = rooms.values().stream().map(Room::getRoomId).toList();
        return roomIds;
    }
}
