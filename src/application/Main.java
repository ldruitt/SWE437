package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import quizretakes.courseBean;
import quizretakes.courseReader;
import quizretakes.quizReader;
import quizretakes.quizschedule;
import quizretakes.quizzes;
import quizretakes.retakes;
import quizretakes.retakesReader;

public class Main extends Application {
	static Stage stg;
	// Data files
	// location maps to /webapps/offutt/WEB-INF/data/ from a terminal window.
	// These names show up in all servlets
	//static final String dataLocation = "/var/www/CS/webapps/offutt/WEB-INF/data/";
	static final String dataLocation = "asst2/src/quizretakes/";
	static final String separator = ",";
	static final String courseBase = "course";
	static final String quizzesBase = "quiz-orig";
	static final String retakesBase = "quiz-retakes";
	static final String apptsBase = "quiz-appts";

	// Filenames to be built from above and the courseID parameter
	public static String courseFileName;
	public static String quizzesFileName;
	public static String retakesFileName;
	public static String apptsFileName;

	// Passed as parameter and stored in course.xml file (format: "swe437")
	public static String courseID;
	// Stored in course.xml file, default 14
	// Number of days a retake is offered after the quiz is given
	public static int daysAvailable = 14;

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("QuizscheduleManager.fxml"));
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);

	}
}
