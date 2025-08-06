# Pure Healthy Eats - Food Delivery Application

A Spring Boot web application for healthy food delivery built with Java 17, Spring Boot 3.2, and Thymeleaf.

## Features

- **User Authentication**: Registration and login system
- **Product Catalog**: Browse healthy food options with detailed information
- **Shopping Cart**: Add items to cart and manage orders
- **Payment Integration**: Stripe and UPI payment support (demo mode)
- **QR Code Generation**: For order tracking
- **Admin Dashboard**: Manage products and orders
- **Responsive Design**: Works on desktop and mobile devices

## Technologies Used

- **Backend**: Spring Boot 3.2, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, Bootstrap 5, HTML/CSS/JavaScript
- **Database**: H2 (in-memory for demo), MySQL support available
- **Build Tool**: Maven
- **Java Version**: 17

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Running Locally

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd pure-healthy-eats
   ```

2. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access the application**
   - Open your browser to: http://localhost:8080
   - The app will automatically create sample data on startup

### Default Login Credentials

- **Admin User**: admin@purehealthyeats.com / admin123
- **Regular User**: user@example.com / user123

## Deployment Options

### 1. Heroku Deployment

1. Install Heroku CLI: `brew install heroku/brew/heroku`
2. Login: `heroku login`
3. Create app: `heroku create pure-healthy-eats-app`
4. Deploy: `git push heroku main`

### 2. Railway Deployment

1. Install Railway CLI: `curl -fsSL https://railway.com/install.sh | sh`
2. Login: `railway login`
3. Initialize: `railway init`
4. Deploy: `railway up`

### 3. Google Cloud Platform (App Engine)

1. Install gcloud CLI
2. Create project: `gcloud projects create your-project-id`
3. Deploy: `gcloud app deploy`

### 4. Docker Deployment

1. **Build the JAR file**
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. **Build Docker image**
   ```bash
   docker build -t pure-healthy-eats .
   ```

3. **Run container**
   ```bash
   docker run -p 8080:8080 pure-healthy-eats
   ```

### 5. Manual JAR Deployment

1. **Build the application**
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. **Run the JAR file**
   ```bash
   java -jar target/pure-healthy-eats-0.0.1-SNAPSHOT.jar
   ```

## Configuration

### Environment Variables (for production)

```bash
SPRING_PROFILES_ACTIVE=production
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/purehealthyeats
SPRING_DATASOURCE_USERNAME=your_db_username
SPRING_DATASOURCE_PASSWORD=your_db_password
STRIPE_API_KEY=your_stripe_secret_key
STRIPE_PUBLISHABLE_KEY=your_stripe_publishable_key
```

### Application Profiles

- **Development**: Uses H2 in-memory database
- **Production**: Configured for external database (MySQL/PostgreSQL)

## Project Structure

```
src/
├── main/
│   ├── java/com/example/purehealthyeats/
│   │   ├── config/          # Security and data configuration
│   │   ├── controller/      # Web controllers
│   │   ├── model/          # Entity classes
│   │   ├── repository/     # Data repositories
│   │   └── service/        # Business logic
│   └── resources/
│       ├── static/         # CSS, JS, images
│       ├── templates/      # Thymeleaf templates
│       └── application.properties
└── test/                   # Unit tests
```

## API Endpoints

- `/` - Home page with featured products
- `/auth/register` - User registration
- `/auth/login` - User login
- `/menu` - Product catalog
- `/checkout` - Checkout process
- `/dashboard` - User dashboard
- `/admin/**` - Admin functions (requires ADMIN role)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if necessary
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For support, please contact: support@purehealthyeats.com

---

**Note**: This is a demonstration application. Payment integrations are in test mode and use dummy data.
