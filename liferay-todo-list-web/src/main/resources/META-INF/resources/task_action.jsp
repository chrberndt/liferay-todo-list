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
	<c:if test="<%= TaskPermission.contains(permissionChecker, task, ActionKeys.UPDATE) %>">
		<portlet:renderURL var="editURL">
			<portlet:param name="mvcRenderCommandName" value="/edit_task" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
		</portlet:renderURL>

		<%
		PortletURL editTaskURL = PortalUtil.getControlPanelPortletURL(request, themeDisplay.getScopeGroup(), ToDoListPortletKeys.TODO_LIST, 0, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

		editTaskURL.setParameter("mvcRenderCommandName", "/edit_task");
		editTaskURL.setParameter("redirect", currentURL);
		editTaskURL.setParameter("portletResource", portletDisplay.getId());
		editTaskURL.setParameter("taskId", String.valueOf(task.getTaskId()));
		%>

		<liferay-ui:icon
			label="<%= true %>"
			message="edit"
			url="<%= editTaskURL.toString() %>"
		/>
	</c:if>

	<c:if test="<%= TaskPermission.contains(permissionChecker, task, ActionKeys.PERMISSIONS) %>">
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
	</c:if>

	<c:if test="<%= TaskPermission.contains(permissionChecker, task, ActionKeys.DELETE) %>">
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
	</c:if>
</liferay-ui:icon-menu>