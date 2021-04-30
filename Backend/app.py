import pyodbc
from dotenv import dotenv_values
from flask import Flask

config = dotenv_values()

# Need to install Microsoft ODBC driver for SQL Server
# Mac: https://docs.microsoft.com/en-us/sql/connect/odbc/linux-mac/install-microsoft-odbc-driver-sql-server-macos?view=sql-server-ver15
# Windows: https://docs.microsoft.com/en-us/sql/connect/odbc/windows/system-requirements-installation-and-driver-files?view=sql-server-ver15#installing-microsoft-odbc-driver-for-sql-server

# Some other example server values are
# server = 'localhost\sqlexpress' # for a named instance
# server = 'myserver,port' # to specify an alternate port
def setup_db_conn():
    server = config['DB_SERVER']
    database = config['DB_NAME']
    username = config['DB_USERNAME']
    password = config['DB_PASSWORD']

    cnxn = pyodbc.connect('DRIVER={ODBC Driver 17 for SQL Server};SERVER=' /
            + server + ';DATABASE=' + database + ';UID=' + username + ';PWD=' /
            + password)
    return cnxn

app = Flask(__name__)
db_conn = setup_db_conn()

@app.route('/')
def hello_world():
    return 'Hello, World!'

# CreateParentAccount
@app.route('/api/parents/new', methods=['POST'])
def create_parent():
    return 

#CreateChore
@app.route('api/parents/chores/new', methods=['POST'])

# GetChoresParent
@app.route('/api//parents/chores', methods=['POST'])

# CreateReward
@app.route('/api/parents/rewards/new', methods=['POST'])

# GetRewards
@app.route('/api/parents/rewards/', methods=['POST'])

# GetRedeemedRewards
@app.route('/api/parents/rewards/redeemed', methods=['POST'])


#CreateChildAccount
@app.route('api/children/new', methods=['POST'])


#GetChoresChild
@app.route('api/children/chores', methods=['POST'])

@app.route('api/children/new', methods=['POST'])

@app.route('api/children/new', methods=['POST'])
# cursor = cnxn.cursor()
# lel = cursor.execute("""select * from Parents""")
# row = cursor.fetchone()
# cnxn.commit()
# print(row)