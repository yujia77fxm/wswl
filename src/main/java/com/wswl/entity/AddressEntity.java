package com.wswl.entity;

import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.sql.Date;

@Data
@ToString(exclude = "body")
public class AddressEntity implements Serializable {
    private static final long serialVersionUID = -880556268070777592L;
    private Integer id;//id
    private String base58Check;//
    private String hexAddress;//Hex地址
    private String publicKey;
    private String privateKey;
    private Date createTime;
}
