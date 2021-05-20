def test_get_child_bad_request(client):
    rv = client.post("/api/children/info", json=dict())
    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data


def test_get_child_valid(client):
    # req = {"GoogleAccountId": "TestParent", "GoogleTokenId": "TestParent"}
    req = dict(GoogleAccountId="TestChild", GoogleTokenId="TestChild")
    rv = client.post("/api/children/info", json=req)
    assert rv.status_code == 200
    assert rv.is_json
    res_json = rv.get_json()
    exp_json = {
        "Name": "Test Child",
        "Email": "chorecenter@gmail.com",
        "ParentGoogleAccountId": "TestParent",
        "GoogleAccountId": "TestChild",
        "Points": 9999999,
    }
    assert res_json == exp_json


def test_get_child_invalid(client):
    req = dict(GoogleAccountId="InvalidChild", GoogleTokenId="InvalidChild")
    rv = client.post("/api/children/info", json=req)
    assert rv.status_code == 404
    assert b"Child Account ID does not exist" in rv.data
