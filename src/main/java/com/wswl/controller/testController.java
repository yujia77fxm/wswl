package com.wswl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tron.common.utils.Utils;
import org.tron.protos.Protocol;
import org.tron.walletcli.WalletApiWrapper;
import org.tron.walletserver.WalletApi;

@RestController
public class testController {
    @GetMapping
    public String hello(){
         WalletApiWrapper walletApiWrapper = new WalletApiWrapper();
        String address = "TRfwwLDpr4excH4V4QzghLEsdYwkapTxnm";
        byte[] addressBytes = WalletApi.decodeFromBase58Check(address);
        if (addressBytes == null) {
            return "address parameter not available!! ";
        }

        Protocol.Account account = WalletApi.queryAccount(addressBytes);
        if (account == null) {
            System.out.println("GetAccount failed !!!!");
            return "account == null----";
        } else {
            System.out.println(Utils.formatMessageString(account));
            return account.toString() + "  show account";
        }
    }
}
