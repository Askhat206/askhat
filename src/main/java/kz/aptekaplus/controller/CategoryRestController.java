package kz.aptekaplus.controller;

import jakarta.validation.constraints.Positive;
import kz.aptekaplus.dto.category.CategoryRequestDto;
import kz.aptekaplus.dto.category.CategoryResponseDto;
import kz.aptekaplus.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryRestController {
    private final CategoryService categoryService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponseDto> getAllCategories(){
        return categoryService.getAllCategories();
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponseDto getById(@PathVariable UUID id){
        return categoryService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@RequestBody @Validated CategoryRequestDto requestDto){
        return categoryService.create(requestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CategoryResponseDto updateCategory(
            @PathVariable("id") UUID id,@RequestBody @Validated CategoryRequestDto requestDto
    ){
        return categoryService.update(id,requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
            @RequestParam("id") UUID id
    ) {
        categoryService.delete(id);
    }
}
