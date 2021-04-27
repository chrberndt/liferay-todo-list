package com.chberndt.liferay.todo.list.internal.bulk.selection;

import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskService;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.bulk.selection.BaseSingleEntryBulkSelection;
import com.liferay.bulk.selection.BulkSelection;
import com.liferay.bulk.selection.BulkSelectionFactory;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.Map;

/**
 * @author Christian Berndt
 */
public class SingleTaskBulkSelection
	extends BaseSingleEntryBulkSelection<Task> {

	public SingleTaskBulkSelection(
		long entryId, Map<String, String[]> parameterMap,
		TaskService taskService,
		AssetEntryLocalService assetEntryLocalService) {

		super(entryId, parameterMap);

		_entryId = entryId;
		_taskService = taskService;
		_assetEntryLocalService = assetEntryLocalService;
	}

	@Override
	public Class<? extends BulkSelectionFactory>
		getBulkSelectionFactoryClass() {

		return TaskBulkSelectionFactory.class;
	}

	@Override
	public BulkSelection<AssetEntry> toAssetEntryBulkSelection() {
		return new TaskAssetEntryBulkSelection(this, _assetEntryLocalService);
	}

	@Override
	protected Task getEntry() throws PortalException {
		return _taskService.getTask(_entryId);
	}

	@Override
	protected String getEntryName() throws PortalException {
		Task task = getEntry();

		return task.getTitle();
	}

	private final AssetEntryLocalService _assetEntryLocalService;
	private final long _entryId;
	private final TaskService _taskService;

}