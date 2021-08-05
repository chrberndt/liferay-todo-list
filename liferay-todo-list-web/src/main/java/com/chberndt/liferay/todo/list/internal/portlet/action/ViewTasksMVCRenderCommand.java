package com.chberndt.liferay.todo.list.internal.portlet.action;

import com.chberndt.liferay.todo.list.constants.TodoListPortletKeys;
import com.chberndt.liferay.todo.list.internal.display.context.TasksDisplayContext;
import com.chberndt.liferay.todo.list.web.constants.ToDoListWebKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.trash.TrashHelper;
import com.liferay.trash.util.TrashWebKeys;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + TodoListPortletKeys.TODO_LIST,
		"mvc.command.name=/", "mvc.command.name=/view"
	},
	service = MVCRenderCommand.class
)
public class ViewTasksMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		renderRequest.setAttribute(
			ToDoListWebKeys.TASKS_DISPLAY_CONTEXT,
			new TasksDisplayContext(renderRequest, renderResponse));

		renderRequest.setAttribute(TrashWebKeys.TRASH_HELPER, _trashHelper);

		return "/view.jsp";
	}

	@Reference
	private TrashHelper _trashHelper;

}