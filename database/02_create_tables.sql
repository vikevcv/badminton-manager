USE BadmintonManagement;



CREATE TABLE Account (
    accountId INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR (50) NOT NULL,
    role ENUM('ADMIN', 'MEMBER') DEFAULT 'MEMBER'
);

CREATE TABLE Customer (
    customerId INT AUTO_INCREMENT PRIMARY KEY,
    customerName VARCHAR(50) NOT NULL,
    type ENUM('SINHVIEN', 'NGUOILON') DEFAULT 'NGUOILON',
    phone VARCHAR(20),
    accountId INT UNIQUE NULL, 
    CONSTRAINT fk_customer_account 
        FOREIGN KEY (accountId) REFERENCES Account(accountId)
);



CREATE TABLE Court (
    courtId INT AUTO_INCREMENT PRIMARY KEY,
    courtName VARCHAR(10) NOT NULL,
    pricePerHour DECIMAL(10,2),
    status ENUM('TRONG', 'DA_DAT', 'BAO_TRI') DEFAULT 'TRONG'
);


CREATE TABLE Booking (
    bookingId INT AUTO_INCREMENT PRIMARY KEY,
    customerId INT NOT NULL,
    courtId INT NOT NULL,
    totalPrice DECIMAL(10,2),
    startTime TIME NOT NULL,
    endTime TIME NOT NULL,
    bookingDate DATE NOT NULL,
    CONSTRAINT fk_booking_customer FOREIGN KEY (customerId) REFERENCES Customer(customerId),
    CONSTRAINT fk_booking_courId FOREIGN KEY (courtId) REFERENCES Court(courtId)
);