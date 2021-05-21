get_chores_exp_json = {
    "Chores": [
        {
            "AcceptedTime": None,
            "AccountId": "TestParent",
            "AssignedTo": "TestChild",
            "ChoreId": "4697217C-B0FE-4CA5-ACC2-10C5FC342C06",
            "CompletedTime": None,
            "Description": "Full clean of your room",
            "Name": "Clean Bedroom",
            "Points": 300,
            "Status": "Completed",
            "VerifiedTime": None,
        },
        {
            "AcceptedTime": None,
            "AccountId": "TestParent",
            "AssignedTo": None,
            "ChoreId": "BB9DA6E5-5C2A-44B5-AC83-5BDA5CA95FA8",
            "CompletedTime": None,
            "Description": "Minimum 15 minutes",
            "Name": "Walk the Dog",
            "Points": 200,
            "Status": "Created",
            "VerifiedTime": None,
        },
        {
            "AcceptedTime": None,
            "AccountId": "TestParent",
            "AssignedTo": "TestChild",
            "ChoreId": "FFE61C9A-C1C9-45CA-B6F0-8F850A936E38",
            "CompletedTime": None,
            "Description": "Finish your homework early",
            "Name": "Do Homework",
            "Points": 50,
            "Status": "Completed",
            "VerifiedTime": None,
        },
        {
            "AcceptedTime": None,
            "AccountId": "TestParent",
            "AssignedTo": None,
            "ChoreId": "42CA0914-2A14-4096-8931-A64494719FB6",
            "CompletedTime": None,
            "Description": "Location: Kitchen",
            "Name": "Sweep the Floor",
            "Points": 500,
            "Status": "Created",
            "VerifiedTime": None,
        },
        {
            "AcceptedTime": None,
            "AccountId": "TestParent",
            "AssignedTo": None,
            "ChoreId": "D8342704-AF59-4034-B923-B22B4C98650F",
            "CompletedTime": None,
            "Description": "",
            "Name": "Wash Dishes",
            "Points": 100,
            "Status": "Created",
            "VerifiedTime": None,
        },
        {
            "AcceptedTime": None,
            "AccountId": "TestParent",
            "AssignedTo": "TestChild",
            "ChoreId": "B0168E35-31AA-4049-8821-6AD0CCA2F12E",
            "CompletedTime": None,
            "Description": "Memorize Fur Elise",
            "Name": "Practice Piano",
            "Points": 750,
            "Status": "Created",
            "VerifiedTime": None,
        },
        {
            "AcceptedTime": None,
            "AccountId": "TestParent",
            "AssignedTo": "TestChild",
            "ChoreId": "C3CB2F60-226F-4973-AF3F-701527053AC7",
            "CompletedTime": None,
            "Description": "Take the trash can out",
            "Name": "Take Out the Trash",
            "Points": 150,
            "Status": "Verified",
            "VerifiedTime": None,
        },
        {
            "AcceptedTime": None,
            "AccountId": "TestParent",
            "AssignedTo": "TestChild",
            "ChoreId": "C4092853-80BC-4586-8992-11065F586F33",
            "CompletedTime": None,
            "Description": "Fold and put away the laundry",
            "Name": "Fold Clothes",
            "Points": 250,
            "Status": "Verified",
            "VerifiedTime": None,
        },
    ]
}


def test_get_chores_parent_bad_request(client):
    rv = client.post("/api/parents/chores", json=dict())

    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data


def test_get_chores_parent(client):
    req_json = {"GoogleAccountId": "TestParent"}

    rv = client.post("/api/parents/chores", json=req_json)

    assert rv.status_code == 200
    assert rv.is_json
    res_json = rv.get_json()

    check_response_equality(res_json)


def test_get_chores_child_bad_request(client):
    rv = client.post("/api/children/chores", json=dict())

    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data


def test_get_chores_child(client):
    req_json = {"GoogleAccountId": "TestChild"}

    rv = client.post("/api/children/chores", json=req_json)

    assert rv.status_code == 200
    assert rv.is_json
    res_json = rv.get_json()

    check_response_equality(res_json)


def check_response_equality(res_json):
    exp_list, res_list = get_chores_exp_json["Chores"], res_json["Chores"]
    exp_list.sort(key=lambda chore: chore["Name"])
    res_list.sort(key=lambda chore: chore["Name"])
    for exp_chore, res_chore in zip(exp_list, res_list):
        for field in exp_chore:
            assert exp_chore[field] == res_chore[field]
