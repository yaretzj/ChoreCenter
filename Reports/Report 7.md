# Chore Center Status Report 7:

## Team Report
- ### Past Goals
    - Finish refactoring backend test data and script for table actions
    - Change UpdateChore APIs to deposit points into child account once chore is verified by parent
    - Add endpoints to delete rewards and chores
    - Add delete chore and reward functionality to mobile app
    - Continue implementing and deploying the high-priority github issues

- ### Progress and Issues
    - **Progress**
        - Implemented and tested endpoints to delete rewards and chores
        - Added detailed documentation for endpoints
        - Added support for chore verification by parent to frontend and backend - points deposited into child account once parent verifies the completed chore
        - Added delete chore and reward functionality for parents to mobile app

    - **Issues**
        - Incorporating Google OAuth requires further investigation which may not be completed by the final release deadline and we decide to temporarily remove authentication which Google API but we are still using Google Login to have users sign in to their accounts

- ### Plans and Goals
    - Improve user interface and improve testing of current working functionalities of the mobile app for final release.
    - Record the presentation of the final release


## Contributions of individual team members:

- ### Phi
    - #### Past Goals
        - Refactor test data for use in our python script
        - Implement any necessary changes for frontend to do their work

    - #### Progress and Issues
        - **Progress**
            - Improved test data and changed some tests to work with it
            - Reworked python script to create tables and populate with data
            - Added comments for app.py endpoints

        - **Issues**
            - None
    
    - #### Plans and Goals
        - Work on final demo script and presentation

- ### Dishad
    - #### Past Goals
        - Update the UpdateChoreParent and UpdateChoreChild APIs to deposit points into child account once chore is verified by parents
        - Add documentation for and implement APIs for deleting chores and rewards
        - Features and tests are being prioritized over security for now - Google OAuth, user sessions will likely become stretch goals 

    - #### Progress and Issues
        - **Progress**
            - Finished implementing updated UpdateChoreParent and UpdateChoreChild APIs to support chore verification by parents and depositing points into child account once  to deposit points into child account once chore is verified by parents
            - Implemented and DeleteChore and DeleteReward APIs to enable parents to delete chores and rewards
            - Wrote tests for the delete endpoints
            - Resolved github issues related to setting up the backend for peer reviewers

        - **Issues**
            - None
    
    - #### Plans and Goals
        - Refactoring, modularization and potentially more tests 
        - Work on final demo video, script / slides


- ### Jaden
    - #### Past Goals
        - Add a dialog box skeleton for confirming submissions
        - Help with adding delete functionality

    - #### Progress and Issues
        - **Progress**
            - Added dialog box
            - Worked on fixing issues for peer review
            - Worked on creating signed apk
            - Tested interface of the new features

        - **Issues**
            - Found a bug in the hint text of the creation feature, but was resolved
            - Signed apk with the google sign in is not wanting to work

    - #### Plans and Goals
        - Flush out the apk 
        - Record the final presentation for the app
        
        
- ### Truc
    - #### Past Goals
        - Add more functions such as redemption history, and chore history in kid page
        - Make the design look better 
        - Draw out the logo and set it up. Stretch goal: draw instruction for getting started page

    - #### Progress and Issues
        - **Progress**
            - The app logo is created
            - Delete chore functionality is working. 
            - Unify the design for chore list page and add delete icon for each chore. 
            - Add chore list button for parent’s page

        - **Issues**
            - Found issue when handling delete endpoint, but issue was fixed
    
    - #### Plans and Goals
        - Add more tests
        - Help to build the apk


- ### Yixiao
    - #### Past Goals
        - Look into using Android Calendar View for the stretch goal of adding the starting/expiration time for creating chores.
        - Work on delete chore/reward functionalities stretch goal or higher priority issues by May 25.
        - General maintenance of fixing bugs and improve code by May 25.

    - #### Progress and Issues
        - **Progress**
            - Updated document and onboarding instructions
            - Added reward list functionality
            - Implemented delete reward functionality
            - Added number of redemptions to child redeem reward activity

        - **Issues**
            - The user interface design needs to be improved as some buttons have different display sizes.
    
    - #### Plans and Goals
        - Add more tests and handlings of edge cases by May 30th.
        - General maintenance of the mobile app to improve user experience by May 30th.


- ### Matthew
    - #### Past Goals
        - Add snackbar message for createChore/createReward server responses to make it consistent with submitChore
        - Make createChore/createReward use a box for description
        - Improve the format of the items in the RewardHistory page
        - Add missing buttons to the home pages

    - #### Progress and Issues
        - **Progress**
            - CreateChore/Reward now uses boxes for the editTexts
            - Made items in the RewardHistory page similar to submit chore items
            - Added missing button to KidHomePage

        - **Issues**
            - There was an issue with the boxes not showing text in dark mode but the issue was easy to fix.

    - #### Plans and Goals
        - Handle edge cases in RewardHistory (empty array/end of stream exception)
        - Add additional tests
        - Add snackbar to createChore/Reward if time permits.


