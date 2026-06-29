

## Assignment

### Assignment name : name of git branch ```ASS03```
1. Created new order 'api/v1/orders'
2. Find all orders 'api/v1/orders' [GET] with pagination and sort DESC
3. Find order by ID 'api/v1/orders/{id}' [GET]
4. Soft delete order by ID 'api/v1/orders/{id}/soft-delete' [PUT]
   (only PENDING or CANCELLED unpaid orders)
5. Hard delete order by ID 'api/v1/orders/{id}' [DELETE]
   (only after CANCELLED and never PAID)
6. Set Payment status by ID 'api/v1/orders/{id}/status' [PUT]

### Sonarqube 
1. Run Analysis ```./gradlew clean test sonar -Dsonar.token=YOUR_TOKEN```
2. With .ENV
   ```
    export SONAR_TOKEN=YOUR_TOKEN
    ./gradlew clean test sonar
   ```
3. Sonarqube is running on port: ```http://localhost:9000```