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
6. Post creation/edit (for authenticated users) and get specific post(all users)
7. Create comments and likes/dislikes under post
8. Sorting and filtering for posts
9. Categories creation and update
10. Admins can ban users & delete their posts and comment

Main features to come:

1. Voting to add a new category
2. User rating based on the number of received likes and dislikes
3. Password recovery
4. Searching posts by keywords
5. User profile customizations

and more

---

### Documentation

The documentation is built using OpenAPI and Swagger.

To access it, use the `/swagger-ui/index.html` endpoint.
