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