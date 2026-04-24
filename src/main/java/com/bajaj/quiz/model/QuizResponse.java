package com.bajaj.quiz.model;

import lombok.Data;

import java.util.List;

@Data
public class QuizResponse {
    private String regNo;
    private int pollIndex;
    private List<Events> events;
}