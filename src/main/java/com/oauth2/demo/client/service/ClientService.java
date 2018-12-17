package com.oauth2.demo.client.service;

import com.oauth2.demo.client.entity.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAll();

    Client getClientById(String clientId);

    int insert(Client client);

    int update(Client client);

    int deleteById(String clientId);
}
