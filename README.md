# Hotel CRM

**A SaaS CRM application tailored for the hospitality industry.**  
Manages client relationships, events, follow-ups, and communications across hotels or venues.

---

## 📦 Project Structure

```
hotel-crm/
├── crm-app/         # Backend - Spring Boot (Java 21)
├── crm-frontend/    # Frontend - (Angular, React, or TBD)
├── docker/          # Docker Compose setup
```

## 🛠 Technologies Used

- **Java 21** + Spring Boot 3.4
- **PostgreSQL** (Dockerized)
- **Liquibase** for DB migrations (coming soon)
- Maven for build lifecycle

---

## 🚀 Getting Started

```bash
# Start database
cd docker
docker compose up -d

# Run backend app
cd ../crm-app
./mvnw spring-boot:run
```

---

## 🧭 Roadmap

- [x] Project structure setup
- [ ] Liquibase integration
- [ ] Frontend module development
- [ ] CI/CD with GitHub Actions
- [ ] Deployment on cloud (Render, Fly.io, etc.)

---

## 👤 Author

Created by [@michalswig](https://github.com/michalswig)
