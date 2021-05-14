import pyodbc


def test_create_parent_bad_request(client):
    rv = client.post("/api/parents/new", json=dict())
    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data


def test_create_duplicate_parent(client, cursor):
    delete_test_parent(cursor)

    req_json = {
        "Name": "CreateParentTest",
        "Email": "CreateParentTest",
        "GoogleAccountId": "CreateParentTest",
        "GoogleTokenId": "CreateParentTest",
    }
    rv1 = client.post("/api/parents/new", json=req_json)
    assert rv1.status_code == 201

    rv2 = client.post("/api/parents/new", json=req_json)
    assert rv2.status_code == 400

    """ Need to figure out why this line is causing tests to stall. """
    # delete_test_parent(cursor)


def test_create_parent(client, cursor):
    delete_test_parent(cursor)

    req_json = {
        "Name": "CreateParentTest",
        "Email": "CreateParentTest",
        "GoogleAccountId": "CreateParentTest",
        "GoogleTokenId": "CreateParentTest",
    }
    rv = client.post("/api/parents/new", json=req_json)
    assert rv.status_code == 201
    assert rv.is_json
    res_json = rv.get_json()
    assert res_json["Name"] == req_json["Name"]
    assert res_json["Email"] == req_json["Email"]
    assert "ParentCode" in res_json

    delete_test_parent(cursor)


def delete_test_parent(cursor):
    try:
        cursor.execute(
            """DELETE FROM Parents WHERE Parents.GoogleAccountId = 'CreateParentTest';"""
        )
    except pyodbc.Error as err:
        print(err)
