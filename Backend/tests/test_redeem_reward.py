def test_bad_request(client):
    rv = client.post("/api/children/rewards/redeem", json=dict())
    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data


def test_redeem_reward(client, cursor):
    # create chore
    # mark chore as completed
    # mark chore as verified - after UpdateChoreParent modified
    # redeem reward
    # check redemption history table
    # verify points were dedcuted
    # delete reward redemption history
    # delete chore
    pass
