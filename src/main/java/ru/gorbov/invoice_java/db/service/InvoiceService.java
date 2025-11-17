package ru.gorbov.invoice_java.db.service;

import ru.gorbov.invoice_java.db.entity.Invoice;
import ru.gorbov.invoice_java.db.repositories.JpaRepository;
import ru.gorbov.invoice_java.dto.InvoiceDto;
import ru.gorbov.invoice_java.mapper.Mapper;

import java.util.UUID;

public class InvoiceService extends BaseService<InvoiceDto, Invoice> {

    public InvoiceService(JpaRepository<Invoice, UUID> repository, Mapper<InvoiceDto, Invoice> mapper) {
        super(repository, mapper);
    }
}

