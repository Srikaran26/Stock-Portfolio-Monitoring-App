
# Stock Portfolio Monitoring App

A Spring Boot backend application designed to help users manage their stock portfolios, track stock prices, and receive alerts. The app integrates external APIs for live stock price updates and provides a comprehensive set of REST APIs for portfolio management.
------------------------------------------------------------------------------------------------

🌐 **Domain**

Finance / Stock Market / Investment

------------------------------------------------------------------------------------------------

🎯 **Objectives**

- Manage multiple user portfolios with holdings.
- Fetch and cache live stock prices from external APIs.
- Calculate total portfolio value dynamically.
- Generate price alerts and notifications.
- Provide detailed reporting and analytics.

------------------------------------------------------------------------------------------------

🧱 **Tech Stack**

| Layer       | Technology                      |
|-------------|--------------------------------|
| Framework   | Spring Boot                    |
| Security    | Spring Security + JWT          |
| Persistence | Spring Data JPA                |
| Database    | MySQL                         |
| Build Tool  | Maven                         |
| Utilities   | Lombok, ModelMapper (optional)|
| Testing     | JUnit                         |
| Documentation | Swagger (springdoc-openapi)  |

------------------------------------------------------------------------------------------------

🧩 **Key Modules**

- User & Role Management
- Portfolio Management
- Holdings & Transactions
- Stock Price Fetching & Caching
- Alerts & Notifications
- Reporting & Analytics

------------------------------------------------------------------------------------------------

🔐 **Roles & Access**

| Role       | Access Description                             |
|------------|------------------------------------------------|
| Admin      | Full access, user and role management          |
| User       | Manage own portfolios and holdings             |
| Transporter| (If applicable) Manages alerts & notifications |

------------------------------------------------------------------------------------------------

🗃 **Entity Overview**

- User: id, username, email, password, roles
- Portfolio: id, userId, name, description, createdAt
- Holding: id, portfolioId, stockSymbol, quantity, buyPrice
- StockPriceCache: stockSymbol, price, lastUpdated
- Alert: id, userId, stockSymbol, threshold, active

------------------------------------------------------------------------------------------------

🔁 **REST API Endpoints**

- **AuthController**
  - POST `/api/auth/register`
  - POST `/api/auth/login`

- **UserController**
  - GET `/api/users`
  - PUT `/api/users/{id}/role`

- **PortfolioController**
  - POST `/api/portfolios`
  - GET `/api/portfolios`
  - GET `/api/portfolios/{id}`
  - PUT `/api/portfolios/{id}`
  - DELETE `/api/portfolios/{id}`

- **HoldingController**
  - POST `/api/holdings`
  - GET `/api/holdings/{portfolioId}`
  - PUT `/api/holdings/{id}`
  - DELETE `/api/holdings/{id}`

- **StockPriceController**
  - GET `/api/stockprices/{symbol}`
  - POST `/api/stockprices/refresh/{symbol}`

- **AlertController**
  - GET `/api/alerts`
  - POST `/api/alerts`
  - PUT `/api/alerts/{id}/resolve`

- **ReportController**
  - GET `/api/reports/portfolio-value`
  - GET `/api/reports/alerts`

------------------------------------------------------------------------------------------------

🧪 **Example Workflow**

1. User registers and logs in.
2. User creates portfolios and adds holdings.
3. System fetches live stock prices, caching results.
4. Alerts trigger based on price thresholds.
5. Reports provide portfolio performance insights.

------------------------------------------------------------------------------------------------

📁 **Project Structure**

<img width="281" alt="image" src="https://github.com/user-attachments/assets/d3620fc5-01ce-40d2-aa67-c746ffd4cb8d" />


------------------------------------------------------------------------------------------------

🛠 **Optional Enhancements**
<img width="397" alt="image" src="https://github.com/user-attachments/assets/89661a97-a9e2-4799-a997-aae3dcf26200" />

------------------------------------------------------------------------------------------------

🗂 **Suggested Sprints**
![image](https://github.com/user-attachments/assets/006ea68c-3046-4c5f-9c22-e8310f62bd98)

------------------------------------------------------------------------------------------------
▶️ **How to Run the Project**

- Clone the repository:  
  `git clone https://github.com/Srikaran26/Stock-Portfolio-Monitoring-App.git`

- Configure MySQL and update `application.properties` with your DB credentials.

- Build and run using Maven:  
  `./mvnw clean install`  
  `./mvnw spring-boot:run`

- Access Swagger UI at:  
  `http://localhost:8080/swagger-ui/index.html`

- Register users and start using API endpoints.

------------------------------------------------------------------------------------------------

👥 **Authors & Contributors**

See GitHub contributors list for full details.
- [Modadugu Srikaran](https://github.com/Srikaran26) – Portfolio and Holdings Module  
- [Nichenametla Yeshovardhan](https://github.com/ny3377) – Transactions and Alerts  
- [Kartik Gupta](https://github.com/Srikaran26) – Stock Price Fetching and Integration  
- [Yash Kumar](https://github.com/venomyash-02) – User Management and Security  
- [BIKKI Abhiram](https://github.com/BIKKIABHIRAM) – Testing and Documentation  
- [Abhinav Thirumoorthy Karpur](https://github.com/at5707) – Gain/Loss Calculator and Reporting  
- [Pavithra M](https://github.com/Pavithramutharasu) – Alerts and Notification Module  

Thanks to all team members and contributors for their collaboration, feedback, and testing.

------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------
