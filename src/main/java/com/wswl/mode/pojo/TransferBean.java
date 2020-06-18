package com.wswl.mode.pojo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class TransferBean implements Serializable {
    String fromAddress;
    String toAddress;
    String tokenId;
    String sign;
    Long amount;
}
