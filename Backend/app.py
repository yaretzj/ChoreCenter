"""
    Flask Server
"""
from dotenv import load_dotenv
from flask import abort, Flask, g, request
import pyodbc

from typing import Tuple
from helpers import (
    get_chores_helper,
    get_parent_id_for_child,
    get_rewards_helper,
    validate_request_body,
)
from models.response_models import (
    ChoreModel,
    CreateParentResponseModel,
    RewardRedemptionHistoryParentResponseModel,
    RewardRedemptionHistoryChildResponseModel,
    GetParentAccountResponseModel,
    GetChildAccountResponseModel,
)
import db.queries as queries
from db.connection import setup_db_conn


INTIIAL_CHORE_STATUS = "Created"
COMPLETED_CHORE_STATUS = "Completed"
VERIFIED_CHORE_STATUS = "Verified"


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


@app.route("/api/parents/new", methods=["POST"])
def create_parent():
    """CreateParentAccount, Receives a POST request from the client
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
    """GetParentInfo, Receives a POST request from the client
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
    """CreateChoreParent, Receives a POST request from the client
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
    """UpdateChoreParent, Receives a POST request from the client
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
    if len(body) == 0:
        abort(400, "No fields to update")

    try:
        cursor.execute(queries.get_chore_by_id_and_parent(), chore_id, account_id)
    except Exception as exc:
        abort(500, str(exc))

    chore = cursor.fetchone()
    if not chore:
        abort(404, "ChoreId {} does not exist".format(chore_id))

    chore = ChoreModel(*chore)
    if chore.status == VERIFIED_CHORE_STATUS:
        abort(403, "Cannot update chore with status Verified")
    if (
        "Status" in body
        and body["Status"] == VERIFIED_CHORE_STATUS
        and chore.status != COMPLETED_CHORE_STATUS
    ):
        abort(403, "Cannot change chore status to Verified before it is Completed")

    update_chore_parent_txn(chore, body, cursor, db_conn)

    try:
        cursor.execute(queries.get_chore_by_id_and_parent(), chore_id, account_id)
    except Exception as exc:
        abort(500, str(exc))

    return ChoreModel(*(cursor.fetchone())).get_response()


def update_chore_parent_txn(
    chore: ChoreModel, req_body: dict, cursor: pyodbc.Cursor, db_conn: pyodbc.Connection
):
    columns = req_body.keys()
    try:
        cursor.execute(
            queries.update_chore(columns),
            *[req_body[key] for key in columns],
            chore.chore_id
        )
        if "Status" in req_body and req_body["Status"] == VERIFIED_CHORE_STATUS:
            cursor.execute(
                queries.update_child_points(), chore.points, chore.assigned_to
            )
        cursor.commit()
    except Exception as exc:
        print(exc)
        db_conn.rollback()
        update_chore_parent_txn(chore, req_body, cursor, db_conn)
        # abort(500, str(exc))


# GetChoresParent
@app.route("/api/parents/chores", methods=["POST"])
def get_chores_parent():
    """GetChoresParent, Receives a POST request from the client
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


# DeleteChore
@app.route("/api/parents/chores/<chore_id>", methods=["DELETE"])
def delete_chore(chore_id):
    body = request.json
    validate_request_body(["GoogleAccountId"], body)
    _, cursor = get_db_conn()

    try:
        cursor.execute(
            queries.get_chore_by_id_and_parent(), chore_id, body["GoogleAccountId"]
        )
        chore = cursor.fetchone()
    except Exception as exc:
        abort(500, str(exc))

    if not chore:
        abort(404, "Chore not found")

    try:
        cursor.execute(queries.delete_chore(), chore_id, body["GoogleAccountId"])
        cursor.commit()
    except Exception as exc:
        abort(500, str(exc))

    return "Deleted chore"


# CreateReward
@app.route("/api/parents/rewards/new", methods=["POST"])
def create_reward():
    """CreateReward, Receives a POST request from the client
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
    """GetRewardsParent, Receives a POST request from the client
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


# DeleteReward
@app.route("/api/parents/rewards/<reward_id>", methods=["DELETE"])
def delete_reward(reward_id):
    body = request.json
    validate_request_body(["GoogleAccountId"], body)
    _, cursor = get_db_conn()

    try:
        cursor.execute(
            queries.get_reward_by_id_and_parent(), reward_id, body["GoogleAccountId"]
        )
        reward = cursor.fetchone()
    except Exception as exc:
        abort(500, str(exc))

    if not reward:
        abort(404, "Reward not found")

    try:
        cursor.execute(queries.delete_reward(), reward_id, body["GoogleAccountId"])
        cursor.commit()
    except Exception as exc:
        abort(500, str(exc))

    return "Deleted reward"


# GetRedeemedRewards
@app.route("/api/parents/rewards/history", methods=["POST"])
def get_redeemed_rewards_parent():
    """GetRedeemedRewardsParent, Receives a POST request from the client
    that uses the url path "/api/parents/rewards/history" and verifies the
    Google account information and any other additional other fields are valid JSON.
    Using the data received, a Select statement is made to the database and the
    RedeemedRewards associated with that Parent are received from the SQL table.

    If the query was successful,
    returns a response back with RedeemedRewards, list of RedeemedReward infomation as JSON object.
    If Parent account did not exist, RedeemedRewards will be empty.
    If server error, return 500.
    """
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
    """CreateChildAccount, Receives a POST request from the client
    that uses the url path "/api/children/new" and verifies all the
    child information is valid JSON. Using the data received, an
    Insert statement is made to the database and the information
    is added to the SQL table.

    If the insertion was successful,
    returns a response back with a 201 code saying the Child
    was created.
    If incomplete request body, Child account already exists,
    or invalid parent code, return 400.
    If parent code not found, return 404.
    """
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


@app.route("/api/children/info", methods=["POST"])
def get_child():
    """GetChildInfo, Receives a POST request from the client
    that uses the url path "/api/children/info" and verifies all the
    Google account information is valid JSON. Using the data received,
    a Select statement is made to the database and the Child information
    is received from the SQL table.

    If the Child exists and the query was successful,
    returns a response back with child infomation as JSON object.
    If server error, return 500.
    If Child account not found, return 404.
    """

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


@app.route("/api/children/chores", methods=["POST"])
def get_chores_child():
    """GetChoresChild, Receives a POST request from the client
    that uses the url path "/api/children/chores" and verifies the
    Google account information and any other additional other fields are valid JSON.
    Using the data received, a Select statement is made to the database and the
    Chores associated with that Child are received from the SQL table.

    If the query was successful,
    returns a response back with Chores, list of Chore infomation as JSON object.
    If Child account did not exist, Chores will be empty.
    If incomplete request body, return 400.
    If server error, return 500.
    """
    body = request.json
    validate_request_body(["GoogleAccountId"], body)
    _, cursor = get_db_conn()

    return get_chores_helper(
        cursor, get_parent_id_for_child(cursor, body["GoogleAccountId"])
    )


# UpdateChoreChild
@app.route("/api/children/chores/<chore_id>/update", methods=["POST"])
def update_chore_child(chore_id):
    """UpdateChoreChild, Receives a POST request from the client
    that uses the url path "/api/children/chores/<chore_id>/update" and verifies the
    Google account information and any other additional other fields are valid JSON.
    Using the data received, an Update statement is made to the database and the
    new Chore information is updated in the Chore SQL table to the existing chore.

    If the update was successful, returns the updated chore and 200.
    If no additional fields are providied to update the chore, returns 400.
    If Chore Id or Account ID not found, return 404.
    If server error, returns 500.
    """
    body = request.json
    validate_request_body(["GoogleAccountId"], body)
    db_conn, cursor = get_db_conn()

    account_id = body.pop("GoogleAccountId")
    chore_status = body.pop("Status", None)

    if not chore_status:
        abort(400, "No status to update")
    if chore_status != COMPLETED_CHORE_STATUS:
        abort(400, "Invalid/Unauthorized status value")
    if len(body) > 0:
        abort(403, "Unauthorized update fields")

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

    chore = ChoreModel(*chore)
    if chore.status != INTIIAL_CHORE_STATUS:
        abort(
            403,
            "Cannot update chore with status {} to {}".format(
                chore.status, chore_status
            ),
        )

    try:
        db_conn.autocommit = False
        cursor.execute(
            queries.update_chore(("Status", "AssignedTo")),
            chore_status,
            account_id,
            chore_id,
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
    """GetRewardsChild, Receives a POST request from the client
    that uses the url path "/api/children/rewards" and verifies the
    Google account information and any other additional other fields are valid JSON.
    Using the data received, a Select statement is made to the database and the
    Rewards associated with that Child are received from the SQL table.

    If the query was successful,
    returns a response back with Rewards, list of Reward infomation as JSON object.
    If Child account did not exist, Rewards will be empty.
    If server error, return 500.
    """
    body = request.json
    validate_request_body(["GoogleAccountId"], body)
    _, cursor = get_db_conn()

    return get_rewards_helper(
        cursor, get_parent_id_for_child(cursor, body["GoogleAccountId"])
    )


# RedeemReward
@app.route("/api/children/rewards/redeem", methods=["POST"])
def redeem_reward():
    """RedeemReward, Receives a POST request from the client
    that uses the url path "/api/children/rewards/redeem" and verifies
    that the Google account ID and reward ID are valid JSON.
    Using the data received, we check that the account and reward exist, and
    the given Child has enough points in their account to redeem the reward.

    If successful, returns a response back with RemainingPoints as JSON object.
    If Child Account ID or Reward ID does not exist, return 404.
    If insufficient points in the Child's account, return 405.
    """
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
            -int(reward.Points),
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
    """GetRedeemedRewardsChild, Receives a POST request from the client
    that uses the url path "/api/children/rewards/history" and verifies the
    Google account information and any other additional other fields are valid JSON.
    Using the data received, a Select statement is made to the database and the
    RedeemedRewards associated with that Child are received from the SQL table.

    If the query was successful,
    returns a response back with RedeemedRewards, list of RedeemedReward infomation as JSON object.
    If Child account did not exist, RedeemedRewards will be empty.
    If server error, return 500.
    """
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
