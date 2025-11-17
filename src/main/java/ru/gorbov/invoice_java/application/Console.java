package ru.gorbov.invoice_java.application;



import ru.gorbov.invoice_java.db.repositories.*;
import ru.gorbov.invoice_java.db.service.ClientService;
import ru.gorbov.invoice_java.db.service.InvoiceItemService;
import ru.gorbov.invoice_java.db.service.InvoiceService;
import ru.gorbov.invoice_java.db.service.ProductService;
import ru.gorbov.invoice_java.db.service.WarehouseService;

import ru.gorbov.invoice_java.dto.ClientDto;
import ru.gorbov.invoice_java.dto.InvoiceDto;
import ru.gorbov.invoice_java.dto.InvoiceItemDto;
import ru.gorbov.invoice_java.dto.ProductDto;
import ru.gorbov.invoice_java.dto.WarehouseDto;
import ru.gorbov.invoice_java.mapper.ClientMapper;
import ru.gorbov.invoice_java.mapper.InvoiceItemMapper;
import ru.gorbov.invoice_java.mapper.InvoiceMapper;
import ru.gorbov.invoice_java.mapper.ProductMapper;
import ru.gorbov.invoice_java.mapper.WarehouseMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

public class Console {

    private final Scanner scanner = new Scanner(System.in);

    private final ClientService clientService;
    private final ProductService productService;
    private final WarehouseService warehouseService;
    private final InvoiceService invoiceService;
    private final InvoiceItemService invoiceItemService;

    public Console() {
        ClientMapper clientMapper = new ClientMapper();
        ProductMapper productMapper = new ProductMapper();
        WarehouseMapper warehouseMapper = new WarehouseMapper();
        InvoiceItemMapper invoiceItemMapper = new InvoiceItemMapper();
        InvoiceMapper invoiceMapper = new InvoiceMapper(invoiceItemMapper);

        ClientRepository clientRepository = new ClientRepository();
        ProductRepository productRepository = new ProductRepository();
        WarehouseRepository warehouseRepository = new WarehouseRepository();
        InvoiceRepository invoiceRepository = new InvoiceRepository();
        InvoiceItemRepository invoiceItemRepository = new InvoiceItemRepository();

        this.clientService = new ClientService(clientRepository, clientMapper);
        this.productService = new ProductService(productRepository, productMapper);
        this.warehouseService = new WarehouseService(warehouseRepository, warehouseMapper);
        this.invoiceService = new InvoiceService(invoiceRepository, invoiceMapper);
        this.invoiceItemService = new InvoiceItemService(invoiceItemRepository, invoiceItemMapper);
    }


    public void run() {
        System.out.println("=== Invoice Management Console ===");
        boolean running = true;
        while (running) {
            System.out.println("\nMain menu:");
            System.out.println("1 - Clients");
            System.out.println("2 - Products");
            System.out.println("3 - Warehouses");
            System.out.println("4 - Invoices");
            System.out.println("5 - Invoice items");
            System.out.println("0 - Exit");
            String choice = prompt("Choose option: ");
            switch (choice) {
                case "1":
                    manageClients();
                    break;
                case "2":
                    manageProducts();
                    break;
                case "3":
                    manageWarehouses();
                    break;
                case "4":
                    manageInvoices();
                    break;
                case "5":
                    manageInvoiceItems();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Unknown option: " + choice);
                    break;
            }
        }
        System.out.println("Bye!");
    }

    private void manageClients() {
        boolean back = false;
        while (!back) {
            System.out.println("\nClients menu:");
            System.out.println("1 - List");
            System.out.println("2 - Find by id");
            System.out.println("3 - Create");
            System.out.println("4 - Update");
            System.out.println("5 - Delete");
            System.out.println("0 - Back");
            String choice = prompt("Choose option: ");
            switch (choice) {
                case "1":
                    clientService.findAll().forEach(this::printClient);
                    break;
                case "2": {
                    UUID id = readUUID("Client id: ");
                    if (id != null) {
                        try {
                            printClient(clientService.findById(id));
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                }
                case "3": {
                    ClientDto dto = new ClientDto();
                    dto.setName(prompt("Name: "));
                    dto.setPhone(blankToNull(prompt("Phone (optional): ")));
                    dto.setEmail(blankToNull(prompt("Email (optional): ")));
                    ClientDto saved = clientService.save(dto);
                    System.out.println("Created client " + saved.getId());
                    break;
                }
                case "4": {
                    UUID id = readUUID("Client id: ");
                    if (id != null) {
                        try {
                            ClientDto existing = clientService.findById(id);
                            String name = prompt("Name (" + existing.getName() + "): ");
                            String phone = prompt("Phone (" + nvl(existing.getPhone()) + "): ");
                            String email = prompt("Email (" + nvl(existing.getEmail()) + "): ");
                            if (!name.isBlank()) {
                                existing.setName(name);
                            }
                            if (!phone.isBlank()) {
                                existing.setPhone(blankToNull(phone));
                            }
                            if (!email.isBlank()) {
                                existing.setEmail(blankToNull(email));
                            }
                            ClientDto updated = clientService.update(existing);
                            System.out.println("Updated client " + updated.getId());
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                }
                case "5": {
                    UUID id = readUUID("Client id: ");
                    if (id != null) {
                        clientService.delete(id);
                        System.out.println("Deleted client " + id);
                    }
                    break;
                }
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Unknown option: " + choice);
                    break;
            }
        }
    }

    private void manageProducts() {
        boolean back = false;
        while (!back) {
            System.out.println("\nProducts menu:");
            System.out.println("1 - List");
            System.out.println("2 - Find by id");
            System.out.println("3 - Create");
            System.out.println("4 - Update");
            System.out.println("5 - Delete");
            System.out.println("0 - Back");
            String choice = prompt("Choose option: ");
            switch (choice) {
                case "1":
                    productService.findAll().forEach(this::printProduct);
                    break;
                case "2": {
                    UUID id = readUUID("Product id: ");
                    if (id != null) {
                        try {
                            printProduct(productService.findById(id));
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                }
                case "3": {
                    ProductDto dto = new ProductDto();
                    dto.setName(prompt("Name: "));
                    dto.setPrice(readBigDecimal("Price: "));
                    dto.setParentProductId(readUUIDNullable("Parent product id (optional): "));
                    ProductDto saved = productService.save(dto);
                    System.out.println("Created product " + saved.getId());
                    break;
                }
                case "4": {
                    UUID id = readUUID("Product id: ");
                    if (id != null) {
                        try {
                            ProductDto existing = productService.findById(id);
                            String name = prompt("Name (" + existing.getName() + "): ");
                            String priceInput = prompt("Price (" + existing.getPrice() + "): ");
                            UUID parentId = readUuidForUpdate("Parent product id", existing.getParentProductId());
                            if (!name.isBlank()) {
                                existing.setName(name);
                            }
                            if (!priceInput.isBlank()) {
                                BigDecimal price = parseBigDecimal(priceInput);
                                if (price != null) {
                                    existing.setPrice(price);
                                }
                            }
                            existing.setParentProductId(parentId);
                            ProductDto updated = productService.update(existing);
                            System.out.println("Updated product " + updated.getId());
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                }
                case "5": {
                    UUID id = readUUID("Product id: ");
                    if (id != null) {
                        productService.delete(id);
                        System.out.println("Deleted product " + id);
                    }
                    break;
                }
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Unknown option: " + choice);
                    break;
            }
        }
    }

    private void manageWarehouses() {
        boolean back = false;
        while (!back) {
            System.out.println("\nWarehouses menu:");
            System.out.println("1 - List");
            System.out.println("2 - Find by id");
            System.out.println("3 - Create");
            System.out.println("4 - Update");
            System.out.println("5 - Delete");
            System.out.println("0 - Back");
            String choice = prompt("Choose option: ");
            switch (choice) {
                case "1":
                    warehouseService.findAll().forEach(this::printWarehouse);
                    break;
                case "2": {
                    UUID id = readUUID("Warehouse id: ");
                    if (id != null) {
                        try {
                            printWarehouse(warehouseService.findById(id));
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                }
                case "3": {
                    WarehouseDto dto = new WarehouseDto();
                    dto.setName(prompt("Name: "));
                    dto.setLocation(blankToNull(prompt("Location (optional): ")));
                    dto.setParentWarehouseId(readUUIDNullable("Parent warehouse id (optional): "));
                    WarehouseDto saved = warehouseService.save(dto);
                    System.out.println("Created warehouse " + saved.getId());
                    break;
                }
                case "4": {
                    UUID id = readUUID("Warehouse id: ");
                    if (id != null) {
                        try {
                            WarehouseDto existing = warehouseService.findById(id);
                            String name = prompt("Name (" + existing.getName() + "): ");
                            String location = prompt("Location (" + nvl(existing.getLocation()) + "): ");
                            UUID parentId = readUuidForUpdate("Parent warehouse id", existing.getParentWarehouseId());
                            if (!name.isBlank()) {
                                existing.setName(name);
                            }
                            if (!location.isBlank()) {
                                existing.setLocation(blankToNull(location));
                            }
                            existing.setParentWarehouseId(parentId);
                            WarehouseDto updated = warehouseService.update(existing);
                            System.out.println("Updated warehouse " + updated.getId());
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                }
                case "5": {
                    UUID id = readUUID("Warehouse id: ");
                    if (id != null) {
                        warehouseService.delete(id);
                        System.out.println("Deleted warehouse " + id);
                    }
                    break;
                }
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Unknown option: " + choice);
                    break;
            }
        }
    }

    private void manageInvoices() {
        boolean back = false;
        while (!back) {
            System.out.println("\nInvoices menu:");
            System.out.println("1 - List");
            System.out.println("2 - Find by id");
            System.out.println("3 - Create");
            System.out.println("4 - Update");
            System.out.println("5 - Delete");
            System.out.println("0 - Back");
            String choice = prompt("Choose option: ");
            switch (choice) {
                case "1":
                    invoiceService.findAll().forEach(this::printInvoice);
                    break;
                case "2": {
                    UUID id = readUUID("Invoice id: ");
                    if (id != null) {
                        try {
                            printInvoice(invoiceService.findById(id));
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                }
                case "3":
                    createInvoice();
                    break;
                case "4":
                    updateInvoice();
                    break;
                case "5": {
                    UUID id = readUUID("Invoice id: ");
                    if (id != null) {
                        deleteInvoice(id);
                    }
                    break;
                }
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Unknown option: " + choice);
                    break;
            }
        }
    }

    private void manageInvoiceItems() {
        boolean back = false;
        while (!back) {
            System.out.println("\nInvoice items menu:");
            System.out.println("1 - List");
            System.out.println("2 - Find by id");
            System.out.println("3 - Create");
            System.out.println("4 - Update");
            System.out.println("5 - Delete");
            System.out.println("0 - Back");
            String choice = prompt("Choose option: ");
            switch (choice) {
                case "1":
                    invoiceItemService.findAll().forEach(this::printInvoiceItem);
                    break;
                case "2": {
                    UUID id = readUUID("Invoice item id: ");
                    if (id != null) {
                        try {
                            printInvoiceItem(invoiceItemService.findById(id));
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                }
                case "3":
                    createInvoiceItem();
                    break;
                case "4":
                    updateInvoiceItem();
                    break;
                case "5":
                    deleteInvoiceItem();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Unknown option: " + choice);
                    break;
            }
        }
    }

    private void createInvoice() {
        InvoiceDto dto = new InvoiceDto();
        dto.setClientId(readUUIDNullable("Client id (optional): "));
        dto.setWarehouseId(readUUIDNullable("Warehouse id (optional): "));
        LocalDateTime date = readDateNullable("Date (yyyy-MM-ddTHH:mm) leave empty for now: ");
        dto.setDate(date != null ? date : LocalDateTime.now());
        dto.setItems(readInvoiceItems(null));
        InvoiceDto saved = invoiceService.save(dto);
        syncInvoiceItemsRepository(saved);
        System.out.println("Created invoice " + saved.getId());
    }

    private void updateInvoice() {
        UUID id = readUUID("Invoice id: ");
        if (id == null) {
            return;
        }
        try {
            InvoiceDto existing = invoiceService.findById(id);
            UUID clientId = readUuidForUpdate("Client id", existing.getClientId());
            UUID warehouseId = readUuidForUpdate("Warehouse id", existing.getWarehouseId());
            LocalDateTime date = readDateNullable("Date (" + existing.getDate() + "): ");
            List<InvoiceItemDto> items = readInvoiceItems(existing.getId());

            if (clientId != null) {
                existing.setClientId(clientId);
            }
            if (warehouseId != null) {
                existing.setWarehouseId(warehouseId);
            }
            if (date != null) {
                existing.setDate(date);
            }
            if (!items.isEmpty()) {
                existing.setItems(items);
            }
            InvoiceDto updated = invoiceService.update(existing);
            syncInvoiceItemsRepository(updated);
            System.out.println("Updated invoice " + updated.getId());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deleteInvoice(UUID id) {
        try {
            InvoiceDto existing = invoiceService.findById(id);
            if (existing.getItems() != null) {
                for (InvoiceItemDto item : existing.getItems()) {
                    invoiceItemService.delete(item.getId());
                }
            }
        } catch (IllegalArgumentException ignored) {
        }
        invoiceService.delete(id);
        System.out.println("Deleted invoice " + id);
    }

    private void createInvoiceItem() {
        InvoiceItemDto dto = new InvoiceItemDto();
        dto.setInvoiceId(readUUIDNullable("Invoice id (optional): "));
        dto.setProductId(readUUIDNullable("Product id (optional): "));
        dto.setQuantity(readInt("Quantity: "));
        dto.setTotalPrice(readBigDecimal("Total price: "));
        InvoiceItemDto saved = invoiceItemService.save(dto);
        attachInvoiceItemToInvoice(saved);
        System.out.println("Created invoice item " + saved.getId());
    }

    private void updateInvoiceItem() {
        UUID id = readUUID("Invoice item id: ");
        if (id == null) {
            return;
        }
        try {
            InvoiceItemDto existing = invoiceItemService.findById(id);
            UUID invoiceId = readUuidForUpdate("Invoice id", existing.getInvoiceId());
            UUID productId = readUuidForUpdate("Product id", existing.getProductId());
            Integer quantity = readIntNullable("Quantity (" + existing.getQuantity() + "): ");
            BigDecimal totalPrice = readBigDecimalNullable("Total price (" + existing.getTotalPrice() + "): ");

            if (invoiceId != null) {
                existing.setInvoiceId(invoiceId);
            }
            if (productId != null) {
                existing.setProductId(productId);
            }
            if (quantity != null) {
                existing.setQuantity(quantity);
            }
            if (totalPrice != null) {
                existing.setTotalPrice(totalPrice);
            }
            InvoiceItemDto updated = invoiceItemService.update(existing);
            attachInvoiceItemToInvoice(updated);
            System.out.println("Updated invoice item " + updated.getId());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deleteInvoiceItem() {
        UUID id = readUUID("Invoice item id: ");
        if (id == null) {
            return;
        }
        try {
            InvoiceItemDto existing = invoiceItemService.findById(id);
            if (existing.getInvoiceId() != null) {
                try {
                    InvoiceDto invoice = invoiceService.findById(existing.getInvoiceId());
                    List<InvoiceItemDto> items = invoice.getItems().stream()
                            .filter(item -> !item.getId().equals(id))
                            .collect(Collectors.toCollection(ArrayList::new));
                    invoice.setItems(items);
                    invoiceService.update(invoice);
                } catch (IllegalArgumentException ignored) {
                }
            }
        } catch (IllegalArgumentException ignored) {
        }
        invoiceItemService.delete(id);
        System.out.println("Deleted invoice item " + id);
    }

    private List<InvoiceItemDto> readInvoiceItems(UUID invoiceId) {
        List<InvoiceItemDto> result = new ArrayList<>();
        while (true) {
            String addMore = prompt("Add invoice item? (y/n): ");
            if (!"y".equalsIgnoreCase(addMore)) {
                break;
            }
            InvoiceItemDto item = new InvoiceItemDto();
            item.setInvoiceId(invoiceId);
            item.setProductId(readUUIDNullable("Product id (optional): "));
            item.setQuantity(readInt("Quantity: "));
            item.setTotalPrice(readBigDecimal("Total price: "));
            result.add(item);
        }
        return result;
    }

    private void syncInvoiceItemsRepository(InvoiceDto invoice) {
        if (invoice.getItems() == null) {
            return;
        }
        for (InvoiceItemDto item : invoice.getItems()) {
            item.setInvoiceId(invoice.getId());
            if (item.getId() == null) {
                InvoiceItemDto saved = invoiceItemService.save(item);
                item.setId(saved.getId());
            } else {
                try {
                    invoiceItemService.update(item);
                } catch (IllegalArgumentException ex) {
                    InvoiceItemDto saved = invoiceItemService.save(item);
                    item.setId(saved.getId());
                }
            }
        }
    }

    private void attachInvoiceItemToInvoice(InvoiceItemDto item) {
        if (item.getInvoiceId() == null || item.getId() == null) {
            return;
        }
        try {
            InvoiceDto invoice = invoiceService.findById(item.getInvoiceId());
            List<InvoiceItemDto> items = invoice.getItems() == null
                    ? new ArrayList<>()
                    : new ArrayList<>(invoice.getItems());
            items.removeIf(existing -> existing.getId().equals(item.getId()));
            items.add(item);
            invoice.setItems(items);
            invoiceService.update(invoice);
        } catch (IllegalArgumentException ex) {
            System.out.println("Invoice not found for item: " + item.getInvoiceId());
        }
    }

    private void printClient(ClientDto dto) {
        System.out.println("Client{id=" + dto.getId()
                + ", name='" + dto.getName() + '\''
                + ", phone='" + dto.getPhone() + '\''
                + ", email='" + dto.getEmail() + '\''
                + ", invoiceIds=" + dto.getInvoiceIds()
                + "}");
    }

    private void printProduct(ProductDto dto) {
        System.out.println("Product{id=" + dto.getId()
                + ", name='" + dto.getName() + '\''
                + ", price=" + dto.getPrice()
                + ", parentProductId=" + dto.getParentProductId()
                + "}");
    }

    private void printWarehouse(WarehouseDto dto) {
        System.out.println("Warehouse{id=" + dto.getId()
                + ", name='" + dto.getName() + '\''
                + ", location='" + dto.getLocation() + '\''
                + ", parentWarehouseId=" + dto.getParentWarehouseId()
                + "}");
    }

    private void printInvoice(InvoiceDto dto) {
        System.out.println("Invoice{id=" + dto.getId()
                + ", clientId=" + dto.getClientId()
                + ", warehouseId=" + dto.getWarehouseId()
                + ", date=" + dto.getDate()
                + ", items=" + (dto.getItems() == null ? 0 : dto.getItems().size())
                + "}");
        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            dto.getItems().forEach(item -> System.out.println("  - " + item));
        }
    }

    private void printInvoiceItem(InvoiceItemDto dto) {
        System.out.println("InvoiceItem{id=" + dto.getId()
                + ", invoiceId=" + dto.getInvoiceId()
                + ", productId=" + dto.getProductId()
                + ", quantity=" + dto.getQuantity()
                + ", totalPrice=" + dto.getTotalPrice()
                + "}");
    }

    private String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private UUID readUUID(String message) {
        String input = prompt(message);
        if (input.isBlank()) {
            System.out.println("Value is required.");
            return null;
        }
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid UUID: " + input);
            return null;
        }
    }

    private UUID readUUIDNullable(String message) {
        String input = prompt(message);
        if (input.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid UUID: " + input);
            return null;
        }
    }

    private BigDecimal readBigDecimal(String message) {
        while (true) {
            String input = prompt(message);
            BigDecimal value = parseBigDecimal(input);
            if (value != null) {
                return value;
            }
            System.out.println("Invalid decimal value. Try again.");
        }
    }

    private UUID readUuidForUpdate(String label, UUID currentValue) {
        String input = prompt(label + " (current: " + nvl(currentValue) + ", '-' to clear, empty to keep): ");
        if (input.isBlank()) {
            return currentValue;
        }
        if ("-".equals(input)) {
            return null;
        }
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid UUID: " + input);
            return currentValue;
        }
    }

    private BigDecimal readBigDecimalNullable(String message) {
        String input = prompt(message);
        if (input.isBlank()) {
            return null;
        }
        return parseBigDecimal(input);
    }

    private BigDecimal parseBigDecimal(String input) {
        try {
            return new BigDecimal(input);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid decimal: " + input);
            return null;
        }
    }

    private int readInt(String message) {
        while (true) {
            String input = prompt(message);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid integer: " + input);
            }
        }
    }

    private Integer readIntNullable(String message) {
        String input = prompt(message);
        if (input.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid integer: " + input);
            return null;
        }
    }

    private LocalDateTime readDateNullable(String message) {
        String input = prompt(message);
        if (input.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(input);
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid date-time format. Expected yyyy-MM-ddTHH:mm");
            return null;
        }
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private String nvl(Object value) {
        return value == null ? "-" : value.toString();
    }
}

