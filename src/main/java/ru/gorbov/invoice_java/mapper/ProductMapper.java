package ru.gorbov.invoice_java.mapper;

import ru.gorbov.invoice_java.db.entity.Product;
import ru.gorbov.invoice_java.dto.ProductDto;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductMapper implements Mapper<ProductDto, Product> {
    @Override
    public ProductDto toDto(Product entity) {
        UUID parentId = entity.getProduct() != null ? entity.getProduct().getId() : null;
        return new ProductDto(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                parentId
        );
    }

    @Override
    public Product toCreateEntity(ProductDto dto) {
        Product parent = null;
        if (dto.getParentProductId() != null) {
            parent = Product.builder().id(dto.getParentProductId()).build();
        }

        BigDecimal price = dto.getPrice();
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(price)
                .product(parent)
                .build();
    }
}

