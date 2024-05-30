package kz.aptekaplus.service;

import kz.aptekaplus.dto.subcategory.SubCategoryRequestDto;
import kz.aptekaplus.dto.subcategory.SubCategoryResponseDto;
import kz.aptekaplus.model.SubCategory;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SubCategoryService extends CrudService<SubCategory, SubCategoryRequestDto, SubCategoryResponseDto>{

    Set<SubCategory> getAllSubCategories(UUID categoryID);
}
