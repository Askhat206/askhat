package kz.aptekaplus.service.impl;


import kz.aptekaplus.dto.ProductViewDTO;
import kz.aptekaplus.dto.product.ProductRequestDto;
import kz.aptekaplus.dto.product.ProductResponseDto;
import kz.aptekaplus.exception.EntityNotFoundException;
import kz.aptekaplus.exception.entity.EntityAlreadyExistsException;
import kz.aptekaplus.model.Category;
import kz.aptekaplus.model.Product;
import kz.aptekaplus.model.SubCategory;
import kz.aptekaplus.repository.CategoryRepository;
import kz.aptekaplus.repository.ProductRepository;
import kz.aptekaplus.repository.SubCategoryRepository;
import kz.aptekaplus.service.ProductService;
import kz.aptekaplus.service.StorageService;
import kz.aptekaplus.util.FileUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final String PRODUCT_FILE_FORMAT = "product/%d/%s";
    private final StorageService storageService;
    private static int counter = 1;


    public List<ProductViewDTO> getProductsStartsWith(String temp) {
        List<ProductViewDTO> productViewDTOS = new ArrayList<>();
        for (Product product : productRepository.findByNameStartingWith(temp)) {
            byte[] file = storageService.getFile(product.getImagePath());
            productViewDTOS.add(
                    new ProductViewDTO(product, file)
            );
        }
        return productViewDTOS;
    }

    public ProductViewDTO getProduct(UUID productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        return productOptional.map(product -> new ProductViewDTO(product, storageService.getFile(product.getImagePath()))).orElse(null);
    }

    public Product findProduct(UUID productId) {
        return productRepository.findById(productId).get();
    }

    public List<ProductViewDTO> findBySubCategoryId(UUID id) {
        List<Product> products = productRepository.findBySubCategoryId(id);

        List<ProductViewDTO> productDTOs = products.stream()
                .map(product -> new ProductViewDTO(product, storageService.getFile(product.getImagePath())))
                .collect(Collectors.toList());

        return productDTOs;
    }

    @Override
    public ProductResponseDto getById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found!"));
        byte[] url = storageService.getFile(product.getImagePath());
        ProductResponseDto responseDto = ProductResponseDto.builder()
                .id(product.getId())
                .subCategoryId(product.getSubCategory().getId())
                .name(product.getName())
                .imagePath(url)
                .idRegistrationNumber(product.getIdRegistrationNumber())
                .internationalName(product.getInternationalName())
                .countryProducer(product.getCountryProducer())
                .expirationDate(product.getExpirationDate())
                .dosageForm(product.getDosageForm())
                .structure(product.getStructure())
                .price(product.getPrice())
                .build();

        return responseDto;
    }

    @Override
    @Transactional
    public ProductResponseDto create(ProductRequestDto requestDto) {
        SubCategory subCategory = subCategoryRepository.findById(requestDto.subCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Subcategory with id " + requestDto.subCategoryId() + " not found!"));

        Category category = categoryRepository.findByName(subCategory.getCategory().getName())
                .orElseThrow(() -> new EntityNotFoundException("Category with name " + subCategory.getCategory().getName() + " not found!"));
        MultipartFile file = requestDto.image();
        String filePath = String.format(
                PRODUCT_FILE_FORMAT,
                counter,
                FileUtils.generateFileName(file)
        );
        counter++;
        String path = storageService.uploadFile(
                filePath,
                FileUtils.getInputStreamOrElseThrow(file)
        );
        Product product = Product.builder()
                .name(requestDto.name())
                .subCategory(subCategory)
                .imagePath(path)
                .idRegistrationNumber(requestDto.idRegistrationNumber())
                .internationalName(requestDto.internationalName())
                .countryProducer(requestDto.countryProducer())
                .expirationDate(requestDto.expirationDate())
                .dosageForm(requestDto.dosageForm())
                .structure(requestDto.structure())
                .price(requestDto.price())
                .build();

        productRepository.save(product);

        ProductResponseDto responseDto = ProductResponseDto.builder()
                .id(product.getId())
                .subCategoryId(product.getSubCategory().getId())
                .name(product.getName())
                .imagePath(storageService.getFile(product.getImagePath()))
                .idRegistrationNumber(product.getIdRegistrationNumber())
                .internationalName(product.getInternationalName())
                .countryProducer(product.getCountryProducer())
                .expirationDate(product.getExpirationDate())
                .dosageForm(product.getDosageForm())
                .structure(product.getStructure())
                .price(product.getPrice())
                .build();

        return responseDto;
    }

    @Override
    @Transactional
    public ProductResponseDto update(UUID id, ProductRequestDto requestDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));

        SubCategory subCategory = subCategoryRepository.findById(requestDto.subCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Subcategory with id " + requestDto.subCategoryId() + " not found"));
        MultipartFile file = requestDto.image();
        String filePath = String.format(
                PRODUCT_FILE_FORMAT,
                counter,
                FileUtils.generateFileName(file)
        );
        counter++;
        String path = storageService.uploadFile(
                filePath,
                FileUtils.getInputStreamOrElseThrow(file)
        );
        existingProduct.setImagePath(path);
        existingProduct.setName(requestDto.name() != null ? requestDto.name() : existingProduct.getName());
        existingProduct.setSubCategory(subCategory);
        existingProduct.setIdRegistrationNumber(requestDto.idRegistrationNumber() != null ? requestDto.idRegistrationNumber() : existingProduct.getIdRegistrationNumber());
        existingProduct.setInternationalName(requestDto.internationalName() != null ? requestDto.internationalName() : existingProduct.getInternationalName());
        existingProduct.setCountryProducer(requestDto.countryProducer() != null ? requestDto.countryProducer() : existingProduct.getCountryProducer());
        existingProduct.setExpirationDate(requestDto.expirationDate() != null ? requestDto.expirationDate() : existingProduct.getExpirationDate());
        existingProduct.setDosageForm(requestDto.dosageForm() != null ? requestDto.dosageForm() : existingProduct.getDosageForm());
        existingProduct.setStructure(requestDto.structure() != null ? requestDto.structure() : existingProduct.getStructure());
        existingProduct.setPrice(requestDto.price() != null ? requestDto.price() : existingProduct.getPrice());

        Product updatedProduct = productRepository.save(existingProduct);

        return ProductResponseDto.builder()
                .id(updatedProduct.getId())
                .subCategoryId(updatedProduct.getSubCategory().getId())
                .name(updatedProduct.getName())
                .imagePath(storageService.getFile(updatedProduct.getImagePath()))
                .idRegistrationNumber(updatedProduct.getIdRegistrationNumber())
                .internationalName(updatedProduct.getInternationalName())
                .countryProducer(updatedProduct.getCountryProducer())
                .expirationDate(updatedProduct.getExpirationDate())
                .dosageForm(updatedProduct.getDosageForm())
                .structure(updatedProduct.getStructure())
                .price(updatedProduct.getPrice())
                .build();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found!"));
        productRepository.delete(product);
    }

    @Override
    public Product getEntityById(UUID id) {
        productRepository.findById(id)
                .ifPresent(foundUser -> {
                    throw new EntityAlreadyExistsException(
                            "Product with the id " + id + " already exists"
                    );
                });
        return null;
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product : allProducts){
            byte[] url = storageService.getFile(product.getImagePath());
            ProductResponseDto responseDto = ProductResponseDto.builder()
                    .id(product.getId())
                    .subCategoryId(product.getSubCategory().getId())
                    .name(product.getName())
                    .imagePath(url)
                    .idRegistrationNumber(product.getIdRegistrationNumber())
                    .internationalName(product.getInternationalName())
                    .countryProducer(product.getCountryProducer())
                    .expirationDate(product.getExpirationDate())
                    .dosageForm(product.getDosageForm())
                    .structure(product.getStructure())
                    .price(product.getPrice())
                    .build();

            productResponseDtos.add(responseDto);
        }

        return productResponseDtos;
    }

    @Override
    public List<ProductResponseDto> search(String query) {
        List<Product> byNameContaining = productRepository.findByNameContaining(query);
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for (Product product : byNameContaining) {
            productResponseDtos.add(new ProductResponseDto(
                    product.getId(),
                    product.getSubCategory().getId(),
                    product.getName(),
                    storageService.getFile(product.getImagePath()),
                    product.getIdRegistrationNumber(),
                    product.getInternationalName(),
                    product.getCountryProducer(),
                    product.getExpirationDate(),
                    product.getDosageForm(),
                    product.getStructure(),
                    product.getPrice()
            ));
        }
        return productResponseDtos;
    }
}
