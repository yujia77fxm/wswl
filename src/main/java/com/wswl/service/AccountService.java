package com.wswl.service;

import com.wswl.entity.AddressEntity;

public interface AccountService {

    String getAdress();
    void insertAddress(AddressEntity entity);
    AddressEntity getAdressEntity(String fromAddress);
}
