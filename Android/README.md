## Android

### Onboarding Instructions
Prerequisites: [get latest Java 11 (or 12, 13) with hotspot JVM](https://adoptopenjdk.net/), [JUnit (we are using 4)](https://junit.org/junit4/), [get latest Android Studio](https://developer.android.com/studio/?gclid=CjwKCAjwkN6EBhBNEiwADVfya9HaDQcwUCBRUhf-a6Bhs6oP9Xt77MWjCXfam5GZOxicAAxxY-gylBoCNPYQAvD_BwE&gclsrc=aw.ds)
1. Clone the top level repository to a local directory.
2. Use Android Studio to `Open an Existing Project` from `ChoreCenterApp` directory under the `Android` directory. Do not open the project from `ChoreCenter`, the top-level directory of the git repository because the `gradle.build` file that is required by the Android Studio IDE to correctly configure the project is under the `ChoreCenterApp` directory.
3. The development team used Android Studio with Android SDK version 29. We recommend that you use the same Android SDK version. The minimum version of the Android Operating System required to run the app is set to be SDK 21 (Lollipop) which has 94.1% of the market share. However, please use a more recent version at least Android SDK 24 (Nougat) since a few dependencies used in the project are not supported by SDK version before 24.
4. After opening the project, wait until Android Studio is ready for building and running the app on an Android emulator (with SDK version at least 24, or Nougat). (If there are no availalbe emulators, create one from the `AVD Manager`. You can follow [this guide](https://developer.android.com/studio/run/managing-avds) if you are not sure.
5. To build the app, either use the Android Studio built-in build-and-run feature (follow [this guide](https://developer.android.com/studio/run) if not sure) or use gradle commands (`./gradlew build` for building and `./gradlew check` for verifying and testing the app) from the `ChoreCenterApp` directory. If you don't have permission to run command `./gradlew`, consider using `chmod +x ./gradlew` to add execution permission. (Note that you need to follow the above instructions to open up the project from the `ChoreCenterApp` directory, otherwise you may need to set up your environment variable `ANDROID_SDK_ROOT` to your installation directory of Android SDK package and possibly other resource paths.)
6. (Because our backend server hasn't yet been extended to authenticate users with `Google OAuth`, skip this step for now.) For developing the app using `Google OAuth 2.0` to sign in users, we are using the `Android Studio` IDE to run the app and a debug SHA-1 key for requesting Google IdToken through Google API. Since debug SHA-1 key is different for machines running the project, the Google Authentication used in the project will not work immediately after you clone the project. To have the Google Sign In Client request IdToken of signed-in accounts, use command `./gradlew signingReport` in the `ChoreCenterApp` directory to find out about your debug SHA-1 key. (If you do not see the SHA-1 key or do not have a .keystore file which stores the SHA-1 key, follow [this Android guide](https://developers.google.com/android/guides/client-auth) to get the SHA-1 key or run `./gradlew build` first to generate one.) Next, visit the Google OAuth 2.0 [Android guide](https://developers.google.com/identity/sign-in/android/start-integrating#configure_a_project) to configure a Google API console project and use `com.cse403chorecenter.chorecenterapp` for the package name and your debug SHA-1 key for the SHA key. When you are done, you will be given your Client-ID (also in the given `credentials.json` file). Lastly, open up Java file `/ChoreCenter/Android/ChoreCenterApp/app/src/main/java/com/cse403chorecenter/chorecenterapp/UserLogin.java` and change the argument to the method `.requestIdToken()` on line 84 to be your Client-ID of the configured Google API console project.
7. If you decide to make the Android application send network requests to a local server instead of a deployed server, in the Java file `/ChoreCenter/Android/ChoreCenterApp/app/src/main/java/com/cse403chorecenter/chorecenterapp/MainActivity.java`, simply comment out the `http://chorecenter.westus2.cloudapp.azure.com/` DNS String and uncomment the `http://10.0.2.2:5000/` DNS String to have the Android application running in an Android Studio emulator send all the network requests to the local server on port 5000.

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
    

### User guideline

1. After clicking "Get Started!", user will be directed to choose their account type to be either a "parent" or a "kid."
2. If the user has not been registered yet, he or she will be directed to a page for signing up after they log in with their Google account. The kid account sign-up requires input of the parent code during the sign-up step in order to be linked to the specific parent account. Parent code is accessible from the user navigation page drawer of a parent account.
3. A parent account can access activities to create chores and rewards from user navigation page drawer.
4. A child account can access submit chore and redeem reward activities from user navigation page drawer.
5. Chores and rewards created by a parent account are visible to the linked kid account for submit or redeem.
6. Kids are expected to be honest and only submit a chore after they have finished it but parents can verify that their kids have indeed completed the chores that are submitted {not supported in beta release}.
7. Points earned by kids from submitted chores can be used to redeem exciting rewards customized by their parents.

### Developer guideline

1. The source code is in the `main` directory illustrated in the Directory Structure diagram. In `main.java.com.cse403chorecenter.chorecenterapp` directory, `MainActivity.java` is the entry activity when the application starts up.
2. Directory `main.java.com.cse403chorecenter.chorecenterapp.ui` contains all the fragment activities which are functionalities accessible from the main user navigation page of a parent accout or kid account. These functionalities are expected to be only accessible once a user account is successfully logged in and without signing out.
3. Classes defined outside of the `ui` directory are general purpose activities implementing functionalities accessible regardless of whether a user account is successfully logged in.
4. For prospective developers, make sure you have checked the latest version of the project repository. If you have any questions, reach out to any current project contributors before you decide to make contributions to the project.
5. For developers contributing to extend the current project: fork the current git repository, write code following standard [Android Java coding style guide](https://source.android.com/setup/contribute/code-style) and best programming practice including clear documentation, high cohesion and low coupling, and write high quality tests for your code. Create a pull request from your forked repository to the `master` branch of our project repository. Once the changes are sufficiently reviewed by our code reviewers, we will squash and merge the changes into the `master` branch of the latest project repository.

### Current Progress

- [x] Parent can create chores visible to the linked child account(s) {use case 5}
- [x] Parent can create rewards visible to the linked child account(s) {use case 4}
- [x] Child can submit a chore to indicate completion of the chore
- [x] Child can redeem a reward with pop-up message indicating success/failure {use case 2}
- [x] Child can accumulate points to redeem "costly" rewards by completing chores {use case 3}
- [x] A child only receives points after the parent verifies that a chore has actually been completed {use case 6}
- [ ] Parent can make a chore available only for a specific time period (stretch goal) {use case 1}

### Testing Instructions

#### How to run the tests?
From the `ChoreCenterApp` directory, run `./gradlew check` to execute all verification tasks, including tests and linting. To only execute tests, run `./gradlew test`.

#### How to add a test?
Our testing plan is to have JUnit tests to test individual components of the UI. To add a unit test, do the following:
1. Add a Java class to the `/ChoreCenter/Android/ChoreCenterApp/app/src/test/java/com/cse403chorecenter/chorecenterapp/` directory.
2. Add JUnit unit tests to your test class.
3. Make sure your unit tests have reasonable names. Use the [JUnit 4 cookbook](https://junit.org/junit4/cookbook.html) to learn about how to create a JUnit 4 unit test.

### Build a release of the software

#### Build release APK
Follow the Android [Prepare for release guide](https://developer.android.com/studio/publish/preparing) to build the release package.
Follow the Android [Authenticating your client guide](https://developers.google.com/android/guides/client-auth) to generate production SHA-1 key and provide it for your signing certificate before releasing the product on Google Play store.
