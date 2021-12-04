
import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Project 4 - Learning Management Quiz Tool - Driver
 * Main Method to Execute program
 * <p>
 * 
 * @author Matt Hiatt, Aryan Mathur, Aniket Mohanty, and Nathan Lo
 * @version 11/15/2021
 */
public class Driver {

	public static void main(String[] args) {
		String userPrompt = "Enter a username: ";
		String passPrompt = "Enter a password: ";
		String invalidSelection = "Invalid Selection, Please Try Again.";
		String wrongType = "Please Enter an Integer.";
		String coursePrompt = "Enter a Course Name: ";
		
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to the Quiz Learning Program");
		String user = null;
		
		//Creates Login File 
		File logins = new File("login.txt");
		if (!logins.exists()) {
			try {
				logins.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Creates coursenames file
		File courseNames = new File("CourseNames.txt");
		if (!courseNames.exists()) {
			try {
				courseNames.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Start Menu
		int choice;
		do {
			try {
				System.out.println("1. Create Account\n2. Log in\n3. Exit");
				choice = input.nextInt();
				if (choice < 1 && choice > 3) {
					System.out.println(invalidSelection);
				}
			} catch (InputMismatchException e) {
				choice = 0;
				input.nextLine();
				System.out.println(wrongType);
			}
		} while (choice < 1 || choice > 3);
		
		if (choice == 1) {
			//Create Account
			//Enter classification
			String classification;
			do {
				try {
					System.out.println("1. Teacher\n2. Student");
					choice = input.nextInt();
					if (choice < 1 || choice > 2) {
						System.out.println(invalidSelection);
					}
				} catch (InputMismatchException e) {
					System.out.println(wrongType);
				}
			} while (choice < 1 || choice > 2);
			if (choice == 1) {
				classification = "Teacher";
			} else {
				classification = "Student";
			}
			
			//Enter username
			input.nextLine();
			do {
				System.out.println(userPrompt);
				user = input.nextLine();
				if (Login.isDuplicate(user)) {
					System.out.println("Sorry that username is taken, please try a new one.");
				}
				if (user.isBlank()) {
					System.out.println("Username cannot be blank!");
				}
			} while (Login.isDuplicate(user) || user.isBlank());
			
			//Enter Password
			String password;
			do {
				System.out.println(passPrompt);
				password = input.nextLine();
				if (password.isBlank()) {
					System.out.println("You must enter a password!");
				}
			} while (password.isBlank());
			
			Login.writeNewUser(classification, user, password);
		} else if (choice == 2) {
			//Sign in
			//Enter username
			input.nextLine();
			do {
				System.out.println(userPrompt);
				user = input.nextLine();
				if (!Login.isDuplicate(user)) {
					System.out.println("Username not found, please try again.");
				}
			} while (!Login.isDuplicate(user));
			
			//Enter Password
			String password;
			do {
				System.out.println(passPrompt);
				password = input.nextLine();
				if (!Login.login(user, password)) {
					System.out.println("Incorrect password, please try again.");
				}
			} while (!Login.login(user, password));
		}
		
		if (choice != 3) {
			//control flow for teacher and student
			String type = Login.getClassification(user);
			if (type.equals("Teacher")) {
				//Teacher implementation
				System.out.println("\nWelcome Teacher " + user);
				int ongoingChoice;
				do {
					do {
						try {
							System.out.println("1. Create Course\n2. View Course\n3. Exit");
							ongoingChoice = input.nextInt();
							if (ongoingChoice < 1 || ongoingChoice > 3) {
								System.out.println(invalidSelection);
							}
						} catch (InputMismatchException e) {
							ongoingChoice = 0;
							input.nextLine();
							System.out.println(wrongType);
						}
					} while (ongoingChoice < 1 || ongoingChoice > 3);
					if (ongoingChoice == 1) {
						input.nextLine();
						//Create Course
						//creates CourseNameQuizzesTitles file and adds name it to coursenames file
						String courseName;
						do {
							System.out.println(coursePrompt);
							courseName = input.nextLine(); 
							if (courseName.isBlank()) {
								System.out.println("Course Name cannot be blank!");
							}
						} while (courseName.isBlank());
						
						//check for duplicates in coursenames file
						//new code
						Teacher.createCourse(courseName);
						
					} else if (ongoingChoice == 2) {
						//View C
						//Can delete courses or view quiz
						
						//Finds course
						//Displays course list
						System.out.println("\nAvailable Courses:");
						Teacher.printCourses();
						System.out.println();
						System.out.println(coursePrompt);
						input.nextLine(); 
						String course = input.nextLine();
						
						//view course menu
						if (Teacher.checkCourseExistence(course)) {
							do {
								try {
									System.out.println("1. Delete Course\n2. View Quizzes");
									ongoingChoice = input.nextInt();
									if (ongoingChoice < 1 || ongoingChoice > 2) {
									    System.out.println(invalidSelection);
								    }
								} catch (InputMismatchException e) {
									ongoingChoice = 0;
									System.out.println(wrongType);
								}
							} while (ongoingChoice < 1 || ongoingChoice > 2);
							
							if (ongoingChoice == 1) {
								//delete course
								//delete coursenamequizzes file and delete from coursenames 
								//file, delete all associated quizzes
								Teacher.deleteCourse(course);
							} else {
								//quiz menu
								//displays quizzes
								do {
									try {
										System.out.println("\nView Menu\n1. Create Quiz\n2. Edit Quiz\n3. "
												  + "Delete Quiz\n4. View Submissions");
										ongoingChoice = input.nextInt();
										if (ongoingChoice < 1 || ongoingChoice > 4) {
											System.out.println("Invalid choice, please try again.");
										}
									} catch (InputMismatchException e) {
										System.out.println("Enter an integer choice");
										ongoingChoice = 0;
									}
								} while (ongoingChoice < 1 || ongoingChoice > 4);
								input.nextLine(); //clears return out of buffer
								
								if (ongoingChoice == 1) {
									//Create Quiz
									Teacher.createQuiz(input, course);
								} else if (ongoingChoice == 2) {
									//Edit Quiz
									//lists quizzes
									System.out.println("\nAvailable Quizzes:");
									Teacher.printQuizzes(course);
									System.out.println();
									System.out.println("Enter Quiz Name you wish to edit: ");
									String quizName = input.nextLine(); 
									System.out.println();
									//ensures quiz exists in coursenamesquizzes file
									if (Teacher.checkQuizExistence(course, quizName)) {
										Teacher.editQuiz(input, quizName, course);
									}
									
								} else if (ongoingChoice == 3) {
									//Delete Quiz
									//deletes quiz file and removes it from coursenamequizlist file
									System.out.println("Enter Quiz Name you wish to delete: ");
									String quizName = input.nextLine();
									//ensures quiz exists in coursenamesquizzes file
									if (Teacher.checkQuizExistence(course, quizName)) {
										Teacher.deleteQuiz(course, quizName);
									}
								} else {
									//View Submissions
									System.out.println("\nAvailable Quizzes:");
									Teacher.printQuizzes(course);
									System.out.println("\nEnter Quiz Name you wish to see submissions for: ");
									String quizName = input.nextLine(); //ensure quiz exists in coursenamesquizzes file
									if (Teacher.checkQuizExistence(course, quizName)) {
										System.out.println("\nAvailable Submissions");
										Teacher.printSubmissions(course, quizName);
										System.out.println("\nEnter Submission you wish to view");
										String submission = input.nextLine();
										System.out.println();
										if (Teacher.checkSubmissionExistence(course, quizName, submission)) {
											Teacher.viewSubmission(course, quizName, submission);
											System.out.println();
										}
									}
								}
								ongoingChoice = 1;	
							}
						}
					}
				} while (ongoingChoice > 0 && ongoingChoice < 3);
			} else {
				//Student implementation
				System.out.println("Welcome Student " + user);
				int ongoingChoice;
				do {
					do {
						try {
							System.out.println("1. Take Quiz\n2. View Submissions\n3. Exit");
							ongoingChoice = input.nextInt();
						} catch (InputMismatchException e) {
							System.out.println("Enter a integer choice");
							ongoingChoice = 4;
							input.nextLine();
						}
						if (ongoingChoice < 1 || ongoingChoice > 3) 
							System.out.println("Invalid selection");
						
					} while (ongoingChoice < 1 || ongoingChoice > 3);
					
					if (ongoingChoice == 1) {
						//Takes quiz
						//lists courses
						input.nextLine();
						System.out.println("\nAvailable Courses:");
						Teacher.printCourses();
						System.out.println("Which course would you like to access: ");
						String course = input.nextLine(); //check to sure exists in coursenames.txt
						if (Teacher.checkCourseExistence(course)) {
							//lists quizzes in course
							System.out.println("\nAvailable Quizzes:");
							Teacher.printQuizzes(course);
							System.out.println("Which quiz would you like to take: ");
							String quiz = input.nextLine(); //ensure exists in coursenamesquizzes	
							if (Teacher.checkQuizExistence(course, quiz)) {
								
								ArrayList<String> submission = Student.answer(input, course, quiz);
								String total = submission.get(submission.size() - 1);
								submission.remove(submission.size() - 1);
								Student.writeFile(course, quiz, user, submission, total);
							}
						}
						
					} else if (ongoingChoice == 2) {
						//views submissions
						//lists courses
						input.nextLine();
						System.out.println("\nAvailable Courses:");
						Teacher.printCourses();
						System.out.println("Which course would you like to access: ");
						String course = input.nextLine(); //check to sure exists in coursenames.txt
						if (Teacher.checkCourseExistence(course)) {
							//lists quizzes in course
							System.out.println("\nAvailable Quizzes:");
							Teacher.printQuizzes(course);
							System.out.println("Which quiz would you like to view your submissions for: ");
							String quiz = input.nextLine(); //ensure exists in coursenamesquizzes	
							if (Teacher.checkQuizExistence(course, quiz)) {
								Student.viewSubmissions(input, course, quiz, user);
							}
						}
					}
				} while(ongoingChoice == 1 || ongoingChoice == 2);
			}
		}
		System.out.println("Logged Out");
		System.out.println("Have a Good Day");
	}
}
