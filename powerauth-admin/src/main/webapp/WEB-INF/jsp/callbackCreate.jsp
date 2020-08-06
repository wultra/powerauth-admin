<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="PowerAuth Admin - New Callback URL"/>
</jsp:include>

<ol class="breadcrumb">
    <li><a class="black" href="${pageContext.request.contextPath}/application/list">Applications</a></li>
    <li><a class="black" href="${pageContext.request.contextPath}/application/detail/<c:out value="${applicationId}"/>#callbacks">Application Detail</a></li>
    <li class="active">New Callback</li>
</ol>

<div class="row">
    <div class="col-sm-7">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">New Callback</h3>
            </div>
            <div class="panel-body">
                <form class="form-horizontal" method="POST" action="${pageContext.request.contextPath}/application/detail/<c:out value="${applicationId}"/>/callback/create/do.submit">
                    <div class="form-group">
                        <label for="name" class="col-sm-3 control-label">Callback Name</label>
                        <div class="col-sm-9">
                            <input type="text" id="name" name="name" class="form-control" value="${name}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="callbackUrl" class="col-sm-3 control-label">Callback URL</label>
                        <div class="col-sm-9">
                            <input type="text" id="callbackUrl" name="callbackUrl" class="form-control" value="${callbackUrl}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="attr_activationId" class="col-sm-3 control-label">Activation ID</label>
                        <div class="col-sm-9" style="margin-top: 6px">
                            <input type="checkbox" id="attr_activationId" name="attr_activationId" checked disabled/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="attr_userId" class="col-sm-3 control-label">User ID</label>
                        <div class="col-sm-9" style="margin-top: 6px">
                            <input type="checkbox" id="attr_userId" name="attr_userId" onchange="refreshCallbackJson()" <c:if test="${not empty attr_userId}">checked</c:if>/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="attr_activationName" class="col-sm-3 control-label">Activation Name</label>
                        <div class="col-sm-9" style="margin-top: 6px">
                            <input type="checkbox" id="attr_activationName" name="attr_activationName" onchange="refreshCallbackJson()" <c:if test="${not empty attr_activationName}">checked</c:if>/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="attr_deviceInfo" class="col-sm-3 control-label">Device Info</label>
                        <div class="col-sm-9" style="margin-top: 6px">
                            <input type="checkbox" id="attr_deviceInfo" name="attr_deviceInfo" onchange="refreshCallbackJson()" <c:if test="${not empty attr_deviceInfo}">checked</c:if>/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="attr_platform" class="col-sm-3 control-label">Platform</label>
                        <div class="col-sm-9" style="margin-top: 6px">
                            <input type="checkbox" id="attr_platform" name="attr_platform" onchange="refreshCallbackJson()" <c:if test="${not empty attr_platform}">checked</c:if>/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="attr_activationFlags" class="col-sm-3 control-label">Activation Flags</label>
                        <div class="col-sm-9" style="margin-top: 6px">
                            <input type="checkbox" id="attr_activationFlags" name="attr_activationFlags" onchange="refreshCallbackJson()" <c:if test="${not empty attr_activationFlags}">checked</c:if>/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="attr_activationStatus" class="col-sm-3 control-label">Activation Status</label>
                        <div class="col-sm-9" style="margin-top: 6px">
                            <input type="checkbox" id="attr_activationStatus" name="attr_activationStatus" onchange="refreshCallbackJson()" <c:if test="${not empty attr_activationStatus}">checked</c:if>/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="attr_blockedReason" class="col-sm-3 control-label">Blocked Reason</label>
                        <div class="col-sm-9" style="margin-top: 6px">
                            <input type="checkbox" id="attr_blockedReason" name="attr_blockedReason" onchange="refreshCallbackJson()" <c:if test="${not empty attr_blockedReason}">checked</c:if>/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="attr_applicationId" class="col-sm-3 control-label">Application ID</label>
                        <div class="col-sm-9" style="margin-top: 6px">
                            <input type="checkbox" id="attr_applicationId" name="attr_applicationId" onchange="refreshCallbackJson()" <c:if test="${not empty attr_applicationId}">checked</c:if>/>
                        </div>
                    </div>
                    <div class="form-group text-right">
                        <div class="col-sm-9">
                            <c:if test="${not empty error}">
                                <span style="color: #c0007f; margin-left: 10px;"><c:out value="${error}"/></span>
                            </c:if>
                        </div>
                        <div class="col-sm-3">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <input type="submit" value="Submit" class="btn btn-success"/>
                        </div>
                    </div>
                </form>
            </div>

        </div>
    </div>
    <div class="col-sm-5">
        <div class="panel panel-info">

            <div class="panel-heading">
                <h3 class="panel-title"><span class="glyphicon glyphicon-info-sign"></span> What are the callbacks?</h3>
            </div>

            <div class="panel-body">
                <p>
                    We will <span class="code">POST</span> a following JSON callback to the URL you
                    specify whenever an activation status changes.
                </p>
                <pre class="code"><code class="json" id="callback_json"/></pre>
            </div>

        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
<jsp:include page="footerCallbacks.jsp"/>
