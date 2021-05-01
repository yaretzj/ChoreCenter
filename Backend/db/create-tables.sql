CREATE TABLE Parents (
    Name VARCHAR(255),
    Email VARCHAR(255),
    GoogleTokenId VARCHAR(2048),
    GoogleAccountId VARCHAR(2048),
    ParentCode uniqueidentifier NOT NULL DEFAULT newid(),
    PRIMARY KEY (GoogleAccountId)
);

CREATE TABLE Children (
    Name VARCHAR(255),
    Email VARCHAR(255),
    ParentGoogleAccountId VARCHAR(2048),
    GoogleTokenId VARCHAR(2048),
    GoogleAccountId VARCHAR(2048),
    Points INT DEFAULT 0,
    PRIMARY KEY (GoogleAccountId),
    FOREIGN KEY (ParentGoogleAccountId) REFERENCES Parents(GoogleAccountId)
);

CREATE TABLE Chores (
    ChoreId uniqueidentifier NOT NULL DEFAULT newid() PRIMARY KEY,
    ParentGoogleAccountId VARCHAR(2048),
    Name VARCHAR(255),
    Description VARCHAR(1023),
    Status VARCHAR(15),
    AssignedTo VARCHAR(2048),
    Points INT,
    CreatedTime DATETIME DEFAULT GETDATE(),
    AcceptedTime DATETIME,
    CompletedTime DATETIME,
    VerifiedTime DATETIME,
    LastUpdateTime DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (ParentGoogleAccountId) REFERENCES Parents(GoogleAccountId),
    FOREIGN KEY (AssignedTo) REFERENCES Children(GoogleAccountId)
);

CREATE TABLE Rewards (
    RewardId uniqueidentifier NOT NULL DEFAULT newid() PRIMARY KEY,
    ParentGoogleAccountId VARCHAR(2048),
    Name VARCHAR(255),
    Description VARCHAR(1023),
    Points INT,
    CreatedTime DATETIME DEFAULT GETDATE(),
    LastUpdateTime DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (ParentGoogleAccountId) REFERENCES Parents(GoogleAccountId)
);

CREATE TABLE RewardRedemptionHistory (
    RewardId uniqueidentifier,
    ChildGoogleAccountId VARCHAR(2048),
    ParentGoogleAccountId VARCHAR(2048),
    RedeemedTime DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (ParentGoogleAccountId) REFERENCES Parents(GoogleAccountId),
    FOREIGN KEY (ChildGoogleAccountId) REFERENCES Children(GoogleAccountId),
    FOREIGN KEY (RewardId) REFERENCES Rewards(RewardId),
);

CREATE TRIGGER tgr_lastupdatetime_chore
ON Chores
AFTER UPDATE AS
  UPDATE Chores
  SET LastUpdateTime = GETDATE()
  WHERE ChoreId IN (SELECT DISTINCT ChoreId FROM Inserted);

CREATE TRIGGER tgr_lastupdatetime_reward
ON Rewards
AFTER UPDATE AS
  UPDATE Rewards
  SET LastUpdateTime = GETDATE()
  WHERE RewardId IN (SELECT DISTINCT RewardId FROM Inserted);