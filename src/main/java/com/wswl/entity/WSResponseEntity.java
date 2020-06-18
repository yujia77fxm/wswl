package com.wswl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
public class WSResponseEntity implements Serializable {
    int status;
    int code ;
    String msg;
    String data;
}
