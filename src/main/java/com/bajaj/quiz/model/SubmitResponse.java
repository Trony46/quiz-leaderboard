package com.bajaj.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitResponse {
    private Boolean isCorrect;
    private Boolean isIdempotent;
    private Integer submittedTotal;
    private Integer expectedTotal;
    private String message;
}