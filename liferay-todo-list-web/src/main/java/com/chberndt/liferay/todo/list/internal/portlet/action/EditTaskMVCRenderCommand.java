package com.chberndt.liferay.todo.list.internal.portlet.action;

import com.chberndt.liferay.todo.list.exception.NoSuchTaskException;
import com.chberndt.liferay.todo.list.internal.util.WebKeys;
import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskLocalService;
import com.chberndt.liferay.todo.list.web.constants.ToDoListPortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ToDoListPortletKeys.TODO_LIST,
		"mvc.command.name=/edit_task"
	},
	service = MVCRenderCommand.class
)
public class EditTaskMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		long taskId = ParamUtil.getLong(renderRequest, "taskId");

		try {

			// TODO: use remote service

			Task task = _taskLocalService.getTask(taskId);

			if (task != null) {

				// TODO: Add permission checks

				//				_taskModelResourcePermission.check(
				//					themeDisplay.getPermissionChecker(), task,
				//					ActionKeys.UPDATE);
			}

			HttpServletRequest httpServletRequest =
				_portal.getHttpServletRequest(renderRequest);

			httpServletRequest.setAttribute(WebKeys.TASK, task);
		}
		catch (Exception e) {
			if (e instanceof NoSuchTaskException ||
				e instanceof PrincipalException) {

				SessionErrors.add(renderRequest, e.getClass());

				return "/error.jsp";
			}

			throw new PortletException(e);
		}

		return "/edit_task.jsp";
	}

	@Reference
	private Portal _portal;

	// TODO: use remote service

	@Reference
	private TaskLocalService _taskLocalService;

}