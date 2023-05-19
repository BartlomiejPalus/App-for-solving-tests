package com.example.testy;

import java.util.LinkedList;
import java.util.List;

public class Test {
	private String name;
	private String category;
	private int amountOfQuestionsInApproach;
	private int amountOfApproach;
	private boolean isOverviewable;
	private String password;
	private boolean isPublic;
	private String creatorID;
	private List<Question> questionsList;

	public Test(String name, String category, int amountOfQuestionsInApproach, int amountOfApproach,
				boolean isOverviewable, String password, boolean isPublic, String creatorID) {
		this.name = name;
		this.category = category;
		this.amountOfQuestionsInApproach = amountOfQuestionsInApproach;
		this.amountOfApproach = amountOfApproach;
		this.isOverviewable = isOverviewable;
		this.password = password;
		this.isPublic = isPublic;
		this.creatorID = creatorID;
		this.questionsList = new LinkedList<>();
	}

	public void addQuestion(Question question) {
		questionsList.add(question);
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public int getAmountOfQuestionsInApproach() {
		return amountOfQuestionsInApproach;
	}

	public int getAmountOfApproach() {
		return amountOfApproach;
	}

	public boolean isOverviewable() {
		return isOverviewable;
	}

	public String getPassword() {
		return password;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public String getCreatorID() {
		return creatorID;
	}

	public List<Question> getQuestionsList() {
		return questionsList;
	}
}
