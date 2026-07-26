# 🛡️ Transaction Risk Intelligence System

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![Python](https://img.shields.io/badge/Python-3.x-blue)
![Flask](https://img.shields.io/badge/Flask-ML_Service-black)
![MySQL](https://img.shields.io/badge/MySQL-Railway-blue)
![Render](https://img.shields.io/badge/Deployment-Render-purple)

An AI-powered backend application that detects potentially fraudulent transactions using Machine Learning and generates human-readable explanations using Groq LLM.

---

# 📌 Project Overview

The **Transaction Risk Intelligence System** is a microservice-based backend application that analyzes financial transactions for potential fraud using a Machine Learning model.

The application follows a complete backend workflow where **Spring Boot** handles authentication, transaction management, and business logic, while a deployed **Flask microservice** performs fraud prediction using a trained **Random Forest** model. The predicted risk score is then passed to **Groq LLM**, which generates a concise and explainable reason for the assigned risk level.

Finally, the transaction and its corresponding risk assessment are securely stored in the database.

---

# ✨ Features

## 🔐 Authentication

- User Registration
- User Login
- JWT Authentication
- Password Encryption using BCrypt
- Spring Security

---

## 💳 Transaction Management

- Create Transactions
- Retrieve Transaction History
- Automatic Transaction Status Update
- User-wise Transaction Storage

---

## 🤖 Machine Learning Risk Assessment

- Random Forest Fraud Detection
- Risk Score Generation
- Risk Level Classification

Risk Levels:

- LOW
- MEDIUM
- HIGH

---

## 🧠 Explainable AI

- Generates human-readable fraud explanations
- Powered by Groq LLM
- Explains why a transaction received its predicted risk level

---

## 🗄 Database

Stores:

- Users
- Transactions
- Risk Assessments

---

# 🛠 Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Backend Development |
| Spring Boot | REST APIs |
| Spring Security | Authentication & Authorization |
| JWT | Secure Authentication |
| Maven | Dependency Management |
| Python | Machine Learning |
| Flask | ML REST API |
| Scikit-learn | Random Forest Model |
| Groq API | AI-generated Explanation |
| MySQL | Database |
| Railway | Cloud Database |
| Render | Backend & ML Deployment |
| Postman | API Testing |

---

# ☁️ Deployment

The application is fully deployed using cloud services.

| Component | Platform |
|-----------|----------|
| Spring Boot Backend | Render |
| Flask ML Service | Render |
| MySQL Database | Railway |

The backend communicates with the deployed Flask ML service using REST APIs, while all application data is stored securely in Railway MySQL.

---

# 🏗 System Architecture

```text
                    +----------------------+
                    |       Client         |
                    +----------+-----------+
                               |
                               v
                 Spring Boot Backend (Render)
                               |
        +----------------------+----------------------+
        |                                             |
        |                                             |
        v                                             v
 Railway MySQL                              Flask ML Service (Render)
                                                   |
                                                   v
                                      Random Forest Model
                                                   |
                                                   v
                                             Risk Score
                                                   |
                                                   v
                                              Groq LLM
                                                   |
                                                   v
                                     Explainable Risk Response
```

---

# 🔄 Project Workflow

```text
User
   │
   ▼
JWT Authentication
   │
   ▼
Create Transaction
   │
   ▼
Store Transaction in Railway MySQL
   │
   ▼
Extract Transaction Features
   │
   ▼
Flask ML Service (Render)
   │
   ▼
Random Forest Prediction
   │
   ▼
Risk Score & Risk Level
   │
   ▼
Groq LLM
   │
   ▼
Generate Risk Explanation
   │
   ▼
Store Risk Assessment
   │
   ▼
Return Final Response
```

---

# 📂 Project Structure

```text
Transaction-Risk-Intelligence-System
│
├── Spring Boot Backend
│   ├── config
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   ├── security
│   └── service
│
├── Flask ML Service
│   ├── app.py
│   ├── train_model.py
│   ├── model.pkl
│   └── transaction_dataset.csv
│
└── README.md
```

---

# 📡 API Endpoints

## Authentication

| Method | Endpoint |
|--------|----------|
| POST | `/api/auth/register` |
| POST | `/api/auth/login` |

---

## Transactions

| Method | Endpoint |
|--------|----------|
| POST | `/api/transactions` |
| GET | `/api/transactions` |
| GET | `/api/transactions/{id}` |

---

## Risk Assessment

| Method | Endpoint |
|--------|----------|
| POST | `/api/risk/analyze/{transactionId}` |
| GET | `/api/risk/{transactionId}` |

---

# 🤖 Machine Learning

The fraud detection model is built using a **Random Forest Classifier** trained on transaction data.

### Features Used

- Transaction Amount
- Device Type
- New Device
- Location Changed
- Transaction Hour
- Transaction Frequency
- Previous Risk Score
- Trusted Receiver
- Failed Login Attempts

These features are extracted by the Spring Boot backend and sent to the Flask microservice for fraud prediction.

---

# 📊 Risk Levels

| Risk Score | Risk Level |
|------------|------------|
| 0 – 39 | LOW |
| 40 – 74 | MEDIUM |
| 75 – 100 | HIGH |

---

# ⭐ Key Highlights

- JWT-based Authentication
- Secure REST APIs
- Random Forest Fraud Detection
- Explainable AI using Groq LLM
- Microservice Architecture
- Railway Cloud Database
- Cloud Deployment using Render
- REST Communication between Backend & ML Service
- Production-style Backend Design

---

# 🚀 Future Enhancements

- Dynamic Device History Tracking
- Geo-location Anomaly Detection
- Failed Login Attempt Monitoring
- Email & SMS Fraud Alerts
- Redis Caching
- Kafka Event Streaming
- Docker Compose
- Kubernetes Deployment
- Real-time Fraud Monitoring Dashboard

---

# 👩‍💻 Author

**Ketaki Ramdas Patil**

B.E. Electronics and Telecommunication Engineering

Pune Institute of Computer Technology (PICT)

GitHub:
https://github.com/ketakipatil57

---

# 📄 License

This project is developed for educational and learning purposes.



<img width="1600" height="894" alt="b26b18f7-268b-4997-8b5a-bda100fd1ffc" src="https://github.com/user-attachments/assets/1aed00a2-443a-47f0-a5e2-655f18ce3c2d" />

<img width="1600" height="899" alt="79bd3123-a4e7-4459-8bcb-7cf540447e0f" src="https://github.com/user-attachments/assets/f4c35b6e-6490-45a6-bb98-e010d025fa12" />

<img width="1600" height="895" alt="510010c3-60ed-40f5-a93f-807688cde0e2" src="https://github.com/user-attachments/assets/9f24642a-c37e-4efa-9fd6-c9327d2aedb0" />

<img width="1600" height="899" alt="948684a2-52e1-4fa2-8604-4b96033f9901" src="https://github.com/user-attachments/assets/539107cf-aa1a-42cf-8164-c7e6d5b36bdd" />

<img width="1521" height="741" alt="8c83ca4b-1ae9-4f17-90ec-93311743c814" src="https://github.com/user-attachments/assets/5999d463-d54d-4bbc-880d-b8efbaba3f41" />

<img width="1915" height="910" alt="image" src="https://github.com/user-attachments/assets/ac07a8f6-d003-4d89-a2f3-aba47ba7bde6" />

<img width="1912" height="893" alt="image" src="https://github.com/user-attachments/assets/f3ece13d-b5af-49e2-93c2-fd8263626e77" />

<img width="1915" height="913" alt="image" src="https://github.com/user-attachments/assets/3a8562cc-52ef-4a36-a464-b07d5c8654f2" />





