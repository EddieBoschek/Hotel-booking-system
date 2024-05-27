package com.example.pensionat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingData {

    private Long id;
    private String name;
    private String email;
    private String startDate;
    private String endDate;
    private List<OrderLineDTO> chosenRooms;

}