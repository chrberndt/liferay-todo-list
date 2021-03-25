# liferay-todo-list

A ToDo list application meant to explore, test and demonstrate the development of Liferay custom modules.

## How To Build

TODO

## How To Test

### Integration Tests

1. Change working directory to liferay-todo-list application: `cd $LIFERAY_WORKSPACE`
1. Run integration tests with `./gradlew buildService initBundle modules:liferay-todo-list:liferay-todo-list-test:testIntegration`
1. Open test reports with `firefox modules/liferay-todo-list/liferay-todo-list-test/build/reports/tests/testIntegration/index.html &`

### Sonarqube

1. Change working directory to liferay-todo-list application: `cd $LIFERAY_WORKSPACE/modules/liferay-todo-list`
1. Start local sonarqube server: `docker-compose up` (This will download and start a sonarqube server which can be accessed at [http://localhost:9000](http://localhost:9000))
1. Change working directory to `$LIFERAY_WORKSPACE` and run the `sonarqube` task: `cd $LIFERAY_WORKSPACE; ./gradlew modules:liferay-todo-list:sonarqube`
1. Review test results at [http://localhost:9000](http://localhost:9000)

## Contact

christian.berndt@liferay.com

