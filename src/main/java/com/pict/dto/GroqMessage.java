package com.pict.dto;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroqMessage {

    private String role;
    private String content;

}
