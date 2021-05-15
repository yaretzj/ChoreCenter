def test_bad_request(client):
    rv = client.post("/api/parents/info", json=dict())
    assert rv.status_code == 400
    assert b"Incomplete request body" in rv.data
