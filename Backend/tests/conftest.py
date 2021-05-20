import pytest
from app import app
from db.connection import setup_db_conn


@pytest.fixture
def client():
    app.config["TESTING"] = True
    with app.test_client() as client:
        yield client


@pytest.fixture
def cursor():
    conn = setup_db_conn()
    yield conn.cursor()
    conn.close()
