# BackQL Application

BackQL is a robust Java-based backend application that uses Spring Boot, SQL, and Maven. It provides a comprehensive suite of user management functionalities.

## Features

- **User Registration**: Allows new users to create an account.
- **User Login**: Authenticates users and allows them to access their accounts.
- **User Profile Management**: Enables users to view and update their profile information.
- **Role Management**: Administers user roles for access control.
- **Multi-factor Authentication**: Enhances security by requiring multiple forms of verification.
- **Password Reset**: Allows users to securely change their password.
- **Account Verification**: Verifies user accounts through email confirmation.

## Technologies Used

- **Java**: The main programming language used for development.
- **SQL**: Used for data storage and retrieval.
- **Spring Boot**: A Java-based framework used to create stand-alone, production-grade Spring based Applications.
- **Maven**: A build automation tool used primarily for Java projects.

## Security

Security is a primary focus of BackQL, and as such, several measures have been implemented to ensure the integrity and safety of the application:

- **Authentication**: The application uses secure authentication mechanisms to verify the identity of users.
- **Multi-factor Authentication (MFA)**: MFA provides an additional layer of security. Users have the option to enable MFA for their accounts.
- **Password Reset**: In case a user forgets their password, the application provides a secure way to reset it.
- **Data Encryption**: Sensitive data like passwords are encrypted before being stored in the database.

### Reporting a Vulnerability

If you discover a vulnerability in the BackQL application, please send an email to (mailto:fpala74@gmail.com). All security vulnerabilities will be promptly addressed.

## Setup and Installation

1. Ensure you have Java and Maven installed on your system.
2. Clone the repository to your local machine.
3. Navigate to the project directory and run `mvn clean install` to build the project.
4. Run the application using `mvn spring-boot:run`.

## Usage

The application exposes various RESTful endpoints for managing users and roles. Here are some examples:

- `/user/login`: POST request to authenticate a user.
- `/user/register`: POST request to register a new user.
- `/user/profile`: GET request to retrieve the profile of the authenticated user.
- `/user/update`: PUT request to update the details of the authenticated user.

Please refer to the source code and API documentation for more details on the available endpoints and their usage.

## Contributing

We welcome contributions from the community. If you wish to contribute:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Commit your changes.
4. Create a pull request detailing the changes you made.

## Contact

If you have any questions or feedback, please open an issue in the GitHub repository.
