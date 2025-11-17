package ru.gorbov.invoice_java.mapper;

import ru.gorbov.invoice_java.db.entity.Invoice;
import ru.gorbov.invoice_java.db.entity.InvoiceItem;
import ru.gorbov.invoice_java.db.entity.Product;
import ru.gorbov.invoice_java.dto.InvoiceItemDto;

import java.util.UUID;

public class InvoiceItemMapper implements Mapper<InvoiceItemDto, InvoiceItem> {

    @Override
    public InvoiceItemDto toDto(InvoiceItem entity) {
        UUID invoiceId = entity.getInvoice() != null ? entity.getInvoice().getId() : null;
        UUID productId = entity.getProduct() != null ? entity.getProduct().getId() : null;
        return new InvoiceItemDto(
                entity.getId(),
                invoiceId,
                productId,
                entity.getQuantity(),
                entity.getTotalPrice()
        );
    }

    @Override
    public InvoiceItem toCreateEntity(InvoiceItemDto dto) {
        Invoice invoice = null;
        if (dto.getInvoiceId() != null) {
            invoice = Invoice.builder().id(dto.getInvoiceId()).build();
        }

        Product product = null;
        if (dto.getProductId() != null) {
            product = Product.builder().id(dto.getProductId()).build();
        }

        return InvoiceItem.builder()
                .id(dto.getId())
                .invoice(invoice)
                .product(product)
                .quantity(dto.getQuantity())
                .totalPrice(dto.getTotalPrice())
                .build();
    }
}

