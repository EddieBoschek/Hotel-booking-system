package backEnd1.pensionat.controllers;

import backEnd1.pensionat.DTOs.SimpleCustomerDTO;
import backEnd1.pensionat.services.interfaces.BookingService;
import backEnd1.pensionat.services.interfaces.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/customer")
public class CustomerController {

    private final CustomerService customerService;

    private final BookingService bookingService;

    //TODO Används dessa två?

    @RequestMapping("/{id}/removeHandler")
    public String removeCustomerByIdHandler(@PathVariable Long id, Model model) {
        if (bookingService.getBookingByCustomerId(id)) {
            model.addAttribute("status", "En kund kan inte tas bort om det finns aktiva bokningar");
        }
        else {
            customerService.removeCustomerById(id);
        }
        return handleCustomers(model);
    }

    @RequestMapping("/{email}/update")
    public String updateCustomerHandler(@PathVariable String email, Model model){
        SimpleCustomerDTO c = customerService.getCustomerByEmail(email);
        model.addAttribute("kund", c);
        return "updateCustomers";
    }

    @PostMapping("/handle/update")
    public String handleCustomersUpdate(Model model, SimpleCustomerDTO customer){
        customerService.updateCustomer(customer);
        int currentPage = 1;
        customerService.addToModel(currentPage, model);
        return "handleCustomers";
    }


    @RequestMapping("/customerOrNot")
    public String loadCustomerOrNot(){
        return "customerOrNot";
    }

    @GetMapping("/handle")
    public String handleCustomers(Model model){
        int currentPage = 1;
        customerService.addToModel(currentPage, model);
        return "handleCustomers";
    }

    @GetMapping("/handle/{pageNumber}")
    public String handleByPage(Model model, @PathVariable("pageNumber") int currentPage){
        customerService.addToModel(currentPage, model);
        return "handleCustomers";
    }

    @GetMapping("/search")
    public String getCustomerByEmail(@RequestParam String email, Model model) {
        int currentPage = 1;
        customerService.addToModelEmail(email, currentPage, model);
        return "handleCustomers";
    }

    @GetMapping("/search/{pageNumber}")
    public String getCustomerByEmailByPage(@RequestParam String email, Model model, @PathVariable("pageNumber") int currentPage) {
        customerService.addToModelEmail(email, currentPage, model);
        return "handleCustomers";
    }
}