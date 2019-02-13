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
import java.time.*;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class ScheduleFormController {
	//Members of the Schedule Form GUI controller
	public static String stuName;
	//FXML members to integrate FXML document and java code
	@FXML
    private MenuButton dateMenu;

    @FXML
    private MenuItem act1;

    @FXML
    private MenuItem act2;

    @FXML
    private TextField date;
    
    @FXML
    private TextField endDate;

    @FXML
    private TextField name;

    @FXML
    private MenuButton timeMenu;
    @FXML 
	private ResourceBundle resources;
    @FXML
    private MenuButton quizMenu;
    //Public access constructor to create an instance of ScheduleForm
    public ScheduleFormController() {
    }
    //Initialize method to be called by the FXML document after execution
    @FXML
	private void initialize() {
	}
    //Method to store the user input for name.
    @FXML
    public void getName(){
    	stuName = name.getText();
    }
    public void printQuizSchedule() {
    	boolean skip = false;
    	LocalDate startSkip = QuizscheduleController.course.getStartSkip();
    	LocalDate endSkip = QuizscheduleController.course.getEndSkip();

    	boolean retakePrinted = false;
    	LocalDate today = LocalDate.now();
    	LocalDate endDay = today.plusDays(new Long(Main.daysAvailable));
    	LocalDate origEndDay = endDay;
    	// if endDay is between startSkip and endSkip, add 7 to endDay
    	if (!endDay.isBefore(startSkip) && !endDay.isAfter(endSkip)) { // endDay is in a skip week, add 7 to endDay
    		endDay = endDay.plusDays(new Long(7));
    		skip = true;
    	}
    	String d = (today.getDayOfWeek() + ", " + today.getMonth() + " " + today.getDayOfMonth());
    	date = new TextField(d);
    	date.setEditable(false);
    	String ed = (endDay.getDayOfWeek() + ", " + endDay.getMonth() + " " + endDay.getDayOfMonth());
    	endDate = new TextField(ed);
    	endDate.setEditable(false);

    	for (retakeBean r : QuizscheduleController.retakesList) {
			LocalDate retakeDay = r.getDate();
			if (!(retakeDay.isBefore(today)) && !(retakeDay.isAfter(endDay))) {
				// if skip && retakeDay is after the skip week, print a white bg message
				if (skip && retakeDay.isAfter(origEndDay)) { // A "skip" week such as spring break.
					System.out.println(" Skipping a week, no quiz or retakes.");
					// Just print for the FIRST retake day after the skip week
					skip = false;
				}
				retakePrinted = true;
				System.out.println(retakeDay.getDayOfWeek() + ", " + retakeDay.getMonth() + " "
						+ retakeDay.getDayOfMonth() + ", at " + r.timeAsString() + " in " + r.getLocation());
				for (quizBean q : QuizscheduleController.quizList) {
					LocalDate quizDay = q.getDate();
					LocalDate lastAvailableDay = quizDay.plusDays(new Long(Main.daysAvailable));
				// To retake a quiz on a given retake day, the retake day must be within two
				// ranges:
				// quizDay <= retakeDay <= lastAvailableDay --> (!quizDay > retakeDay) &&
				// !(retakeDay > lastAvailableDay)
				// today <= retakeDay <= endDay --> !(today > retakeDay) && !(retakeDay >
				// endDay)
		
						if (!quizDay.isAfter(retakeDay) && !retakeDay.isAfter(lastAvailableDay) && !today.isAfter(retakeDay)
								&& !retakeDay.isAfter(endDay)) {
							System.out.println("    <tr><td align='right'><label for='q" + q.getID() + "r" + r.getID() + "'>Quiz "
									+ q.getID() + " from " + quizDay.getDayOfWeek() + ", " + quizDay.getMonth() + " "
									+ quizDay.getDayOfMonth() + ":</label> ");
							// Value is "retakeID:quiziD"
							System.out.println("    <td><input type='checkbox' name='retakeReqs'  value='" + r.getID() + Main.separator
									+ q.getID() + "' id='q" + q.getID() + "r" + r.getID() + "'>");
						}
					}
				}
				if (retakePrinted) {
					System.out.println("  </table>");
					System.out.println("  <tr><td>");
					retakePrinted = false;
				}
			}
    	System.out.println(
					"  <tr><td align='middle'><button id='submitRequest' type='submit' name='submitRequest' style='font-size:large'>Submit request</button>");
    	System.out.println("  </table>");
    	System.out.println("</form>");

    	System.out.println("<br/>");
    	System.out.println("<br/>");
    	System.out.println("<br/>");
    	System.out.println("<br/>");
    	System.out.println("<table border=1>");
    	System.out.println("<tr><td align='middle'>All quiz retake opportunities</td></tr>");
			for (retakeBean r : QuizscheduleController.retakesList) {
				System.out.print("  <tr><td>");
				System.out.print(r);
				System.out.println("  </td></td>");
			}
			System.out.println("</table>");
		}

    }