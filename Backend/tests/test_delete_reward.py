from db import queries

req_json = {"GoogleAccountId": "TestParent"}


def test_delete_chore_bad_request(client):
    rv = client.delete(
        "/api/parents/rewards/81301234-93A1-4D59-9E2F-7F13D837A37E", json=dict()
    )
    assert rv.status_code == 400


def test_delete_chore(client, cursor):
    cursor.execute(
        queries.create_reward(), "TestParent", "Test Reward", "test reward", 10
    )

    reward_id = cursor.fetchone()[0]

    rv = client.delete("/api/parents/rewards/{}".format(reward_id), json=req_json)

    assert rv.status_code == 200
    assert b"Deleted reward" in rv.data


def test_delete_invalid_id(client):
    rv = client.delete("/api/parents/rewards/abcd", json=req_json)
    assert rv.status_code == 500
