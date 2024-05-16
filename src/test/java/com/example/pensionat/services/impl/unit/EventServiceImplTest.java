package com.example.pensionat.services.impl.unit;

import com.example.pensionat.models.events.*;
import com.example.pensionat.repositories.EventRepo;
import com.example.pensionat.services.impl.EventServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class EventServiceImplTest {

    @Mock
    private EventRepo eventRepo;

    @Mock
    private ObjectMapper mapper;

    EventServiceImpl sut;

    String roomOpened = "{\"type\":\"RoomOpened\",\"TimeStamp\":\"2024-05-15T09:34:13.396377561\"," +
            "\"RoomNo\":\"402\"}";
    String roomClosed = "{\"type\":\"RoomClosed\",\"TimeStamp\":\"2024-05-16T00:15:13.285568067\"," +
            "\"RoomNo\":\"204\"}";
    String roomCleaningStarted = "{\"type\":\"RoomCleaningStarted\",\"TimeStamp\":\"2024-05-15T17:21:13.299694326\"," +
            "\"RoomNo\":\"403\",\"CleaningByUser\":\"Mrs. Aja Predovic\"}";
    String roomCleaningFinished = "{\"type\":\"RoomCleaningFinished\",\"TimeStamp\":\"2024-05-15T19:23:13.371668625\"," +
            "\"RoomNo\":\"301\",\"CleaningByUser\":\"Santiago Kertzmann\"}";

    @BeforeEach()
    void setup() {
        sut = new EventServiceImpl(eventRepo);
    }

    @Test
    void addToModel() {
    }

    @Test
    void getEventsByRoomId() {
    }

    @Test
    void initializeObjectMapper() {
        ObjectMapper mapper = sut.initializeObjectMapper();

        assertNotNull(mapper);
        assertTrue(mapper.getRegisteredModuleIds().contains("jackson-datatype-jsr310"));
        assertFalse(mapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }

    @Test
    void createChannel() throws Exception {
        Channel channel = sut.createChannelFromConnection();

        assertNotNull(channel);
    }

    /*
    public Channel createChannel() throws Exception {
        ConnectionFactory factory = createConnectionFactory();
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }
     */

    @Test
    void createConnectionFactory() {
        ConnectionFactory factory = sut.createConnectionFactory();

        assertNotNull(factory);
        assertEquals(EventServiceImpl.HOST, factory.getHost());
        assertEquals(EventServiceImpl.USERNAME, factory.getUsername());
        assertEquals(EventServiceImpl.PASSWORD, factory.getPassword());
    }

    @Test
    void setupConsumer() {

    }

    @Test
    void mapToEventShouldMapCorrectlyRoomOpened() {
        Event actual = sut.mapToEvent(roomOpened);

        assertEquals("402", actual.getRoomNo());
        assertEquals(LocalDateTime.parse("2024-05-15T09:34:13.396377561"), actual.getTimeStamp());
        assertTrue(actual instanceof RoomOpened);
    }

    @Test
    void mapToEventShouldMapCorrectlyRoomClosed() {
        Event actual = sut.mapToEvent(roomClosed);

        assertEquals("204", actual.getRoomNo());
        assertEquals(LocalDateTime.parse("2024-05-16T00:15:13.285568067"), actual.getTimeStamp());
        assertTrue(actual instanceof RoomClosed);
    }

    @Test
    void mapToEventShouldMapCorrectlyRoomCleaningStarted() {
        Event actual = sut.mapToEvent(roomCleaningStarted);

        assertEquals("403", actual.getRoomNo());
        assertEquals(LocalDateTime.parse("2024-05-15T17:21:13.299694326"), actual.getTimeStamp());
        assertTrue(actual instanceof RoomCleaningStarted);
        assertEquals("Mrs. Aja Predovic", ((RoomCleaningStarted) actual).getCleaningByUser());
    }

    @Test
    void mapToEventShouldMapCorrectlyRoomCleaningFinished() {
        Event actual = sut.mapToEvent(roomCleaningFinished);

        assertEquals("301", actual.getRoomNo());
        assertEquals(LocalDateTime.parse("2024-05-15T19:23:13.371668625"), actual.getTimeStamp());
        assertTrue(actual instanceof RoomCleaningFinished);
        assertEquals("Santiago Kertzmann", ((RoomCleaningFinished) actual).getCleaningByUser());
    }

    @Test
    void SaveEventToDatabaseShouldCallSave() {
        Event event = new Event();

        sut.saveEventToDatabase(event);

        verify(eventRepo, times(1)).save(event);
    }
}