# Quiz Leaderboard System

A Spring Boot application that polls a quiz API 10 times, deduplicates score events, builds a leaderboard and submits it.

## How to Run

1. Open the project in IntelliJ
2. Change `regNo` in `QuizService.java` to your registration number
3. Run `QuizLeaderboardApplication.java`
4. Wait ~50 seconds for all 10 polls to complete
5. Check console for `isCorrect: true`

## How It Works

**Poll** — Calls `/quiz/messages?regNo=...&poll=0` through `poll=9` with a 5 second gap between each call.

**Deduplicate** — Each event has a `roundId` and `participant`. I combine these as a key (`R1Alice`) and track them in a `HashSet`. If I've seen that key before, I skip the event.

**Aggregate** — After deduplication, I loop through clean events and add up scores per participant using a `HashMap`.

**Submit** — Sorted leaderboard (high to low) is posted once to `/quiz/submit`.

## Project Structure
src/main/java/com/quiz/leaderboard/
├── QuizLeaderboardApplication.java   → entry point
├── model/                            → POJO classes matching API JSON
├── service/QuizService.java          → all logic lives here
└── runner/QuizRunner.java            → runs on startup, calls service