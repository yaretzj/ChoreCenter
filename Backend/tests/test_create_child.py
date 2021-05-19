import pyodbc


def test_create_child_bad_request(client):
    rv = client.post("/api/children/new", json=dict())
    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data


def test_create_duplicate_child(client, cursor):
    req_json = {
        "Name": "TestChild",
        "Email": "TestChild",
        "GoogleAccountId": "TestChild",
        "GoogleTokenId": "TestChild",
    }

    rv = client.post("/api/children/new", json=req_json)
    assert rv.status_code == 400


def test_create_child(client, cursor):
    delete_test_child(cursor)

    req_json = {
        "Name": "CreateChildTest",
        "Email": "CreateChildTest",
        "GoogleAccountId": "CreateChildTest",
        "GoogleTokenId": "CreateChildTest",
        "ParentCode": "d7abc322-e627-4b09-bd51-9da55599b7ea",
    }

    rv = client.post("/api/children/new", json=req_json)
    assert rv.status_code == 201
    assert b"Created Child Account with AccountID CreateChildTest" in rv.data

    delete_test_child(cursor)


def delete_test_child(cursor):
    try:
        cursor.execute(
            """DELETE FROM Children WHERE Children.GoogleAccountId = 'CreateChildTest';"""
        )
    except pyodbc.Error as err:
        print(err)
