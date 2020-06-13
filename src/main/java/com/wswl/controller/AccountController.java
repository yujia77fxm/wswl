package com.wswl.controller;


import com.wswl.entity.AddressEntity;
import com.wswl.service.AccountService;
import com.wswl.util.WalletApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
//        try {
//            WalletApiUtil.getInstance().transferAsset(para);
//        }catch (IOException e1){
//            //return ""
//        } catch( CipherException e2){
//
//        } catch( CancelException e3){
//
//        }
        return para.toString();
        //return ResponseEntity.status(HttpStatus.FOUND).body(accoutService.getAdress());
    }
    @GetMapping("/createAdress")
    public String createAdress() {
        AddressEntity entity =  WalletApiUtil.generateLocalAddress();
        accoutService.insertAddress(entity);
        return entity.getHexAddress();
        //return ResponseEntity.status(HttpStatus.FOUND).body(accoutService.getAdress());
    }
    @GetMapping("/transferAsset")
    public String transferAsset(@RequestParam(value="fromAddress", required=true) String fromAddress,
                                @RequestParam(value="toAddress", required=true) String toAddress,
                                ) {

        AddressEntity entity =  accoutService.getAdressEntity(fromAddress);
        accoutService.insertAddress(entity);
        return entity.getHexAddress();
        //return ResponseEntity.status(HttpStatus.FOUND).body(accoutService.getAdress());
    }
}
