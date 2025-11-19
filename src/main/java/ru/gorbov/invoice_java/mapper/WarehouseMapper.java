package ru.gorbov.invoice_java.mapper;

import ru.gorbov.invoice_java.db.entity.Warehouse;
import ru.gorbov.invoice_java.dto.WarehouseDto;

import java.util.UUID;

public class WarehouseMapper implements Mapper<WarehouseDto, Warehouse> {

    @Override
    public WarehouseDto toDto(Warehouse entity) {
        UUID parentId = entity.getWarehouse() != null ? entity.getWarehouse().getId() : null;
        return new WarehouseDto(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                parentId
        );
    }

    @Override
    public Warehouse toCreateEntity(WarehouseDto dto) {
        Warehouse parent = null;
        if (dto.getParentWarehouseId() != null) {
            parent = Warehouse.builder().id(dto.getParentWarehouseId()).build();
        }

        return Warehouse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .location(dto.getLocation())
                .warehouse(parent)
                .build();
    }
}

