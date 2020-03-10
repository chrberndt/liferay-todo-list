package com.chberndt.liferay.todo.list.internal.servlet.taglib.clay;

import com.chberndt.liferay.todo.list.model.Task;

import com.liferay.frontend.taglib.clay.servlet.taglib.soy.BaseVerticalCard;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.util.HtmlUtil;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Christian Berndt
 */
public class TaskVerticalCard extends BaseVerticalCard {

	public TaskVerticalCard(
		Task task, RenderRequest renderRequest, RenderResponse renderResponse,
		RowChecker rowChecker, String taskURL) {

		super(task, renderRequest, rowChecker);

		_task = task;

		// TODO
		// _trashHelper = trashHelper;

		_taskURL = taskURL;

		// TODO
		// _permissionChecker = permissionChecker;

	}

	// TODO

	//	public List<DropdownItem> getActionDropdownItems() {
	//	}

	// TODO

	//	public String getDefaultEventHandler() {
	//	}

	@Override
	public String getHref() {

		// TODO: add permission check

		return _taskURL;
	}

	@Override
	public String getIcon() {
		return "check-circle";
	}

	@Override
	public String getSubtitle() {
		return "TODO: sub-title";
	}

	@Override
	public String getTitle() {
		return HtmlUtil.unescape(_task.getTitle());

		// return _task.getTitle();

	}

	private final Task _task;
	private final String _taskURL;

	// TODO: Add permission support
	// private final PermissionChecker _permissionChecker;
	// TODO: Add TrashHelper support
	// private final TrashHelper _trashHelper;

}