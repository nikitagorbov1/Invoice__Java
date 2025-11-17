package ru.gorbov.invoice_java.db.service;

import ru.gorbov.invoice_java.db.entity.Client;
import ru.gorbov.invoice_java.db.repositories.JpaRepository;
import ru.gorbov.invoice_java.dto.ClientDto;
import ru.gorbov.invoice_java.mapper.Mapper;

import java.util.UUID;

public class ClientService extends BaseService<ClientDto, Client> {

    public ClientService(JpaRepository<Client, UUID> repository, Mapper<ClientDto, Client> mapper) {
        super(repository, mapper);
    }
}

