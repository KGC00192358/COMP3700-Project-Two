PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE Customers(
mCustomerID int PRIMARY KEY,
mName String,
mPhone String,
mAddress String
);
INSERT INTO Customers VALUES(1,'Barry',3345555555,'Auburn');
INSERT INTO Customers VALUES(2,'Karen','334-785-5554','Auburn');
INSERT INTO Customers VALUES(3,'Mike','333-444-6666','Auburn');
INSERT INTO Customers VALUES(4,'Bykal','333-444-6667','Auburn');
INSERT INTO Customers VALUES(5,'June','333-444-6867','Auburn');
CREATE TABLE Products (
mProductID int PRIMARY KEY,
mName String,
mVendor String,
mDesc String,
mPrice Double,
mQuantity Double
);
INSERT INTO Products VALUES(1,'Paper','GP','Paper',10.990000000000000213,100.0);
INSERT INTO Products VALUES(2,'Pencils','Ticon','Number 2',0.98999999999999999111,1000.0);
INSERT INTO Products VALUES(3,'Pens','Ticon','Gel',1.9899999999999999911,1000.0);
INSERT INTO Products VALUES(4,'Eraser','Ticon','For big mistakes',1.1200000000000001065,10000.0);
INSERT INTO Products VALUES(5,'Eraser','Ticon','For small mistakes',0.68000000000000004884,10000.0);
COMMIT;
