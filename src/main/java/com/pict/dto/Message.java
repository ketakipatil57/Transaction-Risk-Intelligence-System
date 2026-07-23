package com.pict.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Message {


    private String role;
    private String content;
}
