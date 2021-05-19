get_rewards_exp_json = {
    "Rewards": [
        {
            "Description": "Get an icecream cone after dinner",
            "Name": "Ice Cream",
            "ParentGoogleAccountId": "TestParent",
            "Points": 1500,
            "RewardId": "81301234-93A1-4D59-9E2F-7F13D837A37E",
        },
        {
            "Description": "Pizza for dinner on Friday",
            "Name": "Pizza Party",
            "ParentGoogleAccountId": "TestParent",
            "Points": 12000,
            "RewardId": "24191524-2CCB-46E3-973A-B9F3C6C8DA40",
        },
        {
            "Description": "$10 per 10000 pts",
            "Name": "Cash Exchange",
            "ParentGoogleAccountId": "TestParent",
            "Points": 10000,
            "RewardId": "11A2EDDE-9FDD-4CB2-AC05-CA84D3A0B03C",
        },
    ]
}


def test_get_rewards_parent_bad_request(client):
    rv = client.post("/api/parents/rewards", json=dict())

    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data


def test_get_rewards_parent(client):
    req_json = {"GoogleAccountId": "TestParent"}

    rv = client.post("/api/parents/rewards", json=req_json)

    assert rv.status_code == 200
    assert rv.is_json
    check_response_equality(rv.get_json())


def test_get_rewards_child_bad_request(client):
    rv = client.post("/api/children/rewards", json=dict())

    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data


def test_get_rewards_invalid_child(client):
    req_json = {"GoogleAccountId": "InvalidChild"}

    rv = client.post("/api/children/rewards", json=req_json)

    assert rv.status_code == 404
    assert b"Child Account ID not found" in rv.data


def test_get_rewards_child(client):
    req_json = {"GoogleAccountId": "TestChild"}

    rv = client.post("/api/children/rewards", json=req_json)

    assert rv.status_code == 200
    assert rv.is_json
    check_response_equality(rv.get_json())


def check_response_equality(res_json):
    exp_list, res_list = get_rewards_exp_json["Rewards"], res_json["Rewards"]
    exp_list.sort(key=lambda reward: reward["Name"])
    res_list.sort(key=lambda reward: reward["Name"])
    for exp_reward, res_reward in zip(exp_list, res_list):
        for field in exp_reward:
            assert exp_reward[field] == res_reward[field]
