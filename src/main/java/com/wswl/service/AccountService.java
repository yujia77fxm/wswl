package com.wswl.service;

import com.wswl.entity.AddressEntity;
import com.wswl.entity.TransferEntity;

public interface AccountService {

    String getAdress();
    void insertAddress(AddressEntity entity);
    AddressEntity getAdressEntity(String fromAddress);
}
