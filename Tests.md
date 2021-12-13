#Testing Pathways for QuickQuiz#

##*FRIENDLY REMINDER: ALL PATHWAYS MUST BE TESTED IN SUCESSION*##

##Test 1: Create Account (Teacher)##
1. Click [Create]
2. Select option [Teacher] form drop down list
3. Write "TestTeacher" in the [Username] text field
4. Write 'password' in the [Password] password field
5. Click [OK]
6. Click [Back]

##Expected Output:##
1. login.txt file will be created with the following text:
Teacher
TestTeacher
password
2. You can now login with these details anytime.

##Test 2: Create Account (Student)##

1. Click [Create]
2. Select option [Student] form drop down list
3. Write "TestStudent" in the [Username] text field
4. Write 'password' in the [Password] password field
5. Click [OK]
6. Click [Exit]

##Expected Output:##
1. login.txt file will be created with the following text:
Student
TestStudent
password
2. You can now login with these details anytime.

##Test 3: Create Course (Teacher)##
1. Click [login]
2. Write "TestTeacher" in the [Username] text field
3. Write 'password' in the [Password] password field
4. Click [Create Course]
5. Enter "CS180" in the text field and click [OK].
6. Click [OK] on the Course Created message
7. Click [Exit]

##Expected Output:##
1. "CS180" is appended to the CourseNames.txt file
2. There is now a CS180Quizzes.txt file 

##Test 4: Create Quiz through GUIs (Teacher)##
1. Click [login]
2. Write "TestTeacher" in the [Username] text field
3. Write 'password' in the [Password] password field
4. Click [View Specific Course] and write "CS180"
5. Click [Create Quiz]
----TODO----
(FYI:
COURSE: CS180
QUIZ: Quiz1
No of Questions: 1
Correct Answer: A

##Test 5: Take Quiz (Answering through GUI) (Student)##
1. Click [login]
2. Write "TestStudent" in the [Username] text field
3. Write 'password' in the [Password] password field
4. Click [Take Quiz]
5. Press [OK]
6. Select "CS180" from the drop down list and press [OK]
7. Press [OK]
8. Select "Quiz1" from drop down list and press [OK]
9. Select "Answer through GUI" and press [OK]
10. Enter answer "A" into input box and press [OK]
11. Press [OK]
12. Press [Back]
13. Press Exit

##Expected Output:##
1. A submission file called "CS180Quiz1TestStudent.txt" is now created (we will view this later).
2. This file is stored under "CS180Quiz1Submissions.txt" too.

##Test 6: Take Quiz (Answering through file imports) (Student)##
*FOR THIS TEST, YOU MUST CREATE A .txt FILE ON YOUR PC WHERE THE ONLY INPUT CAN BE A SINGLE LETTER i,e A*
1. Click [login]
2. Write "TestStudent" in the [Username] text field
3. Write 'password' in the [Password] password field
4. Click [Take Quiz]
5. Press [OK]
6. Select "CS180" from the drop down list and press [OK]
7. Press [OK]
8. Select "Quiz1" from drop down list and press [OK]
9. Select "Answer through File Imports" and press [OK]
10. Find file in your PC and select [OK]
11. Press [OK]
12. Press [Back]
13. Press Exit

##Expected Output:##
1. A submission file called "CS180Quiz1TestStudent2.txt" is now created (we will view this later).
2. This file is stored under "CS180Quiz1Submissions.txt" too.

##Test 7: View Submissions (Student)##
1. Click [login]
2. Write "TestStudent" in the [Username] text field
3. Write 'password' in the [Password] password field
4. Click [Take Quiz]
5. Press [OK]
6. Select "CS180" from the drop down list and press [OK]
7. Press [OK]
8. Select "Quiz1" from drop down list and press [View Submissions]
9. Select "CS180Quiz1TestStudent.txt" from drop down list and press [OK]
10. Press [Back]
11. Press [Back]
12. Press [Back]
13. Press [Exit]

##Expected Output:##
1. You can view your submission in a GUI with its points and timestamp.
