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
import java.util.ResourceBundle;

import javax.servlet.ServletException;

import java.io.*;
import java.time.LocalDate;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class QuizscheduleController {
	
	//Members of the Quiz Schedule GUI controller integrating objects from the quizretakes package
	public static quizzes quizList;
	public static retakes retakesList;
	public static courseBean course;
	
	//FXML members to integrate the java code and FXML code
	@FXML
	private TextField textbox;
	@FXML
	private Button pushButton;

	//Public access constructor for an instance of this class
	public QuizscheduleController() {
	}
	//Initialize method for the FXML document to call at the end of execution
	@FXML
	private void initialize() {	
	}
	//Method to set the courseID string from the user input
	@FXML
	public void getCourse() {
		Main.courseID = textbox.getText();
	}
	//Method to accept the courseID into the course reader, find the data for the course, and bring up the next GUI page
	@FXML
	public void buttonManage(ActionEvent event) throws IOException {
		//checks that there is a course ID entered and that it has a data file to read from the course reader
		if (Main.courseID != null && !Main.courseID.isEmpty()) { // If not, ask for one.
			courseReader cr = new courseReader();
			Main.courseFileName = Main.dataLocation + Main.courseBase + "-" + Main.courseID + ".xml";
			try {
				course = cr.read(Main.courseFileName);
			} catch (Exception e) {
				String message = "<p>Can't find the data files for course ID " + Main.courseID + ". You can try again.";
				System.out.println(message);
				return;
			}
			//If there is a matching data file, save the information into the appropriate fields with the following format
			Main.daysAvailable = Integer.parseInt(course.getRetakeDuration());
			// Filenames to be built from above and the courseID
			Main.quizzesFileName = Main.dataLocation + Main.quizzesBase + "-" + Main.courseID + ".xml";
			Main.retakesFileName = Main.dataLocation + Main.retakesBase + "-" + Main.courseID + ".xml";
			Main.apptsFileName = Main.dataLocation + Main.apptsBase + "-" + Main.courseID + ".txt";
			// Load the quizzes and the retake times from disk
			quizList = new quizzes();
			retakesList = new retakes();
			quizReader qr = new quizReader();
			retakesReader rr = new retakesReader();

			try { // Read the files and print the form
				quizList = qr.read(Main.quizzesFileName);
				retakesList = rr.read(Main.retakesFileName);
				this.printQuizScheduleForm(quizList, retakesList, course);
			} catch (Exception e) {
				String message = "<p>Can't find the data files for course ID " + Main.courseID + ". You can try again.";
				// servletUtils.printNeedCourseID (out, thisServlet, message);
			}
		try {
    	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ScheduleForm.fxml"));
    	    Parent root1 = (Parent) fxmlLoader.load();
    	           
    	    Stage stage = new Stage();
    	    stage.setScene(new Scene(root1));
    	    stage.show();
    	    Main.stg.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		}
	}
		private void printQuizScheduleForm( quizzes quizList, retakes retakesList, courseBean course)
				throws ServletException, IOException {
			// Check for a week to skip
			boolean skip = false;
			LocalDate startSkip = course.getStartSkip();
			LocalDate endSkip = course.getEndSkip();

			boolean retakePrinted = false;

			System.out.println("<body onLoad=\"setFocusMain()\" bgcolor=\"#DDEEDD\">");
			System.out.println("");
			System.out.println("<center><h2>GMU quiz retake scheduler for class " + course.getCourseTitle() + "</h2></center>");
			System.out.println("<hr/>");
			System.out.println("");

			// print the main form
			System.out.println("<form name='quizSchedule' method='post' action=' ?courseID=" + Main.courseID + "' >");
			System.out.print("  <p>You can sign up for quiz retakes within the next two weeks. ");
			System.out.print("Enter your name (as it appears on the class roster), ");
			System.out.println("then select which date, time, and quiz you wish to retake from the following list.");
			System.out.println("  <br/>");

			LocalDate today = LocalDate.now();
			LocalDate endDay = today.plusDays(new Long(Main.daysAvailable));
			LocalDate origEndDay = endDay;
			// if endDay is between startSkip and endSkip, add 7 to endDay
			if (!endDay.isBefore(startSkip) && !endDay.isAfter(endSkip)) { // endDay is in a skip week, add 7 to endDay
				endDay = endDay.plusDays(new Long(7));
				skip = true;
			}

			System.out.print("  <p>Today is ");
			System.out.println((today.getDayOfWeek()) + ", " + today.getMonth() + " " + today.getDayOfMonth());
			System.out.print("  <p>Currently scheduling quizzes for the next two weeks, until ");
			System.out.println((endDay.getDayOfWeek()) + ", " + endDay.getMonth() + " " + endDay.getDayOfMonth());
			System.out.println("  <br/>");

			System.out.print("  <p>Name: ");
			System.out.println("  <input type='text' id='studentName' name='studentName' size='50' />");
			System.out.println("  <br/>");
			System.out.println("  <br/>");

			System.out.println("  <table border=1 style='background-color:#99dd99'><tr><td>"); // outer table for borders
			System.out.println("  <tr><td>");
			for (retakeBean r : retakesList) {
				LocalDate retakeDay = r.getDate();
				if (!(retakeDay.isBefore(today)) && !(retakeDay.isAfter(endDay))) {
					// if skip && retakeDay is after the skip week, print a white bg message
					if (skip && retakeDay.isAfter(origEndDay)) { // A "skip" week such as spring break.
						System.out.println("    <table border=1 width=100% style='background-color:white'>"); // inner table to
																										// format skip week
						System.out.println("      <tr><td>Skipping a week, no quiz or retakes.");
						System.out.println("    </table>"); // inner table for skip week
						// Just print for the FIRST retake day after the skip week
						skip = false;
					}
					retakePrinted = true;
					System.out.println("    <table width=100%>"); // inner table to format one retake
					// format: Friday, January 12, at 10:00am in EB 4430
					System.out.println("    <tr><td>" + retakeDay.getDayOfWeek() + ", " + retakeDay.getMonth() + " "
							+ retakeDay.getDayOfMonth() + ", at " + r.timeAsString() + " in " + r.getLocation());

					for (quizBean q : quizList) {
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
			for (retakeBean r : retakesList) {
				System.out.print("  <tr><td>");
				System.out.print(r);
				System.out.println("  </td></td>");
			}
			System.out.println("</table>");
		}


}
