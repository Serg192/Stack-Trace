# Stack Trace 
**Pet project for learning full-stack web development with Spring Boot + React**

Stack Trace is a platform that serves as a community-driven question-and-answer (Q&A) 
forum for programming and software development-related topics. The platform is currently 
in development, and very few features are actually implemented. 

This project is a rework of the [usof-front](https://github.com/Serg192/usof-front) project.

---
### Backend
Implemented features: 
1. User registration
2. Email verification
3. JWT based authentication and authorization with access & refresh tokens
4. API method security with user roles
5. User logout
6. Creating posts (for authenticated users) and get specific post(all users)
---
### Endpoints
#### User registration: ```POST '/api/v0/users/new' ``` <br>
 Request body example: <br>
```json
{
    "username": "admin",
    "password": "123456",
    "email": "test@gmail.com"
}
```
JSON response example:
```json
{
    "status": "Successful",
    "message": "User registered successfully",
    "data": {
        "id": 1,
        "username": "admin",
        "email": "test@gmail.com",
        "rating": 0,
        "emailVerified": false,
        "accountDeleted": false,
        "accountBanned": false,
        "roles": [
            {
                "roleName": "type_user"
            }
        ]
    }
}
```
---
#### User logging in: ```POST '/api/v0/auth/login' ``` <br>
Request body example: <br>
```json
{
    "username": "admin",
    "password": "123456"
}
```
JSON response example:
```json
{
    "accessToken": "__access__token__"
}
```
The refresh token will be sent to the client as an HTTP-only cookie

---
#### Refresh token endpoint: ```POST '/api/v0/auth/refresh' ``` <br>
JSON response example:
```json
{
    "accessToken": "__access__token__"
}
```

---

#### User logout: ```POST '/api/v0/auth/logout' ``` <br>
This request cleans up the refresh token cookie and removes the refresh token from the whitelist.

---
#### Email confirmation endpoint: ```GET '/api/v0/auth/email-confirmation?token=[TOKEN]' ```

---
#### Get all users (paginated response): ```GET '/api/v0/users?page=[PAGE]&size=[SIZE]'```

---
## Posts
Get post by id: ```GET '/api/v0/posts/{post_id}'```

JSON response example:
```json
{
    "status": "Successful",
    "message": null,
    "data": {
        "id": 1,
        "user": {
            "id": 1,
            "username": "user",
            "email": "test@gmail.com",
            "rating": 0,
            "emailVerified": true,
            "accountDeleted": false,
            "accountBanned": false,
            "roles": [
                {
                    "roleName": "type_user"
                }
            ]
        },
        "title": "Test post num 1",
        "postContent": "This is my first post",
        "publishDate": "2023-12-19",
        "categories": [
            {
                "id": 1,
                "title": "General",
                "description": "General discussions and questions"
            },
            {
                "id": 2,
                "title": "Java",
                "description": "Java programming language"
            }
        ],
        "problemSolved": false,
        "postBanned": false
    }
}
```
---
Create post: ```POST '/api/v0/posts'```

Request body example:
```json
{
  "title": "Test post num 1",
  "postContent": "This is my first post",
  "categories": [1, 2, 2]
}
```