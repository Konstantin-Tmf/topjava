# MealRestController curl

Base URL:

```bash
http://localhost:8080/topjava
```

Optional: set current user to `100000` before testing meals.

```bash
curl -i -X POST "http://localhost:8080/topjava/users" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data "userId=100000"
```

Get all meals:

```bash
curl -i "http://localhost:8080/topjava/rest/meals"
```

Get meal by id:

```bash
curl -i "http://localhost:8080/topjava/rest/meals/100003"
```

Filter meals by date and time:

```bash
curl -i "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-30&startTime=07:00&endTime=11:00"
```

Filter meals by time only:

```bash
curl -i "http://localhost:8080/topjava/rest/meals/filter?startTime=07:00&endTime=11:00"
```

Filter meals by date only:

```bash
curl -i "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-30"
```

Create meal:

```bash
curl -i -X POST "http://localhost:8080/topjava/rest/meals" \
  -H "Content-Type: application/json" \
  -d "{\"dateTime\":\"2020-02-01T18:00:00\",\"description\":\"Created dinner\",\"calories\":300}"
```

Update meal:

```bash
curl -i -X PUT "http://localhost:8080/topjava/rest/meals/100003" \
  -H "Content-Type: application/json" \
  -d "{\"id\":100003,\"dateTime\":\"2020-01-30T10:02:00\",\"description\":\"Updated breakfast\",\"calories\":200}"
```

Delete meal:

```bash
curl -i -X DELETE "http://localhost:8080/topjava/rest/meals/100003"
```
