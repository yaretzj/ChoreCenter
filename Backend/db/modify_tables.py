import os
import argparse
from dotenv import dotenv_values, load_dotenv
from connection import setup_db_conn

# Drop tables action currently disabled - want to avoid dropping tables accidentally. You can
# enable it by removing the commented out parts if you want to.

# Command line arguments handling
parser = argparse.ArgumentParser()
group = parser.add_mutually_exclusive_group()
group.add_argument(
    "-c",
    "--create",
    help="Creates the database tables specified in create-tables.sql with the triggers in create-triggers.sql.",
    action="store_true",
)
group.add_argument(
    "-p",
    "--populate",
    help="Populates the database tables with the test data specified in create_test_data.sql.",
    action="store_true",
)
group.add_argument(
    "-d",
    "--drop",
    help="Drops the database tables using the commands in drop-tables.sql.",
    action="store_true",
)
args = parser.parse_args()

BASEDIR = os.path.abspath(os.path.dirname(".."))
config = dotenv_values((os.path.join(BASEDIR, ".env")))

load_dotenv()
conn = setup_db_conn()
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
    # Note: this may not work yet until create_test_data format gets revamped
    print("Clearing and adding test data!")

    with open(
        os.path.join(os.path.abspath(os.path.dirname(__file__)), "create_test_data.sql")
    ) as f:
        create_statements = f.read().split("\n\n")
    [cursor.execute(stmt) for stmt in create_statements]
    cursor.commit()

elif args.drop:
    print("Dropping tables!")

    with open(
        os.path.join(os.path.abspath(os.path.dirname(__file__)), "drop-tables.sql")
    ) as f:
        drop_statements = f.read().split("\n")
    [cursor.execute(stmt) for stmt in drop_statements]
    cursor.commit()

else:
    print("Default, no args: create tables and populate with data.")

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
    with open(
        os.path.join(os.path.abspath(os.path.dirname(__file__)), "create_test_data.sql")
    ) as f:
        create_statements = f.read().split("\n\n")
    [cursor.execute(stmt) for stmt in create_statements]
    cursor.commit()

conn.close()
