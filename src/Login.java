
import java.io.*;
import java.util.*;

/**
 * Project 5 - Learning Management Quiz Tool - Login Contains methods for login
 * system functionality
 * <p>
 * 
 * @author Matt Hiatt, Aryan Mathur, Aniket Mohanty, and Nathan Lo
 * @version 12/12/2021
 */
public class Login {

	public synchronized static boolean isDuplicate(String user) {
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(new File("login.txt")));
			ArrayList<String> content = new ArrayList<>();
			while (true) {
				String line = bfr.readLine();
				if (line == null) {
					break;
				} else {
					content.add(line);
				}
			}

			for (int i = 1; i < content.size(); i += 3) {
				if (user.equals(content.get(i))) {
					bfr.close();
					return true;
				}
			}
			bfr.close();
			return false;
		} catch (IOException e) {
			System.out.println("Error validating username, please try again.");
			return true;
		}
	}

	public synchronized static void writeNewUser(String classification, String user, String password) {
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream("login.txt", true));
			pw.println(classification);
			pw.println(user);
			pw.println(password);
			pw.close();
		} catch (IOException e) {
			System.out.println("Error creating account, please exit and try again.");
		}
	}

	public synchronized static boolean login(String user, String password) {
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(new File("login.txt")));
			ArrayList<String> content = new ArrayList<>();
			while (true) {
				String line = bfr.readLine();
				if (line == null) {
					break;
				} else {
					content.add(line);
				}
			}

			for (int i = 1; i < content.size(); i += 3) {
				if (user.equals(content.get(i))) {
					if (password.equals(content.get(i + 1))) {
						bfr.close();
						return true;
					} else {
						bfr.close();
						return false;
					}
				}
			}
			bfr.close();
			return false;
		} catch (IOException e) {
			System.out.println("Error validating username, please try again.");
			return false;
		}
	}

	public synchronized static String getClassification(String user) {
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(new File("login.txt")));
			ArrayList<String> content = new ArrayList<>();
			while (true) {
				String line = bfr.readLine();
				if (line == null) {
					break;
				} else {
					content.add(line);
				}
			}

			for (int i = 1; i < content.size(); i += 3) {
				if (user.equals(content.get(i))) {
					return content.get(i - 1);
				}
			}
			bfr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
