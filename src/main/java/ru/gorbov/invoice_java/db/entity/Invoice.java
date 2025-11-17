package ru.gorbov.invoice_java.db.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice implements Entitytable {
    private UUID id;

    private Client client;

    private Warehouse warehouse;

    private LocalDateTime date;

    private List<InvoiceItem> items = new ArrayList<>();
}