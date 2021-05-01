"""
    Flask Server
"""
import db.queries as queries
import pyodbc

from dotenv import dotenv_values
from flask import abort, Flask, request
from models.response_models import CreateParentResponseModel

config = dotenv_values()

# Need to install Microsoft ODBC driver for SQL Server
# Mac: https://docs.microsoft.com/en-us/sql/connect/odbc/linux-mac/install-microsoft-odbc-driver-sql-server-macos?view=sql-server-ver15
# Windows: https://docs.microsoft.com/en-us/sql/connect/odbc/windows/system-requirements-installation-and-driver-files?view=sql-server-ver15#installing-microsoft-odbc-driver-for-sql-server

# Some other example server values are
# server = 'localhost\sqlexpress' # for a named instance
# server = 'myserver,port' # to specify an alternate port
def setup_db_conn():
    server = config["DB_SERVER"]
    database = config["DB_NAME"]
    username = config["DB_USERNAME"]
    password = config["DB_PASSWORD"]

    cnxn = pyodbc.connect(
        "DRIVER={ODBC Driver 17 for SQL Server};SERVER="
        + server
        + ";DATABASE="
        + database
        + ";UID="
        + username
        + ";PWD="
        + password
    )
    return cnxn


app = Flask(__name__)
db_conn = setup_db_conn()
cursor = db_conn.cursor()


@app.route("/")
def hello_world():
    return "Hello, World!"


# CreateParentAccount
@app.route("/api/parents/new/", methods=["POST"])
def create_parent():
    body = request.json

    if not validate_request_body(
        ["Name", "Email", "GoogleTokenId", "GoogleAccountId"], body
    ):
        abort(400, "Invalid request body")

    cursor.execute(
        queries.create_parent(),
        body["Name"],
        body["Email"],
        body["GoogleTokenId"],
        body["GoogleAccountId"],
    )
    create_parent_response_model = CreateParentResponseModel(
        *cursor.fetchone()
    )  # Get parent data returned by SQL
    db_conn.commit()

    return create_parent_response_model.get_response(), 201


# CreateChore
@app.route("/api/parents/chores/new/", methods=["POST"])
def create_chore_parent():
    pass


# UpdateChoreParent
@app.route("/api/parents/chores/<chore_id>/update", methods=["POST"])
def update_chore_parent():
    pass


# GetChoresParent
@app.route("/api//parents/chores", methods=["POST"])
def get_chores_parent():
    pass


# CreateReward
@app.route("/api/parents/rewards/new", methods=["POST"])
def create_reward():
    pass


# GetRewardsParent
@app.route("/api/parents/rewards/", methods=["POST"])
def get_reward_parent():
    pass


# GetRedeemedRewards
@app.route("/api/parents/rewards/redeemed", methods=["POST"])
def get_redeemed_rewards_parent():
    pass


# CreateChildAccount
@app.route("/api/children/new", methods=["POST"])
def create_child():
    body = request.json
    if not validate_request_body(
        ["Name", "Email", "GoogleTokenId", "GoogleAccountId", "ParentCode"], body
    ):
        abort(400, "Invalid request body")

    # try:
    cursor.execute(queries.get_parent_by_code(), body["ParentCode"])
    # except:
    #     abort(400, "Invalid Parent Code")

    res = cursor.fetchone()
    print(res.GoogleAccountId)

    if not res:
        abort(404, "Parent Code not found")

    res = create_child_account_txn(
        body["Name"],
        body["Email"],
        res.GoogleAccountId,
        body["GoogleTokenId"],
        body["GoogleAccountId"],
    )
    if res:
        return (
            "Created Child Account with AccountID {}".format(body["GoogleAccountId"]),
            200,
        )
    else:
        abort(500)


def create_child_account_txn(
    name: str, email: str, parent_acc_id: str, token_id: str, acc_id: str
):
    try:
        db_conn.autocommit = False
        cursor.execute(queries.get_child_by_account_id(), acc_id)
        child_acc = cursor.fetchone()
        if child_acc:
            abort(400, "Child account with AccountId {} already exists.".format(acc_id))
        cursor.execute(
            queries.create_child(), name, email, parent_acc_id, token_id, acc_id
        )
    except pyodbc.DatabaseError as err:
        db_conn.rollback()
        create_child_account_txn(name, email, parent_acc_id, acc_id, token_id)
    else:
        db_conn.commit()
        return True
    finally:
        db_conn.autocommit = True


# GetChoresChild
@app.route("/api/children/chores", methods=["POST"])
def get_chores_child():
    pass


# UpdateChoreChild
@app.route("/api/children/chores/<chore_id>/update", methods=["POST"])
def update_chore_child():
    pass


# GetRewardsChild
@app.route("/api/children/rewards/", methods=["POST"])
def get_rewards_child():
    pass


# RedeemReward
@app.route("/api/children/rewards/redeem", methods=["POST"])
def redeem_reward():
    pass


# GetRedeemedRewardsChild
@app.route("/api/children/rewards/redeemed", methods=["POST"])
def get_redeemed_rewards_child():
    pass


def validate_request_body(fields: list, body: dict):
    return all(f in body for f in fields)


# cursor.execute("Select * from Parents")
# res = cursor.fetchall()
# print(res)
