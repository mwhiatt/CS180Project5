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
	public static void createCourse(String courseName) {
		try {
			String nameAndQuiz = courseName + "Quizzes.txt";
			File newFileQuiz = new File(nameAndQuiz);

			if (newFileQuiz.createNewFile()) {
				System.out.println("Creating a new file!");
				PrintWriter myWriter = new PrintWriter(new FileOutputStream("CourseNames.txt", true));
				myWriter.write(courseName + "\n");
				myWriter.close();
			} else {
				System.out.println("The course already exists!");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error writing to file. Try again.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Undocumented Error. Try again.");
		}
	}

	public static void deleteCourse(String courseName) {
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
				System.out.println("No need to delete, that course doesn't exist.");
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
			e.printStackTrace();
			System.out.println("Error writing to file.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Undocumented Error.");
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
			e.printStackTrace();
			System.out.println("File Not Found.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error reading or writing to file.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Undocumented Error.");

		}
		// Deleting entire file with all Quiz names. Finished using the file to find all
		// quizzes, so can delete now.
		File deletedFile = new File(courseName + "Quizzes.txt");
		if (deletedFile.delete()) {
			System.out.println("Successfully deleted the file " + courseName + "Quizzes.txt");
		} else {
			System.out.println("Failed to delete file.");
		}
	}

	// DELETES QUIZ AND QUIZ SUBMISSIONS
	public static void deleteQuiz(String courseName, String quizName) {
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
					System.out.println("Successfully deleted the file " + allSubmissions.get(a));
				} else {
					System.out.println("Failed to delete file.");
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
				System.out.println("Successfully deleted the file " + courseName + quizName + ".txt");
			} else {
				System.out.println("Failed to delete file." + courseName + quizName + ".txt");
			}
			File deletedSubmissionFile = new File(courseName + quizName + "Submissions.txt");
			if (deletedSubmissionFile.delete()) {
				System.out.println("Successfully deleted the file " + courseName + quizName + "Submissions.txt");
			} else {
				System.out.println("Failed to delete file.");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error Writing/Reading to file.");
		}

	}

	public static void createQuiz(Scanner input, String courseName) {
		try {
			boolean contWriting = false; // variable for knowing if they want to keep writing quizzes (do while loop)
			String fileName = "";
			PrintWriter myWriter;
			System.out.println("Would you like to import a quiz? Yes or No (If no, you will be directed to create a "
					+ "file inside terminal immediately)"); // Check if importing or creating
			String importOrCreate = input.nextLine();
			// checks for valid input
			if (!(importOrCreate.equalsIgnoreCase("no") || importOrCreate.equalsIgnoreCase("yes"))) {
				while (!(importOrCreate.equalsIgnoreCase("no") || importOrCreate.equalsIgnoreCase("yes"))) {
					System.out.println("Please input a valid answer, Yes or No.");
					System.out.println("Would you like to import a quiz? Yes or No (If no, you will be directed to "
							+ "create a file inside terminal immediately");
					importOrCreate = input.nextLine();
				}
			}
			if (importOrCreate.equalsIgnoreCase("yes")) { // if importing, do this
				System.out.println(
						"\nRemember File must be in the format:\nName of Quiz\nQuestion\nChoice A\nChoice B\n"
								+ "Choice C\\nChoice D\nCorrect Choice\nPoints");
				System.out.println("What is the name of the file?");
				String premadeFile = input.nextLine();
				boolean check = false;
				do {
					try {
						quizImport(premadeFile, courseName);
					} catch (FileNotFoundException e) {
						check = true;
						System.out.println("File not found! Please try again");
						System.out.println("\nRemeber File must be in the format:"
								+ "\nQuestion\nChoice A\nChoice B\nChoice C\n" + "Choice D\nCorrect Choice\nPoints");
						System.out.println("What is the name of the file?");
						premadeFile = input.nextLine();
					} catch (IOException e) {
						check = true;
						System.out.println("Error! Please try again");
						System.out.println("\nRemeber File must be in the format:"
								+ "\nQuestion\nChoice A\nChoice B\nChoice C\n" + "Choice D\nCorrect Choice\nPoints");
						System.out.println("What is the name of the file?");
						premadeFile = input.nextLine();
					}
				} while (check);
			} else { // if creating, do this
				do {
					System.out.println("What would you like to name the quiz?");
					fileName = input.nextLine();
					if (fileName.isBlank()) {
						System.out.println("Quiz title cannot be blank!");
					}
				} while (fileName.isBlank());
				File newQuiz = new File(courseName + fileName + ".txt");

				if (newQuiz.createNewFile()) {
					System.out.println("Creating a new file!");
				} else {
					System.out.println("Overwriting the file, it already exists!");
				}
				File newQuizSubmissions = new File(courseName + fileName + "Submissions.txt");
				myWriter = new PrintWriter(new FileOutputStream(courseName + fileName + ".txt", true));
				do { // looping for each question
					System.out.println("Please write the question");
					myWriter.write(input.nextLine() + "\n");
					System.out.println("Please write answer A):");
					myWriter.write("A." + input.nextLine() + "\n");
					System.out.println("Please write answer B):");
					myWriter.write("B. " + input.nextLine() + "\n");
					System.out.println("Please write answer C):");
					myWriter.write("C. " + input.nextLine() + "\n");
					System.out.println("Please write answer D):");
					myWriter.write("D. " + input.nextLine() + "\n");
					System.out.println("Please write which letter is the answer:");
					myWriter.write(input.nextLine() + "\n");
					System.out.println("What you like to put a point value in? Yes or No");
					if (input.nextLine().equalsIgnoreCase("yes")) {
						System.out.println("Please input a value (digit):");
						myWriter.write(input.nextLine() + "\n");
					} else {
						myWriter.write("1\n");
					}
					System.out.println("Would you like to write another question? Yes or No");
					String another = input.nextLine();
					while (!(another.equalsIgnoreCase("yes") || another.equalsIgnoreCase("no"))) {
						System.out.println("Please input a valid answer, Yes or No.");
						System.out.println("Would you like to write another question? Yes or No");
						another = input.nextLine();
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
					System.out.println("Creating a new file!");
				} else {
					System.out.println("Appending the file, it already exists!");
				}
				PrintWriter writeToList = new PrintWriter(new FileOutputStream(allQuizzes, true));
				writeToList.write(fileName + "\n");
				writeToList.close();
			}
			System.out.println("Done!");

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error writing to file.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Undocumented Error.");
		}
	}

	public static void editQuiz(Scanner input, String quizName, String courseName) {
		// converting file into ArrayList
		try {
			BufferedReader bfrForEditing = new BufferedReader(new FileReader(courseName + quizName + ".txt"));
			ArrayList<String> courseQuizQuestions = new ArrayList<String>();
			String quizContents = bfrForEditing.readLine();
			while (quizContents != null) {
				courseQuizQuestions.add(quizContents);
				quizContents = bfrForEditing.readLine();
			}
			bfrForEditing.close();
			System.out.println("Which question would you like to edit?");
			// first boolean to make sure valid input, second to make sure the requested
			// question is an available
			// question.
			boolean converted = false;
			boolean properNumber = false;
			String editQuestion = input.nextLine();
			int convertNumber = 0;
			int questionNumber = 0;
			while (properNumber == false) {
				while (converted == false) {
					try {
						convertNumber = Integer.parseInt(editQuestion);
						converted = true;
					} catch (NumberFormatException e) {
						System.out.println("Please input a valid integer.");
						System.out.println("Which question would you like to edit?");
						editQuestion = input.nextLine();
					}
				}
				// in case when a new input is needed, needs to convert to integer again.
				converted = false;
				if (convertNumber < 1) {
					System.out.println("Please input a valid question number. Which question would you like to edit?");
					editQuestion = input.nextLine();
				}
				questionNumber = ((convertNumber - 1) * 7);
				if (questionNumber > (courseQuizQuestions.size() / 7)) {
					System.out.println("Please input a valid question number. Which question would you like to edit?");
					editQuestion = input.nextLine();
				} else {
					properNumber = true;
				}
			}
			System.out.println("Please write the question");
			courseQuizQuestions.set(questionNumber, input.nextLine());
			System.out.println("Please write answer A):");
			courseQuizQuestions.set((questionNumber + 1), "A. " + input.nextLine());
			System.out.println("Please write answer B):");
			courseQuizQuestions.set(questionNumber + 2, "B. " + input.nextLine());
			System.out.println("Please write answer C):");
			courseQuizQuestions.set(questionNumber + 3, "C. " + input.nextLine());
			System.out.println("Please write answer D):");
			courseQuizQuestions.set(questionNumber + 4, "D. " + input.nextLine());
			System.out.println("Please write which letter is the answer:");
			courseQuizQuestions.set(questionNumber + 5, input.nextLine());
			System.out.println("What you like to put a point value in? Yes or No");
			if (input.nextLine().toLowerCase().equals("yes")) {
				System.out.println("Please input a value (digit):");
				courseQuizQuestions.set(questionNumber + 6, input.nextLine());
			} else {
				courseQuizQuestions.set(questionNumber + 6, "1");
			}
			PrintWriter myWriterToQuiz = new PrintWriter(new FileOutputStream(courseName + quizName + ".txt"));
			for (int c = 0; c < courseQuizQuestions.size(); c++) {
				myWriterToQuiz.write(courseQuizQuestions.get(c) + "\n");
			}
			myWriterToQuiz.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void viewSubmission(String courseName, String quizName, String submissionName) {
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(courseName + quizName + submissionName + ".txt"));
			String details = bfr.readLine();
			while (details != null) {
				System.out.println(details);
				details = bfr.readLine();
			}
			bfr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String[] printCourses() {
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

	public static String[] printQuizzes(String courseName) {
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

	public static void printSubmissions(String courseName, String quizName) {
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
				System.out.println(line);
			}
			bfr.close();
		} catch (IOException e) {
			System.out.println("Error Displaying Submissions.");
		}
	}

	public static boolean checkSubmissionExistence(String courseName, String quizName, String submission) {
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
			System.out.println("Submission Not Found");
			return false;
		} catch (IOException e) {
			System.out.println("Error Validating Submission.");
			return false;
		}
	}

	public static boolean checkQuizExistence(String courseName, String quizName) {
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
			System.out.println("Quiz Not Found");
			return false;
		} catch (IOException e) {
			System.out.println("Error Validating Quiz.");
			return false;
		}
	}

	public static boolean checkCourseExistence(String courseName) {
		try {
			BufferedReader bfr = new BufferedReader(new FileReader("CourseNames.txt"));
			while (true) {
				String line = bfr.readLine();
				if (line == null) {
					break;
				}
				if (line.equals(courseName)) {
					bfr.close();
					return true;
				}
			}
			bfr.close();
			System.out.println("Course Not Found\n");
			return false;
		} catch (IOException e) {
			System.out.println("Error Validating Course.");
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