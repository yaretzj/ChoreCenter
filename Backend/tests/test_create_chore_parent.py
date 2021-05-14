def test_create_chore_parent_bad_request(client):
    rv = client.post("/api/parents/chores/new", json=dict())
    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data


def test_create_chore_parent(client, cursor):
    delete_test_chore(cursor)

    req_json = {
        "Name": "CreateChoreTestChore",
        "Description": "Create Chore Test Chore",
        "GoogleAccountId": "TestParent",
        "Points": 1000,
    }

    rv = client.post("/api/parents/chores/new", json=req_json)
    assert rv.status_code == 201
    assert b"Created chore" in rv.data

    delete_test_chore(cursor)


def test_create_chore_invalid_parent(client):
    req_json = {
        "Name": "CreateChoreTestChore",
        "Description": "Create Chore Test Chore",
        "GoogleAccountId": "InvalidParent",
        "Points": 1000,
    }

    rv = client.post("/api/parents/chores/new", json=req_json)
    assert rv.status_code == 404
    assert b"Parent Account ID InvalidParent does not exist" in rv.data


def delete_test_chore(cursor):
    cursor.execute("""DELETE FROM Chores WHERE Name = 'CreateChoreTestChore';""")
