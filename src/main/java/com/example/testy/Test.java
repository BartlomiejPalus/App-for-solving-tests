package com.example.testy;

import java.util.List;

public class Test {
	private String name;
	private List<Question> questionsList;

	public Test(String name, List<Question> questionsList) {
		this.name = name;
		this.questionsList = questionsList;
	}

	public List<Question> getQuestionsList() {
		return questionsList;
	}
}
