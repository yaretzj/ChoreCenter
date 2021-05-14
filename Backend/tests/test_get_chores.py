get_chores_exp_json = {
    "Chores": [
        {
            "AcceptedTime": None,
            "AccountId": "TestParent",
            "AssignedTo": "TestChild",
            "ChoreId": "4697217C-B0FE-4CA5-ACC2-10C5FC342C06",
            "CompletedTime": None,
            "CreatedTime": "Thu, 13 May 2021 23:10:45 GMT",
            "Description": "Full clean of your room",
            "LastUpdateTime": "Thu, 13 May 2021 23:10:45 GMT",
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
            "CreatedTime": "Thu, 13 May 2021 23:10:45 GMT",
            "Description": "Minimum 15 minutes",
            "LastUpdateTime": "Thu, 13 May 2021 23:10:45 GMT",
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
            "CreatedTime": "Thu, 13 May 2021 23:10:45 GMT",
            "Description": "Finish your homework early",
            "LastUpdateTime": "Thu, 13 May 2021 23:10:45 GMT",
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
            "CreatedTime": "Thu, 13 May 2021 23:10:45 GMT",
            "Description": "Location: Kitchen",
            "LastUpdateTime": "Thu, 13 May 2021 23:10:45 GMT",
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
            "CreatedTime": "Thu, 13 May 2021 23:10:45 GMT",
            "Description": "",
            "LastUpdateTime": "Thu, 13 May 2021 23:10:45 GMT",
            "Name": "Wash Dishes",
            "Points": 100,
            "Status": "Created",
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
    assert get_chores_exp_json["Chores"] == res_json["Chores"]


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
    assert get_chores_exp_json["Chores"] == res_json["Chores"]
