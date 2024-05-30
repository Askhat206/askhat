package kz.aptekaplus.controller;


import kz.aptekaplus.dto.ProductViewDTO;
import kz.aptekaplus.service.impl.CategoryServiceImpl;
import kz.aptekaplus.service.impl.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/handbook")
@AllArgsConstructor
public class HandbookController {

    private final ProductServiceImpl productServiceImpl;
    private final CategoryServiceImpl categoryServiceImpl;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("categories", categoryServiceImpl.getCategories());
        return "spravochnik";
    }

    @GetMapping("/starts-with/{temp}")
    public String getStartsWith(@PathVariable("temp") String temp, Model model) {
        List<ProductViewDTO> productsStartsWith = productServiceImpl.getProductsStartsWith(temp);
        model.addAttribute("products", productsStartsWith);
        model.addAttribute("isEmpty", productsStartsWith.isEmpty());
        System.out.println(productsStartsWith);
        return "spravochnik";
    }
}
