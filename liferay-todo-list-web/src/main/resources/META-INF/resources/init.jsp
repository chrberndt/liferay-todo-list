<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/clay" prefix="clay" %><%@
taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %><%@
taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.chberndt.liferay.todo.list.exception.NoSuchTaskException" %><%@
page import="com.chberndt.liferay.todo.list.internal.display.context.TasksDisplayContext" %><%@
page import="com.chberndt.liferay.todo.list.internal.display.context.TasksManagementToolbarDisplayContext" %><%@
page import="com.chberndt.liferay.todo.list.internal.servlet.taglib.clay.TaskVerticalCard" %><%@
page import="com.chberndt.liferay.todo.list.internal.servlet.taglib.util.TaskActionDropdownItemsProvider" %><%@
page import="com.chberndt.liferay.todo.list.internal.util.WebKeys" %><%@
page import="com.chberndt.liferay.todo.list.model.Task" %><%@
page import="com.chberndt.liferay.todo.list.web.constants.ToDoListWebKeys" %>

<%@ page import="com.liferay.portal.kernel.bean.BeanParamUtil" %><%@
page import="com.liferay.portal.kernel.dao.search.SearchContainer" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.util.Constants" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.StringUtil" %>

<%@ page import="java.util.Date" %><%@
page import="java.util.HashMap" %><%@
page import="java.util.Map" %>

<%@ page import="javax.portlet.PortletURL" %>

<liferay-frontend:defineObjects />

<liferay-theme:defineObjects />

<portlet:defineObjects />