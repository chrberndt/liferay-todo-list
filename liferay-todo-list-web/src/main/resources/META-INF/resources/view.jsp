
<%@ include file="/init.jsp" %>

<%
	TasksDisplayContext tasksDisplayContext = (TasksDisplayContext)request.getAttribute(ToDoListWebKeys.TASKS_DISPLAY_CONTEXT);

String displayStyle = tasksDisplayContext.getDisplayStyle();

SearchContainer tasksSearchContainer = tasksDisplayContext.getTaskSearchContainer();

PortletURL portletURL = tasksSearchContainer.getIteratorURL();

TrashHelper trashHelper = (TrashHelper)request.getAttribute(TrashWebKeys.TRASH_HELPER);
%>

<clay:management-toolbar
	displayContext="<%= new TasksManagementToolbarDisplayContext(liferayPortletRequest, liferayPortletResponse, request, tasksSearchContainer, trashHelper, displayStyle) %>"
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

				<%
				PortletURL rowURL = PortalUtil.getControlPanelPortletURL(request, themeDisplay.getScopeGroup(), ToDoListPortletKeys.TODO_LIST, 0, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE);

				rowURL.setParameter("mvcRenderCommandName", "/edit_task");
				rowURL.setParameter("redirect", currentURL);
				rowURL.setParameter("portletResource", portletDisplay.getId());
				rowURL.setParameter("taskId", String.valueOf(task.getTaskId()));
				%>

				<%
				Map<String, Object> rowData = new HashMap<>();

				// TODO
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

<aui:script>
	var deleteTasks = function() {

		if (
			confirm(
				'<liferay-ui:message key="are-you-sure-you-want-to-delete-the-selected-tasks" />'
			)
		) {
			var form = document.getElementById('<portlet:namespace />fm');

			if (form) {
				form.setAttribute('method', 'post');

				var cmd = form.querySelector(
					'#<portlet:namespace /><%= Constants.CMD %>'
				);

				if (cmd) {
					cmd.setAttribute('value', '<%= Constants.DELETE %>');
				}

				submitForm(
					form,
					'<portlet:actionURL name="/edit_task" />'
				);
			}
		}
	};

	var ACTIONS = {
		deleteTasks: deleteTasks
	};

	Liferay.componentReady('tasksManagementToolbar').then(function(
		managementToolbar
	) {
		managementToolbar.on('actionItemClicked', function(event) {
			var itemData = event.data.item.data;

			if (itemData && itemData.action && ACTIONS[itemData.action]) {
				ACTIONS[itemData.action]();
			}
		});
	});
</aui:script>