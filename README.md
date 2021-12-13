# CS180Project5
Updated Version of our quiz tool application to include support for multiple users and a graphical user interface. 

Our CS180 project to construct the quiz tool of a learning management system.
To run the project, ensure the following files are included in your directory:
- Login.java
- Server.java
- Client.java
- Student.java
- Teacher.java
1. Run Server.java
2. Run Client.java (Set SERVERADDRESS (Line 37) to desired server address, address is localhost by default)
3. Follow directions on the User Interface
Pointers: No login has been created so please create a login before proceeding

Parts submitted by:
 - Vocareum: Matt Hiatt
 - Report on Brightspace: Matt Hiatt
 - Video Presentation: Aryan Mathur
Class Descriptions:


- Login: The Login class contains methods that are called in Client to sign users in to the program. It contains   
 the isDuplicate method which returns true or false depending on if the username being entered is already stored  
 in logins.txt. It contains the writeNewUser method which writes a new classification, username, and password to the  
 logins.txt file. It contains the login method which returns true or false depending on if the username and password  
 a user enters match a pair stored in the logins.txt file. It contains the getClassification method which returns the   
 Teacher or Student classification of a given user. It was tested with test cases to ensure the methods are at full  
 functionality and all inputs that could be received as parameters from Client are handled. 
 
- Server: The server class receives written information from the client class. It handles the inputs and makes the appropriate method calls. Method calls and handling
 are only done in Server so that all data is available to all our users and not stuck on a local machine. 

- Client: The Client class handles all the GUI's and contains methods to handle our programs UI. 
   
- Student: The Student class contains methods that are called in Client to implement the Student functionality. The methods are as follows: readQuiz, writeFile,
answerImportFile, printSubmission, viewSubmissions and grading. Read Quiz is used to read the quiz file that the student has chosen to take and inserts the content
of the quiz into an array which can be displayed in the Client class for the student. It returns an arraylist of the students answers to be formed into
the submission file by writeFile. The writeFile method creates and timestamps the submission for each time the student takes a quiz. It also adds the submission
name to the master submission file for the quiz. The answerImportFile enables the student to answer the quiz by importing file. The grading method is used to grade
the submission of a student and returns an array with points earned for each question. The print submissions method is used to print the submissions of the student
for a quiz they have taken and displays all of their attempts. Last is the viewSubmissions method which prints out the submission that they have chosen to view.
   
- Teacher: The Teacher class contains the methods responsible for the teacher functionality as well as some quality of life methods  
 that are used in driver to reduce code recycling. The createCourse method creates the quiz title master file for the course as well  
 as adds the course title to the CourseNames file. The deleteCourse method delets a course from the CourseNames file and deletes all   
 files related to the course in question with help from the deleteQuiz method. The deleteQuiz method deletes a quiz, removes the title  
 from the course quizzes master file and deletes all related submission files. createQuiz adds a new quiz title to the course's quiz   
 name master file as well as creates the quiz either through terminal entry or through the importation of a file. editQuiz allows a  
 teacher to review a quiz and pick a question to change. viewSubmission shows a teacher a students submission for the submission title  
 they select. printCourses prints the names of the courses so the user can see their choices. printQuizzes prints the names of the   
 quizzes. printSubmissions prints the names of the submissions. checkSubmissionExistence returns true if a submission exists to be  
 viewed, it is used to prevent misinputs. checkQuizExistence returns true if a quiz exists. checkCourseExistence returns true if a  
 given course exists. quizImport writes the imported quiz title to the master file of quizzes for a course and creates a file for   
 the new quiz. Test cases were written to test each intended path in the Teacher menu system and ensure that all outputs from methods  
 in driver matched our expected outputs.  
