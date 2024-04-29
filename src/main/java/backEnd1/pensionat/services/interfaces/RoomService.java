package backEnd1.pensionat.services.interfaces;

import backEnd1.pensionat.DTOs.BookingFormQueryDTO;
import backEnd1.pensionat.DTOs.RoomDTO;
import backEnd1.pensionat.Models.Room;

import java.util.List;

public interface RoomService {

    public List<Room> getAllRooms();
    public String addRoom(Room r);
    public String removeRoomById(Long id);
    RoomDTO getRoomByID(Long Id);
    List<RoomDTO> findAvailableRooms(BookingFormQueryDTO query);
    String enoughRooms(BookingFormQueryDTO query, List<RoomDTO> rooms);
}