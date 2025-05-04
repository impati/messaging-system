package com.example.impati.messaging_system.domain;

public interface ClientRepository {

    void save(Client client);

    Client getById(String clientId);

    Client getByClientName(String clientName);
}
