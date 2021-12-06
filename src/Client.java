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
//            if (e.getSource() == answerQuiz) {
//                this.answerQuiz();
//
//            }

        }

    public void answerQuiz() {
    }

    public void takeQuiz() {
        frame5 =  new JFrame("Available Quizzes");
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

                    String[]choicesQuizzes = Teacher.printQuizzes(courseName);
                    final JComboBox<String> cb2 = new JComboBox<String>(choicesQuizzes);

                    cb2.setMaximumSize(cb2.getPreferredSize());
                    panel2.add(cb2, BorderLayout.AFTER_LINE_ENDS);
                    String quiz = (String) cb2.getSelectedItem();
                    JButton ok4 = new JButton("OK");
                    ok4.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
//                            if (Teacher.checkQuizExistence(courseName, quiz)) {
//
//                                ArrayList<String> submission = Student.answer(input, course, quiz);
//                                String total = submission.get(submission.size() - 1);
//                                submission.remove(submission.size() - 1);
//                                Student.writeFile(course, quiz, user, submission, total);
//                            }
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

        String[] choices = { "Teacher", "Student" };

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
        } else if (type.equals("Teacher")){
            //TO-DO
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
    }

    public void createGUI() {

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
