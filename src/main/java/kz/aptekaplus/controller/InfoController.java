package kz.aptekaplus.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/info")
public class InfoController {



    @GetMapping("/payment")
    public String payment() {
        return "payment";
    }

    @GetMapping("/delivery")
    public String delivery() {
        return "delivery";
    }


    @GetMapping("/how-to-buy")
    public String howtobuy() {
        return "howtobuy";
    }

    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }


}
