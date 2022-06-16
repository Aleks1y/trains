package com.example.trains.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomQueryResponseDTO {
    private List<String> headers;
    private List<Object> rows;
}
