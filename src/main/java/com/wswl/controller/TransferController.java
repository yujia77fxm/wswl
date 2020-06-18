package com.wswl.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.wswl.entity.*;
import com.wswl.mode.pojo.TransferBean;
import com.wswl.service.AccountService;
import com.wswl.service.TransferService;
import com.wswl.util.EncryptUtils;
import com.wswl.util.WalletApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tron.core.exception.CancelException;
import org.tron.core.exception.CipherException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.wswl.entity.BaseEnumError.WALLETFAIL;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private AccountService accoutService;
    @Autowired
    private TransferService transferService;

    @PostMapping("/transferAsset")
    public ApiResp transferAsset(@RequestBody TransferBean transferBean) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("fromAddress",transferBean.getFromAddress());
        map.put("toAddress",transferBean.getToAddress());
        map.put("tokenId",transferBean.getTokenId());
        map.put("amount",transferBean.getAmount());

        String sign = EncryptUtils.getSignData(map);
        if(!sign.equalsIgnoreCase(transferBean.getSign())){
             return ApiResp.retFail(BaseEnumError.FAIL);
        }

        AddressEntity entity =  accoutService.getAdressEntity(transferBean.getFromAddress());
        TransferResult result = null;
        String privateKey = "";

        if(entity != null){
            privateKey = entity.getPrivateKey();
        }
        try {
            result =  WalletApiUtil.transferAssetTrc10(transferBean.getFromAddress(),
                                                        transferBean.getToAddress(),
                                                        transferBean.getTokenId(),
                                                        transferBean.getAmount(),
                                                        privateKey);
        }catch ( InvalidProtocolBufferException e){
            return ApiResp.retFail(WALLETFAIL);//("bad sign");
        }catch (CancelException | CipherException | IOException e){
            return ApiResp.retFail(WALLETFAIL);//("bad sign");
        }

        if(result != null && result.isResult()) {
            TransferEntity transferEntity = new TransferEntity(transferBean.getFromAddress(),
                                                                transferBean.getToAddress(),
                                                                transferBean.getAmount(),
                                                                result.isResult());
            transferEntity.setTranctionId(result.getTxid());
            transferEntity.setTokenId(transferBean.getTokenId());
            transferService.insertTransfer(transferEntity);
            return ApiResp.retOK(result.getTxid());
        }else {
            return ApiResp.retFail(BaseEnumError.FAIL);//("bad sign");

        }
        //return result;
    }
    @GetMapping("/getTransfer")
    public ApiResp getTransferEntity(@RequestParam(value="fromAddress", required=true) String fromAddress,
                                            @RequestParam(value="toAddress", required=true) String toAddress
                                            ){

        TransferEntity transferEntity = new TransferEntity(fromAddress,toAddress,0L,false);
        List<TransferEntity> list = transferService.getTransferEntity(transferEntity);

        return ApiResp.retOK(list);
    }


}
