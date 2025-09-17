# SkillMate AI API Documentation

## Base URL
```

Example: `/api/v1/content/course-path/progress/COURSE_PATH_ID`
https://skillmate-ai-analyzer-3eb6ee2b4a94.herokuapp.com/
```

---


Example: `/api/v1/content/course-path/COURSE_PATH_ID`
## Authentication Endpoints

> **Authentication System Overview:** This section contains all endpoints related to user authentication, registration, and account verification. The system uses JWT tokens for authentication and requires email verification for new accounts.


Example: `/api/v1/content/course-path/topic/TOPIC_ID`
### User Signup
*Creates a new user account and initiates email verification process*

`/api/v1/auth/signup`

#### Flow
User accesses signup page -> User provides registration details -> System creates account and sends verification

#### Request Body
```json
{
  "email": "user@example.com",
  "password": "yourPassword123",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Method: POST

**Public endpoint**

#### Success Conditions
- `email`, `password`, and `firstName` must be non-empty strings
- `email` must be a valid educational email format
- `password` must be 8-50 characters
- No existing user with the same email
- User is created and saved successfully

#### Response List
- **User successfully created:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "User successfully created",
        "jwt": "<jwt-token>",
        "refreshToken": "<refresh-token>",
        "userInfo": {
          "email": "user@example.com",
          "firstName": "John",
          "lastName": "Doe",
          "isVerified": false,
          "isAccountEnabled": true,
          "isBlocked": false
        }
      }
      ```

- **Request body is missing:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **Email, password, or firstName is empty:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Email, password or first name is empty" }
      ```

- **Invalid email format:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "This email address is not valid. You must use a JUST edu email" }
      ```

- **Password not between 8 and 50 characters:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Password must be between 8 and 50 characters" }
      ```

- **User already exists:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Another user with this email already exists" }
      ```

- **User creation failed:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "User creation failed, please try again" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while creating new user" }
      ```

---

### User Login
*Authenticates user credentials and returns JWT access and refresh tokens*

`/api/v1/auth/login`

#### Flow
User accesses login page -> User provides credentials -> System authenticates and returns tokens

#### Request Body
```json
{
  "email": "user@example.com",
  "password": "yourPassword123"
}
```

#### Method: POST

**Public endpoint**

#### Success Conditions
- `email` and `password` must be non-empty strings
- `email` must be valid format
- User account exists
- Password matches stored password
- User account is not blocked

#### Response List
- **Login successful:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Login successful",
        "jwt": "<jwt-token>",
        "refreshToken": "<refresh-token>",
        "userInfo": {
          "email": "user@example.com",
          "firstName": "John",
          "lastName": "Doe",
          "isVerified": true,
          "isAccountEnabled": true,
          "isBlocked": false
        }
      }
      ```

- **Request body is missing:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **Email or password is empty:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Email or password is empty" }
      ```

- **Invalid email format:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "This email address is not valid" }
      ```

- **Account does not exist:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "Account does not exist" }
      ```

- **Account does not exist:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "Account does not exist" }
      ```

- **Email or password is incorrect:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "Email or password is incorrect" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while logging user in" }
      ```

---

### Refresh JWT Token
*Generates a new access token using a valid refresh token*

`/api/v1/auth/refresh`

#### Flow
User's JWT token is about to expire -> User provides refresh token -> System generates new JWT token

#### Query Parameters
- `refreshToken` (string, required): The refresh token to use for generating new JWT

#### Method: GET

**Public endpoint**

#### Response List
- **New JWT generated successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "New JWT generated",
        "jwt": "<new-jwt-token>"
      }
      ```

- **Refresh token is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Refresh token is required" }
      ```

- **Refresh token is invalid or expired:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "Refresh token is invalid or expired" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while refreshing JWT" }
      ```

---

### Verify Token
*Validates JWT token and returns token information and user details*

`/api/v1/auth/token-verification`

#### Flow
System or user needs to verify token validity -> Token is provided -> System validates and returns token type

#### Query Parameters
- `token` (string, required): The token to verify (JWT or Refresh Token)

#### Method: GET

**Public endpoint**

#### Response List
- **Valid JWT token:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      { "success": true, "message": "This a valid JWT" }
      ```

- **Valid Refresh token:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      { "success": true, "message": "This a valid Refresh Token" }
      ```

- **Token is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Token is required" }
      ```

- **Token is invalid or expired:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "This token is invalid or expired" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while verifying the token" }
      ```

---

## User Management Endpoints

> **User Management Overview:** These endpoints provide authenticated users with access to their account information, profile management, and account settings. All endpoints require valid JWT authentication.

### Get User Info
*Retrieves basic authenticated user account information*

`/api/v1/user/info`

#### Flow
Authenticated user wants to view their profile information -> System returns user details

#### Method: GET

**Requires Authentication (JWT Token)**

#### Response List
- **User info retrieved successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "User found",
        "userInfo": {
          "email": "user@example.com",
          "firstName": "John",
          "lastName": "Doe",
          "isVerified": true,
          "isAccountEnabled": true,
          "isBlocked": false
        }
      }
      ```

- **Unauthorized:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while fetching user" }
      ```

---

### Get User Profile (Extended)
*Retrieves comprehensive user profile including enrolled and created course lists*

`/api/v1/user/profile`

#### Flow
Authenticated user wants to view their complete profile including course information -> System returns extended user details with enrolled and created courses

#### Method: GET

**Requires Authentication (JWT Token)**

#### Response List
- **User profile retrieved successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "User profile found",
        "userProfile": {
          "email": "user@example.com",
          "firstName": "John",
          "lastName": "Doe",
          "isVerified": true,
          "isAccountEnabled": true,
          "isBlocked": false,
          "enrolledCoursePaths": ["course_123", "course_456"],
          "createdCoursePaths": ["course_789", "course_abc"]
        }
      }
      ```

- **Unauthorized:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while fetching user profile" }
      ```

---

### Delete User Account
*Permanently deletes the authenticated user's account and all associated data*

`/api/v1/user/delete`

#### Flow
Authenticated user wants to delete their account -> System removes user and all associated data

#### Method: DELETE

**Requires Authentication (JWT Token)**

#### Response List
- **User successfully deleted:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      { "success": true, "message": "User successfully deleted" }
      ```

- **Unauthorized:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **User deletion partial or unsuccessful:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "User deletion has concluded partial or unsuccessful" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while deleting user" }
      ```

---

### Update Name
*Updates the authenticated user's first and last name*

`/api/v1/user/update/name`

#### Flow
Authenticated user wants to update their name -> User provides new name details -> System validates and updates

#### Request Body
```json
{
  "newFirstName": "UpdatedFirstName",
  "newLastName": "UpdatedLastName"
}
```

#### Method: PATCH

**Requires Authentication (JWT Token)**

#### Response List
- **Name successfully updated:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      { "success": true, "message": "Name successfully updated" }
      ```

- **Request body is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **Name cannot be empty:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
  { "success": false, "message": "Name cannot be empty" }
      ```

```

- **Unauthorized:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while updating name" }
      ```

---

### Update Password
*Changes the authenticated user's password after validating current password*

`/api/v1/user/update/password`

#### Flow
Authenticated user wants to change password -> User provides current and new password -> System validates and updates

#### Request Body
```json
{
  "oldPassword": "currentPassword123",
  "newPassword": "newPassword456"
}
```

#### Method: PATCH

**Requires Authentication (JWT Token)**

#### Response List
- **Password successfully changed:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      { "success": true, "message": "Password successfully changed" }
      ```

- **Request body is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **Old password is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Old password is required" }
      ```

- **New password is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "New password is required" }
      ```

- **Previous password did not match:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "Previous password did not match" }
      ```

- **Password must be between 8 and 50 characters:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Password must be between 8 and 50 characters" }
      ```

- **New password cannot be same as previous:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "New password cannot be same as your previous password" }
      ```

- **Unauthorized:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while resetting password" }
      ```

---

### Send Verification Code
*Sends email verification code to user's registered email address*

`/api/v1/auth/email-verification/code`

#### Flow
User requests verification -> System generates and sends verification code via email

#### Request Body
```json
{}
```

#### Method: POST

**Requires Authentication (JWT Token)**

#### Response List
- **Verification code sent successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      { "success": true, "message": "Email verification code sent" }
      ```

- **User is already verified:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "This user is already verified" }
      ```

- **Unauthorized (no/invalid token):**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "JWT token has expired" }
      ```
      or
      ```json
      { "message": "Authentication failed" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while sending verification code" }
      ```

---

### Verify Account
*Verifies user's email address using the verification code sent via email*

`/api/v1/auth/email-verification/verify`

#### Flow
User receives verification code -> User enters code -> System verifies and marks account as verified

#### Request Body
```json
{
  "verificationCode": "123456"
}
```

#### Method: POST

**Requires Authentication (JWT Token)**

#### Response List
- **Account verified successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      { "success": true, "message": "Email verification code verified. User is now verified" }
      ```

- **Request body is missing:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **Verification code is empty:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Verification code is required" }
      ```

- **User has not requested verification code:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "This user has not requested for verification code" }
      ```

- **Incorrect verification code:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "This code is incorrect" }
      ```

- **Verification code has expired:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "This code has expired" }
      ```

- **Unauthorized:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while verifying verification code" }
      ```

---



### Send Forgot Password Code
*Sends a password reset verification code to user's email address*

`/api/v1/auth/forget-password/code`

#### Flow
User forgot password -> User provides email -> System sends reset code

#### Request Body
```json
{
  "email": "user@example.com"
}
```

#### Method: POST

**Public endpoint**

#### Response List
- **Forgot password verification code sent:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      { "success": true, "message": "Forgot password verification code sent" }
      ```

- **Request body is missing:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **Email is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Email is required" }
      ```

- **User does not exist:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "User does not exist" }
      ```

- **User with this email does not exist:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "User with this email does not exist" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while sending password reset verification code" }
      ```

---

### Verify Forgot Password Code and Reset Password
*Verifies password reset code and sets new password for the user account*

`/api/v1/auth/forget-password/verify-and-reset`

#### Flow
User receives reset code -> User enters code and new password -> System verifies code and resets password

#### Request Body
```json
{
  "email": "user@example.com",
  "verificationCode": "123456",
  "newPassword": "newPassword123"
}
```

#### Method: POST

**Public endpoint**

#### Response List
- **Password reset successful:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Forgot password code was verified and password reset is successful",
        "jwt": "<jwt-token>",
        "refreshToken": "<refresh-token>",
        "userInfo": {
          "email": "user@example.com",
          "firstName": "John",
          "lastName": "Doe",
          "isVerified": true,
          "isAccountEnabled": true,
          "isBlocked": false
        }
      }
      ```

- **Request body is missing:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **Email is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Email is required" }
      ```

- **Verification code is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Verification code is required" }
      ```

- **New password is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "New password is required" }
      ```

- **User does not exist:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "User does not exist" }
      ```

- **This code is incorrect:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "This code is incorrect" }
      ```

- **This code has expired:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "This code has expired" }
      ```

- **Password must be between 8 and 50 characters:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Password must be between 8 and 50 characters" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while verifying password reset verification code" }
      ```

---

### Health Check
*Checks if the API service is running and responding properly*

`/api/v1/public/health-check`

#### Flow
System health monitoring -> Check if service is running

#### Method: GET

**Public endpoint**

#### Response List
- **Service is healthy:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      { "success": true, "message": "API is up and running" }
      ```

- **Service error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred" }
      ```

---

## Content Management Endpoints

**Important Note:** All Content Management endpoints require both JWT authentication and user verification. If the user is not authenticated, a 401 UNAUTHORIZED response is returned. If the user is not verified, a 403 FORBIDDEN response with the message "User account is not verified. Please verify your email address" is returned.

### Request Course Path Generation (Async)

`/api/v1/content/course-path/generate`

#### Flow
Authenticated and verified user requests a new course path -> Server forwards the request (including the user's email) to the external AI Analyzer -> Analyzer accepts the job -> Server returns 202 Accepted without any course data

#### Request Body
```json
{
  "subject": "Python Programming",
  "difficulty": "beginner"
}
```

#### Method: POST

**Requires Authentication (JWT Token)**

#### Notes
- This endpoint is asynchronous. No course, topic, or progress data is persisted or returned in this call.
- The server includes the authenticated user's `email` when calling the external analyzer. Clients should NOT send email in the request body.

#### Response List
- **Accepted for processing (no data returned):**
    - **Status:** 202 ACCEPTED
    - **Body:**
      ```json
      { "success": true, "message": "Course path will be generated soon" }
      ```

- **Generated immediately (no data returned):**
    - **Status:** 200 OK
    - **Body:**
      ```json
      { "success": true, "message": "Course path generated" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "User not authenticated" }
      ```

- **User not verified:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "User account is not verified" }
      ```

- **Request body is missing:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **Subject and difficulty are required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Subject and difficulty are required" }
      ```

- **AI Analyzer unreachable:**
    - **Status:** 502 BAD GATEWAY
    - **Body:**
      ```json
      { "success": false, "message": "AI Analyzer unreachable" }
      ```

- **Failed to generate course path (analyzer unsuccessful):**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "Failed to generate course path" }
      ```

- **Internal error generating course path:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while generating course path" }
      ```

---

### Get My Course Paths

`/api/v1/content/course-path/mine`

#### Flow
Authenticated user wants to see both their created and enrolled courses -> System returns lists of user's created and enrolled courses

#### Method: GET

**Requires Authentication (JWT Token)**

#### Response List
- **Course paths retrieved successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Fetched user course paths",
        "userCoursePaths": {
          "createdCoursePaths": [
            {
              "id": "course_123",
              "title": "Python Programming Basics",
              "description": "A comprehensive beginner's guide to Python",
              "topicCount": 10
            }
          ],
          "enrolledCoursePaths": [
            {
              "id": "course_456",
              "title": "JavaScript Fundamentals",
              "description": "Learn JavaScript from scratch",
              "topicCount": 8
            }
          ]
        }
      }
      ```

- **User not verified:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "User account is not verified" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "User not authenticated" }
      ```

- **Internal error fetching course paths:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while fetching course paths" }
      ```

---

### Enroll in Course Path

`/api/v1/content/course-path/enroll`

#### Flow
Authenticated user wants to enroll in a course -> User provides course ID -> System creates progress tracking

#### Request Body
```json
{
  "coursePathId": "course_123"
}
```

#### Method: POST

**Requires Authentication (JWT Token)**

#### Response List
- **Enrolled successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Successfully enrolled in course path",
        "data": {
          "progressId": "progress_456",
          "coursePath": {
            "id": "course_123",
            "title": "Python Programming Basics",
            "description": "A comprehensive beginner's guide to Python"
          }
        }
      }
      ```

- **User not verified:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "User account is not verified" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "User not authenticated" }
      ```

- **Request body is missing:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **Course path ID is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Course path ID is required" }
      ```

- **Course path not found:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "Course path not found" }
      ```

- **Already enrolled in this course path:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Already enrolled in this course path" }
      ```

- **Internal error enrolling in course path:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while enrolling" }
      ```

---

### Get User Progress

`/api/v1/content/course-path/progress/{coursePathId}`

#### Flow
Authenticated user wants to check their progress in a course -> System returns progress details

#### Method: GET

**Requires Authentication (JWT Token)**

#### URL Parameters
- `coursePathId` (string): The ID of the course path

#### Response List
- **Progress retrieved successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Fetched user progress",
        "progress": {
          "userId": "user_123",
          "coursePathId": "course_123",
          "startedAt": 1692084000000,
          "readiness": 60,
          "progress": [
            {
              "topicId": "topic_1",
              "isCovered": true,
              "lastUpdated": 1692084000000
            }
          ]
        }
      }
      ```

- **User not verified:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "User account is not verified" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "User not authenticated" }
      ```

- **No progress found for this course path:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "No progress found for this course path" }
      ```

- **Internal error fetching progress:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while fetching progress" }
      ```

---

### Generate Course Path with Duplicate Check ⚠️ **EXPERIMENTAL - NOT RECOMMENDED**

`/api/v1/content/course-path/generate/check-duplicate`

> **⚠️ EXPERIMENTAL ENDPOINT:** This endpoint is currently experimental and has been disabled. It is not recommended for production use. Please use the standard `/api/v1/content/course-path/generate` endpoint instead.

#### Flow
~~Authenticated user wants to create course but check for duplicates first -> User provides subject and difficulty -> System checks for similar courses and generates new one if needed~~ **DISABLED**

#### Request Body
```json
{
  "subject": "Python Programming",
  "difficulty": "beginner"
}
```

#### Method: POST

**~~Requires Authentication (JWT Token)~~** **DISABLED**

> **Note:** This endpoint has been disabled and is no longer available. Use the standard course path generation endpoint instead.

#### Response List
- **Course path created successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Course path processed",
        "data": {
          "coursePath": {
            "id": "course_123",
            "title": "Python Programming Basics",
            "description": "A comprehensive beginner's guide to Python"
          },
          "topicsCount": 10,
          "progressId": "progress_123"
        }
      }
      ```

- **Similar course exists:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Course path processed",
        "data": {
          "existingCourse": {
            "id": "course_456",
            "title": "Python Programming Fundamentals",
            "description": "Similar course already exists"
          },
          "message": "Similar course path already exists"
        }
      }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "User not authenticated" }
      ```

- **Request body is missing:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **Subject and difficulty are required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Subject and difficulty are required" }
      ```

- **AI Analyzer unreachable:**
    - **Status:** 502 BAD GATEWAY
    - **Body:**
      ```json
      { "success": false, "message": "AI Analyzer unreachable" }
      ```

- **Internal error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while generating course path" }
      ```

---

### Search Course Paths

`/api/v1/content/course-path/search`

Example: `/api/v1/content/course-path/search?query=python%20beginner`

#### Flow
User wants to search for existing course paths -> User provides search query -> System returns matching courses (case-insensitive one-word match across title)

#### Query Parameters
- `query` (string, required): Search terms to find course paths

#### Method: GET

**Requires Authentication (JWT Token)**

#### Response List
- **Search completed:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Search completed",
        "coursePaths": [
          {
            "id": "course_123",
            "title": "Python Programming Basics",
            "topicCount": 10,
            "createdAt": 1692084000000
          }
        ]
      }
      ```

- **Search query is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Search query is required" }
      ```

- **User not verified:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "User account is not verified" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **Internal error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while searching" }
      ```

---

### Add Review to Course Path

`/api/v1/content/course-path/review`

#### Flow
Authenticated user wants to review a course -> User provides rating and comment -> System adds review and updates average rating

#### Request Body
```json
{
  "coursePathId": "course_123",
  "rating": 5,
  "comment": "Great course! Very helpful."
}
```

#### Method: POST

**Requires Authentication (JWT Token)**

#### Response List
- **Review added successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Review added successfully",
        "data": {
          "review": {
            "reviewerId": "user_123",
            "reviewerName": "John Doe",
            "rating": 5,
            "comment": "Great course! Very helpful.",
            "reviewDate": 1692084000000
          },
          "newAverageRating": 4.5,
          "totalReviews": 10
        }
      }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "User not authenticated" }
      ```

- **Request body is missing:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **Course path ID and rating are required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Course path ID and rating are required" }
      ```

- **Rating must be between 1 and 5:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Rating must be between 1 and 5" }
      ```

- **Course path not found:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "Course path not found" }
      ```

- **You have already reviewed this course path:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "You have already reviewed this course path" }
      ```

- **Internal error adding review:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while adding review" }
      ```

---

### Get Course Path by ID

`/api/v1/content/course-path/{coursePathId}`

#### Flow
User wants to view specific course path details -> System returns course path information

#### Method: GET

**Requires Authentication (JWT Token)**

#### URL Parameters
- `coursePathId` (string): The ID of the course path

#### Response List
- **Course path retrieved successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Course path fetched successfully",
        "coursePath": {
          "id": "course_123",
          "title": "Python Programming Basics",
          "description": "A comprehensive beginner's guide to Python",
          "targetLevel": "beginner",
          "creatorId": "user_123",
          "createdAt": 1692084000000,
          "topics": ["topic_1", "topic_2"],
          "averageRating": 4.5
        }
      }
      ```

- **User not verified:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "User account is not verified" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "User not authenticated" }
      ```

- **Course path ID is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Course path ID is required" }
      ```

- **Course path not found:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "Course path not found" }
      ```

- **Internal error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while fetching course path" }
      ```

---

### Search Course Paths

`/api/v1/content/course-path/search`

#### Flow
User wants to search for existing course paths -> User provides search query -> System returns matching courses

#### Query Parameters
- `query` (string, required): Search terms to find course paths

#### Method: GET

**Requires Authentication (JWT Token)**

#### Response List
- **Search completed:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Search completed",
        "coursePaths": [
          {
            "id": "course_123",
            "title": "Python Programming Basics",
            "topicCount": 10,
            "createdAt": 1692084000000
          }
        ]
      }
      ```

- **Search query is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Search query is required" }
      ```

- **User not verified:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "User account is not verified" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **Internal error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while searching" }
      ```

---

### Get Topic by ID

`/api/v1/content/course-path/topic/{topicId}`

#### Flow
User wants to view topic details -> System returns topic information

#### Method: GET

**Requires Authentication (JWT Token)**

#### URL Parameters
- `topicId` (string): The ID of the topic

#### Response List
- **Topic retrieved successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Topic fetched successfully",
        "topic": {
          "id": "topic_123",
          "name": "Introduction to Python",
          "description": "Basic concepts of Python programming",
          "videoInfo": {
            "youtubeUrl": "https://youtube.com/watch?v=xyz",
            "title": "Python Basics",
            "startTime": 0,
            "endTime": 600
          },
          "prerequisites": ["topic_0"],
          "tags": ["programming", "python", "basics"]
        }
      }
      ```

- **User not verified:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "User account is not verified" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "User not authenticated" }
      ```

- **Topic not found:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "Topic not found" }
      ```

- **Internal error fetching topic:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while fetching topic" }
      ```

---

### Toggle Topic Covered Status

`/api/v1/content/course-path/progress/toggle-topic-status`

#### Flow
User marks/unmarks topic as completed -> System updates progress and recalculates readiness percentage

#### Request Body
```json
{
  "progressId": "progress_123",
  "topicId": "topic_123"
}
```

#### Method: POST

**Requires Authentication (JWT Token)**

#### Response List
- **Topic status updated successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Topic status toggled successfully",
        "data": {
          "topicId": "topic_123",
          "progressId": "progress_123",
          "action": "marked as covered",
          "isCovered": true,
          "newReadiness": 70,
          "coveredTopics": 7,
          "totalTopics": 10
        }
      }
      ```

- **User not verified:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "User account is not verified" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "User not authenticated" }
      ```

- **Request body is missing:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **Progress ID is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Progress ID is required" }
      ```

- **Topic ID is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Topic ID is required" }
      ```

- **Progress not found:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "Progress not found" }
      ```

- **You don't have permission to modify this progress:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "You don't have permission to modify this progress" }
      ```

- **Topic not found in this progress:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "Topic not found in this progress" }
      ```

- **Internal error toggling topic covered status:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An error occurred while toggling topic status" }
      ```

---

## Admin Management Endpoints

> **Admin System Overview:** These endpoints provide administrative functions for platform management including user administration, content moderation, and system oversight. All endpoints require admin privileges and verified accounts.

**Important Note:** All Admin Management endpoints require both JWT authentication and user verification. If the user is not authenticated, a 401 UNAUTHORIZED response is returned. If the user is not verified, a 403 FORBIDDEN response with the message "User account is not verified. Please verify your email address" is returned.

### Get All Users
*Retrieves a complete list of all registered users with their account information*

`/api/v1/admin/users`

#### Flow
Admin wants to view all users -> System returns list of all users with admin information

#### Method: GET

**Requires Authentication (JWT Token) + Admin Role**

#### Response List
- **Users retrieved successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Users fetched successfully",
        "users": [
          {
            "id": "user_123",
            "email": "user@example.com",
            "firstName": "John",
            "lastName": "Doe",
            "roles": ["USER"],
            "isAdmin": false
          },
          {
            "id": "user_456",
            "email": "admin@example.com",
            "firstName": "Admin",
            "lastName": "User",
            "roles": ["USER", "ADMIN"],
            "isAdmin": true
          }
        ],
        "totalCount": 2
      }
      ```

- **Access denied - Admin role required:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "Access denied" }
      ```

- **User not verified:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "User account is not verified" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **Internal error fetching users:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "Internal server error" }
      ```

---

### Get All Course Paths
*Retrieves a complete list of all course paths created by users for admin monitoring*

`/api/v1/admin/course-paths`

#### Flow
Admin wants to view all course paths -> System returns list of all course paths

#### Method: GET

**Requires Authentication (JWT Token) + Admin Role**

#### Response List
- **Course paths retrieved successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Course paths fetched successfully",
        "coursePaths": [
          {
            "id": "course_123",
            "title": "Python Programming Basics",
            "description": "A comprehensive beginner's guide to Python",
            "targetLevel": "beginner",
            "topicCount": 10,
            "createdAt": 1692084000000,
            "createdBy": "user_123",
            "averageRating": 4.5
          }
        ],
        "totalCount": 1
      }
      ```

- **Access denied - Admin role required:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "Access denied" }
      ```

- **User not verified:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "User account is not verified" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **Internal error fetching course paths:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "Internal server error" }
      ```

---

### Delete User
*Permanently removes a user account and all associated data from the system*

`/api/v1/admin/user/delete`

#### Flow
Admin wants to delete a user -> Admin provides user ID -> System deletes user and all associated data

#### Request Body
```json
{
  "userId": "user_123"
}
```

#### Method: DELETE

**Requires Authentication (JWT Token) + Admin Role**

#### Response List
- **User deleted successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "User deleted successfully",
        "deletedUserId": "user_123"
      }
      ```

- **Access denied - Admin role required:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "Access denied" }
      ```

- **User not verified:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "User account is not verified" }
      ```

- **Request body is missing:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **User ID is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "User ID is required" }
      ```

- **User not found:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "User not found" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **Internal error deleting user:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "Internal error deleting user" }
      ```

---

### Delete Course Path
*Permanently removes a course path and all associated topics and progress data*

`/api/v1/admin/course-path/delete`

#### Flow
Admin wants to delete a course path -> Admin provides course path ID -> System deletes course and all associated data

#### Request Body
```json
{
  "coursePathId": "course_123"
}
```

#### Method: DELETE

**Requires Authentication (JWT Token) + Admin Role**

#### Response List
- **Course path deleted successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Course path deleted successfully",
        "deletedCoursePathId": "course_123"
      }
      ```

- **Access denied - Admin role required:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "Access denied" }
      ```

- **User not verified:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "User account is not verified" }
      ```

- **Request body is missing:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Request body is required" }
      ```

- **Course path ID is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Course path ID is required" }
      ```

- **Course path not found:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "Course path not found" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **Internal error deleting course path:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "Internal error deleting course path" }
      ```

---

### Search User by Email
*Searches for a specific user account using their email address*

`/api/v1/admin/users/search`

Example: `/api/v1/admin/users/search?email=user@example.com`

#### Flow
Admin wants to find a specific user -> Admin provides email -> System searches and returns user details

#### Query Parameters
- `email` (string, required): The email address to search

#### Method: GET

**Requires Authentication (JWT Token) + Admin Role**

#### Response List
- **User found:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "User found",
        "user": {
          "id": "user_123",
          "email": "user@example.com",
          "firstName": "John",
          "lastName": "Doe",
          "roles": ["USER"],
          "isAdmin": false
        }
      }
      ```

- **No user found:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      { "success": true, "message": "No user found with the provided email" }
      ```

- **Access denied - Admin role required:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "Access denied" }
      ```

 

- **Email is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Email is required" }
      ```

- **Invalid email format:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Invalid email format" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **Internal error searching user:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "Internal error searching user" }
      ```

---

### Manage Admin Privilege
*Grants or revokes admin privileges for a specific user account*

`/api/v1/admin/users/manage-admin-privilege`

#### Flow
Admin wants to grant or revoke admin privileges -> Admin provides user ID and action -> System modifies user's admin privileges

#### Request Body
```json
{
  "userId": "user_123",
  "action": "GRANT"
}
```

- **Email is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Email is required" }
      ```

- **Invalid email format:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Invalid email format" }
      ```
or
```json
{
  "userId": "user_123",
  "action": "REVOKE"
}
```

#### Method: POST

**Requires Authentication (JWT Token) + Admin Role**

#### Response List
- **Admin privileges granted successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Admin privileges granted successfully",
        "user": {
          "id": "user_123",
          "email": "user@example.com",
          "firstName": "John",
          "lastName": "Doe",
          "roles": ["USER", "ADMIN"],
          "isAdmin": true
        }
      }
      ```

- **Admin privileges revoked successfully:**
    - **Status:** 200 OK
    - **Body:**
      ```json
      {
        "success": true,
        "message": "Admin privileges revoked successfully",
        "user": {
          "id": "user_123",
          "email": "user@example.com",
          "firstName": "John",
          "lastName": "Doe",
          "roles": ["USER"],
          "isAdmin": false
        }
      }
      ```

- **Access denied - Admin role required:**
    - **Status:** 403 FORBIDDEN
    - **Body:**
      ```json
      { "success": false, "message": "Access denied" }
      ```

- **User ID is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "User ID is required" }
      ```

- **Action is required:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Action is required (GRANT or REVOKE)" }
      ```

- **Invalid action:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Action must be either GRANT or REVOKE" }
      ```

- **User already has admin privileges:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "User already has admin privileges" }
      ```

- **User does not have admin privileges:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "User does not have admin privileges" }
      ```

- **User not found:**
    - **Status:** 404 NOT FOUND
    - **Body:**
      ```json
      { "success": false, "message": "User not found" }
      ```

- **User not authenticated:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

- **Internal server error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "Internal server error" }
      ```

---

## Common Error Responses

### Rate Limiting
- **Too many requests:**
    - **Status:** 429 TOO MANY REQUESTS
    - **Body:**
      ```json
      { "success": false, "message": "Too many requests from your IP. Try again later" }
      ```

### JWT Token Errors
- **JWT token has expired:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "JWT token has expired" }
      ```

- **Authentication failed:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "message": "Authentication failed" }
      ```

### Method Not Allowed
- **Request method not supported:**
    - **Status:** 405 METHOD NOT ALLOWED
    - **Body:**
      ```json
      { "success": false, "message": "Request method 'POST' is not supported. Supported methods: GET, PUT" }
      ```

### Validation Errors
- **Validation error:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "Field validation errors" }
      ```

### Internal Server Errors
- **Previous password mismatch:**
    - **Status:** 401 UNAUTHORIZED
    - **Body:**
      ```json
      { "success": false, "message": "Previous password did not match" }
      ```

- **New password same as old:**
    - **Status:** 400 BAD REQUEST
    - **Body:**
      ```json
      { "success": false, "message": "New password cannot be same as your previous password" }
      ```

- **Runtime exception:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An internal error occurred" }
      ```

- **Unexpected error:**
    - **Status:** 500 INTERNAL SERVER ERROR
    - **Body:**
      ```json
      { "success": false, "message": "An unexpected error occurred" }
      ```

---

## Authentication Requirements

### Public Endpoints (No Authentication Required)
- `POST /api/v1/auth/signup`
- `POST /api/v1/auth/login`
- `GET /api/v1/auth/refresh`
- `GET /api/v1/auth/token-verification`
- `POST /api/v1/auth/forget-password/code`
- `POST /api/v1/auth/forget-password/verify-and-reset`
- `GET /api/v1/public/health-check`

### User Endpoints (JWT Token Required)
- `GET /api/v1/user/info`
- `GET /api/v1/user/profile`
- `DELETE /api/v1/user/delete`
 - `PATCH /api/v1/user/update/name`
 - `PATCH /api/v1/user/update/password`
- `POST /api/v1/auth/email-verification/code`
- `POST /api/v1/auth/email-verification/verify`
- `POST /api/v1/content/course-path/generate`
  - Asynchronous request; returns 202 Accepted or 200 OK with a message only. No course data is returned.
- `GET /api/v1/content/course-path/mine`
- `POST /api/v1/content/course-path/enroll`
 - `GET /api/v1/content/course-path/progress/{coursePathId}`
 - `GET /api/v1/content/course-path/search`
- `POST /api/v1/content/course-path/review`
- `GET /api/v1/content/course-path/{coursePathId}`
- `GET /api/v1/content/course-path/topic/{topicId}`
- `POST /api/v1/content/course-path/progress/toggle-topic-status`

### Admin Endpoints (JWT Token + Admin Role Required)
- `GET /api/v1/admin/users`
- `GET /api/v1/admin/course-paths`
- `DELETE /api/v1/admin/user/delete`
- `DELETE /api/v1/admin/course-path/delete`
 - `GET /api/v1/admin/users/search`
- `POST /api/v1/admin/users/manage-admin-privilege`

---

## Notes

1. **JWT Token Format**: All authenticated requests must include the JWT token in the Authorization header:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

2. **Rate Limiting**: All API endpoints under `/api/` are rate-limited to 30 requests per minute per IP address.

3. **Email Validation**: The system enforces educational email addresses for user registration.

4. **Password Requirements**: Passwords must be between 8 and 50 characters long.

5. **Admin Roles**: Admin endpoints require the user to have both valid authentication and the "ADMIN" role.

6. **Data Persistence**: Course generation with authentication persists data and creates progress tracking, while the no-auth version only returns generated content.

7. **Error Handling**: The system includes comprehensive error handling with specific error messages for different failure scenarios.

---

## API Endpoints Summary

### Authentication Endpoints
- `POST /api/v1/auth/signup` - User registration with email verification
- `POST /api/v1/auth/email-verification/code` - Request email verification code
- `POST /api/v1/auth/email-verification/verify` - Email verification with code
- `POST /api/v1/auth/login` - User login with JWT token generation
- `GET /api/v1/auth/refresh` - Refresh expired JWT tokens
- `GET /api/v1/auth/token-verification` - Verify JWT token validity
- `POST /api/v1/auth/forget-password/code` - Request password reset code
- `POST /api/v1/auth/forget-password/verify-and-reset` - Reset password with verification code

### User Management Endpoints
- `GET /api/v1/user/info` - Get authenticated user information
- `GET /api/v1/user/profile` - Get extended user profile with course lists
- `DELETE /api/v1/user/delete` - Delete user account
- `PATCH /api/v1/user/update/name` - Update user name
- `PATCH /api/v1/user/update/password` - Update user password

### Course Path Management Endpoints
- `POST /api/v1/content/course-path/generate` - Generate course path (authenticated)
- `GET /api/v1/content/course-path/mine` - Get user's created and enrolled course paths
- `POST /api/v1/content/course-path/enroll` - Enroll in course path
- `GET /api/v1/content/course-path/progress/{coursePathId}` - Get user progress
- ~~`POST /api/v1/content/course-path/generate/check-duplicate` - Generate with duplicate check~~ **⚠️ EXPERIMENTAL - DISABLED**
- `GET /api/v1/content/course-path/search` - Search course paths
- `POST /api/v1/content/course-path/review` - Add review to course path
- `GET /api/v1/content/course-path/{coursePathId}` - Get course path by ID

### Topic Management Endpoints
- `GET /api/v1/content/course-path/topic/{topicId}` - Get topic details
- `POST /api/v1/content/course-path/progress/toggle-topic-status` - Toggle topic completion status

### Admin Management Endpoints
- `GET /api/v1/admin/users` - Get all users (admin only)
- `GET /api/v1/admin/course-paths` - Get all course paths (admin only)
- `DELETE /api/v1/admin/course-path/delete` - Delete course path (admin only)
- `DELETE /api/v1/admin/user/delete` - Delete user (admin only)
- `GET /api/v1/admin/users/search` - Search users by email (admin only)
- `POST /api/v1/admin/users/manage-admin-privilege` - Grant/revoke admin privileges

### Content Management Notes
- All endpoints require JWT authentication except for password reset request
- Admin endpoints require both authentication and ADMIN role
- Rate limiting applies to all `/api/` endpoints (30 requests/minute per IP)
- Course path generation request is asynchronous and does not persist or return data in this call
