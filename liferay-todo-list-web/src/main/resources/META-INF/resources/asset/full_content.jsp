<%@ include file="/init.jsp" %>

<!-- <liferay-util:dynamic-include key="com.liferay.blogs.web#/blogs/asset/full_content.jsp#pre" /> -->

<%
Task task = (Task)request.getAttribute(ToDoListWebKeys.TASK);

// String taskTitle = TaskUtil.getDisplayTitle(resourceBundle, task);

%>

<div class="widget-mode-simple" data-analytics-asset-id="<%= String.valueOf(task.getTaskId()) %>" data-analytics-asset-title="<%= HtmlUtil.escapeAttribute(task.getTitle()) %>" data-analytics-asset-type="task">
	<div class="widget-mode-simple-task">
		<div class="widget-content" id="<portlet:namespace /><%= task.getTaskId() %>">
			<%= task.getDescription() %>
		</div>

		<liferay-expando:custom-attributes-available
			className="<%= Task.class.getName() %>"
		>
			<liferay-expando:custom-attribute-list
				className="<%= Task.class.getName() %>"
				classPK="<%= (task != null) ? task.getTaskId() : 0 %>"
				editable="<%= false %>"
				label="<%= true %>"
			/>
		</liferay-expando:custom-attributes-available>
	</div>
</div>

<!-- <liferay-util:dynamic-include key="com.liferay.blogs.web#/blogs/asset/full_content.jsp#post" /> -->