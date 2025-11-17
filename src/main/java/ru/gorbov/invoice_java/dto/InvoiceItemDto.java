package ru.gorbov.invoice_java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceItemDto implements CRUDable {
    private UUID id;
    private UUID invoiceId;
    private UUID productId;
    private int quantity;
    private BigDecimal totalPrice;
}

