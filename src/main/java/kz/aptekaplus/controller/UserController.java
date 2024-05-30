package kz.aptekaplus.controller;

import kz.aptekaplus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<HttpStatus> signin(@RequestParam("email") String email) {
        System.out.println("==================");
        System.out.println(email);
        System.out.println("==================");
//        userService.sendSms();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/submit")
    public ResponseEntity<HttpStatus> submitCode(@RequestParam("email") String email,
                                                 @RequestParam("code") String code) {

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping()
    public String test(Principal principal) {
        return principal.getName();
    }

}
