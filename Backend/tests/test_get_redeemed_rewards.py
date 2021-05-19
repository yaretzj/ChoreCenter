get_redeemed_rewards_parent_exp_json = {
    "RedeemedRewards": [
        {
            "RewardId": "81301234-93A1-4D59-9E2F-7F13D837A37E",
            "Name": "Ice Cream",
            "Description": "Get an icecream cone after dinner",
            "ChildName": "Test Child",
            "RedeemedTime": "Sat, 15 May 2021 00:18:01 GMT",
        },
        {
            "RewardId": "11A2EDDE-9FDD-4CB2-AC05-CA84D3A0B03C",
            "Name": "Cash Exchange",
            "Description": "$10 per 10000 pts",
            "ChildName": "Test Child",
            "RedeemedTime": "Sat, 15 May 2021 00:18:01 GMT",
        },
        {
            "RewardId": "11A2EDDE-9FDD-4CB2-AC05-CA84D3A0B03C",
            "Name": "Cash Exchange",
            "Description": "$10 per 10000 pts",
            "ChildName": "Test Child",
            "RedeemedTime": "Sat, 15 May 2021 00:18:01 GMT",
        },
    ]
}

get_redeemed_rewards_child_exp_json = {
    "RedeemedRewards": [
        {
            "RewardId": "81301234-93A1-4D59-9E2F-7F13D837A37E",
            "Name": "Ice Cream",
            "Description": "Get an icecream cone after dinner",
            "RedeemedTime": "Sat, 15 May 2021 00:18:01 GMT",
        },
        {
            "RewardId": "11A2EDDE-9FDD-4CB2-AC05-CA84D3A0B03C",
            "Name": "Cash Exchange",
            "Description": "$10 per 10000 pts",
            "RedeemedTime": "Sat, 15 May 2021 00:18:01 GMT",
        },
        {
            "RewardId": "11A2EDDE-9FDD-4CB2-AC05-CA84D3A0B03C",
            "Name": "Cash Exchange",
            "Description": "$10 per 10000 pts",
            "RedeemedTime": "Sat, 15 May 2021 00:18:01 GMT",
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
    assert (
        get_redeemed_rewards_parent_exp_json["RedeemedRewards"]
        == rv.get_json()["RedeemedRewards"]
    )


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
    assert (
        get_redeemed_rewards_child_exp_json["RedeemedRewards"]
        == rv.get_json()["RedeemedRewards"]
    )


def test_get_child_history_invalid(client):
    req = dict(GoogleAccountId="InvalidChild")
    rv = client.post("/api/children/rewards/history", json=req)

    assert rv.status_code == 200
    assert rv.is_json

    res_json = rv.get_json()
    exp_json = {"RedeemedRewards": []}
    assert res_json == exp_json
