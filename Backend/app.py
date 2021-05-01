import pyodbc

from dotenv import dotenv_values
from flask import Flask, request
from models.response_models import *

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

    cursor.execute(
        """Insert into Parents(Name, Email, GoogleTokenId, GoogleAccountId) OUTPUT INSERTED.Name, INSERTED.Email, INSERTED.ParentCode Values (?, ?, ?, ?);""",
        body["Name"],
        body["Email"],
        body["GoogleTokenId"],
        body["GoogleAccountId"],
    )
    create_parent_response_model = CreateParentResponseModel(
        *cursor.fetchone()
    )  # Get parent code returned by SQL
    db_conn.commit()

    return create_parent_response_model.get_response(), 201


# CreateChore
@app.route("/api/parents/chores/new", methods=["POST"])
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
    pass


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
