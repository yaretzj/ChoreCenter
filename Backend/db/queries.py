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


def get_child_by_account_id(columns: tuple = ("*")) -> str:
    """Return the columns passed in where the children's google account id matches a given parameter."""
    query = children_table.select(*columns).where(
        children_table.GoogleAccountId == Parameter("?")
    )
    return query.get_sql()


def get_parent_by_code() -> str:
    query = parents_table.select("*").where(parents_table.ParentCode == Parameter("?"))
    return query.get_sql()


def create_chore(parent_account_id, name, desc, status, assigned, points):
    query = (
        Query.into(chores_table)
        .columns(
            "ParentGoogleAccountId",
            "Name",
            "Description",
            "Status",
            "AssignedTo",
            "Points",
        )
        .insert(parent_account_id, name, desc, status, assigned, points)
    )
    return query


def create_reward(parent_account_id, name, desc, points):
    query = (
        Query.into(rewards_table)
        .columns("ParentGoogleAccountId", "Name", "Description", "Points")
        .insert(parent_account_id, name, desc, points)
    )
    return query


# Test to show example generated SQL string
# print(get_child_by_account_id(("Name", "Points")))
# print(get_parent_by_code(("name", "Email")))
