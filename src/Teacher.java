import javax.swing.*;
import java.util.*;
import java.io.*;

/**
 * Project 5 - Learning Management Quiz Tool - Teacher Contains methods for
 * teacher functionality
 * <p>
 *
 * @author Matt Hiatt, Aryan Mathur, Aniket Mohanty, and Nathan Lo
 * @version 12/13/2021
 */

public class Teacher {
	public static Object courseListKeeper = new Object();
	public static Object quizListKeeper = new Object();
	public static Object submissionListKeeper = new Object();
	public static Object quizKeeper = new Object();
	public static Object submissionKeeper = new Object();

	public static String createCourse(String courseName) {
		try {
			String nameAndQuiz = courseName + "Quizzes.txt";
			File newFileQuiz = new File(nameAndQuiz);
			if (checkCourseExistence(courseName, true)) {
				return "exists";
			}
			if (newFileQuiz.createNewFile()) {
				synchronized (courseListKeeper) {
					PrintWriter myWriter = new PrintWriter(new FileOutputStream("CourseNames.txt", true));
					myWriter.write(courseName + "\n");
					myWriter.close();
				}
				return "success";
			} else {
				return "fail";
			}
		} catch (IOException e) {
			return "fail";
		} catch (Exception e) {
			return "fail";
		}
	}

	public static String deleteCourse(String courseName) {
		try {
			synchronized (courseListKeeper) {
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
					bfr.close();
					return "DNE";
				}
				PrintWriter myWriter = new PrintWriter(new FileOutputStream("CourseNames.txt"));
				for (int j = 0; j < allCourses.size(); j++) {
					myWriter.write(allCourses.get(j) + "\n");
				}
				bfr.close();
				myWriter.close();
			}
			// FINISHED REWRITING FILES AFTER DELETING NAME
		} catch (FileNotFoundException e) {
			return "fail";
		} catch (Exception e) {
			return "fail";
		}
		// SEARCHING FOR ALL QUIZZES INSIDE courseName + Quizzes, then calling
		// deleteQuiz
		try {
			// Reading off all the names of the quizzes inside CourseName + Quizzes
			synchronized (quizListKeeper) {
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
			}
		} catch (FileNotFoundException e) {
			return "fail";
		} catch (IOException e) {
			return "fail";
		} catch (Exception e) {
			return "fail";
		}
		// Deleting entire file with all Quiz names. Finished using the file to find all
		// quizzes, so can delete now.
		synchronized (quizListKeeper) {
			File deletedFile = new File(courseName + "Quizzes.txt");
			if (deletedFile.delete()) {
				return "success";
			} else {
				return "fail";
			}
		}
	}

	// DELETES QUIZ AND QUIZ SUBMISSIONS
	public static String deleteQuiz(String courseName, String quizName) {
		try {
			// read file to get name of all submissions
			synchronized (submissionListKeeper) {
				BufferedReader bfrSubmissions = new BufferedReader(
						new FileReader(courseName + quizName + "Submissions.txt"));
				ArrayList<String> allSubmissions = new ArrayList<>();
				String submissionsNames = bfrSubmissions.readLine();
				while (submissionsNames != null) {
					allSubmissions.add(submissionsNames);
					submissionsNames = bfrSubmissions.readLine();
				}
				File deletedSubmissions;

				// goes through all submissions for the quizzes, loops through to delete them.
				synchronized (submissionKeeper) {
					for (int a = 0; a < allSubmissions.size(); a++) {
						deletedSubmissions = new File(allSubmissions.get(a));
						if (deletedSubmissions.delete()) {
						} else {
							return "fail";
						}
					}
				}
				bfrSubmissions.close();
			}
			// delete name from _course_name_ + Quizzes file.
			synchronized (quizListKeeper) {
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
			}
			// FINISHED REWRITING FILES AFTER DELETING NAME from courseName + Quizzes
			File deletedQuiz;
			// delete quiz now that it is no longer needed for the submissions
			synchronized (quizKeeper) {
				deletedQuiz = new File(courseName + quizName + ".txt");
				if (deletedQuiz.delete()) {
				} else {
					return "fail";
				}
			}
			synchronized (submissionListKeeper) {
				File deletedSubmissionFile = new File(courseName + quizName + "Submissions.txt");
				if (deletedSubmissionFile.delete()) {
					return "success";
				} else {
					return "fail";
				}
			}
		} catch (FileNotFoundException e) {
			return "fail";
		} catch (IOException e) {
			return "fail";
		}
	}

	public static void createQuiz(String courseName) {
		try {
			boolean contWriting = false; // variable for knowing if they want to keep writing quizzes (do while loop)
			String fileName = "";
			PrintWriter myWriter;
			String importOrCreate = JOptionPane.showInputDialog(null,
					"Would you like to import a quiz? Yes or No (If no, you will be directed to create a "
							+ "file inside the program immediately)");

			// ^^checks if importing or creating
			// checks for valid input
			if (!(importOrCreate.equalsIgnoreCase("no") || importOrCreate.equalsIgnoreCase("yes"))) {
				while (!(importOrCreate.equalsIgnoreCase("no") || importOrCreate.equalsIgnoreCase("yes"))) {
					importOrCreate = JOptionPane.showInputDialog(null,
							"Please input a valid answer, Yes or No.\n" + "Would you like to import a quiz? "
									+ "Yes or No (If no, you will be directed to "
									+ "create a file inside program immediately");
					if (importOrCreate == null) {
						return;
					}
				}
			}
			if (importOrCreate.equalsIgnoreCase("yes")) { // if importing, do this
				String premadeFile = JOptionPane.showInputDialog(null,
						"\nRemember File must be in " + "the format:\nName of Quiz\nQuestion\nChoice A\nChoice B\n"
								+ "Choice C\nChoice D\nCorrect Choice\nPoints\n" + "What is the name of the file?");
				if (premadeFile == null) {
					return;
				}
				boolean check = false;
				do {
					try {
						quizImport(premadeFile, courseName);
					} catch (FileNotFoundException e) {
						check = true;
						JOptionPane.showMessageDialog(null, "File not found! Please try again", "File Status",
								JOptionPane.ERROR_MESSAGE);
						premadeFile = JOptionPane.showInputDialog(null, "\nRemember File must be in "
								+ "the format:\nName of Quiz\nQuestion\nChoice A\nChoice B\n"
								+ "Choice C\\nChoice D\nCorrect Choice\nPoints\n" + "What is the name of the file?");
						if (premadeFile == null) {
							return;
						}
					} catch (IOException e) {
						check = true;
						JOptionPane.showMessageDialog(null, "Error, please try again.", "File Status",
								JOptionPane.ERROR_MESSAGE);
						premadeFile = JOptionPane.showInputDialog(null, "\nRemember File must be in "
								+ "the format:\nName of Quiz\nQuestion\nChoice A\nChoice B\n"
								+ "Choice C\\nChoice D\nCorrect Choice\nPoints\n" + "What is the name of the file?");
						if (premadeFile == null) {
							return;
						}
					}
				} while (check);
			} else { // if creating, do this
				do {
					fileName = JOptionPane.showInputDialog(null, "What would you like to name the quiz?");
					if (fileName == null) {
						return;
					}
					if (fileName.isBlank()) {
						JOptionPane.showMessageDialog(null, "Quiz title cannot be blank.", "File Status",
								JOptionPane.ERROR_MESSAGE);
					}
				} while (fileName.isBlank());
				synchronized (quizKeeper) {
					File newQuiz = new File(courseName + fileName + ".txt");

					if (newQuiz.createNewFile()) {
						JOptionPane.showMessageDialog(null, "Creating a new file!", "File Status",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Overwriting the file, it already exists!", "File Status",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
				synchronized (submissionListKeeper) {
					File newQuizSubmissions = new File(courseName + fileName + "Submissions.txt");
					newQuizSubmissions.createNewFile();
				}
				synchronized (quizKeeper) {
					myWriter = new PrintWriter(new FileOutputStream(courseName + fileName + ".txt", true));
					myWriter.write(fileName + "\n");
					String upcomingAnswersValid = "";
					String pointsAdded = "";
					String pointsGiven = "";
					do { // looping for each question
						upcomingAnswersValid = JOptionPane.showInputDialog(null, "Please write the question");
						if (upcomingAnswersValid == null) {
							myWriter.close();
							return;
						} else {
							myWriter.write(upcomingAnswersValid + "\n");
						}
						upcomingAnswersValid = JOptionPane.showInputDialog(null, "Please write answer A):");
						if (upcomingAnswersValid == null) {
							myWriter.close();
							return;
						} else {
							myWriter.write("A." + upcomingAnswersValid + "\n");
						}
						upcomingAnswersValid = JOptionPane.showInputDialog(null, "Please write answer B):");
						if (upcomingAnswersValid == null) {
							myWriter.close();
							return;
						} else {
							myWriter.write("B." + upcomingAnswersValid + "\n");
						}
						upcomingAnswersValid = JOptionPane.showInputDialog(null, "Please write answer C):");
						if (upcomingAnswersValid == null) {
							myWriter.close();
							return;
						} else {
							myWriter.write("C." + upcomingAnswersValid + "\n");
						}
						upcomingAnswersValid = JOptionPane.showInputDialog(null, "Please write answer D):");
						if (upcomingAnswersValid == null) {
							myWriter.close();
							return;
						} else {
							myWriter.write("D." + upcomingAnswersValid + "\n");
						}
						upcomingAnswersValid = JOptionPane.showInputDialog(null,
								"Please write which letter is the answer");
						if (upcomingAnswersValid == null) {
							myWriter.close();
							return;
						} else {
							myWriter.write(upcomingAnswersValid + "\n");
						}
						pointsAdded = JOptionPane.showInputDialog(null,
								"What you like to put a point value in? Yes or No");
						if (pointsAdded == null) {
							myWriter.close();
							return;
						} else if (pointsAdded.equalsIgnoreCase("yes")) {
							pointsGiven = JOptionPane.showInputDialog(null, "Please input a value (digit): ");
							if (pointsGiven == null) {
								myWriter.close();
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
							myWriter.close();
							return;
						}
						while (!(another.equalsIgnoreCase("yes") || another.equalsIgnoreCase("no"))) {
							another = JOptionPane.showInputDialog(null, "Please input a valid answer, " + "Yes or No."
									+ "Would you like to write another question? Yes or No");
							if (another == null) {
								myWriter.close();
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
				}
				// Writing to Course Name + Quizzes
				synchronized (quizListKeeper) {
					File allQuizzes = new File(courseName + "Quizzes.txt");
					if (allQuizzes.exists()) {
						JOptionPane.showMessageDialog(null, "Creating a New File.", "File Status",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Appending the file, it already exists!", "File Status",
								JOptionPane.ERROR_MESSAGE);
					}
					PrintWriter writeToList = new PrintWriter(new FileOutputStream(allQuizzes, true));
					writeToList.write(fileName + "\n");
					writeToList.close();
				}
			}
			JOptionPane.showMessageDialog(null, "DONE!", "File Status", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error Writing/Reading to File.", "File Status",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Undocumented Error.", "File Status", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static ArrayList<String> getQuiz(String quizName, String courseName) {
		try {
			ArrayList<String> courseQuizQuestions = new ArrayList<String>();
			synchronized (quizKeeper) {
				BufferedReader bfrForEditing = new BufferedReader(new FileReader(courseName + quizName + ".txt"));
				String quizContents = bfrForEditing.readLine();
				while (quizContents != null) {
					courseQuizQuestions.add(quizContents);
					quizContents = bfrForEditing.readLine();
				}
				bfrForEditing.close();
			}
			return courseQuizQuestions;
		} catch (IOException e) {
			return null;
		}
	}

	public static void updateQuiz(String courseName, String quizName, ArrayList<String> courseQuizQuestions) {
		try {
			synchronized (quizKeeper) {
				PrintWriter myWriterToQuiz = new PrintWriter(new FileOutputStream(courseName + quizName + ".txt"));
				for (int c = 0; c < courseQuizQuestions.size(); c++) {
					myWriterToQuiz.write(courseQuizQuestions.get(c) + "\n");
				}
				myWriterToQuiz.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String viewSubmission(String courseName, String quizName, String submissionName) {
		String fullSubmission = "";
		try {
			synchronized (submissionKeeper) {
				BufferedReader bfr = new BufferedReader(
						new FileReader(courseName + quizName + submissionName + ".txt"));
				String details = bfr.readLine();
				while (details != null) {
					fullSubmission += details + "\n";
					details = bfr.readLine();
				}
				bfr.close();
				return "SUCCESS|" + fullSubmission + "|" + submissionName;
			}
		} catch (FileNotFoundException e) {
			return "fail";
		} catch (IOException e) {
			return "fail";
		}
	}

	public static String[] printCourses() {
		ArrayList<String> choices = new ArrayList<>();
		try {
			synchronized (courseListKeeper) {
				BufferedReader bfr = new BufferedReader(new FileReader("CourseNames.txt"));
				while (true) {
					String line = bfr.readLine();
					if (line == null) {
						break;
					}
					choices.add(line);
				}
				bfr.close();
			}
			if (choices.size() == 0) {
				choices.add("No courses currently.");
			} else {
				for (int x = 0; x < choices.size(); x++) {
					if (choices.get(x).equals("No courses currently.")) {
						choices.remove(x);
					}
				}
			}
			JOptionPane.showMessageDialog(null, choices, "All Courses", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error Displaying Courses.", "Courses", JOptionPane.ERROR_MESSAGE);// change
																													// in
																													// Teacher
																													// error
																													// message
		}
		String[] choices2 = new String[choices.size()];
		for (int c = 0; c < choices.size(); c++) {
			choices2[c] = choices.get(c);
		}
		return choices2;
	}

	public static String[] printQuizzes(String courseName) {
		ArrayList<String> choices = new ArrayList<>();
		String status = "";
		try {
			synchronized (quizListKeeper) {
				BufferedReader bfr = new BufferedReader(new FileReader(courseName + "Quizzes.txt"));
				while (true) {
					String line = bfr.readLine();
					if (line == null) {
						break;
					}
					choices.add(line);
				}
				status = "success";
				bfr.close();
			}
		} catch (IOException e) {
			status = "fail";
		}
		String[] choices2 = new String[choices.size() + 1];
		choices2[choices.size()] = status;
		for (int c = 0; c < choices.size(); c++) {
			choices2[c] = choices.get(c);
		}
		return choices2;
	}

	public static String printSubmissions(String courseName, String quizName) {
		ArrayList<String> listSubmissions = new ArrayList<>();
		try {
			synchronized (submissionListKeeper) {
				BufferedReader bfr = new BufferedReader(new FileReader(courseName + quizName + "Submissions.txt"));
				while (true) {
					String line = bfr.readLine();
					if (line == null) {
						break;
					}
					line = line.replace(courseName, "");
					line = line.replace(quizName, "");
					line = line.replace(".txt", "");
					listSubmissions.add(line);
				}
				if (listSubmissions.size() == 0) {
					listSubmissions.add("No Submissions Currently");
				}
				bfr.close();
				return "success|" + listSubmissions;
			}
		} catch (IOException e) {
			return "fail";
		}
	}

	public static boolean checkSubmissionExistence(String courseName, String quizName, String submission) {
		try {
			synchronized (submissionListKeeper) {
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
			}
			JOptionPane.showMessageDialog(null, "Submission Not Found.", "File Status", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error Validating Submission.", "File Status",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public static boolean checkQuizExistence(String courseName, String quizName) {
		try {
			synchronized (quizListKeeper) {
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
			}
			JOptionPane.showMessageDialog(null, "Quiz Not Found.", "File Status", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error Validating Quiz.", "File Status", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public static boolean checkCourseExistence(String courseName, boolean creating) {
		try {
			synchronized (courseListKeeper) {
				BufferedReader bfr = new BufferedReader(new FileReader("CourseNames.txt"));
				while (true) {
					String line = bfr.readLine();
					if (line == null) {
						break;
					}
					if (line.equals(courseName)) {
						bfr.close();
						if (creating) {
							JOptionPane.showMessageDialog(null, "Course already created. You can't make another!",
									"File Status", JOptionPane.ERROR_MESSAGE);
						}
						return true;
					}
				}
				bfr.close();
			}
			if (!creating) {
				JOptionPane.showMessageDialog(null, "Course Not Found.", "File Status", JOptionPane.ERROR_MESSAGE);
			}
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error Validating Course.", "File Status", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public static void quizImport(String fileName, String courseName) throws IOException {
		/*
		 * File Format: // Creates file with teacher quiz //
		 */
		try {
			File f = new File(fileName + ".txt");
			FileReader fileReader = new FileReader(f);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String quizName = bufferedReader.readLine();
			synchronized (quizListKeeper) {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(courseName + "Quizzes.txt", true)));
				out.write(quizName + "\n");
				out.close();
			}
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
