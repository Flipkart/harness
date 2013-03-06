# Harness Project

an automation test framework for functional tests

## How to build

$ant -lib lib compile

$ant -lib lib dist

## How to run tests for modules

$ant -lib lib -Dmodule=website test

## How to run tests for features

$ant -lib lib -Dmodule=website -Dfeature=discovery test

## How to run tests for subfeatures

$ant -lib lib -Dmodule=website -Dfeature=discovery -Dsubfeature=landing test

## How to enable database persistence

$ant -lib lib -Dmodule=website -Dpersist=true test


You require the following to build Harness:

* Latest stable [Oracle JDK 7](http://www.oracle.com/technetwork/java/)
* Latest stable [Apache Ant] (http://ant.apache.org/)