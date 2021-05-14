import pyodbc
import os


def setup_db_conn():
    return pyodbc.connect(
        "DRIVER={ODBC Driver 17 for SQL Server};SERVER="
        + os.getenv("DB_SERVER")  # config["DB_SERVER"]
        + ";DATABASE="
        + os.getenv("DB_NAME")  # config["DB_NAME"]
        + ";UID="
        + os.getenv("DB_USERNAME")  # config["DB_USERNAME"]
        + ";PWD="
        + os.getenv("DB_PASSWORD"),  # config["DB_PASSWORD"]
        autocommit=True,
    )
