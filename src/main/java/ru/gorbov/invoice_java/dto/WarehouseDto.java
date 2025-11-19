package ru.gorbov.invoice_java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WarehouseDto implements CRUDable {
    private UUID id;
    private String name;
    private String location;
    private UUID parentWarehouseId;
}

