[![Travis Build status](https://api.travis-ci.com/chrberndt/liferay-todo-list.svg?branch=master&status=started)](https://travis-ci.com/github/chrberndt/liferay-todo-list)


# liferay-todo-list

A ToDo list application meant to explore, test and demonstrate the development of Liferay custom modules.

## Prerequisities

1. Download and install `blade`
1. Create a new Liferay workspace with `blade init`
1. Change working directory to `$LIFERAY_WORKSPACE/modules`
1. Clone `liferay-todo-list` sources with `git clone git@github.com:chrberndt/liferay-todo-list.git`

## How To Build

### `gradle-local.properties`

```
#
# Set the "liferay.workspace.product" to set the
# "app.server.tomcat.version", "liferay.workspace.bundle.url",
# "liferay.workspace.docker.image.liferay", and
# "liferay.workspace.target.platform.version" that matches your Liferay
# Product Version. To override each of these settings, set them
# individually.
#

#liferay.workspace.product = portal-7.3-ga7
liferay.workspace.product=portal-7.3-ga6
```

## How To Deploy

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

