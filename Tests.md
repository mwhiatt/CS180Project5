# Testing Pathways for QuickQuiz #

## *FRIENDLY REMINDER: ALL PATHWAYS MUST BE TESTED IN SUCESSION* ##

## Test 1: Create Account (Teacher) ##
1. Click [Create]
2. Select option [Teacher] form drop down list
3. Write "TestTeacher" in the [Username] text field
4. Write 'password' in the [Password] password field
5. Click [OK]
6. Click [Back]

## Expected Output: ##
1. login.txt file will be created with the following text:
Teacher
TestTeacher
password
2. You can now login with these details anytime.

## Test 2: Create Account (Student) ##

1. Click [Create]
2. Select option [Student] form drop down list
3. Write "TestStudent" in the [Username] text field
4. Write 'password' in the [Password] password field
5. Click [OK]
6. Click [Exit]

## Expected Output: ##
1. login.txt file will be created with the following text:
Student
TestStudent
password
2. You can now login with these details anytime.

## Test 3: Create Course (Teacher) ##
1. Click [login]
2. Write "TestTeacher" in the [Username] text field
3. Write 'password' in the [Password] password field
4. Click [Create Course]
5. Enter "CS180" in the text field and click [OK].
6. Click [OK] on the Course Created message
7. Click [Exit]

## Expected Output: ##
1. "CS180" is appended to the CourseNames.txt file
2. There is now a CS180Quizzes.txt file 

## Test 4: Create Quiz through GUIs (Teacher) ##
1. Click [login]
2. Write "TestTeacher" in the [Username] text field
3. Write 'password' in the [Password] password field
4. Click [View Specific Course] and write "CS180"
5. Click [Create Quiz]
6. Type "no" and click [OK] in the import menu
7. Type the name, "Quiz1" into the quiz name field press [OK]
8. Type the question into the question field and press [OK]
9. Write the answer choice in the field and press [OK] (4 times)
10. Type "no" in the point value field and press [OK]
11. Type "no" in the field that asks if you'd like to enter another question and click [OK]
12. click [OK] on the success message popup

## Expected Output: ##
1. User is returned to Course menu
2. File titled "CS180Quiz1.txt" has been created and contains quiz contents
3. File titled "CS180Quiz1Submissions.txt" has been created and is empty

## Test 5: Take Quiz (Answering through GUI) (Student) ##
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

## Expected Output: ##
1. A submission file called "CS180Quiz1TestStudent.txt" is now created (we will view this later).
2. This file is stored under "CS180Quiz1Submissions.txt" too.

## Test 6: Take Quiz (Answering through file imports) (Student) ##
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

## Expected Output: ##
1. A submission file called "CS180Quiz1TestStudent2.txt" is now created (we will view this later).
2. This file is stored under "CS180Quiz1Submissions.txt" too.

## Test 7: View Submissions (Student) ##
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

## Expected Output: ##
1. You can view your submission in a GUI with its points and timestamp.

## Test 8: View Submissions (Teacher) ##
1. Click [login]
2. Write "TestTeacher" in the [Username] text field
3. Write 'password' in the [Password] password field
4. Click [View Specific Course] and write "CS180"
5. Click [Print Submission Names for Specified Quiz in CS180] to find submission title
6. Enter "Quiz1" and press [OK]
7. View Submissions and press [OK]
8. Click [View Submission] and enter "Quiz1"
9. Enter submission name "TestStudent" and view submission
10. Click [OK]

## Expected Output: ##
1. You can view the student's submission in a GUI component with its points and timestamp.
