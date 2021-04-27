import pyodbc
import os
from dotenv import dotenv_values

config = dotenv_values()
# Need to install Microsoft ODBC driver for SQL Server
# Mac: https://docs.microsoft.com/en-us/sql/connect/odbc/linux-mac/install-microsoft-odbc-driver-sql-server-macos?view=sql-server-ver15
# Windows: https://docs.microsoft.com/en-us/sql/connect/odbc/windows/system-requirements-installation-and-driver-files?view=sql-server-ver15#installing-microsoft-odbc-driver-for-sql-server

# Some other example server values are
# server = 'localhost\sqlexpress' # for a named instance
# server = 'myserver,port' # to specify an alternate port

server = config['DB_SERVER']
database = config['DB_NAME']
username = config['DB_USERNAME']
password = config['DB_PASSWORD']

cnxn = pyodbc.connect('DRIVER={ODBC Driver 17 for SQL Server};SERVER='+server+';DATABASE='+database+';UID='+username+';PWD='+ password)
cursor = cnxn.cursor()
lel = cursor.execute("""select * from Parents""")
row = cursor.fetchone()
cnxn.commit()
print(row)