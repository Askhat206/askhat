package kz.aptekaplus.controller;


import jakarta.validation.constraints.Positive;
import kz.aptekaplus.dto.CategoryViewDTO;
import kz.aptekaplus.dto.category.CategoryRequestDto;
import kz.aptekaplus.dto.category.CategoryResponseDto;
import kz.aptekaplus.service.CategoryService;
import kz.aptekaplus.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceImpl categoryServiceImpl;
    private final CategoryService categoryService;
    @GetMapping
    public ResponseEntity<List<CategoryViewDTO>> getCategories() {
        return ResponseEntity.ok(categoryServiceImpl.getCategories());
    }

}
