package com.example.algopulse.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private String sessionId;
    private List<Integer> array;
    private int target;
}