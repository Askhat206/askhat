package kz.aptekaplus.dto.category;

import kz.aptekaplus.model.SubCategory;
import lombok.Builder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record CategoryResponseDto(
        UUID id,
        String name,
        Set<SubCategory> subCategories
) {
}
