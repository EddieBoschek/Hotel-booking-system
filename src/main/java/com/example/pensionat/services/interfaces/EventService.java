package com.example.pensionat.services.interfaces;

import com.example.pensionat.dtos.EventDTO;
import com.example.pensionat.models.events.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

public interface EventService {

    void addToModel(String id, int currentPage, Model model);
    Page<EventDTO> getEventsByRoomId(String id, int pageNum);
    ObjectMapper initializeObjectMapper();
    Channel createChannelFromConnection() throws Exception;
    ConnectionFactory createConnectionFactory();
    //void setupConsumer(Channel channel) throws Exception;
    void setupConsumer(Channel channel) throws Exception;
    DeliverCallback createDeliverCallback();
    String extractMessage(Delivery delivery);
    Event mapToEvent(String message);
    void saveEventToDatabase(Event event);
}
