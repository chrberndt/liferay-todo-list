package com.chberndt.liferay.todo.list.internal.bulk.selection;

import com.chberndt.liferay.todo.list.exception.NoSuchTaskException;
import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskService;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.bulk.selection.BaseMultipleEntryBulkSelection;
import com.liferay.bulk.selection.BulkSelection;
import com.liferay.bulk.selection.BulkSelectionFactory;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Map;

/**
 * @author Christian Berndt
 */
public class MultipleTaskBulkSelection
	extends BaseMultipleEntryBulkSelection<Task> {

	public MultipleTaskBulkSelection(
		long[] entryIds, Map<String, String[]> parameterMap,
		TaskService taskService,
		AssetEntryLocalService assetEntryLocalService) {

		super(entryIds, parameterMap);

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
	protected Task fetchEntry(long entryId) {
		try {
			return _taskService.getTask(entryId);
		}
		catch (NoSuchTaskException noSuchTaskException) {
			if (_log.isWarnEnabled()) {
				_log.warn(noSuchTaskException, noSuchTaskException);
			}

			return null;
		}
		catch (PortalException portalException) {
			return ReflectionUtil.throwException(portalException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MultipleTaskBulkSelection.class);

	private final AssetEntryLocalService _assetEntryLocalService;
	private final TaskService _taskService;

}