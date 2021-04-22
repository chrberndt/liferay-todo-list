package com.chberndt.liferay.todo.list.internal.bulk.selection;

import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskService;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.bulk.selection.BaseContainerEntryBulkSelection;
import com.liferay.bulk.selection.BulkSelection;
import com.liferay.bulk.selection.BulkSelectionFactory;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.interval.IntervalActionProcessor;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.List;
import java.util.Map;

/**
 * @author Christian Berndt
 */
public class GroupTaskBulkSelection extends BaseContainerEntryBulkSelection<Task> {

	public GroupTaskBulkSelection(long groupId, Map<String, String[]> parameterMap, TaskService taskService,
			AssetEntryLocalService assetEntryLocalService) {

		super(groupId, parameterMap);

		_groupId = groupId;
		_taskService = taskService;
		_assetEntryLocalService = assetEntryLocalService;
	}

	@Override
	public <E extends PortalException> void forEach(UnsafeConsumer<Task, E> unsafeConsumer) throws PortalException {

		IntervalActionProcessor<Task> taskIntervalActionProcessor = new IntervalActionProcessor<>((int) getSize());

		taskIntervalActionProcessor.setPerformIntervalActionMethod((start, end) -> {
			List<Task> tasks = _taskService.getGroupTasks(_groupId, WorkflowConstants.STATUS_APPROVED, start,
					end);

			for (Task task : tasks) {
				unsafeConsumer.accept(task);
			}

			return null;
		});

		taskIntervalActionProcessor.performIntervalActions();
	}

	@Override
	public Class<? extends BulkSelectionFactory> getBulkSelectionFactoryClass() {

		return TaskBulkSelectionFactory.class;
	}

	@Override
	public long getSize() {
		return _taskService.getGroupTasksCount(_groupId, WorkflowConstants.STATUS_APPROVED);
	}

	@Override
	public BulkSelection<AssetEntry> toAssetEntryBulkSelection() {
		return new TaskAssetEntryBulkSelection(this, _assetEntryLocalService);
	}

	private final AssetEntryLocalService _assetEntryLocalService;
	private final TaskService _taskService;
	private final long _groupId;

}
