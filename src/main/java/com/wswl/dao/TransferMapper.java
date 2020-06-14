package com.wswl.dao;

import com.wswl.entity.AddressEntity;
import com.wswl.entity.TransferEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransferMapper {

    List<TransferEntity> getTransferEntity(TransferEntity entity);
    void  insertTranfer(TransferEntity entity);
}
