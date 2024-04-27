package backEnd1.pensionat.services.interfaces;

import backEnd1.pensionat.DTOs.RoomDTO;
import backEnd1.pensionat.Models.Room;

import java.util.List;

public interface RoomService {

    //public SimpleRoomDTO roomToSimpleRoomDto(Room r);
    //public DetailedRoomDTO roomToDetailedRoomDto(Room r);
    //public Room detailedRoomDtoToRoom (DetailedRoomDTO r);

    public List<Room> getAllRooms();
    public String addRoom(Room r);
    public String removeRoomById(Long id);
    Room getRoomByID(Long Id);
    Room roomDtoToRoom(RoomDTO room);
    RoomDTO roomToRoomDto(Room room);
}
