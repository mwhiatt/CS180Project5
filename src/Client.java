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
    JFrame frame;
    JFrame frame2;
    JFrame frame3;
    JFrame frame4;
    JFrame frame5;
    JFrame frame6;
    JFrame frame7;

    JFrame teacherMainMenu;
    JFrame teacherViewCourseMenu;

    JButton create; // create quiz
    JButton login; // login
    JButton exit;
    JButton takeQuiz;
    JButton viewSubmissions;
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
//            if (e.getSource() == answerQuiz) {
//                this.answerQuiz();
//
//            }

    }

    public void answerQuiz(ArrayList<String> quizAndAnswers) {
        for (int c = 0; c < quizAndAnswers.size(); c+=3) {
            frame6 = new JFrame("Quiz Answer");
            frame6.setVisible(true);
            frame6.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame6.setSize(500, 250);
            frame6.setLocation(430, 100);
            JPanel panel = new JPanel();
            JPanel panelForQuestion = new JPanel();
            JTextArea question = new JTextArea(quizAndAnswers.get(c));
            panelForQuestion.add(question, BorderLayout.AFTER_LINE_ENDS);
            JLabel lbl = new JLabel("Select an option click OK");
            panel.add(lbl, BorderLayout.AFTER_LINE_ENDS);

            String[] choices = {"Answer through GUI", "Answer through file imports"};

            final JComboBox<String> cb = new JComboBox<String>(choices);

            cb.setMaximumSize(cb.getPreferredSize());
            panel.add(cb, BorderLayout.AFTER_LINE_ENDS);
            JButton ok3 = new JButton("OK");
            panel.add(ok3, BorderLayout.AFTER_LINE_ENDS);
            frame6.add(panelForQuestion, BorderLayout.NORTH);
            frame6.add(panel, BorderLayout.SOUTH);
            ok3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    {
                        frame6.setVisible(false);
                        String answerChoice = (String) cb.getSelectedItem();
                        ArrayList<String> answerList = new ArrayList<>();
                        if (answerChoice.equals("Answer through GUI")) {
                            frame7 = new JFrame("Answer through GUI");
                            frame7.setSize(500, 250);
                            frame7.setLocation(430, 100);
                            JPanel panelForQuestion = new JPanel();
//                            JTextArea question = new JTextArea(quizAndAnswers.get(c));
                            panelForQuestion.add(question, BorderLayout.AFTER_LINE_ENDS);
                            JLabel answerLabel = new JLabel("Answer:");
                            JPanel panel3 = new JPanel();
                            JTextField answer = new JTextField(8);
                            panel3.add(answerLabel, BorderLayout.WEST);
                            panel3.add(answer, BorderLayout.AFTER_LINE_ENDS);
                            JButton ok4 = new JButton("OK");
                            panel3.add(ok4, BorderLayout.AFTER_LINE_ENDS);
                            ok4.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    answerList.add(answer.getText());
                                }
                            });
                            frame7.add(panelForQuestion, BorderLayout.NORTH);
                            frame7.add(panel3, BorderLayout.CENTER);
                            frame7.setVisible(true);


                        } else {
                            //to-do
                        }
                    }
                }
            });
        }




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
                    JPanel panel2 = new JPanel();
                    JLabel lbl2 = new JLabel("Select an option click OK");
                    panel2.add(lbl2, BorderLayout.AFTER_LINE_ENDS);

                    String[] choicesQuizzes = Teacher.printQuizzes(courseName);
                    final JComboBox<String> cb2 = new JComboBox<String>(choicesQuizzes);

                    cb2.setMaximumSize(cb2.getPreferredSize());
                    panel2.add(cb2, BorderLayout.AFTER_LINE_ENDS);
                    String quiz = (String) cb2.getSelectedItem();
                    JButton ok4 = new JButton("OK");
                    ok4.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (Teacher.checkQuizExistence(courseName, quiz)) {
                                frame5.setVisible(false);
//                                ArrayList<String> quizAndAnswers = Student.readQuiz(courseName, quiz);
//                                answerQuiz(quizAndAnswers);

//                                ArrayList<String> submission = Student.answer(input, course, quiz);
//                                String total = submission.get(submission.size() - 1);
//                                submission.remove(submission.size() - 1);
//                                Student.writeFile(course, quiz, user, submission, total);
                            }
                        }
                    });
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
                    StudentMenu(username.getText());
                    frame3.setVisible(false);
                }
            }
        });
        panel2.add(ok, BorderLayout.AFTER_LINE_ENDS);
        frame3.add(panel2, BorderLayout.CENTER);

        frame3.setVisible(true);

    }

    public void exit() { //nullpointerexception thrown
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
            viewSubmissions = new JButton("View Submissions");
            viewSubmissions.addActionListener(this);
            takeQuiz = new JButton("Take Quiz");
            takeQuiz.addActionListener(this);
            exit = new JButton("Exit");
            exit.addActionListener(this);
            JPanel panel2 = new JPanel();
            panel2.add(viewSubmissions);
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
                    while (courseNameRequested.isEmpty() || courseNameRequested.isBlank()) {
                        courseNameRequested = JOptionPane.showInputDialog(null,
                                "Enter something. Please enter the course name.");
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
                    while (courseNameRequested.isEmpty() || courseNameRequested.isBlank()) {
                        courseNameRequested = JOptionPane.showInputDialog(null,
                                "Enter something. Please enter the course name.");
                    }
                    if (Teacher.checkCourseExistence(courseNameRequested)) {
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
                String submissionToView = JOptionPane.showInputDialog(null,
                        "Please enter name of submission.");
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
