
# ===============Ecommerce==============

## Assignment

## Assignment name : name of git branch ```ASS03```
```json
{
    "1. Created new order": "Created new order 'api/v1/orders'",
    "2. Find all orders": "'api/v1/orders' [GET] with pagination and sort DESC",
    "3. Find order by ID": "'api/v1/orders/{id}' [GET]",
    "4. Soft delete order by ID": "'api/v1/orders/{id}/soft-delete' [PUT], (only PENDING or CANCELLED unpaid orders)",
    "5. Hard delete order by ID": "'api/v1/orders/{id}' [DELETE], (only after CANCELLED and never PAID)",
    "6. Set Payment status by ID ": "'api/v1/orders/{id}/status' [PUT]"
}
```

### Assignment name : name of git branch ```ASS04```

## Sonarqube 
1. Run Analysis ```./gradlew clean test sonar -Dsonar.token=YOUR_TOKEN```
2. With .ENV
   ```shell
    export SONAR_TOKEN=YOUR_TOKEN
    ./gradlew clean test sonar
   ```
3. Sonarqube is running on port: 
 ```http request
   http://localhost:9000
 ```

## Repository Workflow
This project uses two Git remotes:
```json
{
   "**GitHub**": "public repository for code submission/portfolio",
   "**GitLab**": "private repository for deployment and CI/CD"
}
```

**Remotes**
```shell
    git remote -v
```
Result:
```json
{
  "github": "git@github.com:thorniedev/ITE-Gen3-SpringBoot.git",
  "gitlab": "git@gitlab.com: ite-gen3/ecommerce.git"
}
```
**Daily Flow**

*1. Create feature branch*
```shell
git checkout main
git pull gitlab main
git checkout -b feature/task-name
```
*2. Commit code*
```shell
git add .
git commit -m "feat: your message"
```

*3. Push to GitHub for public*
```shell
git push github feature/task-name
```

*4. Push to GitLab for deployment*
```shell
git push gitlab feature/task-name
```

*5. Create Merge Request in GitLab*
```shell
feature/task-name → main
```

