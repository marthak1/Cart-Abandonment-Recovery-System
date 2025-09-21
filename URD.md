📄 User Requirements Document
Project Title: Cart Abandonment Recovery System  
Prepared By: Martha [Project Owner & Lead Developer]  
Date: 21 September 2025

---

1. 🎯 Objectives

- Deliver a modular, full-stack e-commerce MVP that addresses cart abandonment
- Enable users to browse products, manage cart items, and receive recovery prompts
- Ensure persistent cart data and automated deployment via CI/CD
- Demonstrate alignment with OTHM Level 5 Learning Outcome 5.5

---

2. 📌 Scope

✅ In Scope
- React frontend with product listing, cart management, and recovery modal
- Spring Boot backend with Hibernate ORM and RESTful APIs
- MySQL database for persistent cart and product data
- Docker Compose orchestration and GitHub Actions CI/CD
- Unit and integration tests across frontend and backend
- Exception handling and user feedback mechanisms

❌ Out of Scope
- Payment gateway integration
- User authentication or account management
- Multi-language or multi-currency support
- Mobile app or PWA optimization

---

3. 📦 Functional Requirements (User Stories + Acceptance Criteria)

| ID     | User Story                                                                                  | Acceptance Criteria                                                                 |
|--------|----------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------|
| FR-01  | As a shopper, I want to view a list of products so I can decide what to buy.                | Products load from backend via /api/products; UI displays name and price clearly. |
| FR-02  | As a shopper, I want to add items to my cart so I can purchase them later.                  | Clicking “Add to Cart” sends POST to /api/cart/add/{productId}; cart updates in UI. |
| FR-03  | As a shopper, I want to remove items from my cart so I can update my selection.             | Clicking “Remove” sends DELETE to /api/cart/remove/{id}; cart updates immediately. |
| FR-04  | As a shopper, I want to be reminded if I leave items in my cart so I don’t forget to checkout. | Recovery modal appears after 5 minutes of inactivity; includes “Resume Checkout” button. |
| FR-05  | As a developer, I want cart data to persist across sessions so users don’t lose progress.   | Cart items stored in MySQL; data remains after reload or restart.                    |
| FR-06  | As a developer, I want RESTful endpoints for cart and product operations.                   | Endpoints return correct status codes and JSON payloads.                             |
| FR-07  | As a shopper, I want clear error messages if something goes wrong.                          | Axios .catch() blocks display fallback alerts; ErrorBoundary handles render errors. |
| FR-08  | As a developer, I want backend errors to be handled gracefully.                             | Global exception handler returns structured JSON with appropriate HTTP status.       |
| FR-09  | As a QA engineer, I want automated tests to validate functionality.                         | Unit and integration tests pass for services, endpoints, and UI components.          |
| FR-10  | As a DevOps engineer, I want the system to build and deploy automatically.                  | GitHub Actions pipeline builds backend and runs tests on push.                       |
| FR-11  | As a developer, I want all services to run locally with one command.                        | Docker Compose spins up frontend, backend, and MySQL successfully.                   |

---

4. 📊 Non-Functional Requirements

| ID     | Requirement Description                                      | Priority |
|--------|--------------------------------------------------------------|----------|
| NFR-01 | System must be modular and maintainable                      | High     |
| NFR-02 | System must be reproducible using containerized services     | High     |
| NFR-03 | System must be testable with automated scripts               | High     |
| NFR-04 | System must be transparent and examiner-friendly             | High     |
| NFR-05 | System must be responsive and accessible                     | Medium   |
| NFR-06 | System must log errors and exceptions for debugging          | Medium   |

---

5. 🔁 User Interaction Flow

1. User visits product page  
2. User adds items to cart  
3. Cart updates and persists in backend  
4. After 5 minutes of inactivity, recovery modal appears  
5. User resumes checkout or modifies cart  
6. Order confirmation completes the flow

---

6. 📁 Data Requirements

| Entity     | Attributes                          |
|------------|-------------------------------------|
| Product    | ID, Name, Price                     |
| CartItem   | ID, ProductID (FK), Quantity        |
| Recovery   | Timer logic (frontend only)         |

---

7. 🧪 Testing Requirements

- Backend unit tests for service and repository layers  
- Backend integration tests for REST endpoints  
- Frontend unit tests for component rendering  
- Frontend integration tests for cart flow and recovery logic  
- CI/CD pipeline validation via GitHub Actions

---

8. 📌 Constraints

- Must use React, Spring Boot, Hibernate, and MySQL  
- Must be containerized using Docker Compose  
- Must be deployable via GitHub Actions  
- Must align with OTHM Level 5 assignment criteria  
- Must be completed within a 4-week sprint cycle

---

9. 🧠 Assumptions

- Users have stable internet access  
- No authentication is required for MVP  
- Product data is pre-seeded or manually entered  
- All services run locally or in a controlled environment

---

10. ⚠️ Risks & Mitigation

| Risk                        | Mitigation Strategy                          |
|-----------------------------|----------------------------------------------|
| API failure or downtime     | Retry logic and error handling in frontend   |
| Data loss                   | Use persistent MySQL volume in Docker        |
| Inactivity detection glitch | Use Jest timers to simulate and test logic   |
| CI/CD build failure         | Validate pipeline with minimal test commits  |
| Scope creep                 | Defined scope and phased delivery plan       |

---
