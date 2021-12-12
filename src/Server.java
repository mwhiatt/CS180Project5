import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
/**
 * Project 5 - Learning Management Quiz Tool - Server
 * Server class to receive connections from multiple users and store information. 
 * <p>
 * 
 * @author Matt Hiatt, Aryan Mathur, Aniket Mohanty, and Nathan Lo
 * @version 11/15/2021
 */
public class Server implements Runnable {
	Socket socket;
	public Server(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//handling
			String message = bfr.readLine();
			while (message != null) {
				System.out.println("Entered Method handling");				
				System.out.println("Message received from client: " + message);
				ArrayList<String> arguments = parseMessage(message);
				String method = arguments.get(0);
				System.out.println("Method:" + method);
				if (method.equals("CREATECOURSE")) {
					String result = Teacher.createCourse(arguments.get(1));
					pw.write(result + "\n");
					pw.flush();
				} else if (method.equals("DELETECOURSE")) {
					String result = Teacher.deleteCourse(arguments.get(1));
					pw.write(result + "\n");
					pw.flush();
				} else if (method.equals("DELETEQUIZ")) {
					pw.write(Teacher.deleteQuiz(arguments.get(1), arguments.get(2)));
					pw.flush();
				} else if (method.equals("CREATEQUIZ")) {
					Teacher.createQuiz(arguments.get(1));
				} else if (method.equals("EDITQUIZ")) {
					Teacher.editQuiz(arguments.get(1), arguments.get(2));
				} else if (method.equals("VIEWSUBMISSION")) {
					Teacher.viewSubmission(arguments.get(1), arguments.get(2), arguments.get(3));
				} else if (method.equals("PRINTCOURSES")) {
					String[] courses = Teacher.printCourses();
					String courseStr = "";
					for (int i = 0; i < courses.length; i++) {
						courseStr += courses[i] + "|";
					}
					String ret = courseStr.substring(0, courseStr.length() - 1);
					pw.write(ret + "\n");
					pw.flush();
				} else if (method.equals("PRINTQUIZZES")) {
					String[] quizzes = Teacher.printQuizzes(arguments.get(1));
					String quizStr = "";
					for (int i = 0; i < quizzes.length; i++) {
						quizStr += quizzes[i] + "|";
					}
					String ret;
					if (quizStr.length() != 0) {
						ret = quizStr.substring(0, quizStr.length() - 1);
					} else {
						ret = " ";
					}
					pw.write(ret + "\n");
					pw.flush();
				} else if (method.equals("PRINTQUIZZES2")) {
					Teacher.printQuizzes(arguments.get(1));
				} else if (method.equals("PRINTSUBMISSIONS")) {
					try {
						ArrayList<String> submissions = Student.printSubmissions(arguments.get(1), arguments.get(2), arguments.get(3));
						String submissionsStr = "";
						for (int i = 0; i < submissions.size(); i++) {
							submissionsStr += submissions.get(i) + "|";
						}
						String ret = submissionsStr.substring(0, submissionsStr.length() - 1);
						pw.write(ret + "\n");
						pw.flush();
					} catch (IOException e) {
						pw.write("");
						pw.flush();
					}
				} else if (method.equals("CHECKQUIZ")) {
					if (Teacher.checkQuizExistence(arguments.get(1), arguments.get(2))) {
						pw.write("true" + "\n");
						pw.flush();
					} else {
						pw.write("false" + "\n");
						pw.flush();
					}
				} else if (method.equals("CHECKCOURSE")) {
					
				} else if (method.equals("QUIZIMPORT")) {
					
				} else if (method.equals("GRADING")) {
					ArrayList<String> arg1 = parseList(arguments.get(1));
					ArrayList<String> arg2 = parseList(arguments.get(2));
					ArrayList<String> points = Student.grading(arg1, arg2);
					String pointsStr = "";
					for (int i = 0; i < points.size(); i++) {
						pointsStr += points.get(i) + "|";
					}

					String ret = pointsStr.substring(0, pointsStr.length() - 1);
					pw.write(ret + "\n");
					pw.flush();
				} else if (method.equals("READQUIZ")) {
					ArrayList<String> quizAndAnswers = Student.readQuiz(arguments.get(1), arguments.get(2));
					String quizAndAnswersStr = "";
					for (int i = 0; i < quizAndAnswers.size(); i++) {
						quizAndAnswersStr += quizAndAnswers.get(i) + "|";
					}
					String ret = quizAndAnswersStr.substring(0, quizAndAnswersStr.length() - 1);
					pw.write(ret + "\n");
					pw.flush();
				} else if (method.equals("WRITEFILE")) {
					Student.writeFile(arguments.get(1), arguments.get(2), arguments.get(3),
							parseList(arguments.get(4)), parseList(arguments.get(5)));
				} else if (method.equals("PRINTSUBMISSIONS2")) {
					Teacher.printSubmissions(arguments.get(1), arguments.get(2));
				} else if (method.equals("VIEWSUBMISSIONS")) {
					try {
						String submission = Student.viewSubmissions(arguments.get(1));
						pw.write(submission + "\n");
					} catch (IOException e) {
						throw e;
					}
					pw.flush();
				} else if (method.equals("LOGIN")) {
					if (Login.login(arguments.get(1), arguments.get(2))) {
						pw.write("true\n");
						pw.flush();
						System.out.println("Valid Login");
					} else {
						pw.write("false\n");
						pw.flush();
						System.out.println("Invalid Login");
					}
				} else if (method.equals("WRITENEWUSER")) {
					Login.writeNewUser(arguments.get(1), arguments.get(2), arguments.get(3));
				} else if (method.equals("ISDUPLICATE")) {
					String arg = arguments.get(1);
					System.out.println(arg);
					if (Login.isDuplicate(arg)) {
						System.out.println("About to write");
						pw.write("true" + "\n");
						pw.flush();
						System.out.println("Written");
					} else {
						pw.write("false" + "\n");
						pw.flush();
					}
				} else if (method.equals("GETCLASSIFICATION")) {
					String classification = Login.getClassification(arguments.get(1));
					System.out.println("Classification: " + classification);
					pw.write(classification + "\n");
					pw.flush();
				}
				message = bfr.readLine();
				if (message != null) {
					System.out.println("Message: " + message);
				} else
					System.out.println("Message is null");
			}
			
			pw.close();
			bfr.close();
			socket.close();
			System.out.println("Connection Closed.\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		//Creates Login File 
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
		try {
			ServerSocket serverSocket = new ServerSocket(4343);
			
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("Accepted");
				Server server = new Server(socket);
				new Thread(server).start();
			}
		} catch (IOException e) {
			System.out.println("Socket exception");
		}
		
	}
	
	public static ArrayList<String> parseMessage(String message) {
		ArrayList<String> arguments = new ArrayList<String>();
		while (message.indexOf('|') != -1) {
			arguments.add(message.substring(0, message.indexOf('|')));
			message = message.substring(message.indexOf('|') + 1);
	    }
		arguments.add(message);
		return arguments;
	}
	
	public static ArrayList<String> parseList(String message) {
		message = message.replace("~","\n");
		ArrayList<String> parsedList = new ArrayList<String>();
		while (message.indexOf('`') != -1) {
			parsedList.add(message.substring(0, message.indexOf('`')));
			message = message.substring(message.indexOf('`') + 1);
	    }
		parsedList.add(message);
		return parsedList;
	}

}

