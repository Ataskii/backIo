DROP SCHEMA IF EXISTS  backio;
CREATE SCHEMA IF NOT EXISTS `backio` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `backio` ;
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------
-- Table `backio`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS `backio`.`users` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `first_name`    VARCHAR(50) NOT NULL,
    `last_name`    VARCHAR(50) NOT NULL,
    `email`        VARCHAR(100) NOT NULL,
    `password`     VARCHAR(255) NOT NULL,
    `address`      VARCHAR(255) NULL DEFAULT NULL,
    `phone`        VARCHAR(30) NULL DEFAULT NULL,
    `title`        VARCHAR(50) NULL DEFAULT NULL,
    `bio`          VARCHAR(255) NULL DEFAULT NULL,
    `enabled`      TINYINT(1) NULL DEFAULT '0',
    `non_Locked`   TINYINT(1) NULL DEFAULT '1',
    `using_mfa`    TINYINT(1) NULL DEFAULT '0',
    `created_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    `image_Url`    VARCHAR(255) NULL DEFAULT 'https://cdn-icons-png.flaticon.com/512/149/149071.png',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `UQ_Users_Email` (`email` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;



DROP TABLE IF EXISTS Events;
CREATE TABLE Events
(
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL CHECK ( type IN ('LOGIN_ATTEMPT', 'LOGIN_ATTEMPT_FAILURE', 'LOGIN_ATTEMPT_SUCCESS', 'PROFILE_UPDATE', 'PROFILE_PICTURE_UPDATE', 'ROLE_UPDATE', 'ACCOUNT_SETTINGS_UPDATE', 'PASSWORD_UPDATE', 'MFA_UPDATE')),
    description VARCHAR(255) NOT NULL,
    CONSTRAINT UQ_Events_Type UNIQUE (type)
);

DROP TABLE IF EXISTS UserEvents;

CREATE TABLE UserEvents
(
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT UNSIGNED NOT NULL,
    event_id   BIGINT UNSIGNED NOT NULL,
    device     VARCHAR(100) DEFAULT NULL,
    ip_address VARCHAR(100) DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (event_id) REFERENCES Events (id) ON DELETE RESTRICT ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Table `backio`.`ResetPasswordaccountVarification`
-- -----------------------------------------------------
DROP TABLE IF EXISTS  ResetPasswordaccountVarification;
CREATE TABLE IF NOT EXISTS `backio`.`ResetPasswordaccountVarification` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `users_id` BIGINT UNSIGNED NOT NULL,
    `url` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `UQ_ResetPassword_userId` (`users_id` ASC) VISIBLE,
    UNIQUE INDEX `UQ_ResetPassword_url` (`url` ASC) VISIBLE,
    CONSTRAINT `resetpasswordaccountvarification_ibfk_1`
    FOREIGN KEY (`users_id`)
    REFERENCES `backio`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `backio`.`TwoFactorVarification`
-- -----------------------------------------------------
DROP TABLE IF EXISTS TwoFactorVarification;
CREATE TABLE IF NOT EXISTS `backio`.`TwoFactorVarification` (
                                                                      `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                                                      `users_id` BIGINT UNSIGNED NOT NULL,
                                                                      `code` VARCHAR(10) NOT NULL,
    `date` DATETIME NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `UQ_TwoFactor_userId` (`users_id` ASC) VISIBLE,
    UNIQUE INDEX `UQ_TwoFactor_code` (`code` ASC) VISIBLE,
    CONSTRAINT `twofactorvarification_ibfk_1`
    FOREIGN KEY (`users_id`)
    REFERENCES `backio`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `backio`.`accountVarification`
-- -----------------------------------------------------
DROP TABLE IF EXISTS accountVarification;
CREATE TABLE IF NOT EXISTS `backio`.`accountVarification` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `users_id` BIGINT UNSIGNED NOT NULL,
    `url` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `UQ_accountVarification_userId` (`users_id` ASC) VISIBLE,
    UNIQUE INDEX `UQ_accountVarification_url` (`url` ASC) VISIBLE,
    CONSTRAINT `accountvarification_ibfk_1`
    FOREIGN KEY (`users_id`)
    REFERENCES `backio`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `backio`.`events`
-- -----------------------------------------------------
DROP TABLE IF EXISTS events;
CREATE TABLE IF NOT EXISTS `backio`.`events` (
                                                       `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                                       `type` VARCHAR(45) NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `UQ_Events_Type` (`type` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `backio`.`roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS roles;
CREATE TABLE IF NOT EXISTS `backio`.`roles` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `permission` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `UQ_Roles_name` (`name` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `backio`.`userEvents`
-- -----------------------------------------------------
DROP TABLE IF EXISTS userEvents;
CREATE TABLE IF NOT EXISTS `backio`.`userEvents` (
                                                           `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                                           `users_id` BIGINT UNSIGNED NOT NULL,
                                                           `event_id` BIGINT UNSIGNED NOT NULL,
                                                           `device` VARCHAR(50) NULL DEFAULT NULL,
    `ip_address` VARCHAR(50) NULL DEFAULT NULL,
    `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `users_id` (`users_id` ASC) VISIBLE,
    INDEX `event_id` (`event_id`
                      ASC) VISIBLE,
    CONSTRAINT `userevents_ibfk_1`
    FOREIGN KEY (`users_id`)
    REFERENCES `backio`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT `userevents_ibfk_2`
    FOREIGN KEY (`event_id`)
    REFERENCES `backio`.`events` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `backio`.`userRoles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS userRoles;
CREATE TABLE IF NOT EXISTS `backio`.`userRoles` (
      `id`  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
      `users_id` BIGINT UNSIGNED NOT NULL,
      `roles_id` BIGINT UNSIGNED NOT NULL,
      PRIMARY KEY (`id`),
    UNIQUE INDEX `UQ_userRoles_UsersId` (`users_id` ASC) VISIBLE,
    UNIQUE INDEX `UQ_userRoles_RolesId` (`roles_id` ASC) VISIBLE,
    CONSTRAINT `userroles_ibfk_1`
    FOREIGN KEY (`users_id`)
    REFERENCES `backio`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT `userroles_ibfk_2`
    FOREIGN KEY (`roles_id`)
    REFERENCES `backio`.`roles` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO backio.Roles (name, permission)
VALUES  ('ROLE_USER', 'READ:USER, READ:CUSTOMER'),
        ('ROLE_MANAGER', 'READ:USER, READ:CUSTOMER, UPDATE:USER, UPDATE:CUSTOMER'),
        ('ROLE_ADMIN', 'READ:USER, READ:CUSTOMER, CREATE:USER, CREATE:CUSTOMER, UPDATE:USER, UPDATE:CUSTOMER'),
        ('ROLE_SYSADMIN', 'READ:USER, READ:CUSTOMER, CREATE:USER, CREATE:CUSTOMER, UPDATE:USER, UPDATE:CUSTOMER, DELETE:USER, DELETE:CUSTOMER');



SET FOREIGN_KEY_CHECKS = 1;
--
-- --
-- CREATE SCHEMA IF NOT EXISTS backio DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
--
-- USE backio;
-- SET foreign_key_checks = 0;
--
-- DROP TABLE IF EXISTS Users;
-- CREATE TABLE Users
-- (
--     id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
--     first_name VARCHAR(50) NOT NULL,
--     last_name  VARCHAR(50) NOT NULL,
--     email      VARCHAR(100) NOT NULL,
--     password   VARCHAR(255) DEFAULT NULL,
--     address    VARCHAR(255) DEFAULT NULL,
--     phone      VARCHAR(30) DEFAULT NULL,
--     title      VARCHAR(50) DEFAULT NULL,
--     bio        VARCHAR(255) DEFAULT NULL,
--     enabled    BOOLEAN DEFAULT FALSE,
--     non_locked BOOLEAN DEFAULT TRUE,
--     using_mfa  BOOLEAN DEFAULT FALSE,
--     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
--     image_url  VARCHAR(255) DEFAULT 'https://cdn-icons-png.flaticon.com/512/149/149071.png',
--     CONSTRAINT UQ_Users_Email UNIQUE (email)
-- );
--
-- DROP TABLE IF EXISTS Roles;
--
-- CREATE TABLE Roles
-- (
--     id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
--     name       VARCHAR(50) NOT NULL,
--     permission VARCHAR(255) NOT NULL,
--     CONSTRAINT UQ_Roles_Name UNIQUE (name)
-- );
--
-- DROP TABLE IF EXISTS UserRoles;
--
-- CREATE TABLE UserRoles
-- (
--     id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
--     user_id BIGINT UNSIGNED NOT NULL,
--     role_id BIGINT UNSIGNED NOT NULL,
--     FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
--     FOREIGN KEY (role_id) REFERENCES Roles (id) ON DELETE RESTRICT ON UPDATE CASCADE,
--     CONSTRAINT UQ_UserRoles_User_Id UNIQUE (user_id)
-- );
--
-- DROP TABLE IF EXISTS Events;
--
-- CREATE TABLE Events
-- (
--     id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
--     type        VARCHAR(50) NOT NULL CHECK(type IN ('LOGIN_ATTEMPT', 'LOGIN_ATTEMPT_FAILURE', 'LOGIN_ATTEMPT_SUCCESS', 'PROFILE_UPDATE', 'PROFILE_PICTURE_UPDATE', 'ROLE_UPDATE', 'ACCOUNT_SETTINGS_UPDATE', 'PASSWORD_UPDATE', 'MFA_UPDATE')),
--     description VARCHAR(255) NOT NULL,
--     CONSTRAINT UQ_Events_Type UNIQUE (type)
-- );
--
-- DROP TABLE IF EXISTS UserEvents;
--
-- CREATE TABLE UserEvents
-- (
--     id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
--     user_id    BIGINT UNSIGNED NOT NULL,
--     event_id   BIGINT UNSIGNED NOT NULL,
--     device     VARCHAR(100) DEFAULT NULL,
--     ip_address VARCHAR(100) DEFAULT NULL,
--     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
--     FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
--     FOREIGN KEY (event_id) REFERENCES Events (id) ON DELETE RESTRICT ON UPDATE CASCADE
-- );
--
-- DROP TABLE IF EXISTS AccountVerifications;
--
-- CREATE TABLE AccountVerifications
-- (
--     id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
--     user_id BIGINT UNSIGNED NOT NULL,
--     url     VARCHAR(255) NOT NULL,
--     -- date     DATETIME NOT NULL,
--     FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
--     CONSTRAINT UQ_AccountVerifications_User_Id UNIQUE (user_id),
--     CONSTRAINT UQ_AccountVerifications_Url UNIQUE (url)
-- );
--
-- DROP TABLE IF EXISTS ResetPasswordVerifications;
--
-- CREATE TABLE ResetPasswordVerifications
-- (
--     id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
--     user_id         BIGINT UNSIGNED NOT NULL,
--     url             VARCHAR(255) NOT NULL,
--     expiration_date DATETIME NOT NULL,
--     FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
--     CONSTRAINT UQ_ResetPasswordVerifications_User_Id UNIQUE (user_id),
--     CONSTRAINT UQ_ResetPasswordVerifications_Url UNIQUE (url)
-- );
--
-- DROP TABLE IF EXISTS TwoFactorVerifications;
--
-- CREATE TABLE TwoFactorVerifications
-- (
--     id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
--     user_id         BIGINT UNSIGNED NOT NULL,
--     code            VARCHAR(10) NOT NULL,
--     expiration_date DATETIME NOT NULL,
--     FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
--     CONSTRAINT UQ_TwoFactorVerifications_User_Id UNIQUE (user_id),
--     CONSTRAINT UQ_TwoFactorVerifications_Code UNIQUE (code)
-- );
-- INSERT INTO backio.Roles (name, permission)
-- VALUES  ('ROLE_USER', 'READ:USER, READ:CUSTOMER'),
--         ('ROLE_MANAGER', 'READ:USER, READ:CUSTOMER, UPDATE:USER, UPDATE:CUSTOMER'),
--         ('ROLE_ADMIN', 'READ:USER, READ:CUSTOMER, CREATE:USER, CREATE:CUSTOMER, UPDATE:USER, UPDATE:CUSTOMER'),
--         ('ROLE_SYSADMIN', 'READ:USER, READ:CUSTOMER, CREATE:USER, CREATE:CUSTOMER, UPDATE:USER, UPDATE:CUSTOMER, DELETE:USER, DELETE:CUSTOMER');
--
--
-- SET FOREIGN_KEY_CHECKS = 1;