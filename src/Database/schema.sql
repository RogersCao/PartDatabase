-- DROP TABLE Record;
-- DROP TABLE Part;
-- DROP TABLE Customer;
-- DROP TABLE Category;

CREATE TABLE IF NOT EXISTS Category ( CategoryID uuid default random_uuid() PRIMARY KEY,  Name VARCHAR(50) NOT NULL);

CREATE TABLE IF NOT EXISTS Customer ( CustomerID uuid default random_uuid() PRIMARY KEY,  Name VARCHAR(50) NOT NULL);

CREATE TABLE IF NOT EXISTS Part (
	PartID VARCHAR(50) NOT NULL PRIMARY KEY,
	ModelNum VARCHAR(50) NOT NULL,
	Name VARCHAR(50) NOT NULL,
	CategoryID uuid NOT NULL,
	Stock int NOT NULL,
	foreign key (CategoryID) references Category(CategoryID)
);

CREATE TABLE IF NOT EXISTS Record (
	RecordID uuid default random_uuid() PRIMARY KEY,
	Date DATE NOT NULL,
	CustomerID uuid NOT NULL,
	PartID VARCHAR(50) NOT NULL,
	QuantityIN int NOT NULL,
	QuantityOUT int NOT NULL,
	CurrentStock int NOT NULL,
	Remark VARCHAR(300),
	foreign key (CustomerID) references Customer(CustomerID),
	foreign key (PartID) references Part(PartID)
);