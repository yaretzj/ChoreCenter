import pytest
import os

from flask import Flask

import sys
from app import app


@pytest.fixture
def client():
    # db_fd, flaskr.app.config['DATABASE'] = tempfile.mkstemp()
    # BASEDIR = os.path.abspath(os.path.dirname(".."))
    # app = Flask(BASEDIR)
    # print(BASEDIR)
    # app = Flask('../app.')
    app.config["TESTING"] = True
    with app.test_client() as client:
        yield client

    # os.close(db_fd)
    # os.unlink(flaskr.app.config['DATABASE'])


def test_hello_world(client):
    rv = client.get("/")
    print(rv.data)
    # print("hello, world")
    assert rv.status_code == 200
    assert rv.data == b"Hello, world!"


def test_get_parent(client):
    # req = {"GoogleAccountId": "lel@gmail.com", "GoogleTokenId": "foo"}
    req = dict(GoogleAccountId="lel@gmail.com", GoogleTokenId="foo")
    res = client.post("/api/parents/info", json=req)
    assert res.status_code == 200