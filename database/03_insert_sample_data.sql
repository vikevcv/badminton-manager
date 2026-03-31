USE BadmintonManagement;


INSERT INTO Account (username, password, role) VALUES 
('admin', '123456', 'ADMIN'),
('user1', '123456', 'MEMBER'),
('user2', '123456', 'MEMBER'),
('user3', '123456', 'MEMBER'),
('user4', '123456', 'MEMBER'),
('user5', '123456', 'MEMBER'),
('user6', '123456', 'MEMBER'),
('user7', '123456', 'MEMBER'),
('user8', '123456', 'MEMBER'),
('user9', '123456', 'MEMBER'),
('user10', '123456', 'MEMBER');

INSERT INTO Customer (customerName, type, phone, accountId) VALUES
('Nguyễn Văn An', 'SINHVIEN', '0900000001',2),
('Trần Văn Bảo', 'NGUOILON', '0900000002',3),
('Lê Văn Chung', 'SINHVIEN', '0900000003',4),
('Phạm Văn Duy', 'NGUOILON', '0900000004',5),
('Hoàng Văn Nghĩa', 'SINHVIEN', '0900000005',6),
('Võ Văn Minh', 'NGUOILON', '0900000006',7),
('Đặng Văn Hòa', 'SINHVIEN', '0900000007',8),
('Bùi Văn Hoàng', 'NGUOILON', '0900000008',9),
('Đỗ Văn Hiếu', 'SINHVIEN', '0900000009',10),
('Hồ Văn Khanh', 'NGUOILON', '0900000010',11);


INSERT INTO Court (courtName, pricePerHour, status) VALUES
('Sân 1', 100000, 'TRONG'),
('Sân 2', 120000, 'TRONG'),
('Sân 3', 90000, 'TRONG'),
('Sân 4', 110000, 'TRONG'),
('Sân 5', 100000, 'BAO_TRI'),
('Sân 6', 130000, 'TRONG');

INSERT INTO Booking (customerId, courtId, totalPrice, startTime, endTime, bookingDate) VALUES
(1, 1, 200000, '08:00:00', '10:00:00', '2026-03-28'),
(2, 2, 240000, '09:00:00', '11:00:00', '2026-03-28'),
(3, 3, 180000, '10:00:00', '12:00:00', '2026-03-28'),
(4, 4, 220000, '13:00:00', '15:00:00', '2026-03-28'),
(5, 6, 260000, '16:00:00', '18:00:00', '2026-03-28');

