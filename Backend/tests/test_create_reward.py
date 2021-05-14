def test_create_reward_bad_request(client):
    rv = client.post("/api/parents/rewards/new", json=dict())
    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data


def test_create_reward_invalid_parent(client):
    req_json = {
        "Name": "Create Reward Test Reward",
        "Description": "Create Reward Test Reward",
        "GoogleAccountId": "InvalidParent",
        "Points": 1000,
    }

    rv = client.post("/api/parents/rewards/new", json=req_json)
    assert rv.status_code == 404
    assert b"Parent Account ID InvalidParent does not exist" in rv.data


def test_create_reward(client, cursor):
    delete_test_reward(cursor)

    req_json = {
        "Name": "Create Reward Test Reward",
        "Description": "Create Reward Test Reward",
        "GoogleAccountId": "TestParent",
        "Points": 1000,
    }

    rv = client.post("/api/parents/rewards/new", json=req_json)
    assert rv.status_code == 201
    assert b"Created reward" in rv.data

    delete_test_reward(cursor)


def delete_test_reward(cursor):
    cursor.execute("""DELETE FROM Rewards WHERE Name = 'Create Reward Test Reward';""")
