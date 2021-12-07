import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Client implements ActionListener {
    ArrayList<String> currentPoints = new ArrayList<>();
    ArrayList<String> currentAnswerList =  new ArrayList<>();
    ArrayList<String> currentQuizAndAnswers =  new ArrayList<>();
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
        ArrayList<String> userSubmissions = Student.printSubmissions(getCurrentCourse(), getCurrentQuiz(), getUsername());
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
                String submission = Student.viewSubmissions((String) cb.getSelectedItem());
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
        JButton finishQuiz = new JButton ("Finish Quiz");
        panel.add(finishQuiz, BorderLayout.AFTER_LINE_ENDS);
        finishQuiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame6.setVisible(false);
                ArrayList<String> points = Student.grading(getCurrentAnswerList(), quizAndAnswers);
                setCurrentPoints(points);
//                System.out.println(answerList);
//                System.out.println(points);
                Student.writeFile(getCurrentCourse(), getCurrentQuiz(), getUsername(), getCurrentPoints(), getCurrentAnswerList());
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

        String[] choices = Teacher.printCourses();

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

                    String[] choicesQuizzes = Teacher.printQuizzes(courseName);
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
                            if (Teacher.checkQuizExistence(courseName, quiz)) {
                                frame5.setVisible(false);
                                ArrayList<String> quizAndAnswers = Student.readQuiz(courseName, quiz);
                                answerQuiz(quizAndAnswers);
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
        JTextField password = new JTextField(8);
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
                    if (Login.isDuplicate(username.getText())) {
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
                        Login.writeNewUser(classification, username.getText(), password.getText());
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
        JTextField password = new JTextField(8);
        panel2.add(lbl3, BorderLayout.EAST);
        panel2.add(password, BorderLayout.AFTER_LINE_ENDS);
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean prompt2 = true;
                if (!Login.isDuplicate(username.getText())) {
                    JOptionPane.showMessageDialog(null, "Username not found, please try again.", "Username Error",
                            JOptionPane.ERROR_MESSAGE);
                    prompt2 = false;
                }
                if (!Login.login(username.getText(), password.getText())) {
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
        String type = Login.getClassification(username);
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
            //TO-DO
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
                Teacher.deleteCourse(courseName);
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
                Teacher.deleteQuiz(quizNameToDelete, courseName);
                teacherViewCourseMenu.setVisible(true);
            }
        });
        createQuiz = new JButton("Create Quiz");
        createQuiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacherViewCourseMenu.setVisible(false);
                Teacher.createQuiz(courseName);
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
                Teacher.editQuiz(quizNameToEdit, courseName);
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
                Teacher.viewSubmission(courseName, quizNameToEdit, submissionToView);
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
                Teacher.printQuizzes(courseName);
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
                Teacher.printSubmissions(courseName, quizNameToView);
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

public void createGUI(){

        //Creates Login File
        File logins=new File("login.txt");
        if(!logins.exists()){
        try{
        logins.createNewFile();
        }catch(IOException e){
        e.printStackTrace();
        }
        }

        //Creates coursenames file
        File courseNames=new File("CourseNames.txt");
        if(!courseNames.exists()){
        try{
        courseNames.createNewFile();
        }catch(IOException e){
        e.printStackTrace();
        }
        }


        frame=new JFrame("Welcome to the Quiz Learning Program");

        Container content=frame.getContentPane();
        content.setLayout(new BorderLayout());

        frame.setSize(600,100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        create=new JButton("Create");
        create.addActionListener(this);
        login=new JButton("Login");
        login.addActionListener(this);
        exit=new JButton("Exit");
        exit.addActionListener(this);
        JPanel panel=new JPanel();
        panel.add(create);
        panel.add(login);
        panel.add(exit);
        frame.add(panel,BorderLayout.NORTH);
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
