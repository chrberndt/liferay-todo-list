<%@ include file="/init.jsp" %>

<liferay-ui:error-header />

<liferay-ui:error exception="<%= NoSuchTaskException.class %>" message="the-task-could-not-be-found" />

<liferay-ui:error-principal />