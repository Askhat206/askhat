package kz.aptekaplus.controller;

import jakarta.validation.constraints.Positive;
import kz.aptekaplus.dto.subcategory.SubCategoryRequestDto;
import kz.aptekaplus.dto.subcategory.SubCategoryResponseDto;
import kz.aptekaplus.model.SubCategory;
import kz.aptekaplus.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RequestMapping("/api/v1/subcategory")
@RequiredArgsConstructor
@RestController
@Slf4j
public class SubCategoryRestController {
    private final SubCategoryService subCategoryService;
    @GetMapping("/sub/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubCategoryResponseDto getById(@PathVariable UUID id){
        return subCategoryService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubCategoryResponseDto createCategory(@RequestBody @Validated SubCategoryRequestDto requestDto){
        return subCategoryService.create(requestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SubCategoryResponseDto updateCategory(@PathVariable("id") UUID id,@RequestBody @Validated SubCategoryRequestDto requestDto){
        return subCategoryService.update(id,requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
            @RequestParam("id") UUID id
    ) {
        subCategoryService.delete(id);
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<?> getSubCategoriesByCategory(@PathVariable UUID categoryId) {
        Set<SubCategory> subCategories = subCategoryService.getAllSubCategories(categoryId);
        if (subCategories.isEmpty()) {
            return ResponseEntity.ok("Nooo");
        }
        return ResponseEntity.ok(subCategories);
    }
}
