# Chore Center Status Report 3:

## Team Report
- ### Past Goals
    - Backend team works with frontend team to implement Google login page
    - Complete the first draft of the API Specification 
    - Backend team sets up SQL Server tables (SQL queries and prepared statements setup for stretch goal)
    - Frontend team will implement creating chores functionality and more pages for app (edit, history, redeem for stretch goals)
    - If time permits, start thinking about architecture and design of the Flask backend

- ### Progress and Issues
    - **Progress**
        - Backend Team set up Azure resource for SQL Server, created, and configured the database
        - Created a sql file for creating database tables
        - Completed API Spec and ER Diagram
        - Configure development environment for pyodbc Python development - Completed setup required to get python to interface with SQL server to send queries - installed required drivers and created database connection in python script 
    - **Issues**
        - Now considering Google login as stretch goal for MVP
        - MacOS updates cause Android emulator to stop working

- ### Plans and Goals
    - Backend team creates queries for API
    - Start setting up API endpoints with flask
    - Choose and set up testing framework


## Contributions of individual team members:

- ### Phi
    - #### Past Goals
        - Help set up Flask framework
        - Implement Google login

    - #### Progress and Issues
        - **Progress**
            - Worked on setting up server on Azure and initializing tables
            - Installed dependencies for connecting to database with Python
            - Submitting weekly reports
        - **Issues**
            - Figuring out if any changes have to be made to database since Google OAuth may be too complicated to add for now
    
    - #### Plans and Goals
        - Write SQL queries in Python using PyPika for the API
        - Set up some tests and test data for these to make sure they query the correct data

- ### Dishad
    - #### Past Goals
        - Complete and finalize the ER diagram for the database - remaining details are cardinalities of relationships and relationship attributes (if any)
        - Work with the frontend team to finalize remaining API endpoints required and define meaningful response status codes in case of failures/errors
        - Start work on creating tables in SQL database
        - If time permits, start thinking about architecture and design of the Flask backend

    - #### Progress and Issues
        - **Progress**
            - completed the ER diagram
            - completed the API specification
            - had a meeting with the backend team to complete the Azure setup and create the database, write the create-tables sql file, create the tables in SQL Server
            - Configured local development environment for pyodbc python development and got python to interface with SQL server to send queries and receive data
        - **Issues**
            - configuring pyodbc development environment was complicated and installing required drivers required updating many other local libraries including XCode on mac
    
    - #### Plans and Goals
        - Start setting up flask API endpoints
        - Create objects for the HTTP request and response body and start writing SQL queries using pypika 
        - Create project in Google developer console to get credentials and keys required for Google OAuth login


- ### Jaden
    - #### Past Goals
        - Get the Google Sign API sorted
        - Help backend setup in Flask
        - Possibly work on Prepared statements if have extra time

    - #### Progress and Issues
        - **Progress**
            - Set up SQL Server in Azure and put tables in
            - Got Python to interact and retrieve data from database
            - Worked on Architecture and Design Doc
        - **Issues**
            - Google Signin API is now a stretch goal so we are implementing a more basic login for the MVP
            - Realizing that we many not have enough time to complete by deadlines just due to size of project

    - #### Plans and Goals
        - Work on prepared statements for database queries
        - Test the prepared statements
        - Coordinate with Frontend team


- ### Truc
    - #### Past Goals
        - Create feedback section for design in document, I will change the design based on that
        - Goals: finish designing the function’s page of parent and kid

    - #### Progress and Issues
        - **Progress**
            - Finished the design for function’s page of parent and kid
            - Set up android studio and start on implementation of accept chore page 
        - **Issues**
    
    - #### Plans and Goals
        - Implement accept chore page for kid
        - Implement the ui design for home page of parent

- ### Yixiao
    - #### Past Goals
        - Getting the Google authentication activity working as expected by Apr 26
        - Implement createChore and redeemReward activities by Apr 27 and work on stretch goals (choreHistory and rewardHistory activities)

    - #### Progress and Issues
        - **Progress**
            - Created view and flow for CreateReward and RedeemReward, SubmitChore activities
        - **Issues**
            - Google authentication is still not working. Need to use mitigation plan or after checking if we can set up a Google API Console project for the authentication
    
    - #### Plans and Goals
        - Work on implementing and testing CreateReward and RedeemReward and complete by May 4th
        - Work on SubmitChore for stretch goal


- ### Matthew
    - #### Past Goals
        - Create basic functionality for a CreateReward activity Apr 27
        - Implement different menu navigations for the parent and the child
        - Add buttons to the main navigation page

    - #### Progress and Issues
        - **Progress**
            - Implemented basic functionality for CreateReward
            - Added the buttons to the main navigation page
            - Adjusted CreateChore to allow the user to give a description for the chore
            - Created a plan for implementing the different menu navigations but have not finished implementing them
        - **Issues**

    - #### Plans and Goals
        - Finish implementing the different menu navigations for the parent and the child
        - With extra time create requests to the server for createChore/createReward
