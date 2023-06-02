package com.example.testy;

import java.util.LinkedList;
import java.util.List;

public class Question {
	private final String content;
	private final List<Answer> answers;
	private final int goodAnswerPoints;
	private final int badAnswerPoints;

	Question(String content, int goodAnswerPoints, int badAnswerPoints) {
		this.content = content;
		answers = new LinkedList<>();
		this.goodAnswerPoints = goodAnswerPoints;
		this.badAnswerPoints = badAnswerPoints;
	}

	public void addAnswer(Answer answer) {
		answers.add(answer);
	}

	public String getContent() {
		return content;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public int getGoodAnswerPoints() {
		return goodAnswerPoints;
	}

	public int getBadAnswerPoints() {
		return badAnswerPoints;
	}
}
