package ru.gorbov.invoice_java.db.service;

import ru.gorbov.invoice_java.db.entity.Warehouse;
import ru.gorbov.invoice_java.db.repositories.JpaRepository;
import ru.gorbov.invoice_java.dto.WarehouseDto;
import ru.gorbov.invoice_java.mapper.Mapper;

import java.util.UUID;

public class WarehouseService extends BaseService<WarehouseDto, Warehouse> {

    public WarehouseService(JpaRepository<Warehouse, UUID> repository, Mapper<WarehouseDto, Warehouse> mapper) {
        super(repository, mapper);
    }
}

