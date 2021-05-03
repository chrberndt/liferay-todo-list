package com.chberndt.liferay.todo.list.internal.bulk.selection;

import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskService;

import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.bulk.selection.BulkSelection;
import com.liferay.bulk.selection.BulkSelectionFactory;
import com.liferay.bulk.selection.EmptyBulkSelection;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true,
	property = "model.class.name=com.chberndt.liferay.todo.list.model.Task",
	service = {TaskBulkSelectionFactory.class, BulkSelectionFactory.class}
)
public class TaskBulkSelectionFactory implements BulkSelectionFactory<Task> {

	@Override
	public BulkSelection<Task> create(Map<String, String[]> parameterMap) {
		boolean selectAll = MapUtil.getBoolean(parameterMap, "selectAll");

		if (selectAll) {
			long groupId = MapUtil.getLong(parameterMap, "groupId");

			return new GroupTaskBulkSelection(
				groupId, parameterMap, _taskService, _assetEntryLocalService);
		}

		long taskId = MapUtil.getLong(parameterMap, "taskId");

		if (taskId > 0) {
			return new SingleTaskBulkSelection(
				taskId, parameterMap, _taskService, _assetEntryLocalService);
		}

		long[] taskIds = GetterUtil.getLongValues(
			StringUtil.split(MapUtil.getString(parameterMap, "deleteTaskIds")));

		if (ArrayUtil.isNotEmpty(taskIds)) {
			return new MultipleTaskBulkSelection(
				taskIds, parameterMap, _taskService, _assetEntryLocalService);
		}

		return new EmptyBulkSelection<>();
	}

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private TaskService _taskService;

}