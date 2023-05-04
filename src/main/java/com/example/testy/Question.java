package com.example.testy;

import java.util.ArrayList;
import java.util.List;

public class Question {
	private String content;
	private List<Answer> answers;

	Question() {
		answers = new ArrayList<>();
	}

	Question(String content, Answer answer1, Answer answer2) {
		this.content = content;
		answers = new ArrayList<>();
		answers.add(answer1);
		answers.add(answer2);
	}

	public List<Answer> getAnswers() {
		return answers;
	}
}
