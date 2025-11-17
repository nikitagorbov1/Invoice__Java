package ru.gorbov.invoice_java.mapper;


import ru.gorbov.invoice_java.db.entity.Entitytable;
import ru.gorbov.invoice_java.dto.CRUDable;

public interface Mapper<T extends CRUDable, D extends Entitytable> {
    T toDto(D entity);

    D toCreateEntity(T dto);

}
