# 🛒 Cart Abandonment Recovery MVP

## 🎯 Project Overview

This project addresses cart abandonment in e-commerce by developing and deploying a full-stack application. It includes a React frontend, Spring Boot backend, and MySQL database. The system is containerized using Docker Compose and automated via GitHub Actions.
---

## 🧱 Technologies Used

| Layer       | Technology         | Purpose                              |
|-------------|--------------------|--------------------------------------|
| Frontend    | React              | UI for product listing and cart      |
| Backend     | Spring Boot        | REST API for cart operations         |
| Database    | MySQL              | Persistent cart data                 |
| Deployment  | Docker Compose     | Container orchestration              |
| CI/CD       | GitHub Actions     | Automated build and test pipeline    |

---

## 🚀 Features

- Product listing and cart functionality
- Cart recovery modal triggered after inactivity
- RESTful API with persistent storage
- Dockerized services for reproducible deployment
- CI/CD pipeline for automated builds

---
## 📦 Folder Structure
🖥️ Frontend: React + Axios + Tailwind

📁 Structure
```bash
frontend/
├── src/
│   ├── components/
│   │   ├── ProductList.js
│   │   ├── Cart.js
│   │   └── RecoveryModal.js
│   ├── App.js
│   └── index.js
├── Dockerfile
├── package.json

```
🧱 Backend: Spring Boot + Hibernate + MySQL

📁 Structure
```bash
backend/
├── src/main/java/com/example/cart/
│   ├── CartRecoveryApplication.java
│   ├── controller/CartController.java
│   ├── model/Product.java
│   ├── model/CartItem.java
│   ├── repository/ProductRepository.java
│   ├── repository/CartItemRepository.java
│   └── service/CartService.java
├── resources/
│   ├── application.properties
├── Dockerfile
├── pom.xml
```

🔹 Full-Stack Repository Structure

📁 Structure
```bash
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── recoveryModal.js
│   │   └── App.js
├── backend/
│   ├── src/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── model/
│   │   ├── repository/
│   │   └── exception/
├── docker-compose.yml
├── .github/
│   └── workflows/
│       └── ci-cd.yml      
├── README.md
```
## 🚀 Live deployment: Railway
### Frontend: (https://cart-recovery-system-production.up.railway.app/)

## Validation checklists:
 “Open the Railway deployment link → product listing loads: add product to cart → cart is created: increase item quantity → decrease item quantity → remove an item from cart → play around: cart persist after refresh → wait 30s inactivity → recovery modal appears. → continue shopping or clear all items”

### Backend Healthcheck: (https://backend-production-b6f6.up.railway.app/actuator/health)

### Product API: (https://backend-production-b6f6.up.railway.app/api/products)
