import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.net.*;

public class Client implements ActionListener {

    public static ArrayList<String> parseMessage(String message) {
        ArrayList<String> parsedMessage = new ArrayList<String>();
        while (message.indexOf('|') != -1) {
            parsedMessage.add(message.substring(0, message.indexOf('|')));
            message = message.substring(message.indexOf('|') + 1);
        }
        parsedMessage.add(message);
        return parsedMessage;
    }

    public static String packageList(ArrayList<String> list) {
        String packedList = "";
        //packaging currentAnswers list to a string to be sent to server
        for (int i = 0; i < list.size(); i++) {
            packedList += list.get(i) + "`";
        }
        packedList = packedList.substring(0, packedList.length() - 1);
        return packedList;
    }

    ArrayList<String> currentPoints = new ArrayList<>();
    ArrayList<String> currentAnswerList = new ArrayList<>();
    ArrayList<String> currentQuizAndAnswers = new ArrayList<>();
    int currentCount = 0;
    String currentCourse;
    String currentQuiz;
    String username;
    JFrame frame;
    JFrame frame2;
    JFrame frame3;
    JFrame frame4;
    JFrame frame5;
    JFrame frame6;
    JFrame frame7;
    JFrame frame8;
    JFrame frame9;
    JFrame teacherMainMenu;
    JFrame teacherViewCourseMenu;
    //all teacher functions below
    JButton createCourse;
    JButton viewCourse;
    JButton viewAllCourses;
    JButton goBack;

    JButton deleteCourse;
    JButton deleteQuiz;
    JButton createQuiz;
    JButton editQuiz;
    JButton viewSubmission;
    JButton printCourse;
    JButton printQuizzes;
    JButton printSubmissions;
    JButton checkSubmissionExistence;

    //all teacher functions/buttons above

    JButton create; // create quiz
    JButton login; // login
    JButton exit;
    JButton takeQuiz;
    JButton viewSubmissions;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == create) {
            this.create();
            frame.setVisible(false);
        }
        if (e.getSource() == login) {
            this.login();
            frame.setVisible(false);
        }
        if (e.getSource() == exit) {
            this.exit();
            frame.setVisible(false);
            frame4.setVisible(false);
        }
        if (e.getSource() == takeQuiz) {
            this.takeQuiz();
            frame4.setVisible(false);
        }
    }

    public void viewSubmissions() {
        frame8 = new JFrame("View Submissions");
        frame8.setVisible(true);
        frame8.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame8.setSize(500, 250);
        frame8.setLocation(430, 100);
        JPanel panel = new JPanel();
        String response = "";
        try {
            Socket socket = new Socket("localhost", 4343);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            pw.write("PRINTSUBMISSIONS|" + getCurrentCourse() + "|" + getCurrentQuiz() + "|" + getUsername());
            pw.flush();
            response = bfr.readLine();

            bfr.close();
            pw.close();
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        ArrayList<String> userSubmissions = parseMessage(response);
        String[] userSubmissionsArray = new String[userSubmissions.size()];
        for (int c = 0; c < userSubmissions.size(); c++) {
            userSubmissionsArray[c] = userSubmissions.get(c);
        }
        JLabel lbl = new JLabel("Select an option click OK");
        panel.add(lbl, BorderLayout.AFTER_LINE_ENDS);
        final JComboBox<String> cb = new JComboBox<String>(userSubmissionsArray);
        cb.setMaximumSize(cb.getPreferredSize());
        panel.add(lbl, BorderLayout.EAST);
        panel.add(cb, BorderLayout.AFTER_LINE_ENDS);
        JButton ok = new JButton("OK");
        panel.add(ok, BorderLayout.AFTER_LINE_ENDS);
        frame8.add(panel, BorderLayout.NORTH);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame8.setVisible(false);
                frame9 = new JFrame("View Submissions");
                frame9.setVisible(true);
                frame9.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame9.setSize(500, 250);
                frame9.setLocation(430, 100);
                JPanel panel = new JPanel();
                String submission = "";
                try {
                    Socket socket = new Socket("localhost", 4343);
                    BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("VIEWSUBMISSIONS|" + (String) cb.getSelectedItem());
                    pw.flush();
                    submission = bfr.readLine();

                    bfr.close();
                    pw.close();
                    socket.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                JTextArea selectedSubmission = new JTextArea(submission);
                JButton ok2 = new JButton("Continue viewing other submissions?");
                panel.add(selectedSubmission, BorderLayout.NORTH);
                panel.add(ok2, BorderLayout.AFTER_LINE_ENDS);
                frame9.add(panel, BorderLayout.NORTH);
                ok2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame9.setVisible(false);
                        frame8.setVisible(true);
                    }
                });
                frame9.setVisible(true);
            }
        });
        frame8.setVisible(true);

    }


    public void answerQuiz(ArrayList<String> quizAndAnswers) {
        if (quizAndAnswers.size() <= getCurrentCount()) {
            return;
        }
//        ArrayList<String> answerList = new ArrayList<>();
        frame6 = new JFrame("Quiz Answer");
        frame6.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame6.setSize(800, 600);
        frame6.setLocation(430, 100);
        JPanel panel = new JPanel();
        JPanel panelForQuestion = new JPanel();
        JTextArea question = new JTextArea(quizAndAnswers.get(getCurrentCount()));
        panelForQuestion.add(question, BorderLayout.AFTER_LINE_ENDS);
        JLabel lbl = new JLabel("Select an option click OK");
        panel.add(lbl, BorderLayout.AFTER_LINE_ENDS);

        String[] choices = {"Answer through GUI", "Answer through file imports"};

        final JComboBox<String> cb = new JComboBox<String>(choices);

        cb.setMaximumSize(cb.getPreferredSize());
        panel.add(cb, BorderLayout.AFTER_LINE_ENDS);
        JButton ok3 = new JButton("OK");
        JButton finishQuiz = new JButton("Finish Quiz");
        panel.add(finishQuiz, BorderLayout.AFTER_LINE_ENDS);
        finishQuiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame6.setVisible(false);
                ArrayList<String> points = new ArrayList<>();
                try {
                    Socket socket = new Socket("localhost", 4343);
                    BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());
                    ArrayList<String> currentAnswers = getCurrentAnswerList();
                    String currentAnswersStr = "";
                    //packaging currentAnswers list to a string to be sent to server
                    for (int i = 0; i < currentAnswers.size(); i++) {
                        currentAnswersStr += currentAnswers.get(i) + "`";
                    }
                    currentAnswersStr = currentAnswersStr.substring(0, currentAnswersStr.length() - 1);

                    //packaging quizAndAnswers list to a string to be sent to server
                    String quizStr = "";
                    for (int i = 0; i < quizAndAnswers.size(); i++) {
                        quizStr += quizAndAnswers.get(i) + "`";
                    }
                    quizStr = quizStr.substring(0, quizStr.length() - 1);

                    //Sending to server to be graded
                    pw.write("GRADING|" + currentAnswersStr + "|" + quizStr);
                    pw.flush();

                    points = parseMessage(bfr.readLine());

                    bfr.close();
                    pw.close();
                    socket.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                setCurrentPoints(points);

//                System.out.println(answerList);
//                System.out.println(points);
                //writes file
                try {
                    Socket socket = new Socket("localhost", 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    //Sending to server to be written
                    pw.write("WRITEFILE|" + getCurrentCourse() + "|" + getCurrentQuiz() + "|" + getUsername()
                            + "|" + packageList(getCurrentPoints()) + "|" + packageList(getCurrentAnswerList()));
                    pw.flush();

                    pw.close();
                    socket.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                setCurrentAnswerList(new ArrayList<String>());
            }
        });
        panel.add(ok3, BorderLayout.AFTER_LINE_ENDS);
        frame6.add(panelForQuestion, BorderLayout.NORTH);
        frame6.add(panel, BorderLayout.SOUTH);
        ok3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                {
                    panelForQuestion.remove(question);
                    frame6.setVisible(false);
                    String answerChoice = (String) cb.getSelectedItem();
                    if (answerChoice.equals("Answer through GUI")) {
                        String answer = JOptionPane.showInputDialog(null, "Answer:");
                        currentAnswerList.add(answer);

                    } else {
                        //to-do
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                        int result = fileChooser.showOpenDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = fileChooser.getSelectedFile();
                        }
                    }
                    setCurrentCount(getCurrentCount() + 3);
                    answerQuiz(quizAndAnswers);
                }
            }
        });
        frame6.setVisible(true);

        //implement write file here


    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public String getCurrentCourse() {
        return currentCourse;
    }

    public void setCurrentCourse(String currentCourse) {
        this.currentCourse = currentCourse;
    }

    public String getCurrentQuiz() {
        return currentQuiz;
    }

    public void setCurrentQuiz(String currentQuiz) {
        this.currentQuiz = currentQuiz;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCurrentPoints(ArrayList<String> points) {
        currentPoints = points;
    }

    public void setCurrentAnswerList(ArrayList<String> answerList) {
        currentAnswerList = answerList;
    }

    public void setCurrentQuizAndAnswers(ArrayList<String> quizAndAnswers) {
        currentQuizAndAnswers = quizAndAnswers;
    }

    public ArrayList<String> getCurrentPoints() {
        return currentPoints;
    }

    public ArrayList<String> getCurrentAnswerList() {
        return currentAnswerList;
    }

    public ArrayList<String> getCurrentQuizAndAnswers() {
        return currentQuizAndAnswers;
    }


    public void takeQuiz() {
        frame5 = new JFrame("Available Quizzes");
        frame5.setVisible(true);
        frame5.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame5.setSize(500, 140);
        frame5.setLocation(430, 100);
        JPanel panel = new JPanel();
        JLabel lbl = new JLabel("Select an option click OK");
        panel.add(lbl, BorderLayout.AFTER_LINE_ENDS);
        ArrayList<String> courseList = new ArrayList<>();
        try {
            Socket socket = new Socket("localhost", 4343);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            //Sending to server to be written
            pw.write("PRINTCOURSES");
            pw.flush();

            courseList = parseMessage(bfr.readLine());

            bfr.close();
            pw.close();
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        String[] choices = new String[courseList.size()];
        for (int i = 0; i < choices.length; i++) {
            choices[i] = courseList.get(i);
        }

        final JComboBox<String> cb = new JComboBox<String>(choices);

        cb.setMaximumSize(cb.getPreferredSize());
        panel.add(cb, BorderLayout.AFTER_LINE_ENDS);
        JButton ok3 = new JButton("OK");
        panel.add(ok3, BorderLayout.AFTER_LINE_ENDS);
        ok3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                {
                    String courseName = (String) cb.getSelectedItem();
                    setCurrentCourse(courseName);
                    JPanel panel2 = new JPanel();
                    JLabel lbl2 = new JLabel("Select an option click OK");
                    panel2.add(lbl2, BorderLayout.AFTER_LINE_ENDS);
                    ArrayList<String> quizList = new ArrayList<>();
                    try {
                        Socket socket = new Socket("localhost", 4343);
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());

                        //Sending to server to be written
                        pw.write("PRINTCOURSES|" + courseName);
                        pw.flush();

                        quizList = parseMessage(bfr.readLine());

                        bfr.close();
                        pw.close();
                        socket.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    String[] choicesQuizzes = new String[quizList.size()];
                    for (int i = 0; i < choices.length; i++) {
                        choicesQuizzes[i] = quizList.get(i);
                    }
                    final JComboBox<String> cb2 = new JComboBox<String>(choicesQuizzes);

                    cb2.setMaximumSize(cb2.getPreferredSize());
                    panel2.add(cb2, BorderLayout.AFTER_LINE_ENDS);
                    String quiz = (String) cb2.getSelectedItem();
                    setCurrentQuiz(quiz);
                    viewSubmissions = new JButton("View Submissions");
                    viewSubmissions.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            viewSubmissions();
                        }
                    });
                    JButton ok4 = new JButton("OK");
                    ok4.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                Socket socket = new Socket("localhost", 4343);
                                BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                PrintWriter pw = new PrintWriter(socket.getOutputStream());

                                pw.write("CHECKQUIZ|" + courseName + "|" + quiz);
                                pw.flush();
                                String response = bfr.readLine();
                                if (response.equals("true")) {
                                    frame5.setVisible(false);
                                    pw.write("READQUIZ|" + courseName + "|" + quiz);
                                    ArrayList<String> quizAndAnswers = parseMessage(bfr.readLine());
                                    answerQuiz(quizAndAnswers);
                                } else {
                                    //GUI implementation saying quiz doesn't exist
                                }
                                pw.close();
                                bfr.close();
                                socket.close();
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        }
                    });
                    panel2.add(viewSubmissions, BorderLayout.AFTER_LINE_ENDS);
                    panel2.add(ok4, BorderLayout.AFTER_LINE_ENDS);

                    frame5.add(panel2, BorderLayout.SOUTH);
                }
            }
        });

        frame5.add(panel, BorderLayout.NORTH);


    }


    public void create() {
        frame2 = new JFrame("Create Account");
        frame2.setVisible(true);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setSize(500, 140);
        frame2.setLocation(430, 100);

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        JLabel lbl = new JLabel("Select an option click OK");
        panel.add(lbl, BorderLayout.AFTER_LINE_ENDS);

        String[] choices = {"Teacher", "Student"};

        final JComboBox<String> cb = new JComboBox<String>(choices);

        cb.setMaximumSize(cb.getPreferredSize());
        panel.add(cb, BorderLayout.AFTER_LINE_ENDS);

        frame2.add(panel, BorderLayout.NORTH);

        JLabel lbl2 = new JLabel("Username");
        JTextField username = new JTextField(8);
        panel2.add(lbl2, BorderLayout.EAST);
        panel2.add(username, BorderLayout.AFTER_LINE_ENDS);
        frame2.add(panel2, BorderLayout.CENTER);

        JLabel lbl3 = new JLabel("Password");
        JPasswordField password = new JPasswordField(8);
        panel3.add(lbl3, BorderLayout.EAST);
        panel3.add(password, BorderLayout.AFTER_LINE_ENDS);
        JButton ok3 = new JButton("OK");
        ok3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                {
                    boolean prompt = true;
                    String classification = (String) cb.getSelectedItem();
                    if (password.getText().isBlank()) {
                        JOptionPane.showMessageDialog(null, "Password cannot be blank!", "Password Error",
                                JOptionPane.ERROR_MESSAGE);
                        prompt = false;

                    }
                    String dupResponse = "";
                    try {
                        Socket socket = new Socket("localhost", 4343);
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());

                        pw.write("ISDUPLICATE|" + username.getText());
                        pw.flush();
                        dupResponse = bfr.readLine();

                        socket.close();
                        bfr.close();
                        pw.close();

                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    if (dupResponse.equals("true")) {
                        JOptionPane.showMessageDialog(null, "Sorry that username is taken, please try a new one.", "Username Error",
                                JOptionPane.ERROR_MESSAGE);
                        prompt = false;
                    }
                    if (username.getText().isBlank()) {
                        JOptionPane.showMessageDialog(null, "Username cannot be blank!", "Username Error",
                                JOptionPane.ERROR_MESSAGE);
                        prompt = false;
                    }
                    if (prompt) {
                        try {
                            Socket socket = new Socket("localhost", 4343);
                            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            PrintWriter pw = new PrintWriter(socket.getOutputStream());

                            pw.write("WRITENEWUSER|" + classification + "|" + username.getText() + "|"
                                    + password.getText());
                            pw.flush();

                            socket.close();
                            bfr.close();
                            pw.close();

                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        setUsername(username.getText());
                        StudentMenu(username.getText());
                        frame2.setVisible(false);
                    }
                }
            }
        });
        panel3.add(ok3, BorderLayout.AFTER_LINE_ENDS);
        frame2.add(panel3, BorderLayout.SOUTH);
//        frame2.setVisible(true);
    }

    public void login() {
        frame3 = new JFrame("Login");
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame3.setSize(500, 120);
        frame3.setLocation(430, 100);
        JLabel lbl2 = new JLabel("Username");
        JTextField username = new JTextField(8);
        panel.add(lbl2, BorderLayout.EAST);
        panel.add(username, BorderLayout.AFTER_LINE_ENDS);
        frame3.add(panel, BorderLayout.NORTH);
        JLabel lbl3 = new JLabel("Password");
        JPasswordField password = new JPasswordField(8);
        panel2.add(lbl3, BorderLayout.EAST);
        panel2.add(password, BorderLayout.AFTER_LINE_ENDS);
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean prompt2 = true;
                String dupResponse = "0";
                try {
                    Socket socket = new Socket("localhost", 4343);
                    BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("ISDUPLICATE|" + username.getText());
                    pw.flush();
                    dupResponse = bfr.readLine();

                    socket.close();
                    bfr.close();
                    pw.close();

                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                if (!dupResponse.equals("true")) {
                    JOptionPane.showMessageDialog(null, "Username not found, please try again.", "Username Error",
                            JOptionPane.ERROR_MESSAGE);
                    prompt2 = false;
                }
                String loginResponse = "";
                try {
                    Socket socket = new Socket("localhost", 4343);
                    BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("LOGIN|" + username.getText() + "|" + password.getText());
                    pw.flush();
                    loginResponse = bfr.readLine();

                    socket.close();
                    bfr.close();
                    pw.close();

                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                if (!loginResponse.equals("true")) {
                    JOptionPane.showMessageDialog(null, "Incorrect password, please try again.", "Username Error",
                            JOptionPane.ERROR_MESSAGE);
                    prompt2 = false;
                }
                if (prompt2) {
                    setUsername(username.getText());
                    StudentMenu(username.getText());
                    frame3.setVisible(false);
                }
            }
        });
        panel2.add(ok, BorderLayout.AFTER_LINE_ENDS);
        frame3.add(panel2, BorderLayout.CENTER);

        frame3.setVisible(true);

    }

    public void exit() {
        JOptionPane.showMessageDialog(null, "Logged Out\nHave a Good Day", "Welcome",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {

        Client Client = new Client();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Client.createGUI();
            }
        });
    }

    public void StudentMenu(String username) {
        String type = "";
        try {
            Socket socket = new Socket("localhost", 4343);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            pw.write("GETCLASSIFICATION|" + username);
            pw.flush();
            type = bfr.readLine();

            socket.close();
            bfr.close();
            pw.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
        if (type.equals("Student")) {
            frame4 = new JFrame("Welcome Student " + username);
            frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame4.setSize(500, 100);
            frame4.setLocation(430, 100);
            takeQuiz = new JButton("Take Quiz");
            takeQuiz.addActionListener(this);
            exit = new JButton("Exit");
            exit.addActionListener(this);
            JPanel panel2 = new JPanel();
            panel2.add(takeQuiz);
            panel2.add(exit);
            frame4.add(panel2, BorderLayout.NORTH);
            frame4.setVisible(true);
        } else if (type.equals("Teacher")) {
            // created december 5
            teacherMainMenu = new JFrame("Welcome Teacher " + username);
            teacherMainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            teacherMainMenu.setSize(500, 100);
            teacherMainMenu.setLocation(430, 100);
            //general shape of frame created
            JPanel teacherFirstMenu = new JPanel();
            //same 3 options provided! just coded differently
            createCourse = new JButton("Create Course");
            createCourse.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String courseNameRequested = JOptionPane.showInputDialog(null,
                            "Please enter the course name.");
                    if (courseNameRequested == null) {
                        JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
                                "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    while (courseNameRequested.isEmpty() || courseNameRequested.isBlank()) {
                        courseNameRequested = JOptionPane.showInputDialog(null,
                                "Enter something. Please enter the course name.");
                        if (courseNameRequested == null) {
                            JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
                                    "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                    Teacher.createCourse(courseNameRequested);
                }
            });
            viewCourse = new JButton("View Specific Course");
        viewCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherMainMenu.setVisible(false);
                String courseNameRequested = JOptionPane.showInputDialog(null,
                        "Please enter the course name.");
                if (courseNameRequested == null) {
                    JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
                            "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                    teacherMainMenu.setVisible(true);
                    return;
                }
                while (courseNameRequested.isEmpty() || courseNameRequested.isBlank()) {
                    courseNameRequested = JOptionPane.showInputDialog(null,
                            "Enter something. Please enter the course name.");
                    if (courseNameRequested == null) {
                        JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
                                "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                        teacherMainMenu.setVisible(true);
                        return;
                    }
                }
                if (Teacher.checkCourseExistence(courseNameRequested, false)) {
                    teacherViewCourse(courseNameRequested);
                } else {
                    teacherMainMenu.setVisible(true);
                }
            }
        });
        viewAllCourses = new JButton("View All Current Courses");
        viewAllCourses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherMainMenu.setVisible(false);
                Teacher.printCourses();
                teacherMainMenu.setVisible(true);
            }
        });
        //general same exit button
        exit = new JButton("Exit");
        exit.addActionListener(this);
        teacherFirstMenu.add(createCourse);
        teacherFirstMenu.add(viewCourse);
        teacherFirstMenu.add(viewAllCourses);
        teacherFirstMenu.add(exit);
        teacherMainMenu.add(teacherFirstMenu, BorderLayout.NORTH);
        teacherMainMenu.setVisible(true);
    }

}

    // NEED TO FIGURE OUT HOW TO IMPLEMENT SHOWING THE QUIZZES OR SUBMISSIONS DURING QUESTIONS
    public void teacherViewCourse(String courseName) {
        //viewCourseMenuCode
        teacherViewCourseMenu = new JFrame("Please choose an option:");
        teacherViewCourseMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        teacherViewCourseMenu.setSize(500, 150);
        teacherViewCourseMenu.setLocation(430, 100);
        JPanel viewCourseMenu = new JPanel();
        JPanel viewCourseMenu2 = new JPanel();
        JPanel viewCourseMenu3 = new JPanel();
        //same options provided before all on one screen as buttons
        deleteCourse = new JButton("Delete Course");
        deleteCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherViewCourseMenu.setVisible(false);
                try {
                    Socket socket = new Socket("localhost", 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("DELETECOURSE|" + courseName);
                    pw.flush();

                    socket.close();
                    pw.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                teacherMainMenu.setVisible(true);
            }
        });
        deleteQuiz = new JButton("Delete Quiz");
        deleteQuiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String quizNameToDelete = JOptionPane.showInputDialog(null,
                        "Please enter the quiz name.");
                if (quizNameToDelete == null) {
                    JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
                            "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                teacherViewCourseMenu.setVisible(false);
                try {
                    Socket socket = new Socket("localhost", 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("DELETEQUIZ|" + courseName + "|" + quizNameToDelete);
                    pw.flush();

                    socket.close();
                    pw.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                teacherViewCourseMenu.setVisible(true);
            }
        });
        createQuiz = new JButton("Create Quiz");
        createQuiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherViewCourseMenu.setVisible(false);
                try {
                    Socket socket = new Socket("localhost", 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("CREATEQUIZ|" + courseName);
                    pw.flush();

                    socket.close();
                    pw.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                teacherViewCourseMenu.setVisible(true);
            }
        });
        editQuiz = new JButton("Edit Quiz");
        editQuiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherViewCourseMenu.setVisible(false);
                String quizNameToEdit = JOptionPane.showInputDialog(null,
                        "Please enter the quiz name.");
                if (quizNameToEdit == null) {
                    JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
                            "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                try {
                    Socket socket = new Socket("localhost", 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("EDITQUIZ|" + quizNameToEdit + "|" + courseName);
                    pw.flush();

                    socket.close();
                    pw.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                teacherViewCourseMenu.setVisible(true);
            }
        });
        viewSubmission = new JButton("View Submission");
        viewSubmission.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherViewCourseMenu.setVisible(false);
                String quizNameToEdit = JOptionPane.showInputDialog(null,
                        "Please enter the quiz name.");
                if (quizNameToEdit == null) {
                    JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
                            "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String submissionToView = JOptionPane.showInputDialog(null,
                        "Please enter name of submission.");
                if (submissionToView == null) {
                    JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
                            "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                try {
                    Socket socket = new Socket("localhost", 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("VIEWSUBMISSION|" + courseName + "|" + quizNameToEdit + "|" + submissionToView);
                    pw.flush();

                    socket.close();
                    pw.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                teacherViewCourseMenu.setVisible(true);
            }
        });
        //Potential work around to display all courses and quizzes and submissions without it being directly in code.
//        printCourse = new JButton("Print All Available Courses");
//        printCourse.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Teacher.printCourses();
//            }
//        });
        printQuizzes = new JButton("Print All Quizzes For " + courseName);
        printQuizzes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherViewCourseMenu.setVisible(false);
                try {
                    Socket socket = new Socket("localhost", 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("PRINTQUIZZES2|" + courseName);
                    pw.flush();

                    socket.close();
                    pw.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                teacherViewCourseMenu.setVisible(true);
            }
        });
        printSubmissions = new JButton("Print Submission Names for Specified Quiz in " + courseName);
        printSubmissions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherViewCourseMenu.setVisible(false);
                String quizNameToView = JOptionPane.showInputDialog(null,
                        "Please enter the quiz name.");
                if (quizNameToView == null) {
                    JOptionPane.showMessageDialog(null, "Operation cancelled. Going back.",
                            "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                try {
                    Socket socket = new Socket("localhost", 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("PRINTSUBMISSIONS|" + courseName + "|" + quizNameToView);
                    pw.flush();

                    socket.close();
                    pw.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                teacherViewCourseMenu.setVisible(true);
            }
        });
        goBack = new JButton("Go Back");
        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherMainMenu.setVisible(true);
                teacherViewCourseMenu.setVisible(false);
            }
        });

        viewCourseMenu.add(deleteCourse);
        viewCourseMenu.add(deleteQuiz);
        viewCourseMenu.add(createQuiz);
        viewCourseMenu2.add(editQuiz);
        viewCourseMenu2.add(viewSubmission);
        viewCourseMenu2.add(printQuizzes);
        viewCourseMenu3.add(printSubmissions);
        viewCourseMenu3.add(goBack);

        teacherViewCourseMenu.add(viewCourseMenu, BorderLayout.NORTH);
        teacherViewCourseMenu.add(viewCourseMenu2, BorderLayout.CENTER);
        teacherViewCourseMenu.add(viewCourseMenu3, BorderLayout.SOUTH);
        teacherViewCourseMenu.setVisible(true);

    }

    public void createGUI() {

        frame = new JFrame("Welcome to the Quiz Learning Program");

        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        frame.setSize(600, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        create = new JButton("Create");
        create.addActionListener(this);
        login = new JButton("Login");
        login.addActionListener(this);
        exit = new JButton("Exit");
        exit.addActionListener(this);
        JPanel panel = new JPanel();
        panel.add(create);
        panel.add(login);
        panel.add(exit);
        frame.add(panel, BorderLayout.NORTH);
    }
}

//        int ongoingChoice;
//        do {
//            do {
//                try {
//                    System.out.println("1. Take Quiz\n2. View Submissions\n3. Exit");
//                    ongoingChoice = input.nextInt();
//                } catch (InputMismatchException e) {
//                    System.out.println("Enter a integer choice");
//                    ongoingChoice = 4;
//                    input.nextLine();
//                }
//                if (ongoingChoice < 1 || ongoingChoice > 3)
//                    System.out.println("Invalid selection");
//
//            } while (ongoingChoice < 1 || ongoingChoice > 3);
//
//            if (ongoingChoice == 1) {
//                //Takes quiz
//                //lists courses
//                input.nextLine();
//                System.out.println("\nAvailable Courses:");
//                Teacher.printCourses();
//                System.out.println("Which course would you like to access: ");
//                String course = input.nextLine(); //check to sure exists in coursenames.txt
//                if (Teacher.checkCourseExistence(course)) {
//                    //lists quizzes in course
//                    System.out.println("\nAvailable Quizzes:");
//                    Teacher.printQuizzes(course);
//                    System.out.println("Which quiz would you like to take: ");
//                    String quiz = input.nextLine(); //ensure exists in coursenamesquizzes
//                    if (Teacher.checkQuizExistence(course, quiz)) {
//
//                        ArrayList<String> submission = Student.answer(input, course, quiz);
//                        String total = submission.get(submission.size() - 1);
//                        submission.remove(submission.size() - 1);
//                        Student.writeFile(course, quiz, user, submission, total);
//                    }
//                }
//
//            } else if (ongoingChoice == 2) {
//                //views submissions
//                //lists courses
//                input.nextLine();
//                System.out.println("\nAvailable Courses:");
//                Teacher.printCourses();
//                System.out.println("Which course would you like to access: ");
//                String course = input.nextLine(); //check to sure exists in coursenames.txt
//                if (Teacher.checkCourseExistence(course)) {
//                    //lists quizzes in course
//                    System.out.println("\nAvailable Quizzes:");
//                    Teacher.printQuizzes(course);
//                    System.out.println("Which quiz would you like to view your submissions for: ");
//                    String quiz = input.nextLine(); //ensure exists in coursenamesquizzes
//                    if (Teacher.checkQuizExistence(course, quiz)) {
//                        Student.viewSubmissions(input, course, quiz, user);
//                    }
//                }
//            }
//        } while(ongoingChoice == 1 || ongoingChoice == 2);
//    }
