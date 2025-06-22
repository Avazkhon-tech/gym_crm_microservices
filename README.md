# Gym Management System

Welcome to the Gym Management System! This platform enables trainers to manage training sessions effectively and allows trainees to track their progress, among other features.

---

## Getting Started

To clone the project to your local machine, follow these steps:

1. Ensure you have Git Bash installed on your Windows system.
2. Clone the repository:
   ```bash
   git clone git@github.com:Avazkhon-tech/gym_crm_microservices.git
   ```

3. Navigate to the project directory. 
4. Run the following command to cleanly build all three services:
   ```bash
   ./easy-build.sh
   ```
   
   If you encounter issues running the script, you can build each project individually.
   first cd into the module then run
   ```bash
   ./gradlew build
   ```

5. Start the services using Docker Compose:
   ```bash
   docker-compose up
   ```


## Users

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

## List of Trainer Usernames
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
- You can access ActiveMQ admin panel using `admin` as both username and password at [ActiveMQ](http://localhost:8161/admin/queues.jsp)
- You can access Mongo express using `user` as both username and password at [Mongo Express](http://localhost:8000)

To observe the message broker in action, turn off the trainer workload service. 
Once you restart it, the service will consume requests from the queue. 
After a few seconds, you can access the H2 console to verify that the process was successful.






   