# Sistem-Reservasi-Bioskop
# 🎬 Cinema Booking System (OOP Final Project)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

**Cinema Booking System** is a desktop-based application designed to simulate a movie ticket reservation process. This project serves as a final assignment for **Object-Oriented Programming (OOP) Class 2C**, demonstrating advanced software architecture concepts including Design Patterns, File Persistence, and GUI development.

---

## ✨ Key Features

### 1. 🎫 Smart Booking System
- **Real-time Stock:** Automatically prevents booking if tickets or snacks are out of stock.
- **Add-ons:** Option to add Popcorn, Softdrinks, or upgrade to VIP Seats.
- **Dynamic Pricing:** Calculates total cost instantly based on selected items.

### 2. 💰 Dynamic Pricing Strategies (Strategy Pattern)
- **Normal Price:** Standard weekday rates.
- **Weekend Surge:** Automatic extra charge for weekends (+Rp 10,000).
- **Member Discount:** 10% discount for registered members.

### 3. 📦 Inventory Management (Singleton Pattern)
- **Centralized Data:** Keeps data synchronized across all application modules.
- **Persistent Storage:** Data (Film stocks & Food inventory) is saved to a local file (`cinema_database.dat`) via **Serialization**. Data remains intact even after the application is closed.

### 4. 📄 PDF Export
- Generates a professional booking receipt in PDF format using the **iText Library**.

---

## 🏗️ Architecture & Design Patterns

This project strictly follows **SOLID Principles** and implements three major Design Patterns:

| Pattern | Implementation | Purpose |
| :--- | :--- | :--- |
| **Singleton** | `CinemaData.java` | Ensures only **ONE** instance of the database exists to maintain data consistency (Single Source of Truth). |
| **Decorator** | `ExtraPopcorn`, `ExtraVip` | Allows dynamic addition of features (snacks/upgrades) to a ticket without altering the original code. |
| **Strategy** | `HargaWeekend`, `DiskonMember` | Enables interchangeable pricing algorithms at runtime (Open/Closed Principle). |

---

## 🛠️ Tech Stack 

* **Language:** Java (JDK 17+)
* **GUI Framework:** Java Swing (Modern Dark Theme)
* **Build Tool:** Apache Maven
* **Libraries:**
    * `iTextPDF` (for PDF generation)
* **Persistence:** Java IO (Object Serialization)

---

## 🚀 How to Run

### Prerequisites
Before running the application, ensure you have the following installed:
* **Java Development Kit (JDK) 17** or newer.
* **Apache Maven** (for managing dependencies like iText).
* **IDE** (IntelliJ IDEA, VS Code, or NetBeans) is recommended.

### Installation Steps

1.  **Clone the Repository**
    Open your terminal or command prompt and run:
    ```bash
    git clone [https://github.com/username-kalian/cinema-booking-system.git](https://github.com/username-kalian/cinema-booking-system.git)
    cd cinema-booking-system
    ```

2.  **Load Project**
    * Open your IDE.
    * Select **Open Project** and choose the `pom.xml` file or the project folder.
    * Wait for Maven to download all dependencies (especially `iTextPDF`).

3.  **Run the Application**
    * Navigate to: `src/main/java/com/bioskop/Main.java`
    * Right-click on the file and select **Run 'Main'**.

    *Alternatively, via Terminal:*
    ```bash
    mvn clean compile exec:java -Dexec.mainClass="com.bioskop.Main"
    ```

---

## 👥 Authors (Team Kelas 2C)

This project was crafted with ❤️ by our team as a Final Project for the Object-Oriented Programming course.

| NIM | Name | Role | Key Responsibilities |
| :--- | :--- | :--- | :--- |
| **[241511068]** | **[Andhini Widya Putri Wastika]** | Frontend Engineer | • GUI Layout & Design<br>• Ticket Logic (Decorator Pattern)<br>• User Experience |
| **[241511085]** | **[Nezya Zulfa Fauziah]** | Backend Architect | • Database Design (Singleton)<br>• File Persistence (Serialization)<br>• Repository Pattern |
| **[241511090]** | **[Siti Soviyyah]** | Logic & QA Lead | • Pricing Logic (Strategy Pattern)<br>• Unit Testing (JUnit)<br>• PDF Report Integration |
