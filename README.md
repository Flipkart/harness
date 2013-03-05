# Harness Project

an automation test framework for functional tests

## How to build

ant compile

ant dist

## How to run tests for modules

ant -Dmodule=website test

## How to run tests for features

ant -Dmodule=website -Dfeature=discovery test

## How to run tests for subfeatures

ant -Dmodule=website -Dfeature=discovery -Dsubfeature=landing test

## How to enable database persistence

ant -Dmodule=website -Dpersist=true test


You require the following to build Harness:

* Latest stable [Oracle JDK 7](http://www.oracle.com/technetwork/java/)
* Latest stable [Apache Ant] (http://ant.apache.org/)
* Latest stable [TestNG] (http://testng.org/doc/index.html)
* Mysql Server for test results persistence
* Selenium 2.0 (http://docs.seleniumhq.org/download/)
