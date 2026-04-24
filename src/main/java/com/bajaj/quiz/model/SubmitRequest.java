package com.bajaj.quiz.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class SubmitRequest {
    private String regNo;
    private List<LeaderboardEntry> leaderboard;
}