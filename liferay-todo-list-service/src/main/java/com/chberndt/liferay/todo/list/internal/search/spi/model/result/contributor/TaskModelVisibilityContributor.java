package com.chberndt.liferay.todo.list.internal.search.spi.model.result.contributor;

import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskLocalService;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.spi.model.result.contributor.ModelVisibilityContributor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.chberndt.liferay.todo.list.model.Task",
	service = ModelVisibilityContributor.class
)
public class TaskModelVisibilityContributor
	implements ModelVisibilityContributor {

	@Override
	public boolean isVisible(long classPK, int status) {
		try {
			Task task = _taskLocalService.getTask(classPK);

			return _isVisible(task.getStatus(), status);
		}
		catch (PortalException portalException) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to check visibility for task ", portalException);
			}

			return false;
		}
	}

	private boolean _isVisible(int taskStatus, int queryStatus) {
		if (((queryStatus != WorkflowConstants.STATUS_ANY) &&
			 (taskStatus == queryStatus)) ||
			(taskStatus != WorkflowConstants.STATUS_IN_TRASH)) {

			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		TaskModelVisibilityContributor.class);

	@Reference
	private TaskLocalService _taskLocalService;

}