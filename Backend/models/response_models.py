class CreateParentResponseModel:
    def __init__(self, name, email, parent_code):
        self.response = {"Name": name, "Email": email, "ParentCode": parent_code}

    def get_response(self):
        return self.response
