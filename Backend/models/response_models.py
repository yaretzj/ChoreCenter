class CreateParentResponseModel:
    def __init__(self, name, email, parent_code):
        self.response = {"Name": name, "Email": email, "ParentCode": parent_code}

    def get_response(self):
        return self.response

class CreateChoresParentResponseModel:
    def __init__(self, chore_id, account_id, chore_name, desc, status, assigned_to, points, created_time, accepted_time, completed_time, verified_time):
        self.response = {"ChoreId": chore_id, "AccountId": account_id, 
                        "ChoreName": chore_name, "Description": desc, "Status": status, 
                        "AssignedTo": assigned_to, "Points": points, 
                        "CreatedTime": created_time, "AcceptedTime": accepted_time, 
                        "CompletedTime": completed_time, "VerifiedTime": verified_time}

    def get_response(self):
        return self.response