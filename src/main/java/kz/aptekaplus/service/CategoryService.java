package kz.aptekaplus.service;

import kz.aptekaplus.dto.category.CategoryRequestDto;
import kz.aptekaplus.dto.category.CategoryResponseDto;
import kz.aptekaplus.model.Category;

import java.util.List;

public interface CategoryService extends CrudService<Category, CategoryRequestDto, CategoryResponseDto>{
    List<CategoryResponseDto> getAllCategories();
}
