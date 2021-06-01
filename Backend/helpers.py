"""
Helpers
"""
from flask import abort
from models.response_models import GetRewardsResponseModel, GetChoresResponseModel
import db.queries as queries
import pyodbc


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


def validate_request_body(fields: list, body: dict) -> bool:
    """Check that the request body has all the fields specified in the fields argument."""
    if not all(f in body for f in fields):
        abort(400, "Incomplete request body")


def get_rewards_helper(cursor: pyodbc.Cursor, account_id: str) -> dict:
    """Helper function for GetRewardsParent to get the rewards given a parent account id."""
    try:
        cursor.execute(queries.get_rewards_by_parent(), account_id)
    except Exception as exc:
        abort(500, str(exc))

    return GetRewardsResponseModel(cursor.fetchall()).get_response()


def get_chores_helper(cursor: pyodbc.Cursor, account_id: str) -> dict:
    """Helper function for GetChoresParent to get the chores given a parent account id."""
    try:
        cursor.execute(queries.get_chores_by_parent(), account_id)
    except Exception as exc:
        abort(500, str(exc))

    return GetChoresResponseModel(cursor.fetchall()).get_response()
