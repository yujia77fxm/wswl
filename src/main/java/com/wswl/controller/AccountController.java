package com.wswl.controller;


import com.alibaba.fastjson.JSON;
import com.wswl.entity.AddressEntity;
import com.wswl.entity.ApiResp;
import com.wswl.entity.BaseEnumError;
import com.wswl.entity.WSResponseEntity;
import com.wswl.mode.pojo.CreateAddressBean;
import com.wswl.service.AccountService;
import com.wswl.util.EncryptUtils;
import com.wswl.util.WalletApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


/**
 * 获得地址
 */

@RestController
@RequestMapping("/address")
public class AccountController {

    @Autowired
    private AccountService accoutService;


    @PostMapping("/createAdress")
    public ApiResp createAdress(@RequestBody CreateAddressBean createAddressBean) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("plat",createAddressBean.getPlat());
        map.put("usn",createAddressBean.getUsn());

        String sign = EncryptUtils.getSignData(map);
        if(!sign.equalsIgnoreCase(createAddressBean.getSign())){
            return ApiResp.retFail(BaseEnumError.FAIL);//("bad sign");
        }

        AddressEntity entity =  WalletApiUtil.generateLocalAddress();
        entity.setUsername(createAddressBean.getUsn());
        accoutService.insertAddress(entity);
        return ApiResp.retOK(entity.getBase58Check());
    }

}
