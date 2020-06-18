
<%@ include file="/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

Task task = (Task)request.getAttribute(WebKeys.TASK);

long taskId = BeanParamUtil.getLong(task, request, "taskId");
%>

<div class="container-fluid-1280">
	<aui:form method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveTask();" %>'>
		<aui:input name="<%= Constants.CMD %>" type="hidden" />
		<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
		<aui:input name="taskId" type="hidden" value="<%= taskId %>" />

		<%--
		<liferay-ui:error exception="<%= TaskDueDateException.class %>" message="please-enter-a-due-date" />
		<liferay-ui:error exception="<%= TaskTitleException.class %>" message="please-enter-a-valid-name" />
		--%>

		<aui:model-context bean="<%= task %>" model="<%= Task.class %>" />

		<aui:fieldset-group markupView="lexicon">
			<aui:fieldset>
				<aui:input autoFocus="<%= true %>" label="title" name="title" placeholder="title" />

				<aui:input name="description" placeholder="description" />

				<aui:input name="dueDate" />

				<aui:input name="completed" />
			</aui:fieldset>
		</aui:fieldset-group>

		<liferay-frontend:edit-form-footer>
			<aui:button type="submit" />

			<aui:button href="<%= redirect %>" type="cancel" />
		</liferay-frontend:edit-form-footer>
	</aui:form>
</div>

<aui:script>
	function <portlet:namespace />saveTask() {
		var form = document.getElementById('<portlet:namespace />fm');

		if (form) {
			form.action = '<portlet:actionURL name="/edit_task"><portlet:param name="mvcRenderCommandName" value="/edit_task" /></portlet:actionURL>';
			form.target = '';

			var cmd = form.querySelector('#<portlet:namespace /><%= Constants.CMD %>');

			if (cmd) {
				cmd.setAttribute('value', '<%= (task == null) ? Constants.ADD : Constants.UPDATE %>');
			}

			submitForm(form);
		}
	}
</aui:script>