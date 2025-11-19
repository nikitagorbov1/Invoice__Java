package ru.gorbov.invoice_java.db.repositories;

import ru.gorbov.invoice_java.db.entity.Invoice;
import ru.gorbov.invoice_java.db.entity.InvoiceItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InvoiceRepository implements JpaRepository<Invoice, UUID> {

    private final ConcurrentMap<UUID, Invoice> storage = new ConcurrentHashMap<>();

    @Override
    public List<Invoice> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Invoice> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Invoice save(Invoice invoice) {
        UUID id = invoice.getId();
        if (id == null) {
            id = UUID.randomUUID();
            invoice.setId(id);
        }
        prepareInvoiceItems(invoice);
        storage.put(id, invoice);
        return invoice;
    }

    @Override
    public Invoice update(Invoice invoice) {
        UUID id = invoice.getId();
        if (id == null || !storage.containsKey(id)) {
            throw new IllegalArgumentException("Invoice not found: " + id);
        }
        prepareInvoiceItems(invoice);
        storage.put(id, invoice);
        return invoice;
    }

    private void prepareInvoiceItems(Invoice invoice) {
        if (invoice.getItems() == null) {
            invoice.setItems(new ArrayList<>());
            return;
        }
        for (InvoiceItem item : invoice.getItems()) {
            if (item.getId() == null) {
                item.setId(UUID.randomUUID());
            }
            item.setInvoice(invoice);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return id != null && storage.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (id != null) {
            storage.remove(id);
        }
    }
}
