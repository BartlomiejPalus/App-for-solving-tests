package com.example.testy;

public class Answer {
	private final String content;
	private final Boolean isCorrect;

	Answer(String content, Boolean isCorrect){
		this.content = content;
		this.isCorrect = isCorrect;
	}

	public String getContent() {
		return content;
	}

	public Boolean getCorrect() {
		return isCorrect;
	}
}
