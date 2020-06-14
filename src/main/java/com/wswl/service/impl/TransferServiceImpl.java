package com.wswl.service.impl;

import com.wswl.dao.AccountMapper;
import com.wswl.dao.TransferMapper;
import com.wswl.entity.AddressEntity;
import com.wswl.entity.TransferEntity;
import com.wswl.service.AccountService;
import com.wswl.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    private TransferMapper transferMapper;

    @Override
    public List<TransferEntity> getTransferEntity(TransferEntity entity) {
        return transferMapper.getTransferEntity( entity);
    }

    @Override
    public void insertTransfer(TransferEntity transferEntity) {
        transferMapper.insertTranfer(transferEntity);
    }
}
