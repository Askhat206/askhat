package kz.aptekaplus.dto.category;

import lombok.Builder;

@Builder
public record CategoryRequestDto(
        String name
) {
}
