<%@ include file="/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute("SEARCH_CONTAINER_RESULT_ROW");

Task task = (Task)row.getObject();
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<portlet:renderURL var="editURL">
		<portlet:param name="mvcRenderCommandName" value="/edit_task" />
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		label="<%= true %>"
		message="edit"
		url="<%= editURL %>"
	/>

	<liferay-security:permissionsURL
		modelResource="<%= Task.class.getName() %>"
		modelResourceDescription="<%= task.getTitle() %>"
		resourcePrimKey="<%= String.valueOf(task.getTaskId()) %>"
		var="permissionsTaskURL"
		windowState="<%= LiferayWindowState.POP_UP.toString() %>"
	/>

	<liferay-ui:icon
		message="permissions"
		method="get"
		url="<%= permissionsTaskURL %>"
		useDialog="<%= true %>"
	/>

	<portlet:actionURL name="/edit_task" var="deleteURL">
		<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.DELETE %>" />

		<%

		// TODO: Add trashHelper support

		%>

		<%--
		<portlet:param name="<%= Constants.CMD %>" value="<%= trashHelper.isTrashEnabled(scopeGroupId) ? Constants.MOVE_TO_TRASH : Constants.DELETE %>" />
		--%>

		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
	</portlet:actionURL>

	<liferay-ui:icon-delete
		url="<%= deleteURL %>"
	/>
</liferay-ui:icon-menu>