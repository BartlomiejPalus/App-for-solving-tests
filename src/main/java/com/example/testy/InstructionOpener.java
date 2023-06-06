package com.example.testy;

import javafx.application.Platform;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class InstructionOpener {
	public static void openPage(String pageName) {
		File file = new File("src\\main\\resources\\com\\example\\testy\\instruction\\" + pageName);
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (file.exists()) {
				try {
					Platform.runLater(() -> {
						try {
							desktop.open(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}