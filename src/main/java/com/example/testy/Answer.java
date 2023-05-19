package com.example.testy;

public class Answer {
	private String content;
	private Boolean isCorrect;

	Answer() {}

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
