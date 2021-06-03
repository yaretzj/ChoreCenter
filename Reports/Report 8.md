# Chore Center Status Report 8:

## Team Report
- ### Past Goals
    - Improve user interface and improve testing of current working functionalities of the mobile app for final release.
    - Record the presentation of the final release

- ### Progress and Issues
    - **Progress**
        - Recorded final video and presentation
        - Bug fixes 
        - Added more features to frontend
        - Set up instrumented tests for frontend
        - More documentation and comments
    - **Issues**
        - Annoying bugs popped up at end that took a while to squash
- ### Plans and Goals
    - Do reflections, attend last project meeting, and discuss future of Chore Center


## Contributions of individual team members:

- ### Phi
    - #### Past Goals
        - Work on final demo script and presentation

    - #### Progress and Issues
        - **Progress**
            - Finished demo presentation

        - **Issues**
            - Do reflection
    
    - #### Plans and Goals
        - 

- ### Dishad
    - #### Past Goals
        - Refactoring, modularization and potentially more tests 
        - Work on final demo video, script / slides

    - #### Progress and Issues
        - **Progress**
            - Worked on demo & presentation
            - Fixed redeem reward api bug
            - Refactored helpers out of main flask app script
            - Installed app APK and did user testing

        - **Issues**
            - None
    
    - #### Plans and Goals
        - Attend final meeting

- ### Jaden
    - #### Past Goals
        - Flush out the apk 
        - Record the final presentation for the app

    - #### Progress and Issues
        - **Progress**
            - Got a working APK
            - Edited and recorded the presentation
            - Tested the app and stress tested for bugs

        - **Issues**
            - Found some bugs, but got those sorted

    - #### Plans and Goals
        - Do the personal reflection and maybe add a couple small features
        
        
- ### Truc
    - #### Past Goals
        - Add more tests
        - Help to build the apk

    - #### Progress and Issues
        - **Progress**
            - fixed the open issue with delete icon to be same size

        - **Issues**
            - the size is set to be the same, but one looks smaller than the other one in different page. found the issue was related the same layout as the button. 
    
    - #### Plans and Goals
        - personal reflection


- ### Yixiao
    - #### Past Goals
        - Add more tests and handlings of edge cases by May 30th.
        - General maintenance of the mobile app to improve user experience by May 30th.

    - #### Progress and Issues
        - **Progress**
            - Added documentation, comments in frontend code.
            - Implemented alert dialogs in many of the activities to pop up messages and confirmations of action.
            - Updated Espresso instrumented testing.

        - **Issues**
            - The instrumented testing appears to be nondeterministic due to the fact that the emulator operating system may schedule the testing checks before a view pops up on the screen because the activities are asynchronous network requests that are handled in some other threads and take nondeterministic time to complete. The current solution is to add more delay to the checks and the result has become much more stable.
    
    - #### Plans and Goals
        - 

- ### Matthew
    - #### Past Goals
        - Handle edge cases in RewardHistory (empty array/end of stream exception)
        - Add additional tests
        - Add snackbar to createChore/Reward if time permits.

    - #### Progress and Issues
        - **Progress**
            - Notifies user when there is empty array/end of stream exception
            - added Toast to createChore/Reward

        - **Issues**
            - I had an issue with the layout of the snackbar so I used a toast instead.

    - #### Plans and Goals
        - 

