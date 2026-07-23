# 🛡️ Transaction Risk Intelligence System

A backend application that analyzes transaction risk using a machine learning model and generates a human-readable explanation using Groq LLM.

---

## Project Overview

This project is built using Spring Boot, Python Flask, MySQL, and Groq LLM.

A user creates a transaction through the Spring Boot backend. The backend extracts transaction features and sends them to a Flask microservice, 
where a trained Random Forest model predicts the transaction's fraud risk. The predicted risk score and risk level are then used to generate a natural language explanation using Groq. Finally, 
the transaction and its risk assessment are stored in the database.

---

## Features

### Authentication
- User Registration
- User Login
- JWT Authentication
- Password Encryption using BCrypt
- Spring Security

### Transaction Management
- Create Transactions
- Retrieve Transaction History
- Update Transaction Status based on Risk Assessment

### Risk Assessment
- Predict transaction risk using a Random Forest model
- Generate a Risk Score
- Classify transactions as:
  - LOW
  - MEDIUM
  - HIGH

### AI Explanation
- Generate a human-readable explanation for the predicted risk using Groq LLM.

### Database
- Store Users
- Store Transactions
- Store Risk Assessments

---

## Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Backend Development |
| Spring Boot | REST APIs |
| Spring Security | Authentication & Authorization |
| JWT | Secure Authentication |
| MySQL | Database |
| Python | Machine Learning Service |
| Flask | ML REST API |
| Scikit-learn | Random Forest Model |
| Groq API | AI-generated Explanation |
| Maven | Dependency Management |
| Postman | API Testing |

---

## Project Workflow

```
User
   │
   ▼
Login (JWT Authentication)
   │
   ▼
Create Transaction
   │
   ▼
Spring Boot Backend
   │
   ▼
Extract Transaction Features
   │
   ▼
Flask ML Service
   │
   ▼
Random Forest Model
   │
   ▼
Risk Score & Risk Level
   │
   ▼
Groq LLM
   │
   ▼
Generate Explanation
   │
   ▼
Store Risk Assessment in MySQL
   │
   ▼
Return Response
```

---

## Project Structure

```
src
├── config
├── controller
├── dto
├── entity
├── repository
├── security
└── service

Flask Service
├── app.py
├── train_model.py
├── model.pkl
└── transaction_dataset.csv
```

---

## API Endpoints

### Authentication

| Method | Endpoint |
|--------|----------|
| POST | /api/auth/register |
| POST | /api/auth/login |

### Transactions

| Method | Endpoint |
|--------|----------|
| POST | /api/transactions |
| GET | /api/transactions |
| GET | /api/transactions/{id} |

### Risk Assessment

| Method | Endpoint |
|--------|----------|
| POST | /api/risk/analyze/{transactionId} |
| GET | /api/risk/{transactionId} |

---

## Machine Learning Features

The Random Forest model uses the following transaction features:

- Transaction Amount
- Device Type
- New Device
- Location Changed
- Transaction Hour
- Transaction Frequency
- Previous Risk Score
- Trusted Receiver
- Failed Login Attempts

These features are sent from the Spring Boot backend to the Flask service for prediction.

---

## Risk Levels

| Risk Score | Risk Level |
|------------|------------|
| 0 – 39 | LOW |
| 40 – 74 | MEDIUM |
| 75 – 100 | HIGH |

---

## Future Improvements

Possible future enhancements include:

- Dynamic device history tracking
- Login attempt history
- Improved trusted receiver calculation
- Docker deployment
- Cloud deployment

---

## Author

**Ketaki Ramdas Patil**

Second Year B.E. Electronics and Telecommunication Engineering

Pune Institute of Computer Technology (PICT)



<img width="1600" height="894" alt="b26b18f7-268b-4997-8b5a-bda100fd1ffc" src="https://github.com/user-attachments/assets/1aed00a2-443a-47f0-a5e2-655f18ce3c2d" />

<img width="1600" height="899" alt="79bd3123-a4e7-4459-8bcb-7cf540447e0f" src="https://github.com/user-attachments/assets/f4c35b6e-6490-45a6-bb98-e010d025fa12" />

<img width="1600" height="895" alt="510010c3-60ed-40f5-a93f-807688cde0e2" src="https://github.com/user-attachments/assets/9f24642a-c37e-4efa-9fd6-c9327d2aedb0" />

<img width="1600" height="899" alt="948684a2-52e1-4fa2-8604-4b96033f9901" src="https://github.com/user-attachments/assets/539107cf-aa1a-42cf-8164-c7e6d5b36bdd" />

<img width="1521" height="741" alt="8c83ca4b-1ae9-4f17-90ec-93311743c814" src="https://github.com/user-attachments/assets/5999d463-d54d-4bbc-880d-b8efbaba3f41" />




