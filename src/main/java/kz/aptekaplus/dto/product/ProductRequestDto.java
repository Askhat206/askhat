package kz.aptekaplus.dto.product;

import org.apache.commons.lang3.mutable.Mutable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record ProductRequestDto(
        UUID subCategoryId,
        String name,
        MultipartFile image,
        String idRegistrationNumber,
        String internationalName,
        String countryProducer,
        Double expirationDate,
        String dosageForm,
        String structure,
        Double price
) {
}
