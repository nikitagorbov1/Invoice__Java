package ru.gorbov.invoice_java.db.repositories;

import ru.gorbov.invoice_java.db.entity.Warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WarehouseRepository implements JpaRepository<Warehouse, UUID> {

    private final ConcurrentMap<UUID, Warehouse> storage = new ConcurrentHashMap<>();

    @Override
    public List<Warehouse> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Warehouse> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        UUID id = warehouse.getId();
        if (id == null) {
            id = UUID.randomUUID();
            warehouse.setId(id);
        }
        storage.put(id, warehouse);
        return warehouse;
    }

    @Override
    public Warehouse update(Warehouse warehouse) {
        UUID id = warehouse.getId();
        if (id == null || !storage.containsKey(id)) {
            throw new IllegalArgumentException("Warehouse not found: " + id);
        }
        storage.put(id, warehouse);
        return warehouse;
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
