from pypika import Query, Tables, Field, Parameter

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
    """Special query for returning the name, email, and parent code on insertion."""
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


def get_child_by_account_id(columns: tuple = ("*")) -> str:
    """Return the columns passed in where the children's
    google account id matches a given parameter."""
    query = (
        Query.from_(children_table)
        .select(*columns)
        .where(children_table.GoogleAccountId == Parameter("?"))
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


def get_chores_by_child() -> str:
    """Return the chores for the child requesting (checks chore's child account id)."""
    query = (
        Query.from_(chores_table)
        .select("*")
        .where(chores_table.AssignedTo == Parameter("?"))
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


def get_rewards_by_child() -> str:
    """Return the rewards for the child requesting."""
    # TODO: determine subquery so can retrieve using children's account id
    query = (
        Query.from_(rewards_table)
        .select("*")
        .where(rewards_table.ParentGoogleAccountId == Parameter("?"))
    )
    return query.get_sql()


# Test to show example generated SQL string
# print(get_child_by_account_id(("Name", "Points")))
# print(get_rewards_by_parent())
