package com.wswl.controller;


import com.google.protobuf.InvalidProtocolBufferException;
import com.wswl.entity.AddressEntity;
import com.wswl.entity.TransferEntity;
import com.wswl.service.AccountService;
import com.wswl.service.TransferService;
import com.wswl.util.WalletApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tron.core.exception.CancelException;
import org.tron.walletserver.WalletApi;


/**
 * 获得地址
 */

@RestController
@RequestMapping("/address")
public class AccountController {

    @Autowired
    private AccountService accoutService;


    @GetMapping("/getAdress")
    public String getAdress() {
        String[] para = {"TWbcHNCYzqAGbrQteKnseKJdxfzBHyTfuh","1000099","1000"};

        return para.toString();
        //return ResponseEntity.status(HttpStatus.FOUND).body(accoutService.getAdress());
    }
    @GetMapping("/createAdress")
    public String createAdress() {
        AddressEntity entity =  WalletApiUtil.generateLocalAddress();
        accoutService.insertAddress(entity);
        return entity.getBase58Check();
        //return ResponseEntity.status(HttpStatus.FOUND).body(accoutService.getAdress());
    }

}
