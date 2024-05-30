package kz.aptekaplus.service.impl;


import kz.aptekaplus.dto.CategoryViewDTO;
import kz.aptekaplus.dto.category.CategoryRequestDto;
import kz.aptekaplus.dto.category.CategoryResponseDto;
import kz.aptekaplus.exception.EntityNotFoundException;
import kz.aptekaplus.exception.entity.EntityAlreadyExistsException;
import kz.aptekaplus.model.Category;
import kz.aptekaplus.model.SubCategory;
import kz.aptekaplus.repository.CategoryRepository;
import kz.aptekaplus.service.CategoryService;
import kz.aptekaplus.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryServiceImpl subCategoryService;

    public List<CategoryViewDTO> getCategories() {
        List<CategoryViewDTO> categoryViewDTOS = new ArrayList<>();
        for (Category category : categoryRepository.findAll()) {
            Set<SubCategory> allSubCategoriesByCategory = subCategoryService.findAllSubCategoriesByCategory(category);
            categoryViewDTOS.add(new CategoryViewDTO(category, allSubCategoriesByCategory));
        }
        return categoryViewDTOS;
    }

    public CategoryResponseDto createCategory(CategoryRequestDto requestDto){
        throwExceptionIfCategoryExists(requestDto.name());

        Category category = Category.builder()
                .name(requestDto.name())
                .build();

        categoryRepository.save(category);

        Set<SubCategory> subCategories = subCategoryService.findAllSubCategoriesByCategory(category);

        CategoryResponseDto responseDto = CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .subCategories(subCategories)
                .build();

        return responseDto;
    }

    public CategoryResponseDto updateCategory(CategoryRequestDto requestDto){
        return null;
    }

    public void throwExceptionIfCategoryExists(String name) {
        categoryRepository.findByName(name)
                .ifPresent(foundUser -> {
                    throw new EntityAlreadyExistsException(
                            "Category with the name " + name+ " already exists"
                    );
                });
    }

    @Override
    public CategoryResponseDto getById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found!"));

        CategoryResponseDto responseDto = CategoryResponseDto.builder()
                .name(category.getName())
                .id(category.getId())
                .subCategories(category.getSubCategories())
                .build();
        return responseDto;
    }
    @Override
    @Transactional
    public CategoryResponseDto create(CategoryRequestDto requestDto) {
        throwExceptionIfCategoryExists(requestDto.name());

        Category category = Category.builder()
                .name(requestDto.name())
                .build();

        categoryRepository.save(category);

        Set<SubCategory> subCategories = subCategoryService.findAllSubCategoriesByCategory(category);

        CategoryResponseDto responseDto = CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .subCategories(subCategories)
                .build();

        return responseDto;
    }

    @Override
    @Transactional
    public CategoryResponseDto update(UUID id, CategoryRequestDto requestDto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found!"));

        String oldName = category.getName();
        String newName = requestDto.name();

        if (!Objects.equals(oldName, newName)) {
            throwExceptionIfCategoryExists(newName);
            category.setName(newName);
            category = categoryRepository.save(category);
        }

        CategoryResponseDto responseDto = CategoryResponseDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                         .subCategories(category.getSubCategories())
                        .build();

        return responseDto;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found!"));
        categoryRepository.delete(category);
    }

    @Override
    public Category getEntityById(UUID id) {
        return null;
    }


    @Override
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryResponseDto> categoryResponseDtos = new ArrayList<>();
        for(Category category : categories){
            CategoryResponseDto responseDto = CategoryResponseDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .subCategories(category.getSubCategories())
                    .build();

            categoryResponseDtos.add(responseDto);
        }
        return categoryResponseDtos;
    }
}
