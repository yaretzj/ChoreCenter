from pypika import Query, Table, Field

# Setup: run pip install pypika
# To convert queries into raw SQL strings, use str(q) or q.get_sql()

parents = Table("Parents")
children = Table("Children")
chores = Table("Chores")
rewards = Table("Rewards")
rewardRedemptionHistory = Table("RewardRedemptionHistory")


def create_parent(name, email, token_id, account_id):
    q = Query.into(parents).insert(name, email, token_id, account_id)
    return q


def create_child(name, email, token_id, account_id, parent_code):
    q = Query.into(children).insert(name, email, account_id, parent_code)
    return q



# Test to show example generated SQL string
print(create_parent("John Doe", "jdoe@uw.edu", "333", "1234").get_sql())
