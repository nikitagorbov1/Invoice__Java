package ru.gorbov.invoice_java.db.repositories;

import ru.gorbov.invoice_java.db.entity.Client;
import ru.gorbov.invoice_java.db.entity.Invoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientRepository implements JpaRepository<Client, UUID> {

    private final ConcurrentMap<UUID, Client> storage = new ConcurrentHashMap<>();

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Client> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Client save(Client client) {
        UUID id = client.getId();
        if (id == null) {
            id = UUID.randomUUID();
            client.setId(id);
        }
        ensureInvoiceLinks(client);
        storage.put(id, client);
        return client;
    }

    @Override
    public Client update(Client client) {
        UUID id = client.getId();
        if (id == null || !storage.containsKey(id)) {
            throw new IllegalArgumentException("Client not found: " + id);
        }
        ensureInvoiceLinks(client);
        storage.put(id, client);
        return client;
    }

    private void ensureInvoiceLinks(Client client) {
        if (client.getInvoices() == null) {
            client.setInvoices(new ArrayList<>());
            return;
        }
        for (Invoice invoice : client.getInvoices()) {
            if (invoice.getClient() == null) {
                invoice.setClient(client);
            }
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
