package ru.gorbov.invoice_java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceDto implements CRUDable {
    private UUID id;
    private UUID clientId;
    private UUID warehouseId;
    private LocalDateTime date;
    private List<InvoiceItemDto> items = new ArrayList<>();

}

