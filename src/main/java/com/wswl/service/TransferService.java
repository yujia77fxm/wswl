package com.wswl.service;

import com.wswl.entity.TransferEntity;
import java.util.List;

public interface TransferService {
    List<TransferEntity> getTransferEntity(TransferEntity entity);
    void insertTransfer(TransferEntity transferEntity);
}
