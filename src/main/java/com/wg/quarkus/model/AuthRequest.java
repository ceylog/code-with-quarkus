package com.wg.quarkus.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

//for login endpoint
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthRequest {

    public String username;
    public String password;
}




