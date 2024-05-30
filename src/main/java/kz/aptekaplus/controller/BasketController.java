package kz.aptekaplus.controller;


import jakarta.servlet.http.HttpServletRequest;
import kz.aptekaplus.service.BasketService;
import kz.aptekaplus.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;
    private final CategoryServiceImpl categoryServiceImpl;

    @PostMapping
    @ResponseBody
    public String addBasket(HttpServletRequest request, @RequestParam("productId") UUID productId) {
        basketService.addBasket(request, productId);
        return "#";
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("categories", categoryServiceImpl.getCategories());
        return "basket";
    }

    @ResponseBody
    @DeleteMapping("/{userId}/{productId}")
    public void deleteFromBasket(@PathVariable(name = "productId") UUID productId, @PathVariable(name = "userId") UUID userId) {
        basketService.deleteFromBasket(productId, userId);
    }
}
