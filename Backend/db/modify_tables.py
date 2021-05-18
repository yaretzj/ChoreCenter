import os
import pyodbc
import argparse
from dotenv import dotenv_values

# Command line arguments handling
parser = argparse.ArgumentParser()
group = parser.add_mutually_exclusive_group()
group.add_argument(
    "-c",
    "--create",
    help="Creates the database tables specified in create-tables.sql with the triggers in create-triggers.sql.",
    action="store_true"
)
group.add_argument(
    "-p",
    "--populate",
    help="Populates the database tables with the test data specified in create_test_data.sql.",
    action="store_true"
)
group.add_argument(
    "-d",
    "--drop",
    help="Drops the database tables using the commands in drop-tables.sql.",
    action="store_true"
)
args = parser.parse_args()

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

if args.create:
    print("Creating tables!")

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
elif args.populate:
    print("Clearing and adding test data!")
elif args.drop:
    print("Dropping tables!")
else:
    print("No argument provided. Try -h or --help for options.")

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
