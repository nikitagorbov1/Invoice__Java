package ru.gorbov.invoice_java.db.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItem implements Entitytable {
    private UUID id;

    private Invoice invoice;

    private Product product;

    private int quantity;

    private BigDecimal totalPrice;
}
