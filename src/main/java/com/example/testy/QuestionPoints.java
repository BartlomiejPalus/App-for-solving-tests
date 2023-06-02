package com.example.testy;

public class QuestionPoints {
	private final int pointsForGoodAnswer;
	private final int pointsForBadAnswer;

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
