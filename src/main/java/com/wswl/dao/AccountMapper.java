package com.wswl.dao;

import com.wswl.entity.AddressEntity;
import com.wswl.entity.TransferEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface AccountMapper {

    void insert();
    void insertAddress(AddressEntity entity);
    AddressEntity getAddressEntity(String fromAddress);
    void  tranferAsset(TransferEntity entity);
}
