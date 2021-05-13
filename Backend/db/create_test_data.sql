/*
Clears Tables of Test Data For Fresh Reset
*/

DELETE FROM RewardRedemptionHistory WHERE ParentGoogleAccountId = 'TestParent';
DELETE FROM Rewards WHERE ParentGoogleAccountId = 'TestParent';
DELETE FROM Chores WHERE ParentGoogleAccountId = 'TestParent';
DELETE FROM Children WHERE ParentGoogleAccountId = 'TestParent';
DELETE FROM Parents WHERE GoogleAccountId = 'TestParent';

-- Creates Parent with Account and Token ID of "TestParent"
INSERT INTO Parents("Name", "Email", "GoogleTokenId", "GoogleAccountId") VALUES ('Test Parent', 'chorecenter@gmail.com', 'TestParent', 'TestParent');

-- Creates Child connected to test parent with Account ID "TestChild"
INSERT INTO Children("Name", "Email", "ParentGoogleAccountId", "GoogleTokenId", "GoogleAccountId", "Points") VALUES ('Test Child', 'chorecenter@gmail.com', 'TestParent', 'TestChild', 'TestChild', 9999999)

/*
Creates 3 standard, unassigned, incomplete chores for test parent
*/
-- Name: "Sweep the Floor", Description: "Location: Kitchen", Status: "Created", AssignedTo: null, Points: 500
INSERT INTO Chores("ParentGoogleAccountId", "Name", "Description", "Status", "AssignedTo", "Points") VALUES ('TestParent', 'Sweep the Floor', 'Location: Kitchen', 'Created', null, 500);

-- Name: "Wash Dishes", Description: "", Status: "Created", AssignedTo: null, Points: 100
INSERT INTO Chores("ParentGoogleAccountId", "Name", "Description", "Status", "AssignedTo", "Points") VALUES ('TestParent', 'Wash Dishes', '', 'Created', null, 100);

-- Name: "Walk the Dog", Description: "Minimum 15 minutes", Status: "Created", AssignedTo: null, Points: 200
INSERT INTO Chores("ParentGoogleAccountId", "Name", "Description", "Status", "AssignedTo", "Points") VALUES ('TestParent', 'Walk the Dog', 'Minimum 15 minutes', 'Created', null, 200);

/*
Creates 2 completed chores for test parent
*/
-- Name: "Clean Bedroom", Description: "Full clean of your room", Status: "Completed", AssignedTo: "TestChild", Points: 300
INSERT INTO Chores("ParentGoogleAccountId", "Name", "Description", "Status", "AssignedTo", "Points") VALUES ('TestParent', 'Clean Bedroom', 'Full clean of your room', 'Completed', 'TestChild', 300);

-- Name: "Do Homework", Description: "Finish your homework early", Status: "Completed", AssignedTo: "TestChild", Points: 50
INSERT INTO Chores("ParentGoogleAccountId", "Name", "Description", "Status", "AssignedTo", "Points") VALUES ('TestParent', 'Do Homework', 'Finish your homework early', 'Completed', 'TestChild', 50);


/*
Creates 3 standard rewards for test parent
*/
-- Name: "Ice Cream", Description: "Get an icecream cone after dinner", Points: 500
INSERT INTO Rewards("ParentGoogleAccountId", "Name", "Description", "Points") VALUES ('TestParent', 'Ice Cream', 'Get an icecream cone after dinner', 1500);

-- Name: "Cash Exchange", Description: "$10 per 10000 pts", Points: 10000
INSERT INTO Rewards("ParentGoogleAccountId", "Name", "Description", "Points") VALUES ('TestParent', 'Cash Exchange', '$10 per 10000 pts', 10000);

-- Name: "Pizza Party", Description: "Pizza for dinner on Friday", Points: 12000
INSERT INTO Rewards("ParentGoogleAccountId", "Name", "Description", "Points") VALUES ('TestParent', 'Pizza Party', 'Pizza for dinner on Friday', 12000);

/*
Creates Redemption History Entry for "Test Childs Redemption History"
*/
-- Name: "Ice Cream"
INSERT INTO RewardRedemptionHistory("RewardId", "ParentGoogleAccountId", "ChildGoogleAccountId") VALUES ((SELECT RewardId FROM Rewards AS R WHERE R.Name = 'Ice Cream' AND R.ParentGoogleAccountId = 'TestParent'),'TestParent', 'TestChild');

-- Name: "Cash Exchange"
INSERT INTO RewardRedemptionHistory("RewardId", "ParentGoogleAccountId", "ChildGoogleAccountId") VALUES ((SELECT RewardId FROM Rewards AS R WHERE R.Name = 'Cash Exchange' AND R.ParentGoogleAccountId = 'TestParent'),'TestParent', 'TestChild');

-- Name: "Cash Exchange" for a second
INSERT INTO RewardRedemptionHistory("RewardId", "ParentGoogleAccountId", "ChildGoogleAccountId") VALUES ((SELECT RewardId FROM Rewards AS R WHERE R.Name = 'Cash Exchange' AND R.ParentGoogleAccountId = 'TestParent'),'TestParent', 'TestChild');