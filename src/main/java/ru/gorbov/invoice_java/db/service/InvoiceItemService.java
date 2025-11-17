package ru.gorbov.invoice_java.db.service;

import ru.gorbov.invoice_java.db.entity.InvoiceItem;
import ru.gorbov.invoice_java.db.repositories.JpaRepository;
import ru.gorbov.invoice_java.dto.InvoiceItemDto;
import ru.gorbov.invoice_java.mapper.Mapper;

import java.util.UUID;

public class InvoiceItemService extends BaseService<InvoiceItemDto, InvoiceItem> {

    public InvoiceItemService(JpaRepository<InvoiceItem, UUID> repository, Mapper<InvoiceItemDto, InvoiceItem> mapper) {
        super(repository, mapper);
    }
}

