import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
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
    final static String SERVERADDRESS = "localhost";
    ArrayList<String> currentPoints = new ArrayList<>();
    ArrayList<String> currentAnswerList = new ArrayList<>();
    ArrayList<String> currentQuizAndAnswers = new ArrayList<>();
    int currentCount = 0;
    String currentCourse;
    String currentQuiz;
    String username;
    JFrame createGUIFrame;
    JFrame createAccountFrame;
    JFrame loginFrame;
    JFrame studentMenuFrame;
    JFrame takeQuizFrame;
    JFrame answerQuizFrame;
    JFrame viewSubmissionsFrame;
    JFrame viewingSubmissionsFrame;
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
            createGUIFrame.setVisible(false);
        }
        if (e.getSource() == login) {
            this.login();
            createGUIFrame.setVisible(false);
        }
        if (e.getSource() == exit) {
            this.exit();
            createGUIFrame.setVisible(false);
            studentMenuFrame.setVisible(false);
        }
        if (e.getSource() == takeQuiz) {
            this.takeQuiz();
            studentMenuFrame.setVisible(false);
        }
    }
    //allows a student to view their previous submissions
    public void viewSubmissions() {
        viewSubmissionsFrame = new JFrame("View Submissions");
        viewSubmissionsFrame.setVisible(true);
        viewSubmissionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewSubmissionsFrame.setSize(500, 250);
        viewSubmissionsFrame.setLocation(430, 100);
        JPanel panel = new JPanel();
        String response = "";
        try {
            Socket socket = new Socket(SERVERADDRESS, 4343);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            pw.write("PRINTSUBMISSIONS|" + getCurrentCourse() + "|" + getCurrentQuiz() + "|" + getUsername() + "\n");
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
        viewSubmissionsFrame.add(panel, BorderLayout.NORTH);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewSubmissionsFrame.setVisible(false);
                viewingSubmissionsFrame = new JFrame("View Submissions");
                viewingSubmissionsFrame.setVisible(true);
                viewingSubmissionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                viewingSubmissionsFrame.setSize(500, 250);
                viewingSubmissionsFrame.setLocation(430, 100);
                JPanel panel = new JPanel();
                String submission = "";
                try {
                    Socket socket = new Socket(SERVERADDRESS, 4343);
                    BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("VIEWSUBMISSIONS|" + (String) cb.getSelectedItem() + "\n");
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
                viewingSubmissionsFrame.add(panel, BorderLayout.NORTH);
                ok2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        viewingSubmissionsFrame.setVisible(false);
                        viewSubmissionsFrame.setVisible(true);
                    }
                });
                viewingSubmissionsFrame.setVisible(true);
            }
        });
        viewSubmissionsFrame.setVisible(true);

    }

    //allows a student to answer a quiz
    public void answerQuiz(ArrayList<String> quizAndAnswers) {
        if (quizAndAnswers.size() <= getCurrentCount()) {
            answerQuizFrame.setVisible(false);
            ArrayList<String> points = Student.grading(getCurrentAnswerList(), quizAndAnswers);
            setCurrentPoints(points);
            setCurrentAnswerList(new ArrayList<String>());
            setCurrentCount(0);
            try {
                Socket socket = new Socket(SERVERADDRESS, 4343);
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
                pw.write("GRADING|" + currentAnswersStr + "|" + quizStr + "\n");
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
                Socket socket = new Socket(SERVERADDRESS, 4343);
                PrintWriter pw = new PrintWriter(socket.getOutputStream());

                //Sending to server to be written
                pw.write("WRITEFILE|" + getCurrentCourse() + "|" + getCurrentQuiz() + "|" + getUsername()
                        + "|" + packageList(getCurrentPoints()) + "|" + packageList(getCurrentAnswerList()) + "\n");
                pw.flush();

                pw.close();
                socket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            setCurrentAnswerList(new ArrayList<String>());
            return;
        }
//        ArrayList<String> answerList = new ArrayList<>();
        answerQuizFrame = new JFrame("Quiz Answer");
        answerQuizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        answerQuizFrame.setSize(800, 600);
        answerQuizFrame.setLocation(430, 100);
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
        panel.add(ok3, BorderLayout.AFTER_LINE_ENDS);
        JButton backAnswerQuiz = new JButton("Back");
        panel.add(backAnswerQuiz, BorderLayout.AFTER_LINE_ENDS);
        answerQuizFrame.add(panelForQuestion, BorderLayout.NORTH);
        answerQuizFrame.add(panel, BorderLayout.SOUTH);
        backAnswerQuiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                answerQuizFrame.setVisible(false);
                takeQuizFrame.setVisible(true);
            }
        });
        ok3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                {
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
                    answerQuizFrame.removeAll();
                    answerQuizFrame.setVisible(false);
                    answerQuiz(quizAndAnswers);
                    //answerQuizFrame.setVisible(false);
                }
            }
        });
        answerQuizFrame.setVisible(true);

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

    //allows students to take a quiz
    public void takeQuiz() {
        takeQuizFrame = new JFrame("Available Quizzes");
        takeQuizFrame.setVisible(true);
        takeQuizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        takeQuizFrame.setSize(500, 140);
        takeQuizFrame.setLocation(430, 100);
        JPanel panel = new JPanel();
        JLabel lbl = new JLabel("Select an option click OK");
        panel.add(lbl, BorderLayout.AFTER_LINE_ENDS);
        ArrayList<String> courseList = new ArrayList<>();
        try {
            Socket socket = new Socket(SERVERADDRESS, 4343);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            //Sending to server to be written
            pw.write("PRINTCOURSES" + "\n");
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
        JButton backTakeQuiz = new JButton("Back");
        panel.add(ok3, BorderLayout.AFTER_LINE_ENDS);
        panel.add(backTakeQuiz, BorderLayout.AFTER_LINE_ENDS);
        ok3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                {
                    takeQuizFrame.setVisible(false);
                    String courseName = (String) cb.getSelectedItem();
                    setCurrentCourse(courseName);
                    JPanel panel2 = new JPanel();
                    JLabel lbl2 = new JLabel("Select an option click OK");
                    panel2.add(lbl2, BorderLayout.AFTER_LINE_ENDS);
                    ArrayList<String> quizList = new ArrayList<>();
                    try {
                        Socket socket = new Socket(SERVERADDRESS, 4343);
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());

                        //Sending to server to be written
                        pw.write("PRINTQUIZZES|" + courseName + "\n");
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
                                Socket socket = new Socket(SERVERADDRESS, 4343);
                                BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                PrintWriter pw = new PrintWriter(socket.getOutputStream());

                                pw.write("CHECKQUIZ|" + getCurrentCourse() + "|" + getCurrentQuiz() + "\n");
                                pw.flush();
                                String response = bfr.readLine();
                                if (response.equals("true")) {
                                    takeQuizFrame.setVisible(false);
                                    pw.write("READQUIZ|" + getCurrentCourse() + "|" + getCurrentQuiz() + "\n");
                                    ArrayList<String> quizAndAnswers = parseMessage(bfr.readLine());
                                    setCurrentQuizAndAnswers(quizAndAnswers);
                                    answerQuiz(quizAndAnswers);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Quiz doesn't exist", "Check Quiz error", JOptionPane.ERROR_MESSAGE);                                    //GUI implementation saying quiz doesn't exist
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

                    takeQuizFrame.add(panel2, BorderLayout.SOUTH);
                    takeQuizFrame.setVisible(true);
                }
            }
        });
        backTakeQuiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                takeQuizFrame.setVisible(false);
                studentMenuFrame.setVisible(true);
            }
        });

        takeQuizFrame.add(panel, BorderLayout.NORTH);


    }

    //allows users to create a new account
    public void create() {
        createAccountFrame = new JFrame("Create Account");
        createAccountFrame.setVisible(true);
        createAccountFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createAccountFrame.setSize(500, 140);
        createAccountFrame.setLocation(430, 100);

        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        JLabel lbl = new JLabel("Select an option click OK");
        panel.add(lbl, BorderLayout.AFTER_LINE_ENDS);

        String[] choices = {"Teacher", "Student"};

        final JComboBox<String> cb = new JComboBox<String>(choices);

        cb.setMaximumSize(cb.getPreferredSize());
        panel.add(cb, BorderLayout.AFTER_LINE_ENDS);

        createAccountFrame.add(panel, BorderLayout.NORTH);

        JLabel lbl2 = new JLabel("Username");
        JTextField username = new JTextField(8);
        panel2.add(lbl2, BorderLayout.EAST);
        panel2.add(username, BorderLayout.AFTER_LINE_ENDS);
        createAccountFrame.add(panel2, BorderLayout.CENTER);

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
                        Socket socket = new Socket(SERVERADDRESS, 4343);
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());

                        pw.write("ISDUPLICATE|" + username.getText() + "\n");
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
                            Socket socket = new Socket(SERVERADDRESS, 4343);
                            PrintWriter pw = new PrintWriter(socket.getOutputStream());

                            pw.write("WRITENEWUSER|" + classification + "|" + username.getText() + "|"
                                    + password.getText() + "\n");
                            pw.flush();

                            socket.close();
                            pw.close();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        setUsername(username.getText());
                        StudentMenu(username.getText());
                        createAccountFrame.setVisible(false);
                    }
                }
            }
        });
        JButton backCreateAccount = new JButton("Back");
        panel3.add(backCreateAccount, BorderLayout.AFTER_LINE_ENDS);
        panel3.add(ok3, BorderLayout.AFTER_LINE_ENDS);
        backCreateAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccountFrame.setVisible(false);
                createGUIFrame.setVisible(true);
            }
        });
        createAccountFrame.add(panel3, BorderLayout.SOUTH);
//        createAccountFrame.setVisible(true);
    }
    //allows users to login
    public void login() {
        loginFrame = new JFrame("Login");
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(500, 120);
        loginFrame.setLocation(430, 100);
        JLabel lbl2 = new JLabel("Username");
        JTextField username = new JTextField(8);
        panel.add(lbl2, BorderLayout.EAST);
        panel.add(username, BorderLayout.AFTER_LINE_ENDS);
        loginFrame.add(panel, BorderLayout.NORTH);
        JLabel lbl3 = new JLabel("Password");
        JPasswordField password = new JPasswordField(8);
        panel2.add(lbl3, BorderLayout.EAST);
        panel2.add(password, BorderLayout.AFTER_LINE_ENDS);
        JButton ok = new JButton("OK");
        JButton backLogin = new JButton("Back");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean prompt2 = true;
                String dupResponse = "0";
                try {
                    Socket socket = new Socket(SERVERADDRESS, 4343);
                    BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("ISDUPLICATE|" + username.getText() + "\n");
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
                    Socket socket = new Socket(SERVERADDRESS, 4343);
                    BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("LOGIN|" + username.getText() + "|" + password.getText() + "\n");
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
                    loginFrame.setVisible(false);
                }
            }
        });
        panel2.add(ok, BorderLayout.AFTER_LINE_ENDS);
        panel2.add(backLogin, BorderLayout.AFTER_LINE_ENDS);
        backLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginFrame.setVisible(false);
                createGUIFrame.setVisible(true);
            }
        });
        loginFrame.add(panel2, BorderLayout.CENTER);

        loginFrame.setVisible(true);

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
    //menu shown for Teacher/Student
    public void StudentMenu(String username) {
        String type = "";
        try {
        	System.out.println("Stuedent Menu get classification hit");
            Socket socket = new Socket(SERVERADDRESS, 4343);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            System.out.println("Connection for get established, user: " + username);
            pw.write("GETCLASSIFICATION|" + username + "\n");
            pw.flush();
            type = bfr.readLine();
            System.out.println("Classification Received: " + type);          
            bfr.close();
            pw.close();
            socket.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
        if (type.equals("Student")) {
            studentMenuFrame = new JFrame("Welcome Student " + username);
            studentMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            studentMenuFrame.setSize(500, 100);
            studentMenuFrame.setLocation(430, 100);
            takeQuiz = new JButton("Take Quiz");
            takeQuiz.addActionListener(this);
            exit = new JButton("Exit");
            exit.addActionListener(this);
            JButton backStudentMenu = new JButton("Back");
            backStudentMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    studentMenuFrame.setVisible(false);
                    loginFrame.setVisible(true);
                }
            });
            JPanel panel2 = new JPanel();
            panel2.add(takeQuiz);
            panel2.add(exit);
            panel2.add(backStudentMenu);
            studentMenuFrame.add(panel2, BorderLayout.NORTH);
            studentMenuFrame.setVisible(true);
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
                    Socket socket = new Socket(SERVERADDRESS, 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("DELETECOURSE|" + courseName + "\n");
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
                    Socket socket = new Socket(SERVERADDRESS, 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("DELETEQUIZ|" + courseName + "|" + quizNameToDelete + "\n");
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
                    Socket socket = new Socket(SERVERADDRESS, 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("CREATEQUIZ|" + courseName + "\n");
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
                    Socket socket = new Socket(SERVERADDRESS, 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("EDITQUIZ|" + quizNameToEdit + "|" + courseName + "\n");
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
                    Socket socket = new Socket(SERVERADDRESS, 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("VIEWSUBMISSION|" + courseName + "|" + quizNameToEdit + "|" + submissionToView + "\n");
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
                    Socket socket = new Socket(SERVERADDRESS, 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("PRINTQUIZZES2|" + courseName + "\n");
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
                    Socket socket = new Socket(SERVERADDRESS, 4343);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream());

                    pw.write("PRINTSUBMISSIONS|" + courseName + "|" + quizNameToView + "\n");
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
    //creates the first menu that users interact with
    public void createGUI() {

        createGUIFrame = new JFrame("Welcome to the Quiz Learning Program");

        Container content = createGUIFrame.getContentPane();
        content.setLayout(new BorderLayout());

        createGUIFrame.setSize(600, 100);
        createGUIFrame.setLocationRelativeTo(null);
        createGUIFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createGUIFrame.setVisible(true);

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
        createGUIFrame.add(panel, BorderLayout.NORTH);
    }
}
