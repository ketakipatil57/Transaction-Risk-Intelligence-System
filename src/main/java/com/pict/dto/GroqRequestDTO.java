package com.pict.dto;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroqRequestDTO {

    private String model;
    private List<Message> messages;
}
