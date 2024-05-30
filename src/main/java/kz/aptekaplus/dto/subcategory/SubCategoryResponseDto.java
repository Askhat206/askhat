package kz.aptekaplus.dto.subcategory;

import lombok.Builder;

import java.util.UUID;

@Builder
public record SubCategoryResponseDto(
        UUID id,
        String name,
        UUID categoryId
) {
}
