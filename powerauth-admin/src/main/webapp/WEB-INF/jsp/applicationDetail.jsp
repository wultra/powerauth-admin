<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="PowerAuth 2.0 - Application Details"/>
</jsp:include>

<ol class="breadcrumb">
    <li><a class="black" href="${pageContext.request.contextPath}/application/list">Applications</a></li>
    <li class="active">Application Detail</li>
</ol>

<div class="row">

    <div class="col-md-4">
        <div class="panel panel-default">

            <div class="panel-heading">
                <h3 class="panel-title">Application #<c:out value="${id}"/>: <c:out value="${name}"/></h3>
            </div>

            <div class="panel-body">
                <p>Master Public Key</p>
                <div class="well code wrap"><c:out value="${masterPublicKey}"/></div>
            </div>

        </div>

    </div>

    <div class="col-md-8">

        <div class="panel panel-default">

            <div class="panel-heading">
                <h3 class="panel-title button-sm pull-left">Application Versions</h3>
                <a href="${pageContext.request.contextPath}/application/detail/<c:out value="${id}"/>/version/create"
                   class="btn btn-sm btn-default pull-right">New Version</a>
                <div class="clearfix"></div>
            </div>

            <table class="table">
                <thead>
                <tr>
                    <th>Version</th>
                    <th>Application Key</th>
                    <th>Application Secret</th>
                    <th colspan="2">Supported</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${versions}" var="item">
                    <tr class="code">
                        <td><c:out value="${item.applicationVersionName}"/></td>
                        <td><c:out value="${item.applicationKey}"/></td>
                        <td><c:out value="${item.applicationSecret}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${item.supported}">
                                    <span>Yes</span>
                                </c:when>
                                <c:otherwise>
                                    <span>No</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${item.supported}">
                                    <form action="${pageContext.request.contextPath}/application/detail/<c:out value="${id}"/>/version/update/do.submit"
                                          method="POST">
                                        <input type="hidden" name="enabled" value="false"/>
                                        <input type="hidden" name="version" value="<c:out value="${item.applicationVersionId}"/>"/>
                                        <input type="hidden" name="${_csrf.parameterName}"   value="${_csrf.token}" />
                                        <input type="submit" value="Disable" class="btn btn-sm btn-danger w100"/>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/application/detail/<c:out value="${id}"/>/version/update/do.submit"
                                          method="POST">
                                        <input type="hidden" name="enabled" value="true"/>
                                        <input type="hidden" name="version" value="<c:out value="${item.applicationVersionId}"/>"/>
                                        <input type="hidden" name="${_csrf.parameterName}"   value="${_csrf.token}" />
                                        <input type="submit" value="Enable" class="btn btn-sm btn-default w100"/>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="panel panel-default">

            <div class="panel-heading">
                <h3 class="panel-title button-sm pull-left">Callbacks</h3>
                <a href="${pageContext.request.contextPath}/application/detail/<c:out value="${id}"/>/callback/create"
                   class="btn btn-sm btn-default pull-right">Add Callback</a>
                <div class="clearfix"></div>
            </div>

            <c:choose>
                <c:when test="${fn:length(callbacks) == 0}">
                    <div class="panel-body">
                        <p class="gray text-center">
                            No callbacks are configured
                        </p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Callback URL</th>
                            <th>&nbsp;</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${callbacks}" var="item">
                            <tr class="code" title="Callback ID: <c:out value="${item.id}"/>">
                                <td><c:out value="${item.name}"/></td>
                                <td><c:out value="${item.callbackUrl}"/></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/application/detail/<c:out value="${id}"/>/callback/remove/do.submit" method="POST" class="action-remove">
                                        <input type="hidden" name="id" value="<c:out value="${item.id}"/>"/>
                                        <input type="hidden" name="${_csrf.parameterName}"   value="${_csrf.token}" />
                                        <input type="submit" value="Remove" class="btn btn-sm btn-default w100"/>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

    </div>

</div>

<jsp:include page="footer.jsp"/>