package com.example.pensionat.controllers;

import com.example.pensionat.dtos.SimpleCustomerDTO;
import com.example.pensionat.services.interfaces.BookingService;
import com.example.pensionat.services.interfaces.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private BookRoomController testController;
    String email = "gudrun@disco.com";

    private void mockAddToModel(Model model, Page<SimpleCustomerDTO> page) {
        model.addAttribute("allCustomers", page.getContent());
        model.addAttribute("currentPage", page.getNumber() + 1);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());

    }

    @BeforeEach
    void init() {
        SimpleCustomerDTO customer = new SimpleCustomerDTO("evert.persson@outbook.com", "Evert Persson");
        Page<SimpleCustomerDTO> mockPage = new PageImpl<>(List.of(customer));

        when(customerService.getCustomerByEmail(anyString())).thenReturn(customer);
        when(customerService.removeCustomerById(anyLong())).thenReturn("Kund borta");

        // Mock for general model attribute setup
        doAnswer(invocation -> {
            Model model = invocation.getArgument(1);
            mockAddToModel(model, mockPage);
            return null;
        }).when(customerService).addToModel(anyInt(), any(Model.class));

        // Specific mock setup for methods that require custom handling
        doAnswer(invocation -> {
            String email = invocation.getArgument(0);
            int currentPage = invocation.getArgument(1);
            Model model = invocation.getArgument(2);
            mockAddToModel(model, new PageImpl<>(List.of(new SimpleCustomerDTO("Test Customer", email)), PageRequest.of(currentPage - 1, 5), 1));
            return null;
        }).when(customerService).addToModelEmail(anyString(), anyInt(), any(Model.class));
    }

    @Test
    void removeCustomerByIdHandler_WithActiveBookings() throws Exception {
        Long customerId = 1L;
        when(bookingService.getBookingByCustomerId(customerId)).thenReturn(true);

        mvc.perform(get("/customer/{id}/removeHandler", customerId))
                .andExpect(status().isOk())
                .andExpect(view().name("handleCustomers"))
                .andExpect(model().attributeExists("status"))
                .andExpect(model().attribute("status", "En kund kan inte tas bort om det finns aktiva bokningar"));
    }

    @Test
    void removeCustomerByIdHandler_NoActiveBookings() throws Exception {
        Long customerId = 1L;
        when(bookingService.getBookingByCustomerId(customerId)).thenReturn(false);

        mvc.perform(get("/customer/{id}/removeHandler", customerId))
                .andExpect(status().isOk())
                .andExpect(view().name("handleCustomers"))
                .andExpect(model().attributeDoesNotExist("status"));
    }

    @Test
    void updateCustomerHandler() throws Exception {
        String email = "test@example.com";
        mvc.perform(get("/customer/{email}/update", email))
                .andExpect(status().isOk())
                .andExpect(view().name("updateCustomers"))
                .andExpect(model().attributeExists("kund"));
    }

    @Test
    void handleCustomersUpdate() throws Exception {
        SimpleCustomerDTO customer = new SimpleCustomerDTO("Test Customer", "test@example.com");

        mvc.perform(post("/customer/handle/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", customer.getEmail())
                        .param("name", customer.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("handleCustomers"))
                .andExpect(model().attributeExists("allCustomers", "currentPage", "totalItems", "totalPages"));
    }

    @Test
    void loadCustomerOrNot() throws Exception {
        mvc.perform(get("/customer/customerOrNot"))
                .andExpect(status().isOk())
                .andExpect(view().name("customerOrNot"));
    }

    @Test
    void handleCustomers() throws Exception {
        mvc.perform(get("/customer/handle"))
                .andExpect(status().isOk())
                .andExpect(view().name("handleCustomers"))
                .andExpect(model().attributeExists("allCustomers", "currentPage", "totalItems", "totalPages"));
    }

    @Test
    void handleByPage() throws Exception {
        int currentPage = 1;
        mvc.perform(get("/customer/handle/{pageNumber}", currentPage))
                .andExpect(status().isOk())
                .andExpect(view().name("handleCustomers"))
                .andExpect(model().attributeExists("allCustomers", "currentPage", "totalItems", "totalPages"));
    }

    @Test
    void getCustomerByEmail() throws Exception {
        mvc.perform(get("/customer/search")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(view().name("handleCustomers"))
                .andExpect(model().attributeExists("allCustomers", "currentPage", "totalItems", "totalPages"));
    }

    @Test
    void getCustomerByEmailByPage() throws Exception {
        int currentPage = 2;
        mvc.perform(get("/customer/search/{pageNumber}", currentPage)
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(view().name("handleCustomers"))
                .andExpect(model().attributeExists("allCustomers", "currentPage", "totalItems", "totalPages"));
    }
}
