package ru.gorbov.invoice_java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientDto implements CRUDable {
    private UUID id;
    private String name;
    private String phone;
    private String email;
    private List<UUID> invoiceIds = new ArrayList<>();


}

