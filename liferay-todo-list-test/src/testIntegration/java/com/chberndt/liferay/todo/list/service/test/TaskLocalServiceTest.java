package com.chberndt.liferay.todo.list.service.test;

import com.chberndt.liferay.todo.list.model.Task;
import com.chberndt.liferay.todo.list.service.TaskLocalService;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Christian Berndt
 */
@RunWith(Arquillian.class)
public class TaskLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
		_user = TestPropsValues.getUser();

		UserTestUtil.setUser(TestPropsValues.getUser());
	}

	@Test
	public void testAddTask() throws Exception {
		int initialCount = _taskLocalService.getTasksCount();

		addTask(false);

		int actualCount = _taskLocalService.getTasksCount();

		Assert.assertEquals(initialCount + 1, actualCount);
	}

	protected Task addTask(boolean completed) throws Exception {
		return addTask(_user.getUserId(), completed);
	}

	protected Task addTask(long userId, boolean completed) throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), userId);

		Calendar dueDate = CalendarFactoryUtil.getCalendar(2020, 1, 1);

		return _taskLocalService.addTask(
			userId, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), completed, dueDate.getTime(),
			serviceContext);
	}

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private TaskLocalService _taskLocalService;

	private User _user;

}