package com.chberndt.liferay.todo.list.internal.search.spi.model.index.contributor;

import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskLocalService;

import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.search.batch.BatchIndexingActionable;
import com.liferay.portal.search.batch.DynamicQueryBatchIndexingActionableFactory;
import com.liferay.portal.search.spi.model.index.contributor.ModelIndexerWriterContributor;
import com.liferay.portal.search.spi.model.index.contributor.helper.IndexerWriterMode;
import com.liferay.portal.search.spi.model.index.contributor.helper.ModelIndexerWriterDocumentHelper;

import java.util.Date;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.chberndt.liferay.todo.list.model.Task",
	service = ModelIndexerWriterContributor.class
)
public class TaskModelIndexerWriterContributor
	implements ModelIndexerWriterContributor<Task> {

	@Override
	public void customize(
		BatchIndexingActionable batchIndexingActionable,
		ModelIndexerWriterDocumentHelper modelIndexerWriterDocumentHelper) {

		batchIndexingActionable.setAddCriteriaMethod(
			dynamicQuery -> {
				Property displayDateProperty = PropertyFactoryUtil.forName(
					"createDate");

				dynamicQuery.add(displayDateProperty.lt(new Date()));
			});
		batchIndexingActionable.setPerformActionMethod(
			(Task task) -> batchIndexingActionable.addDocuments(
				modelIndexerWriterDocumentHelper.getDocument(task)));
	}

	@Override
	public BatchIndexingActionable getBatchIndexingActionable() {
		return _dynamicQueryBatchIndexingActionableFactory.
			getBatchIndexingActionable(
				_taskLocalService.getIndexableActionableDynamicQuery());
	}

	@Override
	public long getCompanyId(Task task) {
		return task.getCompanyId();
	}

	@Override
	public IndexerWriterMode getIndexerWriterMode(Task task) {
		if (task.isApproved() || task.isDraft() || task.isInTrash() ||
			task.isPending()) {

			return IndexerWriterMode.UPDATE;
		}

		if (!task.isApproved() && !task.isInTrash()) {
			return IndexerWriterMode.SKIP;
		}

		return IndexerWriterMode.DELETE;
	}

	@Reference
	private DynamicQueryBatchIndexingActionableFactory
		_dynamicQueryBatchIndexingActionableFactory;

	@Reference
	private TaskLocalService _taskLocalService;

}