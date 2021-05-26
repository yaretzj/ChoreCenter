"""
    Flask Server
"""
# from datetime import datetime
import os
import pyodbc
from dotenv import load_dotenv
from flask import abort, Flask, g, request
from typing import Tuple

from models.response_models import (
    ChoreModel,
    CreateParentResponseModel,
    GetChoresResponseModel,
    GetRewardsResponseModel,
    RewardRedemptionHistoryParentResponseModel,
    RewardRedemptionHistoryChildResponseModel,
    GetParentAccountResponseModel,
    GetChildAccountResponseModel,
)
import db.queries as queries
from db.connection import setup_db_conn


INTIIAL_CHORE_STATUS = "Created"


load_dotenv()
app = Flask(__name__)


def get_db_conn() -> Tuple[pyodbc.Connection, pyodbc.Cursor]:
    """
    Add SQL Server database connection to flask global if it does not already exist.
    Returns the database connection and cursor.
    """

    if "db_conn" not in g:
        g.db_conn = setup_db_conn()
        g.db_conn.autocommit = False

    return g.db_conn, g.db_conn.cursor()


@app.teardown_request
def close_db_conn(_):
    """Close database connection on teardown."""

    db_conn = g.pop("db_conn", None)
    if db_conn is not None:
        db_conn.close()


@app.route("/")
def hello_world():
    """Dummy default path that returns "Hello World" when sent a GET request"""
    # con, cur = get_db_conn()
    # cur.execute("Select * from parents")
    # print(cur.fetchall())
    return "Hello, world!"


@app.route("/api/parents/new", methods=["POST"])
def create_parent():
    """CreateParentAccount, Recieves a POST request from the client
    that uses the url path "/api/parents/new" and verifies all the
    parent information is valid JSON. Using the data received, an
    Insert statement is made to the database and the information
    is added to the SQL table.

    If the insertion was successful,
    returns a response back with a 201 code saying the Parent
    was created.
    If Parent account already exists, return 400.
    """

    body = request.json
    validate_request_body(["Name", "Email", "GoogleTokenId", "GoogleAccountId"], body)

    _, cursor = get_db_conn()

    try:
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
        cursor.commit()
    except Exception:
        abort(
            400,
            "Parent account with AccountId {} already exists.".format(
                body["GoogleAccountId"]
            ),
        )

    return create_parent_response_model.get_response(), 201


@app.route("/api/parents/info", methods=["POST"])
def get_parent():
    """GetParentInfo, Recieves a POST request from the client
    that uses the url path "/api/parents/info" and verifies all the
    Google account information is valid JSON. Using the data received,
    a Select statement is made to the database and the Parent information
    is received from the SQL table.

    If the Parent exists and the query was successful,
    returns a response back with parent infomation as JSON object.
    If server error, return 500.
    If Parent account not found, return 404.
    """

    body = request.json
    validate_request_body(["GoogleAccountId", "GoogleTokenId"], body)
    _, cursor = get_db_conn()

    try:
        cursor.execute(queries.get_parent_by_account_id(), body["GoogleAccountId"])
    except Exception as exc:
        abort(500, str(exc))

    parent_acc = cursor.fetchone()
    if not parent_acc:
        abort(404, "Parent Account ID does not exist")

    return GetParentAccountResponseModel(parent_acc).get_response()


@app.route("/api/parents/chores/new", methods=["POST"])
def create_chore_parent():
    """CreateChoreParent, Recieves a POST request from the client
    that uses the url path "/api/parents/chores/new" and verifies all the
    Google account information, name of chore, description of chore, and point value of
    the chore is valid JSON. Using the data received,
    a Insert statement is made to the database and the Chore information
    is entered into the Chore SQL table as a new row signifiying a new chore.

    If the insert was successful,
    returns "Chore Created" with a code of 201.
    If Parent account not found, return 404.
    """
    body = request.json
    validate_request_body(["GoogleAccountId", "Name", "Description", "Points"], body)
    _, cursor = get_db_conn()

    try:
        cursor.execute(
            queries.create_chore(),
            body["GoogleAccountId"],
            body["Name"],
            body["Description"],
            INTIIAL_CHORE_STATUS,
            int(body["Points"]),
        )
        cursor.commit()
    except Exception as ex:
        abort(
            404, "Parent Account ID {} does not exist".format(body["GoogleAccountId"])
        )

    return "Created chore", 201


@app.route("/api/parents/chores/<chore_id>/update", methods=["POST"])
def update_chore_parent(chore_id):
    """UpdateChoreParent, Recieves a POST request from the client
    that uses the url path "/api/parents/chores/<chore_id>/update" and verifies the
    Google account information and any other additional other fields are valid JSON.
    Using the data received, an Update statement is made to the database and the
    new Chore information is updated in the Chore SQL table to the existing chore.

    If the update was successful,
    returns the updated chore.
    If no additional fields are providied to update the chore, returns 400.
    If server error, returns 500.
    If Chore Id not found, return 404.
    """
    body = request.json
    validate_request_body(["GoogleAccountId"], body)
    db_conn, cursor = get_db_conn()

    account_id = body.pop("GoogleAccountId")
    columns = body.keys()
    if len(columns) == 0:
        abort(400, "No fields to update")

    try:
        cursor.execute(queries.get_chore_by_id_and_parent(), chore_id, account_id)
    except Exception as exc:
        abort(500, str(exc))

    chore = cursor.fetchone()
    if not chore:
        abort(404, "ChoreId {} does not exist".format(chore_id))

    update_values = [body[key] for key in columns]
    try:
        cursor.execute(queries.update_chore(columns), *update_values, chore_id)
        cursor.commit()
    except Exception as exc:
        abort(500, str(exc))

    try:
        cursor.execute(queries.get_chore_by_id_and_parent(), chore_id, account_id)
    except Exception as exc:
        abort(500, str(exc))

    return ChoreModel(*(cursor.fetchone())).get_response()


# GetChoresParent
@app.route("/api/parents/chores", methods=["POST"])
def get_chores_parent():
    """GetChoresParent, Recieves a POST request from the client
    that uses the url path "/api/parents/chores" and verifies the
    Google account information and any other additional other fields are valid JSON.
    Using the data received, a Select statement is made to the database and the
    Chores associated with that Parent are received from the SQL table.
    
    If the query was successful,
    returns a response back with Chores, list of Chore infomation as JSON object.
    If Parent account did not exist, Chores will be empty.
    If incomplete request body, return 400.
    If server error, return 500.
    """
    body = request.json
    validate_request_body(["GoogleAccountId"], body)
    _, cursor = get_db_conn()

    return get_chores_helper(cursor, body["GoogleAccountId"])


def get_chores_helper(cursor: pyodbc.Cursor, account_id: str) -> dict:
    """Helper function for GetChoresParent to get the chores given a parent account id."""
    try:
        cursor.execute(queries.get_chores_by_parent(), account_id)
    except Exception as exc:
        abort(500, str(exc))

    return GetChoresResponseModel(cursor.fetchall()).get_response()


# CreateReward
@app.route("/api/parents/rewards/new", methods=["POST"])
def create_reward():
    """CreateReward, Recieves a POST request from the client
    that uses the url path "/api/parents/rewards/new" and verifies all the
    Google account information, name of reward, description of reward, and point cost of
    the reward is valid JSON. Using the data received,
    a Insert statement is made to the database and the Reward information
    is entered into the Reward SQL table as a new row signifiying a new reward.

    If the insert was successful,
    returns "Reward Created" with a code of 201.
    If incomplete request body, return 400.
    If Parent account not found, return 404.
    If internal server error, return 500.
    """
    body = request.json
    validate_request_body(["Name", "GoogleAccountId", "Description", "Points"], body)

    _, cursor = get_db_conn()

    try:
        cursor.execute(
            queries.create_reward(),
            body["GoogleAccountId"],
            body["Name"],
            body["Description"],
            int(body["Points"]),
        )
        cursor.commit()
    except Exception:
        abort(
            404, "Parent Account ID {} does not exist".format(body["GoogleAccountId"])
        )

    return "Created reward", 201


# GetRewardsParent
@app.route("/api/parents/rewards", methods=["POST"])
def get_rewards_parent():
    """GetRewardsParent, Recieves a POST request from the client
    that uses the url path "/api/parents/rewards" and verifies the
    Google account information and any other additional other fields are valid JSON.
    Using the data received, a Select statement is made to the database and the
    Rewards associated with that Parent are received from the SQL table.
    
    If the query was successful,
    returns a response back with Rewards, list of Reward infomation as JSON object.
    If Parent account did not exist, Rewards will be empty.
    If server error, return 500.
    """
    body = request.json
    validate_request_body(["GoogleAccountId"], body)
    _, cursor = get_db_conn()

    return get_rewards_helper(cursor, body["GoogleAccountId"])


def get_rewards_helper(cursor: pyodbc.Cursor, account_id: str) -> dict:
    """Helper function for GetRewardsParent to get the rewards given a parent account id."""
    try:
        cursor.execute(queries.get_rewards_by_parent(), account_id)
    except Exception as exc:
        abort(500, str(exc))

    return GetRewardsResponseModel(cursor.fetchall()).get_response()


# GetRedeemedRewards
@app.route("/api/parents/rewards/history", methods=["POST"])
def get_redeemed_rewards_parent():
    body = request.json
    validate_request_body(["GoogleAccountId"], body)
    _, cursor = get_db_conn()

    try:
        cursor.execute(
            queries.get_reward_redemption_by_parent(), body["GoogleAccountId"]
        )
    except Exception as exc:
        abort(404, str(exc))

    return RewardRedemptionHistoryParentResponseModel(cursor.fetchall()).get_response()


# CreateChildAccount
@app.route("/api/children/new", methods=["POST"])
def create_child():
    body = request.json
    validate_request_body(
        ["Name", "Email", "GoogleTokenId", "GoogleAccountId", "ParentCode"], body
    )

    db_conn, cursor = get_db_conn()

    try:
        cursor.execute(queries.get_parent_by_code(), body["ParentCode"])
    except Exception:
        abort(400, "Invalid Parent Code")

    parent_res = cursor.fetchone()
    if not parent_res:
        abort(404, "Parent Code not found")

    res = create_child_account_txn(
        db_conn,
        cursor,
        body["Name"],
        body["Email"],
        parent_res.GoogleAccountId,
        body["GoogleTokenId"],
        body["GoogleAccountId"],
    )
    if res:
        return (
            "Created Child Account with AccountID {}".format(body["GoogleAccountId"]),
            201,
        )
    else:
        abort(500)


def create_child_account_txn(
    db_conn: pyodbc.Connection,
    cursor: pyodbc.Cursor,
    name: str,
    email: str,
    parent_acc_id: str,
    token_id: str,
    acc_id: str,
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
        db_conn.commit()
    except pyodbc.DatabaseError:
        db_conn.rollback()
        return create_child_account_txn(
            db_conn, cursor, name, email, parent_acc_id, token_id, acc_id
        )
    else:
        return True
    # finally:
    #     db_conn.autocommit = True


@app.route("/api/children/info", methods=["POST"])
def get_child():
    """GetChildInfo"""

    body = request.json
    validate_request_body(["GoogleAccountId", "GoogleTokenId"], body)
    _, cursor = get_db_conn()

    try:
        cursor.execute(queries.get_child_by_account_id(), body["GoogleAccountId"])
    except Exception as exc:
        abort(500, str(exc))

    child_acc = cursor.fetchone()
    if not child_acc:
        abort(404, "Child Account ID does not exist")

    return GetChildAccountResponseModel(child_acc).get_response()


# GetChoresChild
@app.route("/api/children/chores", methods=["POST"])
def get_chores_child():
    body = request.json
    validate_request_body(["GoogleAccountId"], body)
    _, cursor = get_db_conn()

    return get_chores_helper(
        cursor, get_parent_id_for_child(cursor, body["GoogleAccountId"])
    )


# UpdateChoreChild
@app.route("/api/children/chores/<chore_id>/update", methods=["POST"])
def update_chore_child(chore_id):
    body = request.json
    validate_request_body(["GoogleAccountId"], body)
    db_conn, cursor = get_db_conn()

    account_id = body.pop("GoogleAccountId")
    columns = body.keys()
    if len(columns) == 0:
        abort(400, "No fields to update")

    try:
        cursor.execute(queries.get_child_by_account_id(), account_id)
    except Exception as exc:
        abort(500, str(exc))

    child_acc = cursor.fetchone()
    if not child_acc:
        abort(404, "Child Account ID does not exist")

    try:
        cursor.execute(
            queries.get_chore_by_id_and_parent(),
            chore_id,
            child_acc.ParentGoogleAccountId,
        )
    except Exception as exc:
        abort(500, str(exc))

    chore = cursor.fetchone()
    if not chore or chore.ParentGoogleAccountId != child_acc.ParentGoogleAccountId:
        abort(404, "ChoreId {} does not exist".format(chore_id))

    update_values = [body[key] for key in columns]

    try:
        db_conn.autocommit = False
        cursor.execute(queries.update_chore(columns), *update_values, chore_id)

        if chore.Status != "Completed" and body.get("Status") == "Completed":
            cursor.execute(
                queries.update_child_points(),
                int(chore.Points) + int(child_acc.Points),
                account_id,
            )
        cursor.commit()
    except Exception as exc:
        abort(500, str(exc))

    try:
        cursor.execute(
            queries.get_chore_by_id_and_parent(),
            chore_id,
            child_acc.ParentGoogleAccountId,
        )
    except Exception as exc:
        abort(500, str(exc))

    return ChoreModel(*(cursor.fetchone())).get_response()


# GetRewardsChild
@app.route("/api/children/rewards", methods=["POST"])
def get_rewards_child():
    body = request.json
    validate_request_body(["GoogleAccountId"], body)
    _, cursor = get_db_conn()

    return get_rewards_helper(
        cursor, get_parent_id_for_child(cursor, body["GoogleAccountId"])
    )


# RedeemReward
@app.route("/api/children/rewards/redeem", methods=["POST"])
def redeem_reward():
    body = request.json
    validate_request_body(["GoogleAccountId", "RewardId"], body)
    db_conn, cursor = get_db_conn()
    res = redeem_reward_txn(db_conn, cursor, body["RewardId"], body["GoogleAccountId"])

    return {"RemainingPoints": res}


def redeem_reward_txn(db_conn, cursor, reward_id, child_account_id):
    try:
        db_conn.autocommit = False
        cursor.execute(queries.get_child_by_account_id(), child_account_id)
        child = cursor.fetchone()
        if not child:
            abort(404, "Child Account ID does not exist")
        cursor.execute(queries.get_reward_by_id(), reward_id)
        reward = cursor.fetchone()
        if (
            not reward
            or not reward.ParentGoogleAccountId == child.ParentGoogleAccountId
        ):
            abort(404, "Reward does not exist")

        if int(reward.Points) > int(child.Points):
            abort(405, "Insufficient points")
        remaining_points = int(child.Points) - int(reward.Points)
        cursor.execute(
            queries.update_child_points(),
            remaining_points,
            child_account_id,
        )
        cursor.execute(
            queries.create_reward_history(),
            reward.RewardId,
            child.GoogleAccountId,
            child.ParentGoogleAccountId,
        )
        cursor.commit()
    except pyodbc.DatabaseError:
        db_conn.rollback()
        return redeem_reward_txn(db_conn, cursor, reward_id, child_account_id)
    else:
        return remaining_points


# GetRedeemedRewardsChild
@app.route("/api/children/rewards/history", methods=["POST"])
def get_redeemed_rewards_child():
    body = request.json
    validate_request_body(["GoogleAccountId"], body)
    _, cursor = get_db_conn()

    try:
        cursor.execute(
            queries.get_reward_redemption_by_child(), body["GoogleAccountId"]
        )
    except Exception as exc:
        abort(404, str(exc))

    return RewardRedemptionHistoryChildResponseModel(cursor.fetchall()).get_response()


def validate_request_body(fields: list, body: dict) -> bool:
    if not all(f in body for f in fields):
        abort(400, "Incomplete request body")


def get_parent_id_for_child(cursor: pyodbc.Cursor, child_account_id: str) -> str:
    """Gets the corresponding parent id for a child's id."""
    try:
        cursor.execute(
            queries.get_child_by_account_id(("ParentGoogleAccountId",)),
            child_account_id,
        )
    except Exception as exc:
        abort(400, str(exc))

    child_res = cursor.fetchone()
    if not child_res:
        abort(404, "Child Account ID not found")

    return child_res.ParentGoogleAccountId


# cursor.execute("Select * from Parents")
# res = cursor.fetchall()
# print(res)
