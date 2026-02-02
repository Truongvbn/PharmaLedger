-- =====================================================
-- PharmaLedger Auto Alert - Database Schema
-- SQL Server Script
-- =====================================================

USE master;
GO

-- Create database
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'PharmaLedger')
BEGIN
    CREATE DATABASE PharmaLedger;
END
GO

USE PharmaLedger;
GO

-- =====================================================
-- CORE TABLES
-- =====================================================

-- Users table
CREATE TABLE Users (
    userId INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    fullname NVARCHAR(100) NOT NULL,
    email NVARCHAR(100),
    phone NVARCHAR(20),
    roleId NVARCHAR(10) NOT NULL, -- AD, PH (Pharmacist), AU (Auditor)
    isActive BIT DEFAULT 1,
    createdAt DATETIME DEFAULT GETDATE(),
    updatedAt DATETIME DEFAULT GETDATE()
);

-- Drugs table
CREATE TABLE Drugs (
    drugId INT IDENTITY(1,1) PRIMARY KEY,
    drugCode NVARCHAR(50) NOT NULL UNIQUE,
    drugName NVARCHAR(200) NOT NULL,
    activeIngredient NVARCHAR(200),
    dosageForm NVARCHAR(100), -- Tablet, Capsule, Injection, etc.
    strength NVARCHAR(50),
    unit NVARCHAR(50) NOT NULL, -- Box, Bottle, Vial, etc.
    minStock INT DEFAULT 0,
    description NVARCHAR(500),
    isActive BIT DEFAULT 1,
    createdAt DATETIME DEFAULT GETDATE(),
    updatedAt DATETIME DEFAULT GETDATE()
);

-- Suppliers table
CREATE TABLE Suppliers (
    supplierId INT IDENTITY(1,1) PRIMARY KEY,
    supplierCode NVARCHAR(50) NOT NULL UNIQUE,
    supplierName NVARCHAR(200) NOT NULL,
    contactPerson NVARCHAR(100),
    phone NVARCHAR(20),
    email NVARCHAR(100),
    address NVARCHAR(500),
    taxCode NVARCHAR(50),
    contractPrice DECIMAL(18,2),
    isActive BIT DEFAULT 1,
    createdAt DATETIME DEFAULT GETDATE(),
    updatedAt DATETIME DEFAULT GETDATE()
);

-- InventoryLocations table
CREATE TABLE InventoryLocations (
    locationId INT IDENTITY(1,1) PRIMARY KEY,
    locationCode NVARCHAR(50) NOT NULL UNIQUE,
    locationName NVARCHAR(100) NOT NULL,
    locationType NVARCHAR(50), -- Warehouse, Cabinet, Shelf
    description NVARCHAR(200),
    isActive BIT DEFAULT 1,
    createdAt DATETIME DEFAULT GETDATE()
);

-- Batches table
CREATE TABLE Batches (
    batchId INT IDENTITY(1,1) PRIMARY KEY,
    drugId INT NOT NULL,
    batchNumber NVARCHAR(100) NOT NULL,
    manufactureDate DATE,
    expiryDate DATE NOT NULL,
    initialQuantity INT NOT NULL,
    currentQuantity INT NOT NULL DEFAULT 0,
    unitPrice DECIMAL(18,2),
    createdAt DATETIME DEFAULT GETDATE(),
    updatedAt DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Batches_Drugs FOREIGN KEY (drugId) REFERENCES Drugs(drugId),
    CONSTRAINT UQ_Batch_Drug UNIQUE (drugId, batchNumber)
);

-- InventoryBalances table
CREATE TABLE InventoryBalances (
    balanceId INT IDENTITY(1,1) PRIMARY KEY,
    batchId INT NOT NULL,
    locationId INT NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    updatedAt DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_InventoryBalances_Batches FOREIGN KEY (batchId) REFERENCES Batches(batchId),
    CONSTRAINT FK_InventoryBalances_Locations FOREIGN KEY (locationId) REFERENCES InventoryLocations(locationId),
    CONSTRAINT UQ_Batch_Location UNIQUE (batchId, locationId)
);

-- =====================================================
-- INBOUND/OUTBOUND ORDERS
-- =====================================================

-- InboundOrders table
CREATE TABLE InboundOrders (
    orderId INT IDENTITY(1,1) PRIMARY KEY,
    orderCode NVARCHAR(50) NOT NULL UNIQUE,
    supplierId INT NOT NULL,
    orderDate DATETIME NOT NULL DEFAULT GETDATE(),
    receivedDate DATETIME,
    status NVARCHAR(20) DEFAULT 'PENDING', -- PENDING, APPROVED, RECEIVED, CANCELLED
    totalAmount DECIMAL(18,2),
    notes NVARCHAR(500),
    createdBy INT NOT NULL,
    approvedBy INT,
    createdAt DATETIME DEFAULT GETDATE(),
    updatedAt DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_InboundOrders_Suppliers FOREIGN KEY (supplierId) REFERENCES Suppliers(supplierId),
    CONSTRAINT FK_InboundOrders_CreatedBy FOREIGN KEY (createdBy) REFERENCES Users(userId),
    CONSTRAINT FK_InboundOrders_ApprovedBy FOREIGN KEY (approvedBy) REFERENCES Users(userId)
);

-- InboundLines table
CREATE TABLE InboundLines (
    lineId INT IDENTITY(1,1) PRIMARY KEY,
    orderId INT NOT NULL,
    batchId INT NOT NULL,
    quantity INT NOT NULL,
    unitPrice DECIMAL(18,2),
    totalPrice DECIMAL(18,2),
    CONSTRAINT FK_InboundLines_Orders FOREIGN KEY (orderId) REFERENCES InboundOrders(orderId),
    CONSTRAINT FK_InboundLines_Batches FOREIGN KEY (batchId) REFERENCES Batches(batchId)
);

-- OutboundOrders table
CREATE TABLE OutboundOrders (
    orderId INT IDENTITY(1,1) PRIMARY KEY,
    orderCode NVARCHAR(50) NOT NULL UNIQUE,
    orderDate DATETIME NOT NULL DEFAULT GETDATE(),
    purpose NVARCHAR(200), -- Dispensing, Transfer, Expired disposal
    destinationDept NVARCHAR(100),
    status NVARCHAR(20) DEFAULT 'PENDING', -- PENDING, APPROVED, COMPLETED, CANCELLED
    notes NVARCHAR(500),
    createdBy INT NOT NULL,
    approvedBy INT,
    createdAt DATETIME DEFAULT GETDATE(),
    updatedAt DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_OutboundOrders_CreatedBy FOREIGN KEY (createdBy) REFERENCES Users(userId),
    CONSTRAINT FK_OutboundOrders_ApprovedBy FOREIGN KEY (approvedBy) REFERENCES Users(userId)
);

-- OutboundLines table
CREATE TABLE OutboundLines (
    lineId INT IDENTITY(1,1) PRIMARY KEY,
    orderId INT NOT NULL,
    batchId INT NOT NULL,
    locationId INT NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT FK_OutboundLines_Orders FOREIGN KEY (orderId) REFERENCES OutboundOrders(orderId),
    CONSTRAINT FK_OutboundLines_Batches FOREIGN KEY (batchId) REFERENCES Batches(batchId),
    CONSTRAINT FK_OutboundLines_Locations FOREIGN KEY (locationId) REFERENCES InventoryLocations(locationId)
);

-- =====================================================
-- STOCK LEDGER (Audit Trail)
-- =====================================================

-- StockLedger table
CREATE TABLE StockLedger (
    ledgerId INT IDENTITY(1,1) PRIMARY KEY,
    drugId INT NOT NULL,
    batchId INT,
    locationId INT,
    transactionType NVARCHAR(20) NOT NULL, -- INBOUND, OUTBOUND, ADJUSTMENT, TRANSFER, EXPIRED
    quantity INT NOT NULL, -- Positive for IN, Negative for OUT
    balanceBefore INT,
    balanceAfter INT,
    referenceType NVARCHAR(50), -- InboundOrder, OutboundOrder, Adjustment
    referenceId INT,
    reason NVARCHAR(200),
    userId INT NOT NULL,
    transactionDate DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_StockLedger_Drugs FOREIGN KEY (drugId) REFERENCES Drugs(drugId),
    CONSTRAINT FK_StockLedger_Batches FOREIGN KEY (batchId) REFERENCES Batches(batchId),
    CONSTRAINT FK_StockLedger_Locations FOREIGN KEY (locationId) REFERENCES InventoryLocations(locationId),
    CONSTRAINT FK_StockLedger_Users FOREIGN KEY (userId) REFERENCES Users(userId)
);

-- =====================================================
-- ALERT SYSTEM
-- =====================================================

-- AlertRules table
CREATE TABLE AlertRules (
    ruleId INT IDENTITY(1,1) PRIMARY KEY,
    ruleName NVARCHAR(100) NOT NULL,
    ruleType NVARCHAR(50) NOT NULL, -- LOW_STOCK, NEAR_EXPIRY, ANOMALY_CONSUMPTION, ANOMALY_ADJUSTMENT
    threshold INT, -- e.g., days before expiry, min stock level
    thresholdUnit NVARCHAR(20), -- DAYS, QUANTITY, PERCENTAGE
    severity NVARCHAR(20) DEFAULT 'MEDIUM', -- LOW, MEDIUM, HIGH, CRITICAL
    isActive BIT DEFAULT 1,
    createdAt DATETIME DEFAULT GETDATE(),
    updatedAt DATETIME DEFAULT GETDATE()
);

-- AlertEvents table
CREATE TABLE AlertEvents (
    eventId INT IDENTITY(1,1) PRIMARY KEY,
    ruleId INT NOT NULL,
    drugId INT,
    batchId INT,
    alertType NVARCHAR(50) NOT NULL,
    severity NVARCHAR(20) NOT NULL,
    title NVARCHAR(200) NOT NULL,
    description NVARCHAR(500),
    status NVARCHAR(20) DEFAULT 'OPEN', -- OPEN, ACKNOWLEDGED, RESOLVED, CLOSED
    triggeredAt DATETIME DEFAULT GETDATE(),
    acknowledgedAt DATETIME,
    resolvedAt DATETIME,
    acknowledgedBy INT,
    CONSTRAINT FK_AlertEvents_Rules FOREIGN KEY (ruleId) REFERENCES AlertRules(ruleId),
    CONSTRAINT FK_AlertEvents_Drugs FOREIGN KEY (drugId) REFERENCES Drugs(drugId),
    CONSTRAINT FK_AlertEvents_Batches FOREIGN KEY (batchId) REFERENCES Batches(batchId),
    CONSTRAINT FK_AlertEvents_AcknowledgedBy FOREIGN KEY (acknowledgedBy) REFERENCES Users(userId)
);

-- CaseTickets table
CREATE TABLE CaseTickets (
    caseId INT IDENTITY(1,1) PRIMARY KEY,
    eventId INT NOT NULL UNIQUE,
    caseCode NVARCHAR(50) NOT NULL UNIQUE,
    assigneeId INT,
    status NVARCHAR(20) DEFAULT 'OPEN', -- OPEN, IN_PROGRESS, RESOLVED, CLOSED
    priority NVARCHAR(20) DEFAULT 'MEDIUM',
    resolution NVARCHAR(500),
    evidence NVARCHAR(500), -- File path or description
    createdAt DATETIME DEFAULT GETDATE(),
    updatedAt DATETIME DEFAULT GETDATE(),
    closedAt DATETIME,
    closedBy INT,
    CONSTRAINT FK_CaseTickets_Events FOREIGN KEY (eventId) REFERENCES AlertEvents(eventId),
    CONSTRAINT FK_CaseTickets_Assignee FOREIGN KEY (assigneeId) REFERENCES Users(userId),
    CONSTRAINT FK_CaseTickets_ClosedBy FOREIGN KEY (closedBy) REFERENCES Users(userId)
);

-- =====================================================
-- NOTIFICATION SYSTEM
-- =====================================================

-- NotificationChannels table
CREATE TABLE NotificationChannels (
    channelId INT IDENTITY(1,1) PRIMARY KEY,
    userId INT NOT NULL,
    channelType NVARCHAR(20) NOT NULL, -- EMAIL, TELEGRAM, SMS
    channelAddress NVARCHAR(200) NOT NULL, -- email address, telegram chat id, phone
    isActive BIT DEFAULT 1,
    createdAt DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_NotificationChannels_Users FOREIGN KEY (userId) REFERENCES Users(userId)
);

-- Notifications table (for AlertEvents)
CREATE TABLE Notifications (
    notificationId INT IDENTITY(1,1) PRIMARY KEY,
    eventId INT NOT NULL,
    channelId INT NOT NULL,
    message NVARCHAR(500),
    status NVARCHAR(20) DEFAULT 'PENDING', -- PENDING, SENT, FAILED
    sentAt DATETIME,
    errorMessage NVARCHAR(200),
    CONSTRAINT FK_Notifications_Events FOREIGN KEY (eventId) REFERENCES AlertEvents(eventId),
    CONSTRAINT FK_Notifications_Channels FOREIGN KEY (channelId) REFERENCES NotificationChannels(channelId)
);

-- =====================================================
-- AI FORECAST SYSTEM
-- =====================================================

-- ForecastSeries table
CREATE TABLE ForecastSeries (
    seriesId INT IDENTITY(1,1) PRIMARY KEY,
    drugId INT NOT NULL,
    forecastType NVARCHAR(50) DEFAULT 'WEEKLY', -- DAILY, WEEKLY
    modelName NVARCHAR(100), -- TFT, Prophet, etc.
    horizonDays INT DEFAULT 7,
    createdAt DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_ForecastSeries_Drugs FOREIGN KEY (drugId) REFERENCES Drugs(drugId)
);

-- ForecastPoints table
CREATE TABLE ForecastPoints (
    pointId INT IDENTITY(1,1) PRIMARY KEY,
    seriesId INT NOT NULL,
    forecastDate DATE NOT NULL,
    predictedQuantity DECIMAL(10,2) NOT NULL,
    lowerBound DECIMAL(10,2),
    upperBound DECIMAL(10,2),
    confidence DECIMAL(5,2),
    CONSTRAINT FK_ForecastPoints_Series FOREIGN KEY (seriesId) REFERENCES ForecastSeries(seriesId)
);

-- =====================================================
-- BLOCKCHAIN ANCHORS (Audit Hash)
-- =====================================================

-- BlockchainAnchors table
CREATE TABLE BlockchainAnchors (
    anchorId INT IDENTITY(1,1) PRIMARY KEY,
    referenceType NVARCHAR(50) NOT NULL, -- StockLedger, AlertEvent
    referenceId INT NOT NULL,
    dataHash NVARCHAR(256) NOT NULL,
    txHash NVARCHAR(256),
    blockNumber BIGINT,
    networkName NVARCHAR(50),
    status NVARCHAR(20) DEFAULT 'PENDING', -- PENDING, CONFIRMED, FAILED
    anchoredAt DATETIME DEFAULT GETDATE(),
    confirmedAt DATETIME
);

-- =====================================================
-- INDEXES FOR PERFORMANCE
-- =====================================================

-- Drug search indexes
CREATE INDEX IX_Drugs_DrugCode ON Drugs(drugCode);
CREATE INDEX IX_Drugs_DrugName ON Drugs(drugName);
CREATE INDEX IX_Drugs_ActiveIngredient ON Drugs(activeIngredient);

-- Batch expiry search
CREATE INDEX IX_Batches_ExpiryDate ON Batches(expiryDate);
CREATE INDEX IX_Batches_DrugId ON Batches(drugId);

-- Inventory search
CREATE INDEX IX_InventoryBalances_BatchId ON InventoryBalances(batchId);
CREATE INDEX IX_InventoryBalances_LocationId ON InventoryBalances(locationId);

-- Ledger search
CREATE INDEX IX_StockLedger_DrugId ON StockLedger(drugId);
CREATE INDEX IX_StockLedger_TransactionDate ON StockLedger(transactionDate);
CREATE INDEX IX_StockLedger_UserId ON StockLedger(userId);
CREATE INDEX IX_StockLedger_TransactionType ON StockLedger(transactionType);

-- Alert search
CREATE INDEX IX_AlertEvents_Status ON AlertEvents(status);
CREATE INDEX IX_AlertEvents_DrugId ON AlertEvents(drugId);
CREATE INDEX IX_AlertEvents_TriggeredAt ON AlertEvents(triggeredAt);

-- =====================================================
-- DEFAULT DATA
-- =====================================================

-- Default admin user (password: admin123 - should be hashed in production)
INSERT INTO Users (username, password, fullname, email, roleId)
VALUES ('admin', 'admin123', N'System Administrator', 'admin@pharmaledger.com', 'AD');

-- Default pharmacist
INSERT INTO Users (username, password, fullname, email, roleId)
VALUES ('pharmacist', 'pharma123', N'Default Pharmacist', 'pharmacist@pharmaledger.com', 'PH');

-- Default auditor
INSERT INTO Users (username, password, fullname, email, roleId)
VALUES ('auditor', 'audit123', N'Default Auditor', 'auditor@pharmaledger.com', 'AU');

-- Default locations
INSERT INTO InventoryLocations (locationCode, locationName, locationType)
VALUES 
    ('WH-MAIN', N'Kho chính', 'Warehouse'),
    ('WH-COLD', N'Kho lạnh', 'Warehouse'),
    ('CAB-01', N'Tủ thuốc 01', 'Cabinet'),
    ('CAB-02', N'Tủ thuốc 02', 'Cabinet');

-- Default alert rules
INSERT INTO AlertRules (ruleName, ruleType, threshold, thresholdUnit, severity)
VALUES 
    (N'Cảnh báo hết hàng', 'LOW_STOCK', 10, 'QUANTITY', 'HIGH'),
    (N'Cảnh báo cận hạn 30 ngày', 'NEAR_EXPIRY', 30, 'DAYS', 'MEDIUM'),
    (N'Cảnh báo cận hạn 7 ngày', 'NEAR_EXPIRY', 7, 'DAYS', 'CRITICAL'),
    (N'Bất thường tiêu thụ', 'ANOMALY_CONSUMPTION', 200, 'PERCENTAGE', 'HIGH'),
    (N'Bất thường điều chỉnh tồn', 'ANOMALY_ADJUSTMENT', 50, 'PERCENTAGE', 'HIGH');

GO
PRINT 'PharmaLedger database schema created successfully!';
GO
