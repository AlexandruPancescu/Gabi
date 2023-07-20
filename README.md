GABI

The Personal Finance Android App is a mobile application developed in Java using Android Studio. This app is designed to help individuals manage their personal finances effectively on their Android devices. 
It allows users to track their expenses, income, and financial goals in a convenient and user-friendly manner. 
The app utilizes a local server to store its database, which can be accessed via an API.

Installation
Before running the Personal Finance Android App, ensure you have the following components installed:

Android Studio: The official IDE for Android app development.

XAMPP: This will provide the local server environment to host the app's database. The server file(which also contains the API) is also included

MySQL: The app uses MySQL as the database management system.

Once you have all the prerequisites installed, follow these steps:

Clone this repository to your local machine using the following command:



Configure the database connection settings in the app:

Open the Android Studio project located in the android/ directory.
Locate the Config.java file (or any other appropriate configuration file) and update the database connection parameters with your MySQL credentials.
Build and run the app on your Android device or emulator using Android Studio.

Features
Expense Tracking: Keep track of your daily expenses and categorize them for better financial management.

Income Management: Record your various sources of income to monitor your cash flow.

Financial Goals: Set and track your financial goals, such as savings targets.

Reports and Insights: Gain valuable insights into your spending habits with graphical representations of your finances.

Secure and Private: Your data is stored locally, ensuring maximum privacy and security.

Technologies Used
Android App Development: Java, XML
Backend: PHP (for handling API requests and database operations)
Database: MySQL 
