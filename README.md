# 01Blog - Social Learning Platform

A fullstack social blogging platform built with Spring Boot and Angular, designed for students to share their learning experiences, discoveries, and progress throughout their educational journey.

## 🚀 Features

### Backend Features

- **Authentication & Authorization**: JWT-based authentication with role-based access control (User/Admin)
- **User Management**: User registration, login, profile management
- **Posts**: Create, read, update, delete posts with media upload (images/videos)
- **Social Interactions**: Like posts, comment on posts, follow/unfollow users
- **Notifications**: Real-time notifications for new posts from followed users
- **Reporting System**: Users can report inappropriate content, admins can moderate
- **Admin Panel**: Complete admin dashboard for user and content management
- **Media Upload**: Secure file upload with local storage
- **RESTful API**: Well-documented REST endpoints with consistent response format

### Frontend Features (Angular)

- **Responsive UI**: Built with Angular Material for modern, responsive design
- **User Dashboard**: Personal blog page with full post management
- **Social Feed**: Homepage with posts from followed users
- **Real-time Interactions**: Like, comment, and follow functionality
- **Media Preview**: Image and video upload with previews
- **Admin Interface**: Complete admin panel for content moderation

## 🛠️ Technology Stack

### Backend

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Security** with JWT authentication
- **Spring Data JPA** with Hibernate
- **PostgreSQL** database
- **Maven** for dependency management
- **Docker** for database containerization

### Frontend

- **Angular 21**
- **Angular Material** for UI components
- **TypeScript**
- **RxJS** for reactive programming
- **Angular CLI** for development

## 📋 Prerequisites

- **Java 17** or higher
- **Node.js 18+** and npm
- **Docker** and Docker Compose
- **PostgreSQL** (or use Docker)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/hajji-Ismail/blog
cd blog
```

### 2. Backend Setup

./mvnw spring-boot:run

```bash
# Start PostgreSQL database


# Navigate to backend directory
cd backend

# Run the application
./mvnw spring-boot:run
```

### 3. Frontend Setup

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
ng serve 
```

The frontend will start on `http://localhost:4200`

