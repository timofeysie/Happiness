Happiness
=========

Somtimes happiness takes practice.  This is a mobile app to implement the dot-probe clinical psychological exercise.

The project consists of a native Android app, contained in the root directory, and a web app which can be built for Android, iOS and other mobile platforms supported by Cordova.

The web app is contained in the app directory.  The Cordova build is contained in the cordova-app directory.

Testing
=======

To run the app in a browser, use the Grunt Connect plugin by running this command in the root directory:
grunt connect:target:keepalive

The app will then be available as a website at:
http://0.0.0.0:9001/app/#/main

## Unit Test
Contained in the tests/unit test folder and configured with karam.conf.js file.

## End to End Tests
Contained in the tests/e2e directory.

## Acceptance Tests
Run using Cucumber.

Acceptance tests are created for the Cucumber testing framework.  Features are written with the Gherkin syntax. Step definitions are the glue between features written in Gherkin and the actual system under test (SUT).  All step definitions will run with this set to what is known as the World in Cucumber. It's an object exposing useful methods, helpers and variables to your step definitions.

## Integration Tests
(@TODO)