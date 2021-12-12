import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Project 5 - Learning Management Quiz Tool - Student Contains methods for
 * student functionality
 * <p>
 *
 * @author Matt Hiatt, Aryan Mathur, Aniket Mohanty, and Nathan Lo
 * @version 11/15/2021
 */
public class Student {

   //reads the quiz to the student
   public static ArrayList<String> readQuiz (String course, String quiz) {
       String courseQuizFileName = course + quiz + ".txt";
       ArrayList<String> list = new ArrayList<>();
       synchronized (Teacher.quizKeeper) {
	       try (BufferedReader bfr = new BufferedReader(new FileReader(courseQuizFileName))) {
	           String s = " ";
	           String questionString = "";
	           do {
	               for (int c = 0; c < 5; c++) {
	                   s = bfr.readLine();
	                   if (s == null) {
	                       continue;
	                   }
	                   questionString = questionString + s + "~";
	               }
	               list.add(questionString);
	               if (s == null) {
	                   continue;
	               }
	
	               for (int c = 0; c < 2; c++) {
	                   if (c == 0) {
	                       s = bfr.readLine();
	                       list.add(s); // ans
	                   } else if (c == 1) {
	                       s = bfr.readLine();
	                       list.add(s); // points
	                   }
	               }
	           } while (s != null);
	       } catch (FileNotFoundException e) {
	           JOptionPane.showMessageDialog(null, e.getMessage(), "Read Quiz error", JOptionPane.ERROR_MESSAGE);
	       } catch (IOException e) {
	           JOptionPane.showMessageDialog(null, e.getMessage(), "Read Quiz error", JOptionPane.ERROR_MESSAGE);
	       }
       }
       list.remove(list.size() - 1);
       return list;
    }
    //writes the submission file for a student
    public static void writeFile(String course, String quiz, String user, ArrayList<String> points, ArrayList<String> answerList) {
        String totalString;
        int total = 0;
        for (int y = 0; y < points.size(); y++) {
            total = total + Integer.valueOf(points.get(y));
        }
        totalString = String.valueOf(total);

        int i = 1;
        int n = 1;
        String fileName = course + quiz + user + ".txt";
        synchronized (Teacher.submissionKeeper) {
	        File f = new File(fileName);
	        while (f.exists()) {
	            i++;
	            fileName = course + quiz + user + i + ".txt";
	            f = new File(fileName);
	        }
	        try (PrintWriter pw = new PrintWriter(new FileOutputStream(fileName, false))) {
	            pw.println("Name: " + user);
	            for (int c = 0; c < answerList.size(); c ++) {
	                String ans = "Student Answer: " + answerList.get(c);
	                String point = points.get(c);
	                String fileInput = n + ". " + ans + ", " + point;
	                n++;
	                pw.println(fileInput);
	            }
	            pw.println("Points Earned : " + totalString);
	            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
	            pw.println("Timestamp: " + timeStamp);
	        } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Write Submissions error", JOptionPane.ERROR_MESSAGE);
	        }
        }
        synchronized (Teacher.submissionListKeeper) {
	        String masterFileName = course + quiz + "Submissions.txt";
	        try (PrintWriter pw = new PrintWriter(new FileOutputStream(masterFileName, true))) {
	            pw.println(fileName);
	        } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Write Submissions error", JOptionPane.ERROR_MESSAGE);
	        }
        }
    }

    //allows the student to answer through file imports
    public static String answerImportFile(File fileName) {
    	synchronized (Teacher.submissionKeeper) {
	        File f = null;
	        FileReader fileReader = null;
	        BufferedReader bufferedReader = null;
	        String ans = "";
	        try {
	            f = fileName;
	            fileReader = new FileReader(f);
	            bufferedReader = new BufferedReader(fileReader);
	            ans = bufferedReader.readLine();
	
	        } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Import Answers error", JOptionPane.ERROR_MESSAGE);
	        } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Import Answers error", JOptionPane.ERROR_MESSAGE);
	        }
	        return ans;
    	}
    }

    // Displays student the submission they have chosen to view
    public static ArrayList<String> printSubmissions(String course, String quiz, String user) {
        String fileName = course + quiz + "Submissions.txt";
        ArrayList<String> userSubmissions = new ArrayList<>(); // ArrayList that holds a particular user's submissions
        String prompt = "";
        boolean properInput = false;
        synchronized (Teacher.submissionListKeeper) {
	        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
	            String s = bfr.readLine();
	            while (s != null) {
	                if (s.contains(user)) { // checks whether a submission contains username
	                    userSubmissions.add(s); // adds it to ArrayList
	                }
	                s = bfr.readLine();
	            }
	
	        } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Print Submissions error", JOptionPane.ERROR_MESSAGE);
	        } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Print Submissions error", JOptionPane.ERROR_MESSAGE);
	        }
	        return userSubmissions;
        }
    }
    public static String viewSubmissions(String requestedSubmission) {
        String returnString = "";
        synchronized (Teacher.submissionKeeper) {
            try (BufferedReader bfr = new BufferedReader(new FileReader(requestedSubmission))) {
                String s = bfr.readLine();
                while (s != null) {
                    returnString = returnString + s + "~"; // prints the user submission line by line
                    s = bfr.readLine();
                }
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "View Submissions error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "View Submissions error", JOptionPane.ERROR_MESSAGE);
            }
            return returnString;
        }

    }

    //calculates the points obtained by a student
    public static ArrayList<String> grading(ArrayList<String> answerList, ArrayList<String> quizAndAnswers) {
        ArrayList<String> points = new ArrayList<>();
        int j = 1;
        for (int i = 0; i < answerList.size(); i++) {
            if(answerList.get(i).equals(quizAndAnswers.get(j))) {
                points.add(quizAndAnswers.get(j+1));
            } else {
                points.add("0");
            }
            j += 3;
        }
        return points;
    }


}
