
<%@ include file="/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");

Task task = (Task)request.getAttribute(WebKeys.TASK);

long taskId = BeanParamUtil.getLong(task, request, "taskId");

if (Validator.isNull(redirect)) {
	PortletURL portletURL = renderResponse.createRenderURL();

	redirect = portletURL.toString();
}

// portletDisplay.setShowBackIcon(true);
// portletDisplay.setURLBack(redirect);

renderResponse.setTitle(((task == null) ? LanguageUtil.get(request, "add-task") : task.getTitle()));
%>

<portlet:actionURL name="editTask" var="editTaskURL">
	<portlet:param name="mvcPath" value="/edit_task.jsp" />
</portlet:actionURL>

<liferay-frontend:edit-form
	action="<%= editTaskURL %>"
	name="fm"
>
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	<aui:input name="taskId" type="hidden" value="<%= taskId %>" />

	<liferay-frontend:edit-form-body>

		<%--
		<liferay-ui:error exception="<%= DuplicateTaskException.class %>" message="please-enter-a-unique-name" />
		<liferay-ui:error exception="<%= TaskNameException.class %>" message="please-enter-a-valid-name" />
		--%>

		<aui:model-context bean="<%= task %>" model="<%= Task.class %>" />

		<liferay-frontend:fieldset-group>

		<%--
			<liferay-frontend:fieldset
				collapsed="<%= false %>"
				collapsible="<%= true %>"
				label="details"
			>
		--%>

				<aui:input autoFocus="<%= true %>" label="title" name="title" placeholder="title" />

				<aui:input name="description" placeholder="description" />

				<aui:input name="dueDate" />

		<%--
			</liferay-frontend:fieldset>

			<%@ include file="/edit_task_settings.jspf" %>
		--%>

			<c:if test="<%= task == null %>">
				<liferay-frontend:fieldset
					collapsed="<%= true %>"
					collapsible="<%= true %>"
					label="permissions"
				>
					<liferay-ui:input-permissions
						modelName="<%= Task.class.getName() %>"
					/>
				</liferay-frontend:fieldset>
			</c:if>
		</liferay-frontend:fieldset-group>
	</liferay-frontend:edit-form-body>

	<liferay-frontend:edit-form-footer>
		<aui:button type="submit" />

		<aui:button href="<%= redirect %>" type="cancel" />
	</liferay-frontend:edit-form-footer>
</liferay-frontend:edit-form>