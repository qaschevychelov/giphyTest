#Selenide + Appium + Allure + TestNG
A test framework for **mobile** automation (Android).
##
### Usage:
1. run **Appium** node with `appium` command
2. run a test or a test suite:
* a single test via `mvn test -Dtest=GiphyTest`
* a test suite via `mvn test -Dsuite=testng`
3. run `allure serve target/allure-results` in order to start allure report server
##