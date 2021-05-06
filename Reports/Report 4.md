# Chore Center Status Report 4:

## Team Report
- ### Past Goals
    - Backend team creates queries for API
    - Start setting up API endpoints with flask
    - Choose and set up testing framework

- ### Progress and Issues
    - **Progress**
        - Github Actions testing pipeline set up for the front and backend teams
        - Basic unit tests written to test app functionality
        - Successful login with Google OAuth attempted
    - **Issues**
        - Setting up our Github Actions CI/CD to work with our test had some tricky parts in the process
        - Frontend team had some troubles setting up the backend server

- ### Plans and Goals
    - Bridging the gap between frontend and backend to implement functionality for our beta release and use case
    - Create some mock data that we can use for testing
    - Expand documentation for setting up our project


## Contributions of individual team members:

- ### Phi
    - #### Past Goals
        - Write SQL queries in Python using PyPika for the API
        - Set up some tests and test data for these to make sure they query the correct data

    - #### Progress and Issues
        - **Progress**
            - Worked on writing PyPika create, select, and update queries for API endpoints
            - Helped set up Github Actions testing framework
            - Submitting weekly reports
        - **Issues**
            - Had some troubles with configuring the Github Actions CI/CD to run properly for the backend tests
    
    - #### Plans and Goals
        - Add some of the PyPika equivalents for complex SQL queries
        - Update project documentation and setup instructions
        - Work on whatever functionality is needed for the beta release next week

- ### Dishad
    - #### Past Goals
        - Start setting up flask API endpoints
        - Create objects for the HTTP request and response body and start writing SQL queries using pypika 
        - Create project in Google developer console to get credentials and keys required for Google OAuth login

    - #### Progress and Issues
        - **Progress**
            - a
        - **Issues**
            - a
    
    - #### Plans and Goals
        - 

- ### Jaden
    - #### Past Goals
        - Work on prepared statements for database queries
        - Test the prepared statements
        - Coordinate with Frontend team

    - #### Progress and Issues
        - **Progress**
            - Added Parent and Child Info endpoints
            - Got VM running for backend server
            - Installed all the packages on VM
            - Hosted frontend and backend meetings
        - **Issues**
            - Some issues on booting up the VM, but got them sorted
            - Getting repo secrets to work

    - #### Plans and Goals
        - Readme documentation for VM
        - Get VM visible for http outside communication
        - Create test accounts on SQL server for mock data
        - Test server and endpoints with Postman
        - Work on anaconda environment if time permits

- ### Truc
    - #### Past Goals
         - Implement accept chore page for kid
        - Implement the ui design for home page of parent

    - #### Progress and Issues
        - **Progress**
            - Implemented main kid page with submit function
            - The design for kid menu navigations is partially done with buttons, only the submit chore is clickable, other buttons aren’t clickable yet. 
            - Design the get started page.
        - **Issues**
            - Couldn’t find the bar design for get started page
            - Need to figure out how to setup connection with backend server
    
    - #### Plans and Goals
        - Make other buttons clickable in kid menu navigation
        - Get Submit Chore send request to the backend server

- ### Yixiao
    - #### Past Goals
        - Work on implementing and testing CreateReward and RedeemReward and complete by May 4th
        - Work on SubmitChore for stretch goal

    - #### Progress and Issues
        - **Progress**
            - Implemented RedeemReward Activity and signup Activity.
            - Separated parent/kid login -> signup -> navigation routines.
            - Got Google authentication working.
        - **Issues**
            - Need to figure out how to set up the backend server and do integration testing through http requests.
    
    - #### Plans and Goals
        - Integrate components with others to test the app as a whole and finish all the http request handling by May 10th.

- ### Matthew
    - #### Past Goals
        - Finish implementing the different menu navigations for the parent and the child
        - With extra time create requests to the server for createChore/createReward

    - #### Progress and Issues
        - **Progress**
            - Changed layout/elements of createChore/createReward
            - Added description functionality
        - **Issues**
            - Some issues setting up the local backend server

    - #### Plans and Goals
        - Get the createChore/createReward to send requests to the server and handle errors
