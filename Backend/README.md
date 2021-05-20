# Backend Overview
### API Specification:
https://docs.google.com/document/d/1tn0H75jkZW10amI4bwc_tJ53w9WVDflUd25YEO0T5ks/edit?usp=sharing
### Database Schema ER Diagram
https://drive.google.com/file/d/1szlRbngpRR0erHz1WED1otbmevpOqY_I/view

### Directory Structure:

    Backend
    ├──── app.py          # main script that runs the Flask server
    ├──── db              # .sql files to create database tables, triggers, and insert test data, and migration script
    ├──── test            # pytest unit tests
    ├──── models          # HTTP response model classes
    └──── ...
    

## Onboarding Instructions

### Running the Flask Server Locally
- Requirements: python 3.7+, pip, Microsoft ODBC driver for SQL Server
- ODBC Driver Installation Instructions:
    - Mac: https://docs.microsoft.com/en-us/sql/connect/odbc/linux-mac/install-microsoft-odbc-driver-sql-server-macos?view=sql-server-ver15
    - Windows: https://docs.microsoft.com/en-us/sql/connect/odbc/windows/system-requirements-installation-and-driver-files?view=sql-server-ver15#installing-microsoft-odbc-driver-for-sql-server
- You can check your python version using `python3 --version` (the python3 command points to the python 3 installation on my machine, replace python3 and pip3 in all commands if the python3 command gives you an error on your machine. If `python3 --version` does not work and `python --version` outputs Python 2.x.x you need to install Python 3. Instructions at https://www.python.org/downloads/).
- Create a virtual environment in the Backend directory with the command `python3 -m venv venv`.
- Use the command `. venv/bin/activate` on Mac or `. venv/Scripts/activate` on Windows to activate the virtual environment. You can deactivate the virtual environment with the command `deactivate` when you are done using the application.
- You will need to install the packages for the project using pip with the command `pip install -r requirements.txt`. The packages can be viewed in `requirements.txt`.
- Create a local copy of the `.env.example` file using the command `cp .env.example .env`. Replace the placeholders after the `=` sign with the corresponding config values and credentials. You will need these to connect to the Azure database server. 
- __Follow the instructions below to set up an Azure SQL Server Database and generate your own credentials or reach out to a member of the backend team for the env variable strings.__
- Before you can run the server you will need to export the `FLASK_APP` environment variable to tell your terminal the application to work with with the command `export FLASK_APP=app.py`.
- Run the flask server with the command `flask run`. The server runs on port 5000 by default. If you want to run the server on a different port, export an environment variable named `FLASK_RUN_PORT` and set it to the desired port number. If set, `flask run` will run the server on the port number defined in `FLASK_RUN_PORT`.

### Setting Up Azure
#### Recommended Method: Setting up your own database is complicated. Reach out to us on Slack for the env database credentials to use ours.
- Login to Azure and select your payment plan.
- Create a Resouce Group.
- Make sure to use this resource group throughout your set up.

#### Setting up SQL Server Database
Based off of tutorial from CSE 344
- Create a new server by clicking on "Server". A second panel will appear to the right. Fill in the form as follows:
- Choose a name for the server (e.g., "foobarsqlserver"). Unlike your database name, the server name must be unique across the universe of Azure SQL databases.
- Choose an admin login and password
- Set the location based on where you are at..
- Click "OK"
- Make sure "Want to use SQL elastic pool?" is set to "No".
- Under "Compute + storage", click "Configure database". A second panel will open to the right. On this form,
- Click "Looking for basic, standard, premium?"
- Select "Standard".
- Check that DTUs are set to 10, max data size to 250 GB (this is the current default setting). It should now say the monthly cost is only $15/month.
- Click "Apply".
- Select "Next: Networking", which brings you to another panel.
- Select "Connectivity method" : Public endpoint
- Under Firewall rules, make sure "Allow azure services to access server" is set to "yes".
- Make sure "Advanced Threat Protection" is set to "Not now".
- Click "Review + create"
- Double-check that your settings are correct, and click "Create". This may take a few minutes to deploy.
- Once it's created, select your new database. Select the pushpin icon to "Pin to dashboard" so you can easily find it in the future.
- Finally, click "Set server firewall" at the top of the page. You may need to change the settings before you can upload data. The easiest option is to add a rule that allows connections from any client, which you can do from below. Be sure to click "Save" once you have added this rule.

#### Virtual Machine for Deployment
- Follow this tutorial for the Virtual Machine and set up according to suitable pricing: https://docs.microsoft.com/en-us/azure/virtual-machines/windows/quick-create-portal 
Cheapest tier (B1s) is good enough for testing project.
- Connect to the VM and now download the most recent version of Python3
- Install all the librarys in the requirements.txt in the backend folder as described below

### Deploying Instructions
- Do all the above instructions to set up the server.
- Go into the Backend directory in your command prompt
- Set the `FLASK_RUN_PORT` to 80 to connect via HTTP.
- Run flask using the `flask run --host=0.0.0.0 --with-threads` to then make the server visible to the external network.
- Other release deployment options can be found in the [Flask Deployment documentation](https://flask.palletsprojects.com/en/2.0.x/deploying/index.html)


## Testing Instructions

### How does Chore Center's backend test suite work?

Chore Center uses [Pytest](https://docs.pytest.org/en/6.2.x/#). To run the backend test suite, run `pytest` in any directory. The virtual environment must be activated before running the test suite.

We are using [Github Actions](https://github.com/features/actions) for our CI/CD. Currently, on a push request `pytest` will be automatically run to check the unit tests.

### How do I add a new test?

1. Make sure your system is properly configured to run the existing tests (`Backend/README.md`), as you'll need them to run the Flask tests.
2. Review the [Pytest Documentation](https://docs.pytest.org/en/6.2.x/contents.html#toc) and use the existing tests as references for how to structure your new tests.
3. Locate and open the `Backend/tests` directory.
4. The `conftest.py` file contains all the test fixtures used by the tests.
5. Tests are separated into files by endpoint. Add new tests for an endpoint to the corresponding file (eg: add tests for the `CreateChild` endpoint to the `test_create_child.py` file). If adding a new endpoint, create a new test file with tests for the endpoint. 
6. Write the necessary test(s) for the new or revised functionality.
7. The `pytest`command runs the tests in the test suite and prints the results to the console.

### What should a unit test look like?

- Each test should have a unique name clearly stating what unit is being tested.
- Each test should test only one unit per test, although one test can include several assertions. Create multiple tests for multiple units of functionality.
- Each test should use `assert` to ensure something is expected.
- Each test should follow the project's [Python Code Guidelines](https://pep8.org/), which can be checked for by running `pylint` and `black` on the test file.
