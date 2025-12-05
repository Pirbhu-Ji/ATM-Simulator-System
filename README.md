🏦 ATM Simulator System (Java + MySQL + Swing)

This project implements a complete ATM Simulator System as a Java Desktop Application. It is built using Java Swing for the graphical user interface, MySQL Database for secure data persistence, and JDBC for connectivity, demonstrating core Object-Oriented Programming (OOP) principles.
The application allows users to perform common banking operations securely and efficiently.

🚀 Key Features Implemented
Feature	Description

🔐 1. Login System	Secure authentication using a unique Card Number and 4-digit PIN, validated against the MySQL database.

📝 2. Signup / Account Creation	Creates new bank accounts with strict validation. It auto-generates the Card Number and PIN, initializing the user record in the database.

🏠 3. Dashboard	The main menu after login, providing access to all transaction services (Deposit, Withdraw, Fast Cash, Balance Enquiry, Mini Statement, PIN Change).

💰 4. Deposit	Allows users to add money to their account. Updates the balance in the accounts table and saves a comprehensive transaction record.

💸 5. Withdraw	Allows cash withdrawal, including a critical validation check to prevent overdrafts (low balance). Records the transaction for auditing.

⚡ 6. Fast Cash	Provides quick withdrawal options (100, 500, 1000, 2000, 5000, 10000) that follow the same secure withdrawal logic and balance checks.

📊 7. Balance Enquiry	Instantly fetches and displays the current account balance from the database.

📄 8. Mini Statement	Retrieves and displays the last 15 transactions, showing the Date/Time, Type, Amount, and the Balance after each operation.

🔑 9. PIN Change	Requires old PIN verification and securely updates the new 4-digit PIN in both the login and accounts tables for consistency.


🗄️ Database Structure (MySQL)

The system uses three normalized tables to maintain data integrity and a complete audit trail.
Table Name	Column	Type	Description

login	cardno	VARCHAR	Stores authentication credentials.
	pin	VARCHAR	Stores the 4-digit PIN.
	
accounts	cardno	VARCHAR	Unique card number, linked to login.
	name	VARCHAR	Account holder's name.
	balance	DOUBLE	Current account balance.
	pin	VARCHAR	Account PIN (for redundancy and utility).
	
transactions	id	INT (PK)	Primary Key, Auto-Incrementing.
	cardno	VARCHAR	Foreign Key link to the account.
	` type**	VARCHAR	Type of transaction (DEPOSIT, WITHDRAW, etc.).
	
	
🛠️ Technologies Used

●	Java (JDK 17+)
●	Java Swing / AWT (For the Graphical User Interface)
●	JDBC (Java Database Connectivity)
●	MySQL Database (For persistence)
●	OOP Principles (Inheritance, Encapsulation, Polymorphism)
●	IDE: VS Code / IntelliJ / NetBeans


📁 Folder Structure
The project is organized into a standard Java package structure to ensure modularity and separation of concerns.
BankManagementSystem/
│
├── src/
│   ├── bank/gui/              # Contains all Java Swing classes for the UI
│   │   ├── Login.java
│   │   ├── Signup.java
│   │   ├── Dashboard.java
│   │   ├── Deposit.java
│   │   ├── Withdraw.java
│   │   ├── FastCash.java
│   │   ├── Balance.java
│   │   ├── MiniStatement.java
│   │   └── PinChange.java
│   │
│   ├── bank/db/               # Contains database connectivity logic
│   │   └── DatabaseConnection.java
│   │
│   ├── bank/images/           # Contains all image assets and icons for the GUI
│   │   └── [Your Image Files.png/jpg]
│   │
│   └── bank/main/             # Contains the entry point for the application
│       └── Main.java
│
├── lib/                     # Folder for required JAR files (e.g., mysql-connector-java.jar)
│
└── README.md              # Project documentation


⚙️ How to Run the Project
1. Setup MySQL Database
Execute the following commands in your MySQL client to create the database:
CREATE DATABASE bank_system;
USE bank_system;

You must then create the required tables (login, accounts, transactions) based on the schema above.

2. Configure Database Connection
Open the file src/bank/db/DatabaseConnection.java and update the credentials to match your local MySQL configuration:
// src/bank/db/DatabaseConnection.java
String url = "jdbc:mysql://localhost:3306/bank_system";
String username = "root"; // Your MySQL username
String password = "your_mysql_password"; // Your MySQL password

3. Compile and Run the Project
Ensure your MySQL Connector/J JAR is included in the classpath (e.g., located in the lib/ folder).
# Example compile command:
# javac -cp lib/mysql-connector-java.jar -d classes src/bank/main/Main.java

# Example run command:
# java -cp classes:lib/mysql-connector-java.jar bank.main.Main

Alternatively, simply press Run in your chosen IDE (VS Code, IntelliJ, or NetBeans).

👨‍💻 Developers (Group Project)
This project was developed collaboratively as part of the Object-Oriented Programming (OOP) course.

Name	             Roll Number       	Role
Pirbhu Ji Rajput	133-24-0012	  Lead Developer, Core Logic, Database Integration

Abdul Saboor Memon	133-24-0003	  Developer, GUI Components, Testing, Feature Implementation


Primary Contact:
●	Pirbhu Ji
●	Email: (pirbhujirajput.cse@gmail.com)
