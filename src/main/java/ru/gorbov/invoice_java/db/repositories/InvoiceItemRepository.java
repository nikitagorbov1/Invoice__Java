package ru.gorbov.invoice_java.db.repositories;

import ru.gorbov.invoice_java.db.entity.InvoiceItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InvoiceItemRepository implements JpaRepository<InvoiceItem, UUID> {

    private final ConcurrentMap<UUID, InvoiceItem> storage = new ConcurrentHashMap<>();

    @Override
    public List<InvoiceItem> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<InvoiceItem> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public InvoiceItem save(InvoiceItem invoiceItem) {
        UUID id = invoiceItem.getId();
        if (id == null) {
            id = UUID.randomUUID();
            invoiceItem.setId(id);
        }
        storage.put(id, invoiceItem);
        return invoiceItem;
    }

    @Override
    public InvoiceItem update(InvoiceItem invoiceItem) {
        UUID id = invoiceItem.getId();
        if (id == null || !storage.containsKey(id)) {
            throw new IllegalArgumentException("Invoice item not found: " + id);
        }
        storage.put(id, invoiceItem);
        return invoiceItem;
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
