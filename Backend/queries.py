from pypika import Query, Table, Field

# Setup: run pip install pypika
# To convert queries into raw SQL strings, use str(q) or q.get_sql()

parents = Table('Parents')
children = Table('Children')
chores = Table('Chores')
rewards = Table('Rewards')
rewardRedemptionHistory = Table('RewardRedemptionHistory')

def createParent(name, email, tokenID, accountID):
    q = Query.into(parents).insert(name, email, tokenID, accountID)
    return q

# Test to show example generated SQL string
print(createParent('John Doe', 'jdoe@uw.edu', '333', '1234').get_sql())