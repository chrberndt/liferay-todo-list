
<%@ include file="/init.jsp" %>

<%
TasksDisplayContext tasksDisplayContext = (TasksDisplayContext)request.getAttribute(ToDoListWebKeys.TASKS_DISPLAY_CONTEXT);

String displayStyle = tasksDisplayContext.getDisplayStyle();
SearchContainer tasksSearchContainer = tasksDisplayContext.getSearchContainer();

PortletURL portletURL = tasksSearchContainer.getIteratorURL();

TasksManagementToolbarDisplayContext tasksManagementToolbarDisplayContext = new TasksManagementToolbarDisplayContext(liferayPortletRequest, liferayPortletResponse, request, tasksSearchContainer, displayStyle);
%>

<clay:management-toolbar
	displayContext="<%= tasksManagementToolbarDisplayContext %>"
	searchContainerId="tasks"
/>

<div class="container-fluid container-fluid-max-xl main-content-body">
	<aui:form action="<%= portletURL.toString() %>" method="get" name="fm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" />
		<aui:input name="redirect" type="hidden" value="<%= portletURL.toString() %>" />
		<aui:input name="deleteTaskIds" type="hidden" />

		<liferay-ui:search-container
			id="tasks"
			searchContainer="<%= tasksSearchContainer %>"
		>
			<liferay-ui:search-container-row
				className="com.chberndt.liferay.todo.list.model.Task"
				escapedModel="<%= true %>"
				keyProperty="taskId"
				modelVar="task"
			>
				<liferay-portlet:renderURL varImpl="rowURL">
					<portlet:param name="mvcRenderCommandName" value="/edit_task" />
					<portlet:param name="redirect" value="<%= portletURL.toString() %>" />
					<portlet:param name="taskId" value="<%= String.valueOf(task.getTaskId()) %>" />
				</liferay-portlet:renderURL>

				<%
				Map<String, Object> rowData = new HashMap<>();

				// rowData.put("actions", StringUtil.merge(tasksDisplayContext.getAvailableActions(task)));

				row.setData(rowData);
				%>

				<%@ include file="/task_search_columns.jspf" %>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				displayStyle="<%= displayStyle %>"
				markupView="lexicon"
			/>
		</liferay-ui:search-container>
	</aui:form>
</div>