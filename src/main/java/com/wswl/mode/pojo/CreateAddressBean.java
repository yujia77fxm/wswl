package com.wswl.mode.pojo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class CreateAddressBean implements Serializable {
    int plat;
    String usn;
    String sign;
}
