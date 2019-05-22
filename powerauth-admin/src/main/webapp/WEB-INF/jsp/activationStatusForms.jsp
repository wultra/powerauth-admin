<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
    <c:when test="${param.status == 'CREATED'}">
        <form action="${pageContext.request.contextPath}/activation/remove/do.submit" method="POST"
              class="pull-right action-remove">
            <input type="hidden" name="activationId" value="<c:out value="${param.activationId}"/>"/>
            <input type="hidden" name="redirectUserId" value="<c:out value="${param.redirectUserId}"/>"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input class="btn btn-danger btn-table" type="submit" value="Remove">
        </form>
    </c:when>
    <c:when test="${param.status == 'OTP_USED'}">
        <form action="${pageContext.request.contextPath}/activation/remove/do.submit" method="POST"
              class="pull-right action-remove">
            <input type="hidden" name="activationId" value="<c:out value="${param.activationId}"/>"/>
            <input type="hidden" name="redirectUserId" value="<c:out value="${param.redirectUserId}"/>"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input class="btn btn-danger btn-table" type="submit" value="Remove">
        </form>
        <form action="${pageContext.request.contextPath}/activation/commit/do.submit" method="POST" class="pull-right">
            <input type="hidden" name="activationId" value="<c:out value="${param.activationId}"/>"/>
            <input type="hidden" name="redirectUserId" value="<c:out value="${param.redirectUserId}"/>"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input class="btn btn-success btn-table" type="submit" value="Commit">
        </form>
    </c:when>
    <c:when test="${param.status == 'ACTIVE'}">
        <form action="${pageContext.request.contextPath}/activation/remove/do.submit" method="POST"
              class="pull-right action-remove">
            <input type="hidden" name="activationId" value="<c:out value="${param.activationId}"/>"/>
            <input type="hidden" name="redirectUserId" value="<c:out value="${param.redirectUserId}"/>"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input class="btn btn-danger btn-table" type="submit" value="Remove">
        </form>
        <form action="${pageContext.request.contextPath}/activation/block/do.submit" method="POST" class="pull-right">
            <input type="hidden" name="activationId" value="<c:out value="${param.activationId}"/>"/>
            <input type="hidden" name="redirectUserId" value="<c:out value="${param.redirectUserId}"/>"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input class="btn btn-warning btn-table" type="submit" value="Block">
        </form>
    </c:when>
    <c:when test="${param.status == 'BLOCKED'}">
        <form action="${pageContext.request.contextPath}/activation/remove/do.submit" method="POST"
              class="pull-right action-remove">
            <input type="hidden" name="activationId" value="<c:out value="${param.activationId}"/>"/>
            <input type="hidden" name="redirectUserId" value="<c:out value="${param.redirectUserId}"/>"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input class="btn btn-default btn-table" type="submit" value="Remove">
        </form>
        <form action="${pageContext.request.contextPath}/activation/unblock/do.submit" method="POST" class="pull-right">
            <input type="hidden" name="activationId" value="<c:out value="${param.activationId}"/>"/>
            <input type="hidden" name="redirectUserId" value="<c:out value="${param.redirectUserId}"/>"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input class="btn btn-danger btn-table" type="submit" value="Unblock">
        </form>
    </c:when>
</c:choose>
<div class="clearfix"></div>