package com.pict.dto;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroqResponseDTO {
    private List<Choice> choices;
}
