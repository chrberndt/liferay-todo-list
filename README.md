# liferay-todo-list
A ToDo list application meant to explore, test and demonstrate the development of Liferay custom modules.

## How To Build

TODO

## How To Test

### Integration Tests

1. Change working directory to liferay-todo-list application: `cd $LIFERAY_WORKSPACE/modules/liferay-todo-list`

### Sonarqube

1. Change working directory to liferay-todo-list application: `cd $LIFERAY_WORKSPACE/modules/liferay-todo-list`
1. Start local sonarqube server: `docker-compose up` (This will download and start a sonarqube server which can be accessed at [http://localhost:9000](http://localhost:9000))
1. Run the sonarqube task: `./gradlew modules:liferay-todo-list:sonarqube`

## Contact

christian.berndt@liferay.com

