package com.bajaj.quiz;

import com.bajaj.quiz.model.Events;
import com.bajaj.quiz.model.LeaderboardEntry;
import com.bajaj.quiz.model.QuizResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuizRunner implements CommandLineRunner {

    QuizService quizService;

    // constructor injection
    public QuizRunner(QuizService quizService) {
        this.quizService = quizService;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("starting...");

        //  1
        List<QuizResponse> responses = quizService.pollApi();

        //  2
        List<Events> cleanEvents = quizService.removeDuplicates(responses);

        //  3
        List<LeaderboardEntry> leaderboard = quizService.buildLeaderboard(cleanEvents);

        // print
        System.out.println("Leaderboard:");
        for (LeaderboardEntry entry : leaderboard) {
            System.out.println(entry.getParticipant() + " -> " + entry.getTotalScore());
        }

        //  4
        quizService.submitLeaderboard(leaderboard);
    }
}