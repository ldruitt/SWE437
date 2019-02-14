package application;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import quizretakes.courseBean;
import quizretakes.courseReader;
import quizretakes.quizBean;
import quizretakes.quizReader;
import quizretakes.quizschedule;
import quizretakes.quizzes;
import quizretakes.retakeBean;
import quizretakes.retakes;
import quizretakes.retakesReader;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.scene.control.TextArea;

public class ProfessorViewController {
	//FXML members of the GUI
	@FXML
	private TextArea retakes;
	//constructor
	public ProfessorViewController() {
	}
	 //Initialize method to be called by the FXML document after execution
    @FXML
	private void initialize() {
	}
    @FXML
    public void setRetakes() {
    	
    	LocalDate today = LocalDate.now();
		LocalDate endDay = today.plusDays(new Long(Main.daysAvailable));
		LocalDate origEndDay = endDay;
    	for (retakeBean r : QuizscheduleController.retakesList) {
			LocalDate retakeDay = r.getDate();
			if (!(retakeDay.isBefore(today)) && !(retakeDay.isAfter(endDay))) {
				// if skip && retakeDay is after the skip week, print a white bg message
    	retakes = new TextArea(retakeDay.getDayOfWeek() + ", " + retakeDay.getMonth() + " "
				+ retakeDay.getDayOfMonth() + ", at " + r.timeAsString() + " in " + r.getLocation());
    	retakes.setEditable(false);
    }
    	}

}
}