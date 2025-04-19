package com.example.impati.messaging_system.adaptor.out.data;

import com.example.impati.messaging_system.domain.Client;
import com.example.impati.messaging_system.domain.ClientRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class MemoryClientRepository implements ClientRepository {

    private final Map<String, Client> store = new HashMap<>();

    @Override
    public void save(final Client client) {
        store.put(client.clientId(), client);
    }

    @Override
    public Client getById(final String clientId) {
        return store.get(clientId);
    }
}
