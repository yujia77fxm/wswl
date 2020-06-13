package com.wswl.controller;

import com.wswl.util.WalletApiUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tron.common.utils.Utils;
import org.tron.core.exception.CancelException;
import org.tron.core.exception.CipherException;
import org.tron.protos.Protocol;
import org.tron.walletcli.WalletApiWrapper;
import org.tron.walletserver.WalletApi;

import java.io.IOException;

@RestController
@RequestMapping("/test")
public class testController {
    @GetMapping("/hello")
    public String hello(){

        return WalletApiUtil.getInstance().generateAddress();
        //return  WalletApiUtil.getInstance().GetAddress("TWbcHNCYzqAGbrQteKnseKJdxfzBHyTfuh");
    }


}
