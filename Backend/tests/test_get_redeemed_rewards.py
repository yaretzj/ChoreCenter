get_redeemed_rewards_parent_exp_json = {
    "RedeemedRewards": [
        {
            "RewardId": "81301234-93A1-4D59-9E2F-7F13D837A37E",
            "Name": "Ice Cream",
            "Description": "Get an icecream cone after dinner",
            "ChildName": "Test Child",
        },
        {
            "RewardId": "11A2EDDE-9FDD-4CB2-AC05-CA84D3A0B03C",
            "Name": "Cash Exchange",
            "Description": "$10 per 10000 pts",
            "ChildName": "Test Child",
        },
        {
            "RewardId": "11A2EDDE-9FDD-4CB2-AC05-CA84D3A0B03C",
            "Name": "Cash Exchange",
            "Description": "$10 per 10000 pts",
            "ChildName": "Test Child",
        },
    ]
}

get_redeemed_rewards_child_exp_json = {
    "RedeemedRewards": [
        {
            "RewardId": "81301234-93A1-4D59-9E2F-7F13D837A37E",
            "Name": "Ice Cream",
            "Description": "Get an icecream cone after dinner",
        },
        {
            "RewardId": "11A2EDDE-9FDD-4CB2-AC05-CA84D3A0B03C",
            "Name": "Cash Exchange",
            "Description": "$10 per 10000 pts",
        },
        {
            "RewardId": "11A2EDDE-9FDD-4CB2-AC05-CA84D3A0B03C",
            "Name": "Cash Exchange",
            "Description": "$10 per 10000 pts",
        },
    ]
}


def test_get_parent_history_bad_request(client):
    rv = client.post("/api/parents/rewards/history", json=dict())

    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data


def test_get_parent_history_valid(client):
    req = dict(GoogleAccountId="TestParent")
    rv = client.post("/api/parents/rewards/history", json=req)

    assert rv.status_code == 200
    assert rv.is_json
    check_response_equality(get_redeemed_rewards_parent_exp_json, rv.get_json())


def test_get_parent_history_invalid(client):
    req = dict(GoogleAccountId="InvalidParent")
    rv = client.post("/api/parents/rewards/history", json=req)

    assert rv.status_code == 200
    assert rv.is_json

    res_json = rv.get_json()
    exp_json = {"RedeemedRewards": []}
    assert res_json == exp_json


def test_get_child_history_bad_request(client):
    rv = client.post("/api/children/rewards/history", json=dict())

    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data


def test_get_child_history_valid(client):
    req = dict(GoogleAccountId="TestChild")
    rv = client.post("/api/children/rewards/history", json=req)

    assert rv.status_code == 200
    assert rv.is_json
    check_response_equality(get_redeemed_rewards_child_exp_json, rv.get_json())


def test_get_child_history_invalid(client):
    req = dict(GoogleAccountId="InvalidChild")
    rv = client.post("/api/children/rewards/history", json=req)

    assert rv.status_code == 200
    assert rv.is_json

    res_json = rv.get_json()
    exp_json = {"RedeemedRewards": []}
    assert res_json == exp_json


def check_response_equality(exp_json, res_json):
    exp_list, res_list = (
        exp_json["RedeemedRewards"],
        res_json["RedeemedRewards"],
    )
    exp_list.sort(key=lambda rr: rr["Name"])
    res_list.sort(key=lambda rr: rr["Name"])
    for exp_rr, res_rr in zip(exp_list, res_list):
        for field in exp_rr:
            assert exp_rr[field] == res_rr[field]
