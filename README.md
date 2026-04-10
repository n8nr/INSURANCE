# 🚗 Car Insurance Quote Application

A Java-based desktop application that allows users to generate, save, and manage car insurance quotes through an intuitive graphical user interface.

---

## 📌 Overview

This project was developed as part of a computing assignment to demonstrate:

- Object-Oriented Programming (OOP)
- GUI development using Java Swing
- Database integration using JDBC
- Clean architecture and user interaction design

The application simulates a real-world car insurance system where users can enter their details, select a vehicle, and receive a calculated insurance quote.

---

## ✨ Features

- 🔐 **User Registration & Login**
  - Register new users
  - Login with existing credentials
  - Email validation and duplicate prevention

- 🚘 **Vehicle Selection**
  - Choose make, model, and year
  - Engine size selection
  - Dynamic dropdown updates

- 📊 **Quote Calculation**
  - Premium based on:
    - Vehicle make
    - Driver age
    - No Claims Bonus (NCB)
    - Policy type
    - Engine size

- 🧾 **Quote Management**
  - Save generated quotes
  - View previously created quotes

- 🎨 **Modern UI**
  - Clean and consistent design
  - Multi-screen navigation using `CardLayout`
  - Improved usability with tooltips and structured forms

---

## 🛠️ Technologies Used

- **Java** (Core + OOP principles)
- **Java Swing** (GUI)
- **JDBC** (Database connectivity)
- **SQLite / SQL Database**
- **IntelliJ IDEA**

---

## 🧠 Key Concepts Demonstrated

- Encapsulation, Abstraction, and Modular Design
- MVC-inspired structure
- Event-driven programming
- Form handling and validation
- Database CRUD operations

---

## 📂 Project Structure
src/
├── Main.java
├── Customer.java
├── CustomerDAO.java
├── Quote.java
├── QuoteDAO.java
├── QuoteCalculator.java
├── Repository.java
└── CarData.java

---

## ▶️ How to Run

1. Open the project in IntelliJ IDEA  
2. Ensure database is set up correctly  
3. Run `Main.java`  
4. Use the UI to:
   - Register/Login
   - Select vehicle details
   - Generate a quote  

---

## 🧪 Example Test Data

- **Full Name:** John Smith  
- **Address:** 12 Baker Street, London  
- **Phone:** 07123456789  
- **Email:** john.smith@gmail.com  
- **Password:** Test1234  

---

## 📈 Future Improvements

- Input validation (email, phone format)
- Password hashing for security
- Improved UI responsiveness
- Expanded vehicle database
- Cloud/database hosting

---

## 👩‍💻 Author

Natan Rybacki
Fujjer Shahzad

---

## ✅ Conclusion

This project successfully demonstrates the implementation of a real-world application using Java, combining UI design, backend logic, and database integration into a cohesive system.
