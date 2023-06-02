package com.example.testy;

public enum TestCategories {
	MATEMATYKA("Matematyka"), FIZYKA("Fizyka"), INFORMATYKA("Informatyka"),
	GEOGRAFIA("Geografia"), INNE("Inne");
	private final String text;
	TestCategories(String printedValue) {
		this.text = printedValue;
	}

	public String getText() {
		return text;
	}
}
