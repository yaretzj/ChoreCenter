# Chore Center Status Report 5:

## Team Report
- ### Past Goals
    - Bridging the gap between frontend and backend to implement functionality for our beta release and use case
    - Create some mock data that we can use for testing
    - Expand documentation for setting up our project

- ### Progress and Issues
    - **Progress**
        - Added testing documentation
        - Completed MVP for beta release
        - Implemented all required API endpoints
        - Recorded beta release demo and reflection
    - **Issues**
        - Bots are spamming our production azure server which sometimes causes it to crash

- ### Plans and Goals
    - Implement remaining frontend functionality - display chore status, page to view redeemed rewards, disable / remove buttons from child view, add support for parent chore verification
    - Complete backend test suite and add tests for all endpoints
    - Google OAuth integration for request authorization 
    - Create user sessions on login


## Contributions of individual team members:

- ### Phi
    - #### Past Goals
        - Add some of the PyPika equivalents for complex SQL queries
        - Update project documentation and setup instructions
        - Work on whatever functionality is needed for the beta release next week

    - #### Progress and Issues
        - **Progress**
            - Wrote more PyPika queries
            - Filled out top-level and backend README documentation
            - Created beta demo scripts and presentation
        - **Issues**
            - None
    
    - #### Plans and Goals
        - Work on create_tables.py with Jaden to be a multi-purpose script for the tables
        - Create more detailed tests for API endpoints

- ### Dishad
    - #### Past Goals
        - Finish the remaining 2 endpoints needed for MVP and beta release
        - Start looking into authenticating requests with Google OAuth endpoints using the user's GoogleIdToken (after beta release)
        - Work on creating user sessions so only an initial login request needs to be authenticated with Google rather than every request made to the APIs
        - Improve create table python script to also support dropping all tables

    - #### Progress and Issues
        - **Progress**
            - Implemented the two UpdateChore APIs needed for beta release MVP
            - Implemented functionality to deposit points into child account once chore is submitted
            - Worked on enhancing project documentation and github READMEs
            - Worked on script and slides for beta release and recorded the video
        - **Issues**
            - None
    
    - #### Plans and Goals
        - Refactor backend to make it more modular 
        - Authenticate requests with Google OAuth endpoints using the user's GoogleIdToken
        - Work on creating user sessions
        - Write tests for all APIs

- ### Jaden
    - #### Past Goals
        - Readme documentation for VM
        - Get VM visible for http outside communication
        - Create test accounts on SQL server for mock data
        - Test server and endpoints with Postman
        - Work on anaconda environment if time permits

    - #### Progress and Issues
        - **Progress**
            - Added documentation for Azure component installation and preparation 
            - VM is visible and can be communicated with via http request
            - Created test data
            - Tested endpoints with Postman
            - Filmed and edited beta video
        - **Issues**
            - The VM server sometimes gets caught up on requests and doesn't close database connection

    - #### Plans and Goals
        - Deploy the Flask server on a more permanent Azure resource 
        - Alter database script to be more flexible with argument tags
        - Create unit tests for backend endpoints 

- ### Truc
    - #### Past Goals
        - Make other buttons clickable in kid menu navigation
        - Get Submit Chore send request to the backend server

    - #### Progress and Issues
        - **Progress**
            - Have the submit chore page show the chore. 
            - Redeem page is clickable
        - **Issues**
            - Struggle in organizing the adapter and submit chore function as well as the design with cardview and submit button
            - Need to figure out how to make the cardview disappear when submit button is clicked
            - Issue with google login, but it resolves as we created email account for parent/kid
    
    - #### Plans and Goals
        - Organize the adapter for submit, figure out how to make cardview disappear
        - Set up a similar for redeem page.

- ### Yixiao
    - #### Past Goals
        - Integrate components with others to test the app as a whole and finish all the http request handling by May 10th.

    - #### Progress and Issues
        - **Progress**
            - Updated README.md for the frontend directory.
            - Integrated all the finished activities of user interface together for allowing the basic workloads which satisfied a few use cases.
            - Implemented user sign up activities
        - **Issues**
            - Needed to add some visual interaction and mechanism for better user experience such as hiding buttons in SubmitChore activity.
    
    - #### Plans and Goals
        - Create parent VerifyChore activity or implement history activities including ChoreHistory and RedeemHistory by May 17th.

- ### Matthew
    - #### Past Goals
        - Get the createChore/createReward to send requests to the server and handle errors

    - #### Progress and Issues
        - **Progress**
            - Implemented createChore/createReward functionality to send/receive requests to the server
        - **Issues**
            - Had some issues setting up Google login for my emulator but it’s working now.

    - #### Plans and Goals
        - Create a separate home page for kids
        - Add pop up message for createChore/createReward server responses to make it consistent with submitChore
        - Make createChore/createReward use a box for description
