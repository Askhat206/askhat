package kz.aptekaplus.service.impl;


import kz.aptekaplus.dto.subcategory.SubCategoryRequestDto;
import kz.aptekaplus.dto.subcategory.SubCategoryResponseDto;
import kz.aptekaplus.exception.EntityNotFoundException;
import kz.aptekaplus.exception.entity.EntityAlreadyExistsException;
import kz.aptekaplus.model.Category;
import kz.aptekaplus.model.SubCategory;
import kz.aptekaplus.repository.CategoryRepository;
import kz.aptekaplus.repository.SubCategoryRepository;
import kz.aptekaplus.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    public Set<SubCategory> findAllSubCategoriesByCategory(Category category) {
        return subCategoryRepository.findAllByCategory(category);
    }

    public Set<SubCategory> findAllSubCategoryByCategoryId(UUID id) {
        return subCategoryRepository.findByCategoryId(id);
    }

    @Override
    public SubCategoryResponseDto getById(UUID id) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory with id " + id + " not found!"));
        return mapToResponseDto(subCategory);
    }

    @Override
    @Transactional
    public SubCategoryResponseDto create(SubCategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(requestDto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + requestDto.categoryId() + " not found!"));

        if (subCategoryRepository.existsByNameAndCategoryId(requestDto.name(), requestDto.categoryId())) {
            throw new EntityAlreadyExistsException("Subcategory with name " + requestDto.name() + " already exists in this category.");
        }

        SubCategory subCategory = SubCategory.builder()
                .name(requestDto.name())
                .category(category)
                .build();
        subCategoryRepository.save(subCategory);

        return mapToResponseDto(subCategory);
    }

    @Override
    @Transactional
    public SubCategoryResponseDto update(UUID id, SubCategoryRequestDto requestDto) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory with id " + id + " not found!"));

        Category category = categoryRepository.findById(requestDto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + requestDto.categoryId() + " not found!"));

        subCategory.setName(requestDto.name());
        subCategory.setCategory(category);
        subCategoryRepository.save(subCategory);

        return mapToResponseDto(subCategory);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory with id " + id + " not found!"));
        subCategoryRepository.delete(subCategory);
    }

    @Override
    public SubCategory getEntityById(UUID id) {
        subCategoryRepository.findById(id)
                .ifPresent(foundUser -> {
                    throw new EntityAlreadyExistsException(
                            "Category with the id " + id+ " already exists"
                    );
                });

        return null;
    }
    private SubCategoryResponseDto mapToResponseDto(SubCategory subCategory) {
        return SubCategoryResponseDto.builder()
                .id(subCategory.getId())
                .name(subCategory.getName())
                .categoryId(subCategory.getCategory().getId())
                .build();
    }

    @Override
    public Set<SubCategory> getAllSubCategories(UUID categoryID) {
        Category category = categoryRepository.findById(categoryID)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + categoryID + " not found!"));
        return subCategoryRepository.findByCategory(category);
    }
}
