package kz.aptekaplus.dto.subcategory;

import java.util.UUID;

public record SubCategoryRequestDto(
        String name,
        UUID categoryId
) {
}
