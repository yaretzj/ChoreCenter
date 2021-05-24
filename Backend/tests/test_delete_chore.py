from db import queries

req_json = {"GoogleAccountId": "TestParent"}


def test_delete_chore_bad_request(client):
    rv = client.delete(
        "/api/parents/chores/C4092853-80BC-4586-8992-11065F586F33", json=dict()
    )
    assert rv.status_code == 400


def test_delete_chore(client, cursor):
    cursor.execute(
        queries.create_chore(), "TestParent", "Test Chore", "test chore", "Created", 10
    )

    chore_id = cursor.fetchone()[0]

    rv = client.delete("/api/parents/chores/{}".format(chore_id), json=req_json)

    assert rv.status_code == 200
    assert b"Deleted chore" in rv.data


def test_delete_invalid_id(client):
    rv = client.delete("/api/parents/chores/abcd", json=req_json)
    assert rv.status_code == 500
