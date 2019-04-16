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

    <div class="col-md-12">
        <div class="panel panel-default">

            <div class="panel-heading">
                <h3 class="panel-title">Application #<c:out value="${id}"/>: <c:out value="${name}"/></h3>
            </div>

            <div id="panel-with-tabs" class="panel panel-default">
            <ul class="nav nav-tabs" id="nav-tab" role="tablist">
                <li role="presentation"><a href="#versions" id="tabs-versions" aria-controls="versions" role="tab" data-toggle="tab">Mobile App Config</a></li>
                <li role="presentation"><a href="#callbacks" id="tabs-callbacks" aria-controls="callbacks" role="tab" data-toggle="tab">Callbacks</a></li>
                <li role="presentation"><a href="#recovery" id="tabs-recovery" aria-controls="recovery" role="tab" data-toggle="tab">Recovery Settings</a></li>
            </ul>
            <div id="tab-content" class="tab-content">
                <div role="tabpanel" class="tab-pane" id="versions" aria-labelledby="tabs-versions">
                    <table class="table w100">
                        <tbody>
                        <tr>
                            <td>
                                <div class="panel-body col-md-12">
                                    <h3 class="panel-title pull-left">Master Public Key</h3>
                                    <div class="well code wrap" style="margin-top: 25px; margin-bottom: 5px">
                                        <c:out value="${masterPublicKey}"/>
                                    </div>
                                    <div>
                                        <button class="btn btn-default btn-clipboard" type="button" data-clipboard-text="<c:out value="${masterPublicKey}"/>">
                                            <span class=" glyphicon glyphicon-copy"></span>
                                        </button>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div class="panel-heading">
                                    <h3 class="panel-title button-sm pull-left">Application Versions</h3>
                                    <a href="${pageContext.request.contextPath}/application/detail/<c:out value="${id}"/>/version/create" class="btn btn-sm btn-default pull-right">New Version</a>
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
                                            <td class="text-nowrap">
                                                <c:out value="${item.applicationKey}"/>
                                                <button class="btn btn-default btn-clipboard" type="button" data-clipboard-text="<c:out value="${item.applicationKey}"/>">
                                                    <span class="glyphicon glyphicon-copy"></span>
                                                </button>
                                            </td>
                                            <td class="text-nowrap">
                                                <c:out value="${item.applicationSecret}"/>
                                                <button class="btn btn-default btn-clipboard" type="button" data-clipboard-text="<c:out value="${item.applicationSecret}"/>">
                                                    <span class="glyphicon glyphicon-copy"></span>
                                                </button>
                                            </td>
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
                                                        <form action="${pageContext.request.contextPath}/application/detail/<c:out value="${id}"/>/version/update/do.submit" method="POST">
                                                            <input type="hidden" name="enabled" value="false"/>
                                                            <input type="hidden" name="version" value="<c:out value="${item.applicationVersionId}"/>"/>
                                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                            <input type="submit" value="Disable" class="btn btn-sm btn-danger pull-right"/>
                                                        </form>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form action="${pageContext.request.contextPath}/application/detail/<c:out value="${id}"/>/version/update/do.submit" method="POST">
                                                            <input type="hidden" name="enabled" value="true"/>
                                                            <input type="hidden" name="version" value="<c:out value="${item.applicationVersionId}"/>"/>
                                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                            <input type="submit" value="Enable" class="btn btn-sm btn-default pull-right"/>
                                                        </form>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div role="tabpanel" class="tab-pane" id="callbacks" aria-labelledby="tabs-callbacks">
                    <table class="table w100">
                        <tbody>
                        <tr>
                            <td>
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
                                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                            <input type="submit" value="Remove" class="btn btn-sm btn-default pull-right"/>
                                                        </form>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div role="tabpanel" class="tab-pane" id="recovery" aria-labelledby="tabs-recovery">
                    <form action="${pageContext.request.contextPath}/application/detail/<c:out value="${id}"/>/recovery/update/do.submit" method="POST" class="action-update">
                    <table class="table w100" style="margin-bottom: 0px">
                        <tbody>
                        <tr>
                            <td>
                                <div class="panel-heading">
                                    <h3 class="panel-title">Activation Recovery</h3>
                                </div>

                                    <div class="panel-body">
                                        <p><input type="checkbox" name="activationRecoveryEnabled" <c:if test="${activationRecoveryEnabled}">checked</c:if>/>&nbsp;Activation Recovery Enabled</p>
                                        <c:if test="${activationRecoveryEnabled}">
                                            <p><input type="checkbox" name="recoveryPostcardEnabled" <c:if test="${recoveryPostcardEnabled}">checked</c:if>/>&nbsp;Recovery Postcard Enabled</p>
                                            <c:if test="${recoveryPostcardEnabled}">
                                                <c:if test="${not empty postcardPublicKey}">
                                                    <br/>
                                                    <p><b>Recovery Postcard Public Key</b></p>
                                                    <i>This public key represents PowerAuth server during key exchange with Postcard printing center.</i>
                                                    <div class="well code wrap" style="margin-bottom: 5px">
                                                        <c:out value="${postcardPublicKey}"/>
                                                    </div>
                                                    <div>
                                                        <button class="btn btn-default btn-clipboard" type="button" data-clipboard-text="<c:out value="${postcardPublicKey}"/>">
                                                            <span class="glyphicon glyphicon-copy"></span>
                                                        </button>
                                                    </div>
                                                </c:if>

                                                <p><b>Recovery Postcard Printing Center Public Key</b></p>
                                                <i>This public key represents Postcard printing center during key exchange with PowerAuth server.</i>
                                                <div>
                                                    <textarea class="form-control noresize well code wrap w100" style="resize: none; margin-bottom: 5px" rows="1" name="remotePostcardPublicKeyBase64"><c:out value="${remotePostcardPublicKeyBase64}"/></textarea>
                                                </div>
                                            </c:if>
                                        </c:if>
                                        <input type="hidden" name="applicationId" value="<c:out value="${id}"/>"/>
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>

                    <input type="submit" value="Update Settings" class="btn btn-primary" style="margin-left: 22px; margin-bottom: 22px"/>
                    </form>

                </div>
            </div>

        </div>

        </div>

    </div>

</div>

<jsp:include page="footer.jsp"/>

<script>
    $(document).ready(function (event) {
        // Choose tab by location hash
        if (!window.location.hash) {
            $('a[href="#versions"]').tab('show');
        } else {
            $('a[href="' + window.location.hash + '"]').tab('show');
        }
        // Change location hash on click
        $('.nav-tabs a').on('shown.bs.tab', function (e) {
            if (history.pushState) {
                history.pushState(null, null, e.target.hash);
            } else {
                window.location.hash = e.target.hash;
            }
        });
    });
</script>