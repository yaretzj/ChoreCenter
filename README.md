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

- Frontend:
- Backend: Our tests and testing documentation can be found in `Backend/tests`.