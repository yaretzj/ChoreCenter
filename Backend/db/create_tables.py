import os
import pyodbc
from dotenv import dotenv_values

BASEDIR = os.path.abspath(os.path.dirname(".."))
config = dotenv_values((os.path.join(BASEDIR, ".env")))
print(BASEDIR)

server = config["DB_SERVER"]
database = config["DB_NAME"]
username = config["DB_USERNAME"]
password = config["DB_PASSWORD"]

conn = pyodbc.connect(
    "DRIVER={ODBC Driver 17 for SQL Server};SERVER="
    + server
    + ";DATABASE="
    + database
    + ";UID="
    + username
    + ";PWD="
    + password
)
cursor = conn.cursor()

with open(
    os.path.join(os.path.abspath(os.path.dirname(__file__)), "create-tables.sql")
) as f:
    create_statements = f.read().split("\n\n")
[cursor.execute(stmt) for stmt in create_statements]
cursor.commit()
with open(
    os.path.join(os.path.abspath(os.path.dirname(__file__)), "create-triggers.sql")
) as f:
    create_statements = f.read().split("\n\n")
[cursor.execute(stmt) for stmt in create_statements]
cursor.commit()

# cursor.execute("drop table RewardRedemptionHistory")
# cursor.execute("drop table Rewards")
# cursor.execute("drop table Chores")
# cursor.execute("drop table Children")
# cursor.execute("drop table Parents")
# cursor.commit()

# cursor.execute("select * from Parents")
# res = cursor.fetchall()
# print(res)

conn.close()
