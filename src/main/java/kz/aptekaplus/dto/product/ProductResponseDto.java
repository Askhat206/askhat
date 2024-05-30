package kz.aptekaplus.dto.product;

import lombok.Builder;

import java.util.UUID;

@Builder
public record  ProductResponseDto(
        UUID id,

        UUID subCategoryId,
        String name,
        byte[] imagePath,
        String idRegistrationNumber,
        String internationalName,
        String countryProducer,
        Double expirationDate,
        String dosageForm,
        String structure,
        Double price
) {
}
