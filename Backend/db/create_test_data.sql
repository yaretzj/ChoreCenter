DELETE FROM RewardRedemptionHistory WHERE ParentGoogleAccountId = 'TestParent';

DELETE FROM Rewards WHERE ParentGoogleAccountId = 'TestParent';

DELETE FROM Chores WHERE ParentGoogleAccountId = 'TestParent';

DELETE FROM Children WHERE ParentGoogleAccountId = 'TestParent';

DELETE FROM Parents WHERE GoogleAccountId = 'TestParent';

INSERT INTO Parents("Name", "Email", "GoogleTokenId", "GoogleAccountId", "ParentCode") VALUES ('Test Parent', 'chorecenter@gmail.com', 'TestParent', 'TestParent', 'd7abc322-e627-4b09-bd51-9da55599b7ea');

INSERT INTO Parents ("Name", "Email", "GoogleTokenId", "GoogleAccountId", "ParentCode") VALUES ("Chore Center", "chorecenter@gmail.com", "exp_token", "118319520083633732066", "d5f6c648-d4cf-4f0e-ae11-d1e61c")

INSERT INTO Children("Name", "Email", "ParentGoogleAccountId", "GoogleTokenId", "GoogleAccountId", "Points") VALUES ('Test Child', 'chorecenter@gmail.com', 'TestParent', 'TestChild', 'TestChild', 9999999)

INSERT INTO Chores("ChoreId", "ParentGoogleAccountId", "Name", "Description", "Status", "AssignedTo", "Points") VALUES ('42CA0914-2A14-4096-8931-A64494719FB6', 'TestParent', 'Sweep the Floor', 'Location: Kitchen', 'Created', null, 500);

INSERT INTO Chores("ChoreId", "ParentGoogleAccountId", "Name", "Description", "Status", "AssignedTo", "Points") VALUES ('D8342704-AF59-4034-B923-B22B4C98650F', 'TestParent', 'Wash Dishes', '', 'Created', null, 100);

INSERT INTO Chores("ChoreId", "ParentGoogleAccountId", "Name", "Description", "Status", "AssignedTo", "Points") VALUES ('BB9DA6E5-5C2A-44B5-AC83-5BDA5CA95FA8', 'TestParent', 'Walk the Dog', 'Minimum 15 minutes', 'Created', 'TestChild', 200);

INSERT INTO Chores("ChoreId", "ParentGoogleAccountId", "Name", "Description", "Status", "AssignedTo", "Points") VALUES ('B0168E35-31AA-4049-8821-6AD0CCA2F12E', 'TestParent', 'Practice the Piano', 'Memorize Fur Elise', 'Created', 'TestChild', 750);

INSERT INTO Chores("ChoreId", "ParentGoogleAccountId", "Name", "Description", "Status", "AssignedTo", "Points") VALUES ('4697217C-B0FE-4CA5-ACC2-10C5FC342C06', 'TestParent', 'Clean Bedroom', 'Full clean of your room', 'Completed', 'TestChild', 300);

INSERT INTO Chores("ChoreId", "ParentGoogleAccountId", "Name", "Description", "Status", "AssignedTo", "Points") VALUES ('FFE61C9A-C1C9-45CA-B6F0-8F850A936E38', 'TestParent', 'Do Homework', 'Finish your homework early', 'Completed', 'TestChild', 50);

INSERT INTO Chores("ChoreId", "ParentGoogleAccountId", "Name", "Description", "Status", "AssignedTo", "Points") VALUES ('C3CB2F60-226F-4973-AF3F-701527053AC7', 'TestParent', 'Take Out the Trash', 'Take the trash can out', 'Verified', 'TestChild', 150);

INSERT INTO Chores("ChoreId", "ParentGoogleAccountId", "Name", "Description", "Status", "AssignedTo", "Points") VALUES ('C4092853-80BC-4586-8992-11065F586F33', 'TestParent', 'Fold Clothes', 'Fold and put away the laundry', 'Verified', 'TestChild', 250);

INSERT INTO Rewards("RewardId", "ParentGoogleAccountId", "Name", "Description", "Points") VALUES ('81301234-93A1-4D59-9E2F-7F13D837A37E', 'TestParent', 'Ice Cream', 'Get an icecream cone after dinner', 1500);

INSERT INTO Rewards("RewardId", "ParentGoogleAccountId", "Name", "Description", "Points") VALUES ('11A2EDDE-9FDD-4CB2-AC05-CA84D3A0B03C', 'TestParent', 'Cash Exchange', '$10 per 10000 pts', 10000);

INSERT INTO Rewards("RewardId", "ParentGoogleAccountId", "Name", "Description", "Points") VALUES ('24191524-2ccb-46e3-973a-b9f3c6c8da40', 'TestParent', 'Pizza Party', 'Pizza for dinner on Friday', 12000);

INSERT INTO RewardRedemptionHistory("RewardId", "ParentGoogleAccountId", "ChildGoogleAccountId") VALUES ((SELECT RewardId FROM Rewards WHERE Name = 'Ice Cream'),'TestParent', 'TestChild');

INSERT INTO RewardRedemptionHistory("RewardId", "ParentGoogleAccountId", "ChildGoogleAccountId") VALUES ((SELECT RewardId FROM Rewards WHERE Name = 'Cash Exchange'),'TestParent', 'TestChild');

INSERT INTO RewardRedemptionHistory("RewardId", "ParentGoogleAccountId", "ChildGoogleAccountId") VALUES ((SELECT RewardId FROM Rewards WHERE Name = 'Cash Exchange'),'TestParent', 'TestChild');
