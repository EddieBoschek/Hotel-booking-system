package backEnd1.pensionat.Models;

import backEnd1.pensionat.Enums.RoomType;
import backEnd1.pensionat.services.convert.RoomTypeConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Room {

    @Id
    private Long id;

    @NotNull(message = "Type of room is mandatory")
    private RoomType typeOfRoom;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<OrderLine> orderLines = new ArrayList<>();

    public Room(Long id, RoomType typeOfRoom) {
        this.typeOfRoom = typeOfRoom;
        this.id = id;
    }

    public Room(Long id, int typeOfRoom) {
        this.typeOfRoom = RoomTypeConverter.convertFromInt(typeOfRoom);
        this.id = id;
    }

}
