package com.junyi.permission.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserPassword {
    private String guid;
    private String password;
    private String newPassword;
}
