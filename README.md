# Chore Center

## Overview

Chores aren't fun, and parents often struggle to provide incentives for their children to complete chores. Chore Center is a mobile application that gamifies the task of completing chores for children. Parents can easily create chores for their children to complete in the app and assign points for each chore. The points are awarded to the child once the chore is complete, and can be redeemed for rewards defined by the parents.

## Repository Structure

* Android directory: contains the code and onboarding instructions for the mobile application
    * ChoreCenterApp: top-level directory of Anroid app
        * app.java.com.cse403chorecenter.chorecenterapp: source code for the activities
        * app.java.com.cse403chorecenter.chorecenterapp (androidTest): Android test code for the activities
        * app.java.com.cse403chorecenter.chorecenterapp (test): JUnit test code for the activities
        * app.res: all the resources including layout files and literals
        * Gradle Scripts: gradle/wrapper, build.gradle, etc.
* Backend directory: contains the code and onboarding instructions for the backend
* Reports directory: contains the weekly status reports that will be added throughout the quarter

## Instructions to Build, Run, and Test the Application
Instructions to build, run, and test the mobile application and backend server can be found in the Android and Backend directories respectively.
We are running an existing backend on a VM already that you can interact with using this url: http://chorecenter.westus2.cloudapp.azure.com

## Bug Tracking

Project issues are tracked through [Github Issues](https://guides.github.com/features/issues/).

### Creating a new issue:
- Each issue should have a unique name clearly and quickly stating what it is about.
- Issues should be labeled for greater clarity and organization.
- Issues should be assigned to someone responsible for fixing it.

## Build Systems

- Frontend: [Gradle](https://gradle.org/) is used to build our frontend code.

## CI/CD

We are using [Github Actions](https://github.com/features/actions) for our CI/CD.

- Frontend: Tests and testing documentation can be found in the `Android` directory.
- Backend: Tests and testing documentation can be found in the `Backend` directory.

## Operational Use Cases
- Parents and children can Sign Up/Sign In with their Google account, and child accounts are linked to parent accounts with the Parent Code
- Parents can create chores and rewards and assign points to them
- Children can view the chores and rewards created by the parents
- Children can complete chores by submitting them
- Children can redeem rewards once they have earned sufficient points by completing chores
