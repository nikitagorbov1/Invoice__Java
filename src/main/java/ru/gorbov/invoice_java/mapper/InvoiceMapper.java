package ru.gorbov.invoice_java.mapper;

import ru.gorbov.invoice_java.db.entity.Client;
import ru.gorbov.invoice_java.db.entity.Invoice;
import ru.gorbov.invoice_java.db.entity.InvoiceItem;
import ru.gorbov.invoice_java.db.entity.Warehouse;
import ru.gorbov.invoice_java.dto.InvoiceDto;
import ru.gorbov.invoice_java.dto.InvoiceItemDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class InvoiceMapper implements Mapper<InvoiceDto, Invoice> {

    private final InvoiceItemMapper invoiceItemMapper;

    public InvoiceMapper(InvoiceItemMapper invoiceItemMapper) {
        this.invoiceItemMapper = invoiceItemMapper;
    }

    @Override
    public InvoiceDto toDto(Invoice entity) {
        UUID clientId = entity.getClient() != null ? entity.getClient().getId() : null;
        UUID warehouseId = entity.getWarehouse() != null ? entity.getWarehouse().getId() : null;

        List<InvoiceItemDto> items = entity.getItems() == null
                ? new ArrayList<>()
                : entity.getItems().stream()
                .map(invoiceItemMapper::toDto)
                .collect(Collectors.toList());

        InvoiceDto dto = new InvoiceDto(
                entity.getId(),
                clientId,
                warehouseId,
                entity.getDate(),
                items
        );
        return dto;
    }

    @Override
    public Invoice toCreateEntity(InvoiceDto dto) {
        Client client = null;
        if (dto.getClientId() != null) {
            client = Client.builder().id(dto.getClientId()).build();
        }

        Warehouse warehouse = null;
        if (dto.getWarehouseId() != null) {
            warehouse = Warehouse.builder().id(dto.getWarehouseId()).build();
        }

        List<InvoiceItem> items = dto.getItems() == null
                ? new ArrayList<>()
                : dto.getItems().stream()
                .map(invoiceItemMapper::toCreateEntity)
                .collect(Collectors.toCollection(ArrayList::new));

        Invoice invoice = Invoice.builder()
                .id(dto.getId())
                .client(client)
                .warehouse(warehouse)
                .date(dto.getDate())
                .items(items)
                .build();

        items.forEach(item -> item.setInvoice(invoice));
        return invoice;
    }
}

