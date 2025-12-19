# 🧪 Laboratory Inventory API

This project is a **RESTful API**-based laboratory inventory management system built with **Spring Boot**. Developed as part of the **Distributed System** module assignment for class **3FSD2** at **CEP-CCIT FTUI** **2025/2026 Academic Year**.

---

## 🚀 Key Features

* **Master Data Management**: Manage User data (Admin & Staff), Students, and Equipment Categories.
* **Equipment Inventory**: Real-time monitoring of laboratory equipment stock.
* **Condition Logging**: Recording history of equipment condition changes (Good, Damaged, etc.).
* **Inventory Transactions**: Management of equipment borrowing (OUT) and returning (IN).
* **Business Validation**: Automatic validation to prevent damaged equipment from being borrowed.
* **API Documentation**: Fully integrated with **Swagger UI**.

---

## 🛠️ Tech Stack

* **Java 25** (Oracle JDK).
* **Spring Boot 4.0.0**.
* **Spring Data JPA** & **Hibernate 7**.
* **MySQL Database**.
* **Springdoc OpenAPI (Swagger)**.
* **GitHub Actions** (CI/CD Pipeline).

---

## ⚙️ How to Run the Project

### Prerequisites
* Java 25 installed.
* MySQL installed and running.
* Maven (or use the provided `./mvnw`).

### Installation Steps
1.  **Clone the Repository**:
    ```bash
    git clone [https://github.com/neyzamaylanies/inventory_laboratorium.git](https://github.com/neyzamaylanies/inventory_laboratorium.git)
    cd inventory_laboratorium
    ```
2.  **Database Configuration**:
    * Create a database named `inventory` in MySQL.
    * Import the `db/inventory.sql` file into the database.
    * Adjust the database *username* and *password* in `src/main/resources/application-local.yaml` if necessary.
3.  **Run the Application**:
    ```bash
    ./mvnw spring-boot:run
    ```

---

## 📖 API Documentation

Once the application is running, you can access the interactive documentation at:
👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

The API is divided into several main groups:
* `User`: Manage Admin & Staff access.
* `Student`: Manage borrower data (Students).
* `Equipment`: Manage equipment details and stock.
* `Transaction`: Process IN/OUT movements.

---

## 🔄 CI/CD Pipeline

This project is equipped with **GitHub Actions** that automatically perform *build* and *test* operations every time there is a *push* or *pull request* to the `main` branch using **Oracle JDK 25**.

---

## 👥 Development Team

* **Sarah Nurhaliza** (sarahnrhlza3rut@gmail.com)
* **Neyza Maylanie Santosa** (neyzamayylanies@gmail.com)
* **Class**: 3FSD2 - CEP-CCIT FTUI, 2025/2026.
