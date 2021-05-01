from pypika import Query, Table

# Setup: run pip install pypika
# To convert queries into raw SQL strings, use str(q) or q.get_sql()

parents = Table("Parents")
children = Table("Children")
chores = Table("Chores")
rewards = Table("Rewards")
rewardRedemptionHistory = Table("RewardRedemptionHistory")


def create_parent(name, email, token_id, account_id):
    query = (
        Query.into(parents)
        .columns("Name", "Email", "GoogleTokenId", "GoogleAccountId")
        .insert(name, email, token_id, account_id)
    )
    return query


def create_child(name, email, token_id, account_id, parent_code):
    # TODO: add columns statement here
    query = Query.into(children).insert(name, email, token_id, account_id, parent_code)
    return query


def create_chore(parent_account_id, name, desc, status, assigned, points):
    query = (
        Query.into(chores)
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
        Query.into(rewards)
        .columns("ParentGoogleAccountId", "Name", "Description", "Points")
        .insert(parent_account_id, name, desc, points)
    )
    return query


# Test to show example generated SQL string
print(create_parent("John Doe", "jdoe@uw.edu", "333", "1234").get_sql())
