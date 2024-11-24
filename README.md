
# The Roomsy App

Roomsy App allows users to enter Rooms, share, edit, delete their posts. They can manage their account. We have a safe environment in which administrators handle reports made by users.





## Deploy backend

Database with ready data:
Create empty database with name "android".
Import database android.sql to your localhost phpMyAdmin on port 3306(prefferably) with ready data. (Rooms and default admin + user accounts)

You can change application.properties to match your db port and location in line:
spring.datasource.url=jdbc:mysql://localhost:3306/android


To run this project enter backend/kamark folder and run

mvn install

mvn spring-boot:run

App will run at localhost port 8005 by default.
## Deploy Mobile Android App

To run Android mobile App. Open the project "frontend" in Android Studio.
Create a virtual device with parameters:

Pixel 8 Pro API 34, Android ver 14.0 "Upside Down Cake".

Run the app and enable Notifications when asked for permission on the mobile device.

## Swagger

To test backend with Swagger use link:
http://localhost:8005/swagger-ui/index.html#/

Because of the issue linked to using Swagger with Spring Security - please follow these steps:

1. Register a user at POST /auth/Register
2. Copy the received accessToken
3. Click "Authorize" with open lock icon. (At the top right of routes list)
4. Paste the accessToken and choose "Authorize" - lock icon should be now closed (secured)
5. With every route that needs data from tokens, use this token to access routes.

Note: AccessToken stores roles. For Admin routes(admin-rest-controller) you will need an AccessToken from an Admin account.

## Postman

To use Postman remember to add header "Authorization" with value "Bearer [access-token]" for every request.

## Ready Accounts

1. ready USER role accounts:

{
    "email":"user@gmail.com",
"username": "user",
   "password": "Qwerty12345!"
}

2. ready ADMIN role accounts:

{
    "email":"admin@gmail.com",
"username": "admin",
   "password": "Qwerty12345!"
}
