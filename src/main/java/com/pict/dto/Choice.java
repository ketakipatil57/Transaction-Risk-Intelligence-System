package com.pict.dto;

import lombok.*;

import java.net.URLConnection;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Choice {

    private GroqMessage message;
}
