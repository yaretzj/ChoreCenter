class CreateParentResponseModel:
    def __init__(self, name: str, email: str, parent_code: str):
        self.name = name
        self.email = email
        self.parent_code = parent_code

    def get_response(self) -> dict:
        return {"Name": self.name, "Email": self.email, "ParentCode": self.parent_code}
