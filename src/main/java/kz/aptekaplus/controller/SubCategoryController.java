package kz.aptekaplus.controller;

import jakarta.validation.constraints.Positive;
import kz.aptekaplus.dto.subcategory.SubCategoryRequestDto;
import kz.aptekaplus.dto.subcategory.SubCategoryResponseDto;
import kz.aptekaplus.service.SubCategoryService;
import kz.aptekaplus.service.impl.SubCategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/subcategory")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryServiceImpl subCategoryServiceImpl;
    @GetMapping("/{id}")
    public ResponseEntity<?> getSubCategories(@PathVariable UUID id) {
        System.out.println("UUID " + id);
        return ResponseEntity.ok(subCategoryServiceImpl.findAllSubCategoryByCategoryId(id));
    }
}
