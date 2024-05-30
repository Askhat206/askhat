package kz.aptekaplus.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import kz.aptekaplus.model.Product;
import kz.aptekaplus.model.User;
import kz.aptekaplus.service.JWTService;
import kz.aptekaplus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/personal")
@RequiredArgsConstructor
public class PersonalController {

    private final UserService userService;
    private final JWTService jwtService;
    @GetMapping
    public String index(HttpServletRequest request) {
        return "personal";
    }

    @GetMapping("/addresses")
    public String address(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String refreshToken = null;
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
            if (refreshToken != null) {
                UUID userId = UUID.fromString(jwtService.extractID(refreshToken));
                User user = userService.findById(userId);
                if (user != null) {
                    model.addAttribute("user", user);
                    System.out.println("==================");
                    System.out.println("777");
                    System.out.println(refreshToken);
                    System.out.println("userId");
                    System.out.println(userId);
                    System.out.println("==================");

                }

            }
        }
        return "address";
    }

    @PostMapping("/addresses")
    public String addressUpdate(@ModelAttribute("user") User user, HttpServletRequest request) {
        System.out.println("=========================");
        System.out.println(user);
        System.out.println("IN address");
        System.out.println("=========================");
        userService.updateUser(user, request);
        return "address";
    }

    @GetMapping("/favourites")
    public String favourites(Model model,HttpServletRequest request) {
        List<Product> products = userService.getFavourites(request);
        if (products != null) {
            model.addAttribute("products", products);
        }
        return "favourites";
    }

    @PostMapping("/favourites/{productId}")
    public String favourites(HttpServletRequest request, @PathVariable(name = "productId") UUID productId, Model model) {
        userService.addFavourites(request, productId);
        List<Product> products = userService.getFavourites(request);
        if (products != null) {
            model.addAttribute("products", products);
        }
        return "favourites";
    }
}
