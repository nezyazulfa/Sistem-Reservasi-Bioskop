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

## 📂 Project Structure

```bash
com.bioskop
├── data
│   └── CinemaData.java      # Singleton Database & File Handling
├── model
│   ├── Film.java            # POJO for Movie
│   └── User.java            # POJO for User
├── strategy
│   ├── PricingStrategy.java # Strategy Interface
│   ├── HargaNormal.java
│   ├── HargaWeekend.java
│   └── DiskonMember.java
├── tiket
│   ├── Tiket.java           # Decorator Component
│   ├── TiketFilm.java
│   └── (Decorator Classes)
├── util
│   └── Repository.java      # Generic Interface
└── Main.java                # GUI Entry Point
