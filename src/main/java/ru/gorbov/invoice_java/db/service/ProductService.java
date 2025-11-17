package ru.gorbov.invoice_java.db.service;

import ru.gorbov.invoice_java.db.entity.Product;
import ru.gorbov.invoice_java.db.repositories.JpaRepository;
import ru.gorbov.invoice_java.dto.ProductDto;
import ru.gorbov.invoice_java.mapper.Mapper;

import java.util.UUID;

public class ProductService extends BaseService<ProductDto, Product> {

    public ProductService(JpaRepository<Product, UUID> repository, Mapper<ProductDto, Product> mapper) {
        super(repository, mapper);
    }
}

