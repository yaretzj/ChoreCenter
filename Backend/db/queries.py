from pypika import Query, Tables, Parameter, Field

# Setup: run pip install pypika
# To convert queries into raw SQL strings, use str(q) or q.get_sql()

(
    parents_table,
    children_table,
    chores_table,
    rewards_table,
    redemption_history_table,
) = Tables("Parents", "Children", "Chores", "Rewards", "RewardRedemptionHistory")


##########################
#  SQL insert statements #
##########################


def create_parent() -> str:
    """Special query for returning the name, email, and parent code on insertion.
    pypika does not support the OUTPUT clause so this query cannot be generated with pypika."""
    return """Insert into Parents(Name, Email, GoogleTokenId, GoogleAccountId) \
              OUTPUT INSERTED.Name, INSERTED.Email, INSERTED.ParentCode Values (?, ?, ?, ?);"""


def create_child() -> str:
    """Insert a new child with the listed fields."""
    query = (
        Query.into(children_table)
        .columns(
            "Name", "Email", "ParentGoogleAccountId", "GoogleTokenId", "GoogleAccountId"
        )
        .insert(
            Parameter("?"),
            Parameter("?"),
            Parameter("?"),
            Parameter("?"),
            Parameter("?"),
        )
    )
    return query.get_sql()


def create_chore() -> str:
    """Insert a new chore with the listed fields."""
    query = (
        Query.into(chores_table)
        .columns(
            "ParentGoogleAccountId",
            "Name",
            "Description",
            "Status",
            # "AssignedTo",
            "Points",
        )
        .insert(
            Parameter("?"),
            Parameter("?"),
            Parameter("?"),
            Parameter("?"),
            Parameter("?"),
            # Parameter("?"),
        )
    )
    return query.get_sql()


def create_reward() -> str:
    """Insert a new reward with the listed fields."""
    query = (
        Query.into(rewards_table)
        .columns("ParentGoogleAccountId", "Name", "Description", "Points")
        .insert(Parameter("?"), Parameter("?"), Parameter("?"), Parameter("?"))
    )
    return query.get_sql()


def create_reward_history() -> str:
    """Insert a new reward history record with the listed fields."""
    query = (
        Query.into(redemption_history_table)
        .columns("RewardId", "ChildGoogleAccountId", "ParentGoogleAccountId")
        .insert(Parameter("?"), Parameter("?"), Parameter("?"))
    )
    return query.get_sql()


##########################
#  SQL select statements #
##########################


def get_child_by_account_id(columns: tuple = ("*",)) -> str:
    """Return the columns passed in where the children's
    google account id matches a given parameter."""
    query = (
        Query.from_(children_table)
        .select(*columns)
        .where(children_table.GoogleAccountId == Parameter("?"))
    )
    return query.get_sql()


def get_parent_by_account_id(columns: tuple = ("*",)) -> str:
    """Return the columns passed in where the parent's
    google account id matches a given parameter."""
    query = (
        Query.from_(parents_table)
        .select(*columns)
        .where(parents_table.GoogleAccountId == Parameter("?"))
    )
    return query.get_sql()


def get_parent_by_code() -> str:
    """Return a parent row with the given parent code."""
    query = (
        Query.from_(parents_table)
        .select("*")
        .where(parents_table.ParentCode == Parameter("?"))
    )
    return query.get_sql()


def get_chores_by_parent() -> str:
    """Return the chores for the parent requesting (checks chore's parent account id)."""
    query = (
        Query.from_(chores_table)
        .select("*")
        .where(chores_table.ParentGoogleAccountId == Parameter("?"))
    )
    return query.get_sql()


def get_chore_by_id() -> str:
    """Returns query to get the chore for given ChoreId."""
    query = (
        Query.from_(chores_table)
        .select("*")
        .where(chores_table.ChoreId == Parameter("?"))
    )
    return query.get_sql()


def get_rewards_by_parent() -> str:
    """Return the rewards for the parent requesting."""
    query = (
        Query.from_(rewards_table)
        .select("*")
        .where(rewards_table.ParentGoogleAccountId == Parameter("?"))
    )
    return query.get_sql()


# Get rewards by child is implemented in app.py through getting the parent's
# id from the child's id query and using that in get_rewards_by_parent.


def get_reward_redemption_by_parent() -> str:
    """Return the reward history for the parent requesting."""
    query = (
        Query.from_(redemption_history_table)
        .from_(rewards_table)
        .from_(children_table)
        .select(
            redemption_history_table.RewardId,
            redemption_history_table.RedeemedTime,
            rewards_table.Name,
            rewards_table.Description,
            (children_table.Name).as_("ChildName"),
        )
        .where(redemption_history_table.ParentGoogleAccountId == Parameter("?"))
        .where(redemption_history_table.RewardId == rewards_table.RewardId)
        .where(
            children_table.GoogleAccountId
            == redemption_history_table.ChildGoogleAccountId
        )
    )
    return query.get_sql()


def get_reward_redemption_by_child() -> str:
    """Return the reward history for the child requesting."""
    query = (
        Query.from_(redemption_history_table)
        .from_(rewards_table)
        .select(
            redemption_history_table.RewardId,
            redemption_history_table.RedeemedTime,
            rewards_table.Name,
            rewards_table.Description,
        )
        .where(redemption_history_table.RewardId == rewards_table.RewardId)
        .where(redemption_history_table.ChildGoogleAccountId == Parameter("?"))
    )
    return query.get_sql()


def get_reward_redemption_by_reward() -> str:
    """Return the reward history for a given reward."""
    query = (
        Query.from_(redemption_history_table)
        .select("*")
        .where(redemption_history_table.RewardId == Parameter("?"))
    )
    return query.get_sql()


def get_reward_by_id() -> str:
    query = (
        Query.from_(rewards_table)
        .select("*")
        .where(rewards_table.RewardId == Parameter("?"))
    )
    return query.get_sql()


##########################
#  SQL update statements #
##########################


def update_child_points() -> str:
    """Update the amount of points for a child (given account id) by the given amount."""
    query = (
        Query.update(children_table)
        .set(children_table.Points, Parameter("?"))
        .where(children_table.GoogleAccountId == Parameter("?"))
    )
    return query.get_sql()


def update_chore(columns: list) -> str:
    """Update the status for a chore (given id) to the given status."""
    query = Query.update(chores_table).where(chores_table.ChoreId == Parameter("?"))
    for column in columns:
        query = query.set(Field(column), Parameter("?"))
    return query.get_sql()


# Test to show example generated SQL string
# print(get_child_by_account_id(("Name", "Points")))
# print(get_rewards_by_parent())
# print(get_reward_redemption_by_parent())
# print(update_chore_status(["Status", "AssignedTo"]))
