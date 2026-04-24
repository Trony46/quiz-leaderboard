package com.bajaj.quiz;

import com.bajaj.quiz.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class QuizService {


    String regNo = "RA2311056030019";
    String baseUrl = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";

    RestTemplate restTemplate = new RestTemplate();

    // get 10
    public List<QuizResponse> pollApi() throws InterruptedException {
        List<QuizResponse> allResponses = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            System.out.println("Calling poll " + i);
            String url = baseUrl + "/quiz/messages?regNo=" + regNo + "&poll=" + i;

            allResponses.add(restTemplate.getForObject(url, QuizResponse.class));

            Thread.sleep(5000);
        }
        return allResponses;
    }

    //  remove duplicates
    public List<Events> removeDuplicates(List<QuizResponse> responses) {

        Set<String> seen = new HashSet<>();
        List<Events> cleanEvents = new ArrayList<>();

        for (QuizResponse response : responses) {
            for (Events event : response.getEvents()) {

                String key = event.getRoundId() + event.getParticipant();
                if (!seen.contains(key)) {
                    seen.add(key);
                    cleanEvents.add(event);
                }
                else {
                    System.out.println("duplicate   " + key);
                }
            }
        }

        return cleanEvents;
    }

    // add up scores and sort
    public List<LeaderboardEntry> buildLeaderboard(List<Events> events) {

        Map<String, Integer> scoreMap = new HashMap<>();

        for (Events event : events) {
            String name = event.getParticipant();
            int score = event.getScore();

            if (scoreMap.containsKey(name)) {
                scoreMap.put(name, scoreMap.get(name) + score);
            } else {
                scoreMap.put(name, score);
            }
        }

        // map to list
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            leaderboard.add(new LeaderboardEntry(entry.getKey(), entry.getValue()));
        }

        // sort by score high to low
        leaderboard.sort((a, b) -> b.getTotalScore() - a.getTotalScore());

        return leaderboard;
    }

    //  submit
    public void submitLeaderboard(List<LeaderboardEntry> leaderboard) {

        String url = baseUrl + "/quiz/submit";
        SubmitRequest request = new SubmitRequest(regNo, leaderboard);

        SubmitResponse response = restTemplate.postForObject(url, request, SubmitResponse.class);

        System.out.println("submitted!");
        assert response != null;
        System.out.println("isCorrect: " + response.getIsCorrect());
        System.out.println("message: " + response.getMessage());
        System.out.println("submittedTotal: " + response.getSubmittedTotal());
        System.out.println("expectedTotal: " + response.getExpectedTotal());
    }
}