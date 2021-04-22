package com.chberndt.liferay.todo.list.internal.bulk.selection;

import com.chberndt.liferay.todo.list.model.Task;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.bulk.selection.BulkSelection;
import com.liferay.bulk.selection.BulkSelectionFactory;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.exception.PortalException;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Christian Berndt
 */
public class TaskAssetEntryBulkSelection implements BulkSelection<AssetEntry> {

	public TaskAssetEntryBulkSelection(BulkSelection<Task> taskBulkSelection,
			AssetEntryLocalService assetEntryLocalService) {

		_taskBulkSelection = taskBulkSelection;
		_assetEntryLocalService = assetEntryLocalService;
	}

	@Override
	public <E extends PortalException> void forEach(UnsafeConsumer<AssetEntry, E> unsafeConsumer)
			throws PortalException {

		_taskBulkSelection.forEach(task -> unsafeConsumer.accept(_toAssetEntry(task)));
	}

	@Override
	public Class<? extends BulkSelectionFactory> getBulkSelectionFactoryClass() {

		return _taskBulkSelection.getBulkSelectionFactoryClass();
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return _taskBulkSelection.getParameterMap();
	}

	@Override
	public long getSize() throws PortalException {
		return _taskBulkSelection.getSize();
	}

	@Override
	public Serializable serialize() {
		return _taskBulkSelection.serialize();
	}

	@Override
	public BulkSelection<AssetEntry> toAssetEntryBulkSelection() {
		return this;
	}

	private AssetEntry _toAssetEntry(Task task) {
		try {
			return _assetEntryLocalService.getEntry(Task.class.getName(), task.getTaskId());
		} catch (PortalException portalException) {
			return ReflectionUtil.throwException(portalException);
		}
	}

	private final AssetEntryLocalService _assetEntryLocalService;
	private final BulkSelection<Task> _taskBulkSelection;

}
