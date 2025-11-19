package ru.gorbov.invoice_java.mapper;

import ru.gorbov.invoice_java.db.entity.Client;
import ru.gorbov.invoice_java.db.entity.Invoice;
import ru.gorbov.invoice_java.dto.ClientDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClientMapper implements Mapper<ClientDto, Client> {

    @Override
    public ClientDto toDto(Client entity) {
        List<UUID> invoiceIds = entity.getInvoices() == null
                ? new ArrayList<>()
                : entity.getInvoices().stream()
                .map(Invoice::getId)
                .collect(Collectors.toList());

        ClientDto dto = new ClientDto(
                entity.getId(),
                entity.getName(),
                entity.getPhone(),
                entity.getEmail(),
                invoiceIds
        );
        return dto;
    }

    @Override
    public Client toCreateEntity(ClientDto dto) {
        Client client = Client.builder()
                .id(dto.getId())
                .name(dto.getName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .build();

        if (dto.getInvoiceIds() != null) {
            List<Invoice> invoices = dto.getInvoiceIds().stream()
                    .map(id -> Invoice.builder().id(id).client(client).build())
                    .collect(Collectors.toCollection(ArrayList::new));
            client.setInvoices(invoices);
        } else {
            client.setInvoices(new ArrayList<>());
        }
        return client;
    }
}

