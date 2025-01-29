show databases;

create database learning_platform;

use  learning_platform;

show tables;

-- Table: Users
CREATE TABLE Users (
    user_id INT PRIMARY KEY auto_increment,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(50),
    progress JSON
);

-- Table: Courses
CREATE TABLE Courses (
    course_id INT PRIMARY KEY,
    course_name VARCHAR(100),
    description TEXT,
    level ENUM('Basic', 'Advanced'),
    duration INT
);

-- Table: Lessons
CREATE TABLE Lessons (
    lesson_id INT PRIMARY KEY,
    course_id INT,
    lesson_title VARCHAR(100),
    content TEXT,
    `order` INT,
    FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);

CREATE TABLE User_Course_Enrollment (
    user_id INT,
    course_id INT,
    PRIMARY KEY (user_id, course_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),  -- Assuming you have a Users table
    FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);

-- Table: Quizzes
CREATE TABLE Quizzes (
    quiz_id INT PRIMARY KEY,
    course_id INT,
    quiz_title VARCHAR(100),
    total_marks INT,
    FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);

-- Table: Questions
CREATE TABLE Questions (
    question_id INT PRIMARY KEY,
    quiz_id INT,
    question_text TEXT,
    option_a VARCHAR(100),
    option_b VARCHAR(100),
    option_c VARCHAR(100),
    option_d VARCHAR(100),
    correct_option CHAR(1),
    FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id)
);

-- Table: Enrollments
CREATE TABLE Enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    course_id INT,
    enroll_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completion_status VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);


-- Table: QuizResults
CREATE TABLE QuizResults (
    result_id INT AUTO_INCREMENT PRIMARY KEY,  
    user_id INT,
    quiz_id INT,
    score INT,
    attempt_date DATE,  
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id)
);

-- Inserting Courses into the 'Courses' table
INSERT INTO Courses (course_id, course_name, description, level, duration)
VALUES
(1, 'Introduction to Data Science', 'Learn the basics of Data Science, including data processing, visualization, and analysis.', 'Basic', 30),
(2, 'Advanced Python Programming', 'Master advanced Python concepts, libraries, and frameworks for data analysis.', 'Advanced', 45),
(3, 'Machine Learning Foundations', 'Understand the core principles of machine learning, algorithms, and model evaluation.', 'Advanced', 40),
(4, 'Web Development with JavaScript', 'Learn how to create dynamic, interactive websites using JavaScript.', 'Basic', 25),
(5, 'Introduction to Cloud Computing', 'An overview of cloud computing principles and major cloud platforms.', 'Basic', 35),
(6, 'Advanced Data Analytics', 'Explore advanced data analytics techniques using Python and R for big data analysis.', 'Advanced', 50),
(7, 'Database Management Systems', 'Understand the concepts of relational databases, SQL, and database management systems.', 'Basic', 30);


-- Inserting Lessons into the 'Lessons' table
INSERT INTO Lessons (lesson_id, course_id, lesson_title, content, `order`)
VALUES
(1, 1, 'Introduction to Data Science', 'Overview of Data Science and its applications.', 1),
(2, 1, 'Data Preprocessing', 'Learn how to clean and preprocess data for analysis.', 2),
(3, 2, 'Python Data Structures', 'Learn about lists, tuples, dictionaries, and sets in Python.', 1),
(4, 2, 'Advanced Python Libraries', 'Explore advanced libraries like Pandas, NumPy, and Matplotlib.', 2),
(5, 3, 'Introduction to Machine Learning', 'Basic concepts and types of machine learning algorithms.', 1),
(6, 3, 'Supervised Learning Algorithms', 'Explore supervised learning algorithms like regression and classification.', 2),
(7, 4, 'JavaScript Basics', 'Learn the basics of JavaScript and how it integrates with HTML and CSS.', 1),
(8, 4, 'DOM Manipulation', 'Learn how to manipulate HTML DOM elements using JavaScript.', 2),
(9, 5, 'Cloud Computing Introduction', 'Introduction to cloud computing and popular platforms like AWS, Google Cloud.', 1),
(10, 5, 'Cloud Infrastructure', 'Understand cloud infrastructure and architecture concepts.', 2);

-- Inserting Quizzes into the 'Quizzes' table
INSERT INTO Quizzes (quiz_id, course_id, quiz_title, total_marks)
VALUES
(1, 1, 'Data Science Basics Quiz', 20),
(2, 2, 'Advanced Python Programming Quiz', 30),
(3, 3, 'Machine Learning Quiz', 25),
(4, 4, 'JavaScript Basics Quiz', 20),
(5, 5, 'Cloud Computing Quiz', 15),
(6, 6, 'Data Analytics Quiz', 25),
(7, 7, 'Database Management Systems Quiz', 20);


UPDATE Quizzes
SET total_marks = 25
WHERE course_id IN (1, 2, 3, 4, 5, 6, 7);



-- Questions for Data Science Basics Quiz
INSERT INTO Questions (question_id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
VALUES
(1, 1, 'What is Data Science?', 'A field focused on data cleaning and visualization.', 'A field focused on machine learning only.', 'A field focused on analyzing large datasets.', 'A field of computer science for general tasks.', 'C'),
(2, 1, 'Which library is commonly used for data manipulation in Python?', 'Pandas', 'Matplotlib', 'TensorFlow', 'Numpy', 'A'),
(3, 1, 'What does the term "big data" refer to?', 'Data that is too large to be processed by traditional methods.', 'Data that can be stored on a personal computer.', 'Data that can be visualized using graphs.', 'Data that is irrelevant for analysis.', 'A'),
(4, 1, 'What is the first step in data analysis?', 'Data cleaning and preprocessing.', 'Data visualization.', 'Model building.', 'Data splitting.', 'A'),
(5, 1, 'Which type of data visualization is used to display the relationship between two variables?', 'Bar Chart', 'Pie Chart', 'Line Chart', 'Scatter Plot', 'D');

-- Questions for Data Science Data Preprocessing Quiz
INSERT INTO Questions (question_id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
VALUES
(6, 2, 'Which technique is used to handle missing data?', 'Imputation', 'Deletion', 'Replacement', 'All of the above', 'D'),
(7, 2, 'What is feature scaling?', 'Adjusting the range of features.', 'Removing features from data.', 'Splitting data into training and testing sets.', 'Encoding categorical data.', 'A'),
(8, 2, 'What is the purpose of normalization?', 'To bring all features to the same scale.', 'To increase the feature values.', 'To remove missing values.', 'To split the dataset.', 'A'),
(9, 2, 'Which of the following is a method of encoding categorical variables?', 'Label Encoding', 'Feature Scaling', 'Cross-validation', 'Dimensionality Reduction', 'A'),
(10, 2, 'What is the purpose of data cleaning?', 'To remove inconsistencies and errors in data.', 'To make the data ready for modeling.', 'To preprocess the data for analysis.', 'All of the above', 'D');

-- Questions for Data Science Data Visualization Quiz
INSERT INTO Questions (question_id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
VALUES
(11, 3, 'Which of the following is used to plot a line graph in Python?', 'matplotlib', 'pandas', 'seaborn', 'plotly', 'A'),
(12, 3, 'Which plot is best suited to display the distribution of a dataset?', 'Scatter Plot', 'Bar Chart', 'Box Plot', 'Line Chart', 'C'),
(13, 3, 'What is a heatmap used for?', 'Displaying relationships between variables', 'Showing the distribution of categorical data', 'Displaying the correlation between variables', 'Visualizing time-series data', 'C'),
(14, 3, 'Which of the following is a good method to show categorical data?', 'Pie Chart', 'Line Chart', 'Bar Chart', 'Scatter Plot', 'C'),
(15, 3, 'Which library in Python is used for data visualization?', 'matplotlib', 'scikit-learn', 'tensorflow', 'numpy', 'A');



-- Questions for Python Basics Quiz
INSERT INTO Questions (question_id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
VALUES
(16, 4, 'Which of these is the correct way to create a function in Python?', 'function myFunction()', 'def myFunction()', 'function = myFunction()', 'def = myFunction()', 'B'),
(17, 4, 'Which of the following is a Python data type?', 'Array', 'List', 'Linked List', 'Queue', 'B'),
(18, 4, 'What does the "len()" function do in Python?', 'Returns the number of items in an iterable.', 'Creates a new string.', 'Calculates the sum of a list.', 'Returns the first item of a list.', 'A'),
(19, 4, 'Which of these is not a valid Python operator?', '==', '<=', '<<', '!==', 'D'),
(20, 4, 'How do you create a variable in Python?', 'var x = 10', 'x = 10', 'create x = 10', 'let x = 10', 'B');

-- Questions for Advanced Python Programming Quiz
INSERT INTO Questions (question_id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
VALUES
(21, 5, 'What is the purpose of a lambda function?', 'To create a named function', 'To define an anonymous function', 'To define a method inside a class', 'To implement recursion', 'B'),
(22, 5, 'What does the "map()" function do in Python?', 'Applies a function to each item in an iterable', 'Filters an iterable', 'Sorts an iterable', 'Splits a string', 'A'),
(23, 5, 'What is the result of the expression "3**2" in Python?', '9', '6', '8', '3', 'A'),
(24, 5, 'Which module is used for handling JSON data in Python?', 'json', 'os', 'sys', 'pickle', 'A'),
(25, 5, 'What is the purpose of the "self" keyword in Python?', 'To reference the current instance of the class', 'To define a class variable', 'To import modules', 'None of the above', 'A');

-- Questions for Python Data Structures Quiz
INSERT INTO Questions (question_id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
VALUES
(26, 6, 'What data structure is used to store key-value pairs in Python?', 'List', 'Dictionary', 'Set', 'Tuple', 'B'),
(27, 6, 'Which of the following is mutable in Python?', 'String', 'List', 'Tuple', 'None of the above', 'B'),
(28, 6, 'Which method is used to add an item to a list in Python?', 'append()', 'add()', 'insert()', 'extend()', 'A'),
(29, 6, 'Which data structure does Python use to represent a queue?', 'List', 'Deque', 'Set', 'Tuple', 'B'),
(30, 6, 'How do you access the first element of a list in Python?', 'list[0]', 'list[1]', 'list.last()', 'list.first()', 'A');


-- Questions for Introduction to Machine Learning Quiz
INSERT INTO Questions (question_id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option)
VALUES
(31, 7, 'What is the main goal of machine learning?', 'To make predictions from data', 'To optimize data storage', 'To create algorithms', 'To analyze data manually', 'A'),
(32, 7, 'Which of these is a supervised learning algorithm?', 'Linear Regression', 'K-Means Clustering', 'Principal Component Analysis', 'K-Nearest Neighbors', 'A'),
(33, 7, 'What type of machine learning is used to predict outcomes based on labeled data?', 'Supervised Learning', 'Unsupervised Learning', 'Reinforcement Learning', 'Semi-supervised Learning', 'A'),
(34, 7, 'Which of the following is not a type of machine learning?', 'Supervised Learning', 'Unsupervised Learning', 'Reinforcement Learning', 'Neural Learning', 'D'),
(35, 7, 'What is a common example of a classification problem?', 'Predicting house prices', 'Predicting stock prices', 'Email spam detection', 'Time series forecasting', 'C');


SELECT * FROM Users;

SELECT * FROM Quizzes;

SELECT * FROM Questions;

SELECT * FROM Lessons;

SELECT * FROM Courses;

SELECT * FROM Enrollments;

SELECT * FROM QuizResults;

SELECT * FROM User_Course_Enrollment;



