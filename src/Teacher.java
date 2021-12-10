import javax.swing.*;
import java.util.*;
import java.io.*;

/**
 * Project 5 - Learning Management Quiz Tool - Teacher Contains methods for
 * teacher functionality
 * <p>
 *
 * @author Matt Hiatt, Aryan Mathur, Aniket Mohanty, and Nathan Lo
 * @version 11/15/2021
 */

public class Teacher {
	public static Object gatekeeper = new Object();
	
	public static void createCourse(String courseName) {
		synchronized (gatekeeper) {
			try {
				String nameAndQuiz = courseName + "Quizzes.txt";
				File newFileQuiz = new File(nameAndQuiz);
				if (checkCourseExistence(courseName, true)) {
					return;
				}
				if (newFileQuiz.createNewFile()) {
					JOptionPane.showMessageDialog(null, "New Course Created!", "Course Status",
							JOptionPane.INFORMATION_MESSAGE);
					PrintWriter myWriter = new PrintWriter(new FileOutputStream("CourseNames.txt", true));
					myWriter.write(courseName + "\n");
					myWriter.close();
				} else {
					JOptionPane.showMessageDialog(null, "This File Exists Already!", "File Status",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error writing to file. Try again.", "File Status",
						JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Undocumented Error. Try again.", "File Status",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static void deleteCourse(String courseName) {
		synchronized (gatekeeper) {
			try {
				BufferedReader bfr = new BufferedReader(new FileReader("CourseNames.txt")); // calling courseNames file
				// READING IN ALL THE COURSES, THEN FINDING THE NAME AND DELETING, THEN
				// REWRITING THE FILE
				ArrayList<String> allCourses = new ArrayList<>();
				String line = bfr.readLine();
				while (line != null) {
					allCourses.add(line);
					line = bfr.readLine();
				}
				boolean courseExists = false;
				for (int i = 0; i < allCourses.size(); i++) {
					if (allCourses.get(i).equals(courseName)) {
						allCourses.remove(i);
						courseExists = true;
					}
				}
				// Ends method if course doesn't already exist
				if (!courseExists) {
					JOptionPane.showMessageDialog(null, "No need to delete, " +
									"that course doesn't exist", "File Status", JOptionPane.ERROR_MESSAGE);
					bfr.close();
					return;
				}
				PrintWriter myWriter = new PrintWriter(new FileOutputStream("CourseNames.txt"));
				for (int j = 0; j < allCourses.size(); j++) {
					myWriter.write(allCourses.get(j) + "\n");
				}
				bfr.close();
				myWriter.close();
				// FINISHED REWRITING FILES AFTER DELETING NAME
			} catch (FileNotFoundException e) {
	
				JOptionPane.showMessageDialog(null, "Error writing to file." ,
						"File Status", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
	
				JOptionPane.showMessageDialog(null, "Undocumented Error." ,
						"File Status", JOptionPane.ERROR_MESSAGE);
			}
			// SEARCHING FOR ALL QUIZZES INSIDE courseName + Quizzes, then calling
			// deleteQuiz
			try {
				// Reading off all the names of the quizzes inside CourseName + Quizzes
				BufferedReader bfrQuizzes = new BufferedReader(new FileReader(courseName + "Quizzes.txt"));
				ArrayList<String> allQuizzes = new ArrayList<String>();
				String quizNames = bfrQuizzes.readLine();
				while (quizNames != null) {
					allQuizzes.add(quizNames);
					quizNames = bfrQuizzes.readLine();
				}
				// preparing to delete all the quizzes
	
				// preparing to delete all the quiz submissions and quiz itself after
				for (int k = 0; k < allQuizzes.size(); k++) {
					deleteQuiz(courseName, allQuizzes.get(k)); // calling deleteQuiz for each one
				}
				bfrQuizzes.close();
			} catch (FileNotFoundException e) {
	
				JOptionPane.showMessageDialog(null, "File Not Found." ,
						"File Status", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
	
				JOptionPane.showMessageDialog(null, "Error reading/writing to file." ,
						"File Status", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
	
				JOptionPane.showMessageDialog(null, "Undocumented Error." ,
						"File Status", JOptionPane.ERROR_MESSAGE);
	
			}
			// Deleting entire file with all Quiz names. Finished using the file to find all
			// quizzes, so can delete now.
			File deletedFile = new File(courseName + "Quizzes.txt");
			if (deletedFile.delete()) {
				JOptionPane.showMessageDialog(null, "Successfully deleted the file " + courseName + "Quizzes.txt" ,
						"File Status", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Error deleting file." ,
						"File Status", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// DELETES QUIZ AND QUIZ SUBMISSIONS
	public static void deleteQuiz(String courseName, String quizName) {
		synchronized (gatekeeper) {
			try {
				// read file to get name of all submissions
				BufferedReader bfrSubmissions = new BufferedReader(
						new FileReader(courseName + quizName + "Submissions.txt"));
				ArrayList<String> allSubmissions = new ArrayList<>();
				String submissionsNames = bfrSubmissions.readLine();
				while (submissionsNames != null) {
					allSubmissions.add(submissionsNames);
					submissionsNames = bfrSubmissions.readLine();
				}
				File deletedSubmissions;
				File deletedQuiz;
				// goes through all submissions for the quizzes, loops through to delete them.
				for (int a = 0; a < allSubmissions.size(); a++) {
					deletedSubmissions = new File(allSubmissions.get(a));
					if (deletedSubmissions.delete()) {
						JOptionPane.showMessageDialog(null, "Successfully deleted the file " + allSubmissions.get(a) ,
								"File Status", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Failed to delete file.",
								"File Status", JOptionPane.ERROR_MESSAGE);
					}
				}
				bfrSubmissions.close();
				// delete name from _course_name_ + Quizzes file.
				BufferedReader bfrCourseQuizzes = new BufferedReader(new FileReader(courseName + "Quizzes.txt"));
				// calling courseNames + Quizzes file
				// READING IN ALL THE COURSES, THEN FINDING THE NAME AND DELETING, THEN
				// REWRITING THE FILE
				ArrayList<String> allCourseQuizzes = new ArrayList<String>();
				String courseQuizline = bfrCourseQuizzes.readLine();
				while (courseQuizline != null) {
					allCourseQuizzes.add(courseQuizline);
					courseQuizline = bfrCourseQuizzes.readLine();
				}
				for (int b = 0; b < allCourseQuizzes.size(); b++) {
					if (allCourseQuizzes.get(b).equals(quizName)) {
						allCourseQuizzes.remove(b);
						bfrCourseQuizzes.close();
					}
				}
				PrintWriter myWriterForQuizzes = new PrintWriter(new FileOutputStream(courseName + "Quizzes.txt"));
				for (int j = 0; j < allCourseQuizzes.size(); j++) {
					myWriterForQuizzes.write(allCourseQuizzes.get(j) + "\n");
				}
				bfrCourseQuizzes.close();
				myWriterForQuizzes.close();
				// FINISHED REWRITING FILES AFTER DELETING NAME from courseName + Quizzes
	
				// delete quiz now that it is no longer needed for the submissions
				deletedQuiz = new File(courseName + quizName + ".txt");
				if (deletedQuiz.delete()) {
					JOptionPane.showMessageDialog(null,
							"Successfully deleted the file " + courseName + quizName + ".txt",
							"File Status", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null,
							"Failed to delete file." + courseName + quizName + ".txt",
							"File Status", JOptionPane.ERROR_MESSAGE);
				}
				File deletedSubmissionFile = new File(courseName + quizName + "Submissions.txt");
				if (deletedSubmissionFile.delete()) {
					JOptionPane.showMessageDialog(null,
							"Successfully deleted the file " + courseName + quizName + "Submissions.txt",
							"File Status", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null,
							"Failed to delete file.", "File Status", JOptionPane.ERROR_MESSAGE);
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null,
						"File Not Found.", "File Status", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Error Writing/Reading to File.", "File Status", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public static void createQuiz(String courseName) {
		synchronized (gatekeeper) {
			try {
				boolean contWriting = false; // variable for knowing if they want to keep writing quizzes (do while loop)
				String fileName = "";
				PrintWriter myWriter;
				String importOrCreate = JOptionPane.showInputDialog(null,
								"Would you like to import a quiz? Yes or No (If no, you will be directed to create a "
										+ "file inside the program immediately)");
				if (importOrCreate == null) {
					JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
							"Cancelled", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				// ^^checks if importing or creating
				// checks for valid input
				if (!(importOrCreate.equalsIgnoreCase("no") || importOrCreate.equalsIgnoreCase("yes"))) {
					while (!(importOrCreate.equalsIgnoreCase("no") || importOrCreate.equalsIgnoreCase("yes"))) {
						importOrCreate = JOptionPane.showInputDialog(null,
								"Please input a valid answer, Yes or No.\n" + "Would you like to import a quiz? " +
										"Yes or No (If no, you will be directed to "
										+ "create a file inside program immediately");
						if (importOrCreate == null) {
							JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
									"Cancelled", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					}
				}
				if (importOrCreate.equalsIgnoreCase("yes")) { // if importing, do this
					String premadeFile = JOptionPane.showInputDialog(null,"\nRemember File must be in " +
							"the format:\nName of Quiz\nQuestion\nChoice A\nChoice B\n"
							+ "Choice C\\nChoice D\nCorrect Choice\nPoints\n" + "What is the name of the file?");
					if (premadeFile == null) {
						JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
								"Cancelled", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					boolean check = false;
					do {
						try {
							quizImport(premadeFile, courseName);
						} catch (FileNotFoundException e) {
							check = true;
							JOptionPane.showMessageDialog(null, "File not found! Please try again",
									"File Status", JOptionPane.ERROR_MESSAGE);
							premadeFile = JOptionPane.showInputDialog(null,"\nRemember File must be in " +
									"the format:\nName of Quiz\nQuestion\nChoice A\nChoice B\n"
									+ "Choice C\\nChoice D\nCorrect Choice\nPoints\n" + "What is the name of the file?");
							if (premadeFile == null) {
								JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
										"Cancelled", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						} catch (IOException e) {
							check = true;
							JOptionPane.showMessageDialog(null, "Error, please try again.",
									"File Status", JOptionPane.ERROR_MESSAGE);
							premadeFile = JOptionPane.showInputDialog(null,"\nRemember File must be in " +
									"the format:\nName of Quiz\nQuestion\nChoice A\nChoice B\n"
									+ "Choice C\\nChoice D\nCorrect Choice\nPoints\n" + "What is the name of the file?");
							if (premadeFile == null) {
								JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
										"Cancelled", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						}
					} while (check);
				} else { // if creating, do this
					do {
						fileName = JOptionPane.showInputDialog(null, "What would you like to name the quiz?");
						if (fileName == null) {
							JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
									"Cancelled", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						if (fileName.isBlank()) {
							JOptionPane.showMessageDialog(null, "Quiz title cannot be blank.",
									"File Status", JOptionPane.ERROR_MESSAGE);
						}
					} while (fileName.isBlank());
					File newQuiz = new File(courseName + fileName + ".txt");
	
					if (newQuiz.createNewFile()) {
						JOptionPane.showMessageDialog(null, "Creating a new file!",
								"File Status", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Overwriting the file, it already exists!",
								"File Status", JOptionPane.INFORMATION_MESSAGE);
					}
					File newQuizSubmissions = new File(courseName + fileName + "Submissions.txt");
					myWriter = new PrintWriter(new FileOutputStream(courseName + fileName + ".txt", true));
					String upcomingAnswersValid = "";
					String pointsAdded = "";
					String pointsGiven = "";
					do { // looping for each question
						upcomingAnswersValid = JOptionPane.showInputDialog(null, "Please write the question");
						if (upcomingAnswersValid == null) {
							JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
									"Cancelled", JOptionPane.INFORMATION_MESSAGE);
							return;
						} else {
							myWriter.write(upcomingAnswersValid + "\n");
						}
						upcomingAnswersValid = JOptionPane.showInputDialog(null, "Please write answer A):");
						if (upcomingAnswersValid == null) {
							JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
									"Cancelled", JOptionPane.INFORMATION_MESSAGE);
							return;
						} else {
							myWriter.write("A." + upcomingAnswersValid + "\n");
						}
						upcomingAnswersValid = JOptionPane.showInputDialog(null, "Please write answer B):");
						if (upcomingAnswersValid == null) {
							JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
									"Cancelled", JOptionPane.INFORMATION_MESSAGE);
							return;
						} else {
							myWriter.write("B." +  upcomingAnswersValid + "\n");
						}
						upcomingAnswersValid = JOptionPane.showInputDialog(null, "Please write answer C):");
						if (upcomingAnswersValid == null) {
							JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
									"Cancelled", JOptionPane.INFORMATION_MESSAGE);
							return;
						} else {
							myWriter.write("C." +  upcomingAnswersValid + "\n");
						}
						upcomingAnswersValid = JOptionPane.showInputDialog(null, "Please write answer D):");
						if (upcomingAnswersValid == null) {
							JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
									"Cancelled", JOptionPane.INFORMATION_MESSAGE);
							return;
						} else {
							myWriter.write("D." +  upcomingAnswersValid + "\n");
						}
						upcomingAnswersValid = JOptionPane.showInputDialog(null,
								"Please write which letter is the answer");
						if (upcomingAnswersValid == null) {
							JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
									"Cancelled", JOptionPane.INFORMATION_MESSAGE);
							return;
						} else {
							myWriter.write( upcomingAnswersValid + "\n");
						}
						pointsAdded = JOptionPane.showInputDialog(null,
								"What you like to put a point value in? Yes or No");
						if (pointsAdded == null) {
							JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
									"Cancelled", JOptionPane.INFORMATION_MESSAGE);
							return;
						} else if (pointsAdded.equalsIgnoreCase("yes")) {
							pointsGiven = JOptionPane.showInputDialog(null,
									"Please input a value (digit): ");
							if (pointsGiven == null) {
								JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
										"Cancelled", JOptionPane.INFORMATION_MESSAGE);
								return;
							} else {
								myWriter.write(pointsGiven + "\n");
							}
						} else {
							myWriter.write("1\n");
						}
						String another = JOptionPane.showInputDialog(null,
								"Would you like to write another question? Yes or No");
						if (another == null) {
							JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
									"Cancelled", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						while (!(another.equalsIgnoreCase("yes") || another.equalsIgnoreCase("no"))) {
							another = JOptionPane.showInputDialog(null, "Please input a valid answer, " +
									"Yes or No." + "Would you like to write another question? Yes or No");
							if (another == null) {
								JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
										"Cancelled", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						}
						if (another.equalsIgnoreCase("yes")) {
							contWriting = true;
						} else {
							contWriting = false;
						}
					} while (contWriting);
					myWriter.close();
					// Writing to Course Name + Quizzes
					File allQuizzes = new File(courseName + "Quizzes.txt");
					if (allQuizzes.exists()) {
						JOptionPane.showMessageDialog(null,
								"Creating a New File.", "File Status", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null,
								"Appending the file, it already exists!", "File Status", JOptionPane.ERROR_MESSAGE);
					}
					PrintWriter writeToList = new PrintWriter(new FileOutputStream(allQuizzes, true));
					writeToList.write(fileName + "\n");
					writeToList.close();
				}
				JOptionPane.showMessageDialog(null,
						"DONE!", "File Status", JOptionPane.INFORMATION_MESSAGE);
	
			} catch (IOException e) {
	
				JOptionPane.showMessageDialog(null,
						"Error Writing/Reading to File.", "File Status", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
	
				JOptionPane.showMessageDialog(null,
						"Undocumented Error.", "File Status", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static void editQuiz(String quizName, String courseName) {
		// converting file into ArrayList
		synchronized (gatekeeper) {
			try {
				BufferedReader bfrForEditing = new BufferedReader(new FileReader(courseName + quizName + ".txt"));
				ArrayList<String> courseQuizQuestions = new ArrayList<String>();
				String quizContents = bfrForEditing.readLine();
				while (quizContents != null) {
					courseQuizQuestions.add(quizContents);
					quizContents = bfrForEditing.readLine();
				}
				bfrForEditing.close();
				// first boolean to make sure valid input, second to make sure the requested
				// question is an available
				// question.
				boolean converted = false;
				boolean properNumber = false;
				String editQuestion = JOptionPane.showInputDialog(null,"Which question would you like to edit?");
				if (editQuestion == null) {
					JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
							"Cancelled", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				int convertNumber = 0;
				int questionNumber = 0;
				String validResponse = "";
				String pointsResponse = "";
				String pointsGiven2 = "";
				while (properNumber == false) {
					while (converted == false) {
						try {
							convertNumber = Integer.parseInt(editQuestion);
							converted = true;
						} catch (NumberFormatException e) {
							editQuestion = JOptionPane.showInputDialog(null,"Please input a valid integer." +
									"Which question would you like to edit?");
							if (editQuestion == null) {
								JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
										"Cancelled", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
						}
					}
					// in case when a new input is needed, needs to convert to integer again.
					converted = false;
					if (convertNumber < 1) {
						editQuestion = JOptionPane.showInputDialog(null,
								"Please input a valid question number. Which question would you like to edit?");
						if (editQuestion == null) {
							JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
									"Cancelled", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					}
					questionNumber = ((convertNumber - 1) * 7);
					if (questionNumber > (courseQuizQuestions.size() / 7)) {
						editQuestion = JOptionPane.showInputDialog(null,
								"Please input a valid question number. Which question would you like to edit?");
						if (editQuestion == null) {
							JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
									"Cancelled", JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					} else {
						properNumber = true;
					}
				}
				validResponse = JOptionPane.showInputDialog(null,
						"Please write the question");
				if (validResponse == null) {
					JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
							"Cancelled", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				courseQuizQuestions.set(questionNumber, validResponse);
				validResponse = JOptionPane.showInputDialog(null,
						"Please write answer A):");
				if (validResponse == null) {
					JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
							"Cancelled", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				courseQuizQuestions.set((questionNumber + 1), "A. " + validResponse);
				validResponse = JOptionPane.showInputDialog(null,
						"Please write answer B):");
				if (validResponse == null) {
					JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
							"Cancelled", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				courseQuizQuestions.set((questionNumber + 2), "B. " + validResponse);
				validResponse = JOptionPane.showInputDialog(null,
						"Please write answer C):");
				if (validResponse == null) {
					JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
							"Cancelled", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				courseQuizQuestions.set((questionNumber + 3), "C. " + validResponse);
				validResponse = JOptionPane.showInputDialog(null,
						"Please write answer D):");
				if (validResponse == null) {
					JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
							"Cancelled", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				courseQuizQuestions.set((questionNumber + 4), "D. " + validResponse);
				validResponse = JOptionPane.showInputDialog(null,
						"Please write which letter is the answer:");
				if (validResponse == null) {
					JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
							"Cancelled", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				courseQuizQuestions.set((questionNumber + 5), validResponse);
				//right here
				pointsResponse = JOptionPane.showInputDialog(null,
						"What you like to put a point value in? Yes or No");
				if (pointsResponse == null) {
					JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
							"Cancelled", JOptionPane.INFORMATION_MESSAGE);
					return;
				} else if (pointsResponse.equalsIgnoreCase("yes")) {
						pointsGiven2 = JOptionPane.showInputDialog(null,
								"Please input a value (digit): ");
						if (pointsGiven2 == null) {
							JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
									"Cancelled", JOptionPane.INFORMATION_MESSAGE);
							return;
						} else {
							courseQuizQuestions.set(questionNumber + 6, pointsGiven2);
						}
				} else {
					courseQuizQuestions.set(questionNumber + 6, "1");
				}
	
				PrintWriter myWriterToQuiz = new PrintWriter(new FileOutputStream(courseName + quizName + ".txt"));
				for (int c = 0; c < courseQuizQuestions.size(); c++) {
					myWriterToQuiz.write(courseQuizQuestions.get(c) + "\n");
				}
				myWriterToQuiz.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null,
						"File Not Found.", "File Status", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Error Writing/Reading to File.", "File Status", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public static void viewSubmission(String courseName, String quizName, String submissionName) {
		synchronized (gatekeeper) {
			String fullSubmission = "";
			try {
				BufferedReader bfr = new BufferedReader(new FileReader(courseName + quizName + submissionName + ".txt"));
				String details = bfr.readLine();
				while (details != null) {
					fullSubmission += details + "\n";
					details = bfr.readLine();
				}
				JOptionPane.showMessageDialog(null,
						fullSubmission, submissionName, JOptionPane.INFORMATION_MESSAGE);
				bfr.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null,
						"File Not Found.", "File Status", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Error Writing/Reading to File.", "File Status", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
		

	public static String[] printCourses() {
		synchronized (gatekeeper) {
			ArrayList<String> choices = new ArrayList<>();
			try {
				BufferedReader bfr = new BufferedReader(new FileReader("CourseNames.txt"));
				while (true) {
					String line = bfr.readLine();
					if (line == null) {
						break;
					}
					//System.out.println(line);
					choices.add(line);
				}
				bfr.close();
				JOptionPane.showMessageDialog(null,
						choices, "All Courses", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error Displaying Courses.", "Quizzes",
						JOptionPane.ERROR_MESSAGE);//change in Teacher error message
			}
			String[] choices2 = new String[choices.size()];
			for (int c = 0; c < choices.size(); c++) {
				choices2[c] = choices.get(c);
			}
			return choices2;
		}
	}

	public static String[] printQuizzes(String courseName) {
		synchronized (gatekeeper) {
			ArrayList<String> choices = new ArrayList<>();
			try {
				BufferedReader bfr = new BufferedReader(new FileReader(courseName + "Quizzes.txt"));
				while (true) {
					String line = bfr.readLine();
					if (line == null) {
						break;
					}
					//System.out.println(line);
					choices.add(line);
	
				}
				bfr.close();
				JOptionPane.showMessageDialog(null,
						choices, "All Quizzes", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error Displaying Quizzes.", "Quizzes",
						JOptionPane.ERROR_MESSAGE);//change in Teacher error message
			}
			String[] choices2 = new String[choices.size()];
			for (int c = 0; c < choices.size(); c++) {
				choices2[c] = choices.get(c);
			}
			return choices2;
		}
	}

	public static void printSubmissions(String courseName, String quizName) {
		String allSubmissions = "";
		synchronized (gatekeeper) {
			try {
				BufferedReader bfr = new BufferedReader(new FileReader(courseName + quizName + "Submissions.txt"));
				while (true) {
					String line = bfr.readLine();
					if (line == null) {
						break;
					}
					line = line.replace(courseName, "");
					line = line.replace(quizName, "");
					line = line.replace(".txt", "");
					allSubmissions += line + "\n";
				}
				JOptionPane.showMessageDialog(null,
						allSubmissions, "All Submissions", JOptionPane.INFORMATION_MESSAGE);
				bfr.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Error Displaying Submission.", "File Status", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static boolean checkSubmissionExistence(String courseName, String quizName, String submission) {
		synchronized (gatekeeper) {
			try {
				BufferedReader bfr = new BufferedReader(new FileReader(courseName + quizName + "Submissions.txt"));
				while (true) {
					String line = bfr.readLine();
					if (line == null) {
						break;
					}
					if (line.equals(courseName + quizName + submission + ".txt")) {
						bfr.close();
						return true;
					}
				}
				bfr.close();
				JOptionPane.showMessageDialog(null,
						"Submission Not Found.", "File Status", JOptionPane.ERROR_MESSAGE);
				return false;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Error Validating Submission.", "File Status", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
	}

	public static boolean checkQuizExistence(String courseName, String quizName) {
		synchronized (gatekeeper) {
			try {
				BufferedReader bfr = new BufferedReader(new FileReader(courseName + "Quizzes.txt"));
				while (true) {
					String line = bfr.readLine();
					if (line == null) {
						break;
					}
					if (line.equals(quizName)) {
						bfr.close();
						return true;
					}
				}
				bfr.close();
				JOptionPane.showMessageDialog(null,
						"Quiz Not Found.", "File Status", JOptionPane.ERROR_MESSAGE);
				return false;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Error Validating Quiz.", "File Status", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
	}

	public static boolean checkCourseExistence(String courseName, boolean creating) {
		synchronized (gatekeeper) {
			try {
				BufferedReader bfr = new BufferedReader(new FileReader("CourseNames.txt"));
				while (true) {
					String line = bfr.readLine();
					if (line == null) {
						break;
					}
					if (line.equals(courseName)) {
						bfr.close();
						if (creating) {
							JOptionPane.showMessageDialog(null,
									"Course already created. You can't make another!",
									"File Status", JOptionPane.ERROR_MESSAGE);
						}
						return true;
					}
				}
				bfr.close();
				if (!creating) {
					JOptionPane.showMessageDialog(null,
							"Course Not Found.", "File Status", JOptionPane.ERROR_MESSAGE);
				}
				return false;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Error Validating Course.", "File Status", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
	}

	public static void quizImport(String fileName, String courseName) throws IOException {
		/*
		 * File Format: // Creates file with teacher quiz //
		 */
		synchronized (gatekeeper) {
			try {
				File f = new File(fileName + ".txt");
				FileReader fileReader = new FileReader(f);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
	
				String quizName = bufferedReader.readLine();
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(courseName + "Quizzes.txt", true)));
				out.write(quizName + "\n");
				out.close();
				PrintWriter fw = new PrintWriter(new BufferedWriter(new FileWriter(courseName + quizName + ".txt")));
				String input = bufferedReader.readLine();
				do {
					fw.println(input);
					// System.out.println(input);
					input = bufferedReader.readLine();
				} while (input != null);
	
				bufferedReader.close();
				fw.close();
				File submissions = new File(courseName + quizName + "Submissions.txt");
				submissions.createNewFile();
			} catch (FileNotFoundException e) {
				throw new FileNotFoundException();
			} catch (IOException e) {
				throw new IOException();
			}
		}

	}
}
