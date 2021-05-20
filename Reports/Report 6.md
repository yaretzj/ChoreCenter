# Chore Center Status Report 6:

## Team Report
- ### Past Goals
    - Implement remaining frontend functionality - display chore status, page to view redeemed rewards, disable / remove buttons from child view, add support for parent chore verification
    - Complete backend test suite and add tests for all endpoints
    - Google OAuth integration for request authorization 
    - Create user sessions on login

- ### Progress and Issues
    - **Progress**
        - Created detailed backend tests for our API endpoints
        - Added documentation for users on how to use our app and report bugs
        - Improved API Specification document
        - Added pages to the mobile app to view redemption history and chore history
        - Improve mobile app design and UI
        - Added JUnit tests for frontend 
        - Implemented a new navigation page for kids
        - Added github issues for all planned feature and high-priority labels to features that need to be implemented before final release
    - **Issues**
        - None

- ### Plans and Goals
    - Finish refactoring backend test data and script for table actions
    - Change UpdateChore APIs to deposit points into child account once chore is verified by parent
    - Add endpoints to delete rewards and chores
    - Add delete chore and reward functionality to mobile app
    - Continue implementing and deploying the high-priority github issues


## Contributions of individual team members:

- ### Phi
    - #### Past Goals
        - Work on create_tables.py with Jaden to be a multi-purpose script for the tables
        - Create more detailed tests for API endpoints

    - #### Progress and Issues
        - **Progress**
            - Wrote user documentation and wiki for project
            - Changed python script to have the option to create our tables, populate them with data, or drop them
        - **Issues**
            - None
    
    - #### Plans and Goals
        - Refactor test data for use in our python script
        - Implement any necessary changes for frontend to do their work

- ### Dishad
    - #### Past Goals
        - Refactor backend to make it more modular 
        - Authenticate requests with Google OAuth endpoints using the user's GoogleIdToken
        - Work on creating user sessions
        - Write tests for all APIs

    - #### Progress and Issues
        - **Progress**
            - Refactored backend tests by splitting tests into files by APIs 
            - Updated test documentation
            - Wrote unit tests for APIs 
            - Updated test data as required for the unit tests
            - Added more detailed response codes API Specification document
        - **Issues**
            - None
    
    - #### Plans and Goals
        - Update the UpdateChoreParent and UpdateChoreChild APIs to deposit points into child account once chore is verified by parents
        - Add documentation for and implement APIs for deleting chores and rewards
        - Features and tests are being prioritized over security for now - Google OAuth, user sessions will likely become stretch goals 


- ### Jaden
    - #### Past Goals
        - Deploy the Flask server on a more permanent Azure resource 
        - Alter database script to be more flexible with argument tags
        - Create unit tests for backend endpoints 

    - #### Progress and Issues
        - **Progress**
            - Added Status of chore to child end
            - Fixed some of the test data
            - Added documentation for backend readme
        - **Issues**
            - Had to reevaluate the importance of goals so I sidelined all my backend goals and tasks because they were not as imperative as some frontend work

    - #### Plans and Goals
        - Add a dialog box skeleton for confirming submissions
        Help with adding delete functionality
        
        
- ### Truc
    - #### Past Goals
        - Organize the adapter for submit, figure out how to make cardview disappear
        - Set up a similar for redeem page.

    - #### Progress and Issues
        - **Progress**
            - Complete chore page is working as expected
        - **Issues**
            - New design has conflict with the master branch
    
    - #### Plans and Goals
        - Add more functions such as redemption history, and chore history in kid page
        - Make the design look better 
        - Draw out the logo and set it up. Stretch goal: draw instruction for getting started page


- ### Yixiao
    - #### Past Goals
        - Create parent VerifyChore activity or implement history activities including ChoreHistory and RedeemHistory by May 17th.

    - #### Progress and Issues
        - **Progress**
            - Implemented parent VerifyChore activity
            - Implemented parent/child account name and child points on navigation page header.
            - Added JUnit tests
        - **Issues**
            - None
    
    - #### Plans and Goals
        - Look into using Android Calendar View for the stretch goal of adding the starting/expiration time for creating chores.
        - Work on delete chore/reward functionalities stretch goal or higher priority issues by May 25.
        - General maintenance of fixing bugs and improve code by May 25.

- ### Matthew
    - #### Past Goals
        - Create a separate home page for kids
        - Add pop up message for createChore/createReward server responses to make it consistent with submitChore
        - Make createChore/createReward use a box for description

    - #### Progress and Issues
        - **Progress**
            - Implemented a basic home page for kids
            - Implemented a RewardHistory page for parents to view what rewards their child has redeemed
        - **Issues**
            - Since the RewardHistory page was a high priority I did not implement the ui changes to createChore/createReward I was planning to.

    - #### Plans and Goals
        - Add snackbar message for createChore/createReward server responses to make it consistent with submitChore
        - Make createChore/createReward use a box for description
        - Improve the format of the items in the RewardHistory page
        - Add missing buttons to the home pages

