### Onboarding Instructions
- Requirements python 3.7+, pip, Microsoft ODBC driver for SQL Server
- ODBC Driver Installation Instructions:
    - Mac: https://docs.microsoft.com/en-us/sql/connect/odbc/linux-mac/install-microsoft-odbc-driver-sql-server-macos?view=sql-server-ver15
    - Windows: https://docs.microsoft.com/en-us/sql/connect/odbc/windows/system-requirements-installation-and-driver-files?view=sql-server-ver15#installing-microsoft-odbc-driver-for-sql-server
- You can check your python version using `python3 --version` (the python3 command points to the python 3 installation on my machine, replace python3 and pip3 in all commands if the python3 command gives you an error on your machine. If `python3 --version` does not work and `python --version` outputs Python 2.x.x you need to install Python 3. Instructions at https://www.python.org/downloads/).
- Create a virtual environment in the Backend directory with the command `python3 -m venv venv`
- Use the command `. venv/bin/activate` to activate the virtual environment. You can deactivate the virtual environment with the command `deactivate` when you are done using the application.
- You will need to install the following packages using pip with the command `pip3 install <package_name>` [TODO: will be automated with Anaconda in the future]
    - flask
    - pyodbc
    - pypika
    - python-dotenv
    - pytest
- If you are developing on the backend, you will also want to install `pylint` and `black`. Use black to format files before pushing code to GitHub
- Create a local copy of the `.env.example` file using the command `cp .env.example .env`. Replace the placeholders after the `=` sign with the corresponding config values. Reach out to a member of the backend team if you need the env variable strings.
- Before you can run the server you will need to export the `FLASK_APP` environment variable to tell your terminal the application to work with with the command `export FLASK_APP=app.py`
- Run the flask server with the command `flask run`. The server runs on port 5000 by default. If you want to run the server on a different port, export an environment variable named `FLASK_RUN_PORT` and set it to the desired port number. If set, `flask run` will run the server on the port number defined in `FLASK_RUN_PORT`.

### Deploying Instructions
- Do all the above instructions above to set up the server
- Set the `FLASK_RUN_PORT` to 80 to connect via http
- Run flask using the `flask run --host=0.0.0.0` to then make the server visable to the external network