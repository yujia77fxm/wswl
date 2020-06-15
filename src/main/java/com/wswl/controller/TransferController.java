package com.wswl.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.wswl.entity.AddressEntity;
import com.wswl.entity.TransferEntity;
import com.wswl.entity.TransferResult;
import com.wswl.service.AccountService;
import com.wswl.service.TransferService;
import com.wswl.util.WalletApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tron.core.exception.CancelException;
import org.tron.core.exception.CipherException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private AccountService accoutService;
    @Autowired
    private TransferService transferService;

    @GetMapping("/transferAsset")
    public ResponseEntity transferAsset(@RequestParam(value="fromAddress", required=true) String fromAddress,
                                         @RequestParam(value="toAddress", required=true) String toAddress,
                                         @RequestParam(value="tokenId", required=true) String tokenId,
                                         @RequestParam(value="amount", required=true) long amount
    ) {

        AddressEntity entity =  accoutService.getAdressEntity(fromAddress);
        TransferResult result = null;
        String privateKey = "";

        if(entity != null){
            privateKey = entity.getPrivateKey();
        }
        try {
            result =  WalletApiUtil.transferAssetTrc10(fromAddress,toAddress,tokenId,amount,privateKey);
        }catch ( InvalidProtocolBufferException e){
        }catch (CancelException | CipherException | IOException e){
        }

        if(result != null && result.isResult()) {
            TransferEntity transferEntity = new TransferEntity(fromAddress,toAddress,amount,result.isResult());
            transferEntity.setTranctionId(result.getTxid());
            transferEntity.setTokenId(tokenId);
            transferService.insertTransfer(transferEntity);
            return ResponseEntity.status(HttpStatus.OK).body(result.getTxid());
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("transfer failed");
        }
        //return result;
    }
    @GetMapping("/getTransfer")
    public ResponseEntity getTransferEntity(@RequestParam(value="fromAddress", required=true) String fromAddress,
                                            @RequestParam(value="toAddress", required=true) String toAddress
                                            ){

        TransferEntity transferEntity = new TransferEntity(fromAddress,toAddress,0L,false);
        List<TransferEntity> list = transferService.getTransferEntity(transferEntity);

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }


}
