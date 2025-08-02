package com.sunbeam.entities;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Credentials {
    private String email;
    private String password;
}