from datetime import datetime
from typing import Tuple, Optional


class CreateParentResponseModel:
    def __init__(self, name: str, email: str, parent_code: str):
        self.name = name
        self.email = email
        self.parent_code = parent_code

    def get_response(self) -> dict:
        return {"Name": self.name, "Email": self.email, "ParentCode": self.parent_code}


class GetParentAccountResponseModel:
    def __init__(self, parent_acc: tuple):
        self.name, self.email, _, self.account_id, self.parent_code = parent_acc

    def get_response(self) -> dict:
        return {
            "Name": self.name,
            "Email": self.email,
            "GoogleAccountId": self.account_id,
            "ParentCode": self.parent_code,
        }


class GetChildAccountResponseModel:
    def __init__(self, child_acc: tuple):
        (
            self.name,
            self.email,
            self.parent_account_id,
            _,
            self.account_id,
            self.points,
        ) = child_acc

    def get_response(self) -> dict:
        return {
            "Name": self.name,
            "Email": self.email,
            "GoogleAccountId": self.account_id,
            "ParentGoogleAccountId": self.parent_account_id,
            "Points": self.points,
        }


class ChoreModel:
    def __init__(
        self,
        chore_id: str,
        account_id: str,
        name: str,
        description: str,
        status: str,
        assigned_to: Optional[str],
        points: int,
        created_time: datetime,
        accepted_time: Optional[datetime],
        completed_time: Optional[datetime],
        verified_time: Optional[datetime],
        last_update_time: datetime,
    ):
        self.chore_id = chore_id
        self.account_id = account_id
        self.name = name
        self.description = description
        self.status = status
        self.assigned_to = assigned_to
        self.points = points
        self.created_time = created_time
        self.accepted_time = accepted_time
        self.completed_time = completed_time
        self.verified_time = verified_time
        self.last_update_time = last_update_time

    def get_response(self) -> dict:
        return {
            "ChoreId": self.chore_id,
            "AccountId": self.account_id,
            "Name": self.name,
            "Description": self.description,
            "Status": self.status,
            "AssignedTo": self.assigned_to,
            "Points": self.points,
            "CreatedTime": self.created_time,
            "AcceptedTime": self.accepted_time,
            "CompletedTime": self.completed_time,
            "VerifiedTime": self.verified_time,
            "LastUpdateTime": self.last_update_time,
        }


class GetChoresResponseModel:
    def __init__(self, chores: list):
        self.chores = [ChoreModel(*chore) for chore in chores]

    def get_response(self) -> dict:
        return {"Chores": [chore.get_response() for chore in self.chores]}


class RewardModel:
    def __init__(self, reward: Tuple[str, str, str, str, int, datetime, datetime]):
        (
            self.reward_id,
            self.parent_account_id,
            self.name,
            self.description,
            self.points,
            self.created_time,
            self.last_update_time,
        ) = reward

    def get_response(self) -> dict:
        return {
            "RewardId": self.reward_id,
            "ParentGoogleAccountId": self.parent_account_id,
            "Name": self.name,
            "Description": self.description,
            "Points": self.points,
            "CreatedTime": self.created_time,
            "LastUpdateTime": self.last_update_time,
        }


class GetRewardsResponseModel:
    def __init__(self, rewards: list):
        self.rewards = [RewardModel(reward) for reward in rewards]

    def get_response(self) -> dict:
        return {"Rewards": [reward.get_response() for reward in self.rewards]}


class RewardRedemptionParentModel:
    def __init__(self, redemption: Tuple[str, str, str, datetime]):
        (
            self.reward_id,
            self.name,
            self.description,
            self.redeeded_time,
            self.child_name,
        ) = redemption

    def get_response(self) -> dict:
        return {
            "RewardId": self.reward_id,
            "Name": self.name,
            "Description": self.description,
            "ChildName": self.child_name,
            "RedeemedTime": self.redeeded_time,
        }


class RewardRedemptionHistoryParentResponseModel:
    def __init__(self, redemption_history: list):
        self.redemption_history = [
            RewardRedemptionParentModel(rh) for rh in redemption_history
        ]

    def get_response(self) -> dict:
        return {
            "‘RedeemedRewards": [rh.get_response() for rh in self.redemption_history]
        }


class RewardRedemptionChildModel:
    def __init__(self, redemption: Tuple[str, str, str, datetime]):
        (
            self.reward_id,
            self.name,
            self.description,
            self.redeeded_time,
        ) = redemption

    def get_response(self) -> dict:
        return {
            "RewardId": self.reward_id,
            "Name": self.name,
            "Description": self.description,
            "RedeemedTime": self.redeeded_time,
        }


class RewardRedemptionHistoryChildResponseModel:
    def __init__(self, redemption_history: list):
        self.redemption_history = [
            RewardRedemptionChildModel(rh) for rh in redemption_history
        ]

    def get_response(self) -> dict:
        return {
            "‘RedeemedRewards": [rh.get_response() for rh in self.redemption_history]
        }


class CreateChoresParentResponseModel:
    def __init__(
        self,
        chore_id,
        account_id,
        chore_name,
        desc,
        status,
        assigned_to,
        points,
        created_time,
        accepted_time,
        completed_time,
        verified_time,
    ):
        self.response = {
            "ChoreId": chore_id,
            "ParentGoogleAccountId": account_id,
            "ChoreName": chore_name,
            "Description": desc,
            "Status": status,
            "AssignedTo": assigned_to,
            "Points": points,
            "CreatedTime": created_time,
            "AcceptedTime": accepted_time,
            "CompletedTime": completed_time,
            "VerifiedTime": verified_time,
        }

    def get_response(self):
        return self.response
