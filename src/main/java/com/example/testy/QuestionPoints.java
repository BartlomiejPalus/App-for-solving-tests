package com.example.testy;

public class QuestionPoints {
	int pointsForGoodAnswer;
	int pointsForBadAnswer;

	QuestionPoints(int pointsForGoodAnswer, int pointsForBadAnswer) {
		this.pointsForGoodAnswer = pointsForGoodAnswer;
		this.pointsForBadAnswer = pointsForBadAnswer;
	}

	public int getPointsForGoodAnswer() {
		return pointsForGoodAnswer;
	}

	public int getPointsForBadAnswer() {
		return pointsForBadAnswer;
	}
}
