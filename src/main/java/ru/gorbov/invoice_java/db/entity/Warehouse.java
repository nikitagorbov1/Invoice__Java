package ru.gorbov.invoice_java.db.entity;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse implements Entitytable {

    private UUID id;

    private String name;

    private String location;
    private Warehouse warehouse;

}
