# Online Banking Web Application

## I. Abstract

This project introduces the development of an Online Banking Web Application designed to address the increasing demand for convenient digital banking solutions. With a focus on simplicity and user-friendliness, our platform consolidates essential banking services within a unified interface. 

The application is developed using the Java Spring Framework for backend services and Vue.js for the frontend, ensuring scalability, reliability, and performance optimization. Through personalized customization options and robust backend development, we empower users to efficiently manage their finances in an optimized digital environment.

## II. Introduction

Our Online Banking Web Application provides individuals with a unified, user-friendly banking experience. Responding to the growing demand for digital banking, we have built a platform that focuses on simplicity, efficiency, and personalization. Below are the key features of the platform:

### 1. Smooth Integration and User-Friendliness
The platform’s intuitive design and streamlined workflows make it accessible to users from various technological backgrounds. Whether users are experienced digital bankers or newcomers, the interface ensures easy navigation and financial management.

### 2. Essential Banking Services
Our platform offers a wide range of essential banking services. Users can:
- Conduct basic transactions (e.g., deposits, withdrawals)
- Monitor real-time exchange rates
- Access both internal and external transfer functionalities
- View detailed transaction histories
- Set daily transaction limits

These features provide a comprehensive and centralized solution for managing all financial requirements in one place.

### 3. Personalized Settings
What differentiates our platform is the ability for users to personalize their experience. Key customization options include:
- Setting balance thresholds
- Receiving email notifications when the account balance falls below a specified limit

These features give users greater control over their accounts and ensure timely awareness of potential issues.

## III. Backend Development

### 1. Backend Development with Spring Boot
Spring Boot is utilized to build the backend RESTful API, facilitating CRUD (Create, Read, Update, Delete) operations for various resources and executing business logic operations.

### 2. Database Setup
We use an **H2 database**, which sits in memory and is ideal for fast development and testing phases. This setup enables us to swiftly build and test database features without needing a full-fledged persistent storage solution.

### 3. Data Handling
The application relies on **Spring Data JPA** to handle data access tasks. With Spring Data JPA, we easily manage tasks like creating, reading, updating, and deleting data, allowing for streamlined data operations.

### 4. Email Notifications
For email functionalities, we use **JavaMailSender**. Whether sending alerts or important updates, it ensures smooth communication with users. To enhance the design of the emails, **Thymeleaf** is used as the template engine, allowing the creation of email templates with placeholders to fill with real-time data before dispatching them.

### 5. Automated Tasks
**Spring's scheduling** capabilities automate routine tasks in the application. One critical task is the automated resetting of the daily limit at **12:00 AM** each day, ensuring the system runs smoothly without manual intervention.

### 6. Real-Time Exchange Rates
The application integrates seamlessly with the **Open Exchange Rates API** ([https://openexchangerates.org/](https://openexchangerates.org/)) to fetch real-time exchange rate data, keeping users updated with the latest financial information.

### 7. Data Encryption
For password security, we use **BCryptPasswordEncoder** to securely encrypt user passwords before storing them in the database. This ensures that our application follows the highest security standards for data protection.

## IV. Conclusion

Our Online Banking Web Application is designed to offer convenience, functionality, and personalization. Whether managing day-to-day finances or performing complex transactions, users can enjoy a simplified and reliable banking experience. With its robust technology stack and focus on user satisfaction, this application is the ideal digital banking solution for today’s fast-paced world.
