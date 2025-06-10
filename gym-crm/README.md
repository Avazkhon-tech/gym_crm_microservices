# Gym Management System

Welcome to the Gym Management System! This system allows trainers to manage training sessions, trainees to track their progress, and much more.

---

## 1. Users

Users in the system are either **trainers** or **trainees**.

🔑 **Authentication**  
Provide the token (the one you get from login) in the Authorize section of Swagger (top right corner) for the user you want to authenticate as.

### Trainers
- **Olim Karimov**
    - Username: `olim.karimov`
    - Password: `1234`

### Trainees
- **Aziz Murodov**
    - Username: `aziz.murodov`
    - Password: `1234`

---

## 2. List of Trainer Usernames
Use this list when updating a trainee's assigned trainers:

```json
[
  "olim.karimov",
  "sardor.tursunov",
  "bekzod.mahmudov",
  "jasur.norboyev"
]
```

## Accessing the System
- You can view the API documentation at [Swagger UI](http://localhost:8080/api/v1/swagger-ui/index.html#).
- Check the health of the system at [Health Check](http://localhost:8080/api/v1/actuator/health).
- Get system metrics at [Prometheus Metrics](http://localhost:8080/api/v1/actuator/prometheus).
- Get test results at [Test reports ](http://localhost:63342/gym-crm/build/reports/tests/test/index.html?_ijt=st7kmnml6i4d0jtijifrtplvlj&_ij_reload=RELOAD_ON_SAVE).
- Get h2 console at [H2 console](http://localhost:8081/api/v1/h2-console) 
- (path for db -> ```json jdbc:h2:mem:workloaddb```)


