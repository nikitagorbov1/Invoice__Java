package ru.gorbov.invoice_java.db.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product implements Entitytable {
    private UUID id;

    private String name;

    private BigDecimal price;
    private Product product; // без каскада

}