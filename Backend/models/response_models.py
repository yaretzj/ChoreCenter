class CreateParentResponseModel:
    def __init__(self, name: str, email: str, parent_code: str):
        self.name = name
        self.email = email
        self.parent_code = parent_code

    def get_response(self) -> dict:
        return {"Name": self.name, "Email": self.email, "ParentCode": self.parent_code}


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
            "AccountId": account_id,
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
