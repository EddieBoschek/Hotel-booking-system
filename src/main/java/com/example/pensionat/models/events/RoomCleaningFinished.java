package com.example.pensionat.models.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;

@Entity
public class RoomCleaningFinished extends Event {
    @JsonProperty("CleaningByUser")
    private String CleaningByUser;
}
