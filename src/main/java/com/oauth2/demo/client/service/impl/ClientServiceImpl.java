package com.oauth2.demo.client.service.impl;

import com.oauth2.demo.client.entity.Client;
import com.oauth2.demo.client.mapper.ClientMapper;
import com.oauth2.demo.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientMapper clientMapper;

    @Override
    public List<Client> getAll() {
        return null;
    }

    @Override
    public Client getClientById(String clientId) {
        Client client = this.clientMapper.getClientById(clientId);
        return client;
    }

    @Override
    public int insert(Client client) {
        return 0;
    }

    @Override
    public int update(Client client) {
        return 0;
    }

    @Override
    public int deleteById(String clientId) {
        return 0;
    }
}
