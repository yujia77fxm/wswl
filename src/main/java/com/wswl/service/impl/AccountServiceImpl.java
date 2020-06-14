package com.wswl.service.impl;

import com.wswl.dao.AccountMapper;
import com.wswl.entity.AddressEntity;
import com.wswl.entity.TransferEntity;
import com.wswl.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accoutMapper;

    @Override
    public String getAdress() {
        String getAdress = "getAdress";
        accoutMapper.insert();
        return getAdress;
    }

    @Override
    public void insertAddress(AddressEntity entity) {
        accoutMapper.insertAddress(entity);
    }

    @Override
    public AddressEntity getAdressEntity(String fromAddress) {
        return accoutMapper.getAddressEntity(fromAddress);
    }

}
