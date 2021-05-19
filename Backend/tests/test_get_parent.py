def test_get_parent_bad_request(client):
    rv = client.post("/api/parents/info", json=dict())
    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data


def test_get_parent_valid(client):
    # req = {"GoogleAccountId": "TestParent", "GoogleTokenId": "TestParent"}
    req = dict(GoogleAccountId="TestParent", GoogleTokenId="TestParent")
    rv = client.post("/api/parents/info", json=req)
    assert rv.status_code == 200
    assert rv.is_json
    res_json = rv.get_json()
    exp_json = {
        "Name": "Test Parent",
        "Email": "chorecenter@gmail.com",
        "GoogleAccountId": "TestParent",
        "ParentCode": "d7abc322-e627-4b09-bd51-9da55599b7ea".upper(),
    }
    assert res_json.keys() == exp_json.keys()
    for key in exp_json:
        assert exp_json[key] == res_json[key]


def test_get_parent_invalid(client):
    req = dict(GoogleAccountId="InvalidParent", GoogleTokenId="InvalidParent")
    rv = client.post("/api/parents/info", json=req)
    assert rv.status_code == 404
    assert b"Parent Account ID does not exist" in rv.data
