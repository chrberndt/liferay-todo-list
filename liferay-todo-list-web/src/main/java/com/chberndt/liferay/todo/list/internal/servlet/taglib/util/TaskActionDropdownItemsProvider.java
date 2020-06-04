package com.chberndt.liferay.todo.list.internal.servlet.taglib.util;

import com.chberndt.liferay.todo.list.internal.security.permission.resource.TaskPermission;
import com.chberndt.liferay.todo.list.model.Task;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.taglib.security.PermissionsURLTag;

import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionURL;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Christian Berndt
 */
public class TaskActionDropdownItemsProvider {

	public TaskActionDropdownItemsProvider(
		Task task, RenderRequest renderRequest, RenderResponse renderResponse,
		PermissionChecker permissionChecker, ResourceBundle resourceBundle) {

		_task = task;
		_renderResponse = renderResponse;
		_permissionChecker = permissionChecker;
		_resourceBundle = resourceBundle;

		// TODO

		//		_trashHelper = trashHelper;

		_httpServletRequest = PortalUtil.getHttpServletRequest(renderRequest);
	}

	public List<DropdownItem> getActionDropdownItems() {
		return new DropdownItemList() {
			{
				if (_hasUpdatePermission()) {
					add(_getEditTaskActionUnsafeConsumer());
				}

				if (_hasPermissionsPermission()) {
					add(_getPermissionsActionUnsafeConsumer());
				}

				if (_hasDeletePermission()) {
					if (_isTrashEnabled()) {

						// TODO

						// add(_getMoveTaskToTrashActionUnsafeConsumer());

					}
					else {
						add(_getDeleteTaskActionUnsafeConsumer());
					}
				}
			}
		};
	}

	private UnsafeConsumer<DropdownItem, Exception>
		_getDeleteTaskActionUnsafeConsumer() {

		ActionURL deleteURL = _renderResponse.createActionURL();

		deleteURL.setParameter(ActionRequest.ACTION_NAME, "/edit_task");
		deleteURL.setParameter(Constants.CMD, Constants.DELETE);
		deleteURL.setParameter("redirect", _getRedirectURL());
		deleteURL.setParameter("taskId", String.valueOf(_task.getTaskId()));

		return dropdownItem -> {
			dropdownItem.putData("action", "delete");
			dropdownItem.putData("deleteURL", deleteURL.toString());
			dropdownItem.setLabel(
				LanguageUtil.get(_httpServletRequest, "delete"));
		};
	}

	private UnsafeConsumer<DropdownItem, Exception>
		_getEditTaskActionUnsafeConsumer() {

		return dropdownItem -> {
			dropdownItem.setHref(
				_renderResponse.createRenderURL(), "mvcRenderCommandName",
				"/edit_task", Constants.CMD, Constants.UPDATE, "redirect",
				_getRedirectURL(), "taskId", _task.getTaskId());

			dropdownItem.setIcon("edit");
			dropdownItem.setLabel(LanguageUtil.get(_resourceBundle, "edit"));
		};
	}

	// TODO: add TrashHelper Support

	//	private UnsafeConsumer<DropdownItem, Exception> _getMoveTaskToTrashActionUnsafeConsumer() {
	//
	//	};

	private UnsafeConsumer<DropdownItem, Exception>
		_getPermissionsActionUnsafeConsumer() {

		return dropdownItem -> {
			dropdownItem.putData("action", "permissions");
			dropdownItem.putData("permissionsURL", _getPermissionsURL());
			dropdownItem.setLabel(
				LanguageUtil.get(_httpServletRequest, "permissions"));
		};
	}

	private String _getPermissionsURL() {
		try {
			return PermissionsURLTag.doTag(
				StringPool.BLANK, Task.class.getName(), _task.getTitle(), null,
				String.valueOf(_task.getTaskId()),
				LiferayWindowState.POP_UP.toString(), null,
				_httpServletRequest);
		}
		catch (Exception e) {
			return ReflectionUtil.throwException(e);
		}
	}

	private String _getRedirectURL() {
		PortletURL redirectURL = _renderResponse.createRenderURL();

		redirectURL.setParameter("mvcRenderCommandName", "/view");

		return redirectURL.toString();
	}

	private boolean _hasDeletePermission() {
		try {
			return TaskPermission.contains(
				_permissionChecker, _task, ActionKeys.DELETE);
		}
		catch (PortalException pe) {
			return ReflectionUtil.throwException(pe);
		}
	}

	private boolean _hasPermissionsPermission() {
		try {
			return TaskPermission.contains(
				_permissionChecker, _task, ActionKeys.PERMISSIONS);
		}
		catch (PortalException pe) {
			return ReflectionUtil.throwException(pe);
		}
	}

	private boolean _hasUpdatePermission() {
		try {
			return TaskPermission.contains(
				_permissionChecker, _task, ActionKeys.UPDATE);
		}
		catch (PortalException pe) {
			return ReflectionUtil.throwException(pe);
		}
	}

	private boolean _isTrashEnabled() {
		return false;

		// TODO

		//		try {
		//			return _trashHelper.isTrashEnabled(
		//				PortalUtil.getScopeGroupId(_httpServletRequest));
		//		}
		//		catch (PortalException pe) {
		//			return ReflectionUtil.throwException(pe);
		//		}
	}

	private final HttpServletRequest _httpServletRequest;
	private final PermissionChecker _permissionChecker;
	private final RenderResponse _renderResponse;
	private final ResourceBundle _resourceBundle;
	private final Task _task;

	// TODO: add TrashHelper support

	//	private final TrashHelper _trashHelper;

}