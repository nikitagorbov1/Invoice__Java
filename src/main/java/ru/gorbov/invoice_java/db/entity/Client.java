package ru.gorbov.invoice_java.db.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client implements Entitytable {

    private UUID id;

    private String name;

    private String phone;
    private String email;
    private List<Invoice> invoices = new ArrayList<>();

}
