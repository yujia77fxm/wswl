package com.wswl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransferEntity implements Serializable {

    private static final long serialVersionUID = -880556268070777592L;
    private Integer id;//id
    private String base58Check;//
    private String fromAddress;
    private String toAddress;
    private String tranctionId;
    private String tokenId;

    private Long   amount;
    private Boolean isSuccess;
    private Boolean isConfirmed;
    private Date createTime;

    public TransferEntity(String fromAddress, String toAddress, Long amount, Boolean isSuccess) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
        this.isSuccess = isSuccess;
    }
}
