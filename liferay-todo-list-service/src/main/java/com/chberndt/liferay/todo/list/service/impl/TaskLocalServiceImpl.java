/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.chberndt.liferay.todo.list.service.impl;

import com.chberndt.liferay.todo.list.exception.TaskDueDateException;
import com.chberndt.liferay.todo.list.exception.TaskTitleException;
import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.base.TaskLocalServiceBaseImpl;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.ModelPermissions;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the task local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * <code>com.chberndt.liferay.todo.list.service.TaskLocalService</code>
 * interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Christian Berndt
 * @see TaskLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.chberndt.liferay.todo.list.model.Task",
	service = AopService.class
)
public class TaskLocalServiceImpl extends TaskLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use
	 * <code>com.chberndt.liferay.todo.list.service.TaskLocalService</code> via
	 * injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use
	 * <code>com.chberndt.liferay.todo.list.service.TaskLocalServiceUtil</code>.
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public Task addTask(
			long userId, String title, String description, boolean completed,
			Date dueDate, ServiceContext serviceContext)
		throws PortalException {

		// Task

		User user = userLocalService.getUser(userId);
		long groupId = serviceContext.getScopeGroupId();

		int status = WorkflowConstants.STATUS_DRAFT;

		_validate(title, dueDate);

		long taskId = counterLocalService.increment();

		// TODO: friendlyURL validation

		Task task = taskPersistence.create(taskId);

		task.setUuid(serviceContext.getUuid());
		task.setGroupId(groupId);
		task.setCompanyId(user.getCompanyId());
		task.setUserId(user.getUserId());
		task.setUserName(user.getFullName());
		task.setTitle(title);
		task.setDescription(description);
		task.setCompleted(completed);
		task.setDueDate(dueDate);
		task.setStatus(status);
		task.setStatusByUserId(userId);
		task.setStatusDate(serviceContext.getModifiedDate(null));
		task.setExpandoBridgeAttributes(serviceContext);

		task = taskPersistence.update(task);

		// Resources

		if (serviceContext.isAddGroupPermissions() ||
			serviceContext.isAddGuestPermissions()) {

			addTaskResources(
				task, serviceContext.isAddGroupPermissions(),
				serviceContext.isAddGuestPermissions());
		}
		else {
			addTaskResources(task, serviceContext.getModelPermissions());
		}

		// Asset

		updateAsset(
			userId, task, serviceContext.getAssetCategoryIds(),
			serviceContext.getAssetTagNames());

		// Workflow

		return _startWorkflowInstance(userId, task, serviceContext);
	}

	@Override
	public void addTaskResources(
			long taskId, boolean addGroupPermissions,
			boolean addGuestPermissions)
		throws PortalException {

		Task task = taskPersistence.findByPrimaryKey(taskId);

		addTaskResources(task, addGroupPermissions, addGuestPermissions);
	}

	@Override
	public void addTaskResources(long taskId, ModelPermissions modelPermissions)
		throws PortalException {

		Task task = taskPersistence.findByPrimaryKey(taskId);

		addTaskResources(task, modelPermissions);
	}

	@Override
	public void addTaskResources(
			Task task, boolean addGroupPermissions, boolean addGuestPermissions)
		throws PortalException {

		resourceLocalService.addResources(
			task.getCompanyId(), task.getGroupId(), task.getUserId(),
			Task.class.getName(), task.getTaskId(), false, addGroupPermissions,
			addGuestPermissions);
	}

	@Override
	public void addTaskResources(Task task, ModelPermissions modelPermissions)
		throws PortalException {

		resourceLocalService.addModelResources(
			task.getCompanyId(), task.getGroupId(), task.getUserId(),
			Task.class.getName(), task.getTaskId(), modelPermissions);
	}

	@Indexable(type = IndexableType.DELETE)
	@Override
	public Task deleteTask(Task task) throws PortalException {

		// Task

		taskPersistence.remove(task);

		// Resources

		resourceLocalService.deleteResource(
			task.getCompanyId(), Task.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL, task.getTaskId());

		// TODO: Subscriptions

		// Asset

		assetEntryLocalService.deleteEntry(
			Task.class.getName(), task.getTaskId());

		// Expando

		expandoRowLocalService.deleteRows(
			task.getCompanyId(),
			_classNameLocalService.getClassNameId(Task.class.getName()),
			task.getTaskId());

		// TODO: Trash

		// Workflow

		workflowInstanceLinkLocalService.deleteWorkflowInstanceLinks(
			task.getCompanyId(), task.getGroupId(), Task.class.getName(),
			task.getTaskId());

		return task;
	}

	@Override
	public Task getTask(long taskId) throws PortalException {
		return taskPersistence.findByPrimaryKey(taskId);
	}

	public Task getTask(long groupId, String title) throws PortalException {
		return taskPersistence.fetchByG_T_First(groupId, title, null);
	}

	public List<Task> getTasksByKeywords(
		long groupId, String keywords, int start, int end,
		OrderByComparator<Task> orderByComparator) {

		return taskLocalService.dynamicQuery(
			_getKeywordSearchDynamicQuery(groupId, keywords), start, end,
			orderByComparator);
	}

	public long getTasksCountByKeywords(long groupId, String keywords) {
		return taskLocalService.dynamicQueryCount(
			_getKeywordSearchDynamicQuery(groupId, keywords));
	}

	@Override
	public void updateAsset(
			long userId, Task task, long[] assetCategoryIds,
			String[] assetTagNames)
		throws PortalException {

		assetEntryLocalService.updateEntry(
			userId, task.getGroupId(), task.getCreateDate(),
			task.getModifiedDate(), Task.class.getName(), task.getTaskId(),
			task.getUuid(), 0, assetCategoryIds, assetTagNames, true, false,
			null, null, null, null, ContentTypes.TEXT_HTML, task.getTitle(),
			null, null, null, null, 0, 0, null);
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public Task updateStatus(
			long userId, long taskId, int status, ServiceContext serviceContext)
		throws PortalException {

		// Task

		Task task = taskLocalService.getTask(taskId);

		task.setStatus(status);

		User user = userLocalService.getUser(userId);

		task.setStatusByUserId(user.getUserId());
		task.setStatusByUserName(user.getFullName());

		task.setStatusDate(serviceContext.getModifiedDate(new Date()));

		task = taskPersistence.update(task);

		// Asset

		if (status == WorkflowConstants.STATUS_APPROVED) {
			assetEntryLocalService.updateEntry(
				Task.class.getName(), task.getTaskId(), task.getStatusDate(),
				null, true, false);
		}

		return task;
	}

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public Task updateTask(
			long userId, long taskId, String title, String description,
			boolean completed, Date dueDate, ServiceContext serviceContext)
		throws PortalException {

		// Task

		Task task = taskPersistence.findByPrimaryKey(taskId);

		_validate(title, dueDate);

		task.setTitle(title);
		task.setDescription(description);
		task.setCompleted(completed);
		task.setDueDate(dueDate);

		task.setExpandoBridgeAttributes(serviceContext);

		// Asset

		updateAsset(
			serviceContext.getUserId(), task,
			serviceContext.getAssetCategoryIds(),
			serviceContext.getAssetTagNames());

		task = taskPersistence.update(task);

		// Workflow

		task = _startWorkflowInstance(userId, task, serviceContext);

		return task;
	}

	@Override
	public void updateTaskResources(
			Task task, ModelPermissions modelPermissions)
		throws PortalException {

		resourceLocalService.updateResources(
			task.getCompanyId(), task.getGroupId(), Task.class.getName(),
			task.getTaskId(), modelPermissions);
	}

	@Override
	public void updateTaskResources(
			Task task, String[] groupPermissions, String[] guestPermissions)
		throws PortalException {

		resourceLocalService.updateResources(
			task.getCompanyId(), task.getGroupId(), Task.class.getName(),
			task.getTaskId(), groupPermissions, guestPermissions);
	}

	private DynamicQuery _getKeywordSearchDynamicQuery(
		long groupId, String keywords) {

		DynamicQuery dynamicQuery = dynamicQuery().add(
			RestrictionsFactoryUtil.eq("groupId", groupId));

		if (Validator.isNotNull(keywords)) {
			Disjunction disjunctionQuery =
				RestrictionsFactoryUtil.disjunction();

			disjunctionQuery.add(
				RestrictionsFactoryUtil.like("title", "%" + keywords + "%"));
			disjunctionQuery.add(
				RestrictionsFactoryUtil.like(
					"description", "%" + keywords + "%"));

			dynamicQuery.add(disjunctionQuery);
		}

		return dynamicQuery;
	}

	private Task _startWorkflowInstance(
			long userId, Task task, ServiceContext serviceContext)
		throws PortalException {

		String userPortraitURL = StringPool.BLANK;
		String userURL = StringPool.BLANK;

		if (serviceContext.getThemeDisplay() != null) {
			User user = userLocalService.getUser(userId);

			userPortraitURL = user.getPortraitURL(
				serviceContext.getThemeDisplay());
			userURL = user.getDisplayURL(serviceContext.getThemeDisplay());
		}

		Map<String, Serializable> workflowContext =
			HashMapBuilder.<String, Serializable>put(

			// TODO

//			HashMapBuilder.<String, Serializable>put(
//				WorkflowConstants.CONTEXT_URL,
//				_getEntryURL(task, serviceContext)
//			).put(
				WorkflowConstants.CONTEXT_USER_PORTRAIT_URL, userPortraitURL
			).put(
				WorkflowConstants.CONTEXT_USER_URL, userURL
			).build();

		return WorkflowHandlerRegistryUtil.startWorkflowInstance(
			task.getCompanyId(), task.getGroupId(), userId,
			Task.class.getName(), task.getTaskId(), task, serviceContext,
			workflowContext);
	}

	private void _validate(String title, Date dueDate) throws PortalException {
		if (Validator.isNull(title)) {
			throw new TaskTitleException();
		}

		if (dueDate == null) {
			throw new TaskDueDateException();
		}
	}

	@Reference
	private ClassNameLocalService _classNameLocalService;

}