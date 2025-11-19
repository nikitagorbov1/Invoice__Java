package ru.gorbov.invoice_java.db.repositories;

import ru.gorbov.invoice_java.db.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProductRepository implements JpaRepository<Product, UUID> {

    private final ConcurrentMap<UUID, Product> storage = new ConcurrentHashMap<>();

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Product> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Product save(Product product) {
        UUID id = product.getId();
        if (id == null) {
            id = UUID.randomUUID();
            product.setId(id);
        }
        storage.put(id, product);
        return product;
    }

    @Override
    public Product update(Product product) {
        UUID id = product.getId();
        if (id == null || !storage.containsKey(id)) {
            throw new IllegalArgumentException("Product not found: " + id);
        }
        storage.put(id, product);
        return product;
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
