## Android User Interface

### Instructions

- clone the top level repository to a local directory.
- use Android Studio to 'Open an Existing Project' from 'ChoreCenterApp' directory under the 'Android' directory.
- The development team used Android Studio with Android SDK version 29. The minimum version of the Android Operating System required to run the app is set to be SDK 21 (Lollipop) which has 94.1% of the market share.
- After opening the project, wait until Android Studio is ready for building and running the app on an Android emulator with SDK version at least Lollipop. (If there are no availalbe emulators, create one from the AVD Manager.)

Directory Structure:

    .
    ├── ChoreCenterApp 
    │   ├── app.src
    │   │   ├── main              # source code
    │   │   ├── AndroidTest       # Android tests
    │   │   └── test              # JUnit tests
    │   ├── gradle
    │   └── ...  
    └── ...
    

### UI

- After clicking "Get Started!", user will be directed to choose their account type to be either a "parent" or a "kid."
- If the user has not been registered yet, he or she will be directed to a page for signing up. The kid account requires input of the parent secret message during the signup step in order to be linked to the specific parent account.
- A parent account will have the ability to create chores and rewards while a kid account is used to submit chores and redeem rewards.
  - Chores and rewards created by a parent account are visible to the linked kid account for submit or redeem.
  - Kids are expected to be honest and only submit a chore after they have finished it but parents can verify that their kids have indeed done the chores that are submitted.
  - Points earned by kids from submitted chores can be used to redeem exciting rewards customized by their parents.
