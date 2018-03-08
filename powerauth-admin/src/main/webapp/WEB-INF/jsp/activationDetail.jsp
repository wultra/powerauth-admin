<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="PowerAuth 2.0 - Activation Details"/>
</jsp:include>

<ol class="breadcrumb">
    <li><a class="black" href="${pageContext.request.contextPath}/activation/list">User Selection</a></li>
    <li><a class="black" href="${pageContext.request.contextPath}/activation/list?userId=<c:out value="${userId}"/>">User "<c:out value="${userId}"/>"</a></li>
    <li class="active">Activation Detail</li>
</ol>

<div class="row">
    <div class="col-md-4">

        <c:if test="${status == 'CREATED'}">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title pull-left">New Client Activation</h3>
                    <a href=""><span class="glyphicon glyphicon-refresh black pull-right"></span></a>
                    <div class="clearfix"></div>
                </div>
                <div class="panel-body gray">
                    <p>
                        Client Activation Code<br/>
                        <div class="input-group">
                            <input id="activation-code" type="text" class="form-control" readonly="readonly" value="<c:out value="${activationIdShort}"/>-<c:out value="${activationOtp}"/>">
                            <span class="input-group-btn">
                                <button class="btn btn-default btn-clipboard" type="button" data-clipboard-text="<c:out value="${activationIdShort}"/>-<c:out value="${activationOtp}"/>">
                                    <span class=" glyphicon glyphicon-copy"></span>
                                </button>
                            </span>
                        </div>
                    </p>
                    <p>
                        Client Activation Code Signature<br>
                        <strong class="code black wrap"><c:out value="${activationSignature}"/></strong>
                    </p>
                    <p>
                        <img src="<c:out value="${activationQR}"/>" class="w100" alt="Activation QR Code" style="border: 1px solid #777777"/>
                    </p>
                </div>
            </div>
        </c:if>

        <c:if test="${status == 'OTP_USED'}">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Activation Verification</h3>
                </div>
                <div class="panel-body gray">
                    <p>
                        Device Public Key Fingerprint<br/>
                        <div class="input-group">
                            <input id="activation-fingerprint" type="text" class="form-control" readonly="readonly" value="<c:out value="${activationFingerprint}"/>">
                            <span class="input-group-btn">
                                    <button class="btn btn-default btn-clipboard" type="button" data-clipboard-text="<c:out value="${activationFingerprint}"/>">
                                        <span class=" glyphicon glyphicon-copy"></span>
                                    </button>
                                </span>
                        </div>
                    </p>
                </div>
            </div>
        </c:if>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Basic Activation Information</h3>
            </div>
            <div class="panel-body gray">
                <p>
                    Activation ID<br>
                    <div class="input-group">
                        <input type="text" class="form-control code black" readonly="readonly" value="<c:out value="${activationId}"/>"/>
                        <span class="input-group-btn">
                            <button class="btn btn-default btn-clipboard" type="button" data-clipboard-text="<c:out value="${activationId}"/>">
                                <span class=" glyphicon glyphicon-copy"></span>
                            </button>
                        </span>
                    </div>
                </p>
                <c:if test="${activationName != null}">
                    <p>
                        Activation Name<br>
                        <span class="black"><c:out value="${activationName}"/></span>
                    </p>
                </c:if>
                <table class="w100">
                    <tr>
                        <td>
                            <p>
                                Created<br>
                                <span class="black">
                                    <fmt:formatDate type="both" pattern="yyyy/MM/dd hh:mm:ss" value="${timestampCreated.toGregorianCalendar().time}"/>
                                </span>
                            </p>
                        </td>
                        <td>
                            <p>
                                Last Used<br>
                                <span class="black">
                                    <fmt:formatDate type="both" pattern="yyyy/MM/dd hh:mm:ss" value="${timestampLastUsed.toGregorianCalendar().time}"/>
                                </span>
                            </p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>
                                Application<br>
                                <span class="black">
                                    <a class="black" href="${pageContext.request.contextPath}/application/detail/<c:out value="${applicationId}"/>">
                                        <c:out value="${applicationName}"/>
                                    </a>
                                </span>
                            </p>
                        </td>
                        <td>
                            <p>
                                Status<br>
                                <jsp:include page="activationStatusSnippet.jsp">
                                    <jsp:param value="${status}" name="status"/>
                                </jsp:include>
                            </p>
                        </td>
                    </tr>
                    <c:if test="${not empty blockedReason}">
                        <tr>
                            <td>
                                &nbsp;
                            </td>
                            <td>
                                Blocked Reason<br>
                                <span class="orange code">
                                    <c:out value="${blockedReason}"/>
                                </span>
                            </td>
                        </tr>
                    </c:if>
                </table>
            </div>
            <c:if test="${status != 'REMOVED'}">
                <div class="panel-footer">
                    <jsp:include page="activationStatusForms.jsp">
                        <jsp:param value="${status}" name="status"/>
                        <jsp:param value="${activationId}" name="activationId"/>
                    </jsp:include>
                </div>
            </c:if>
        </div>
    </div>

    <div class="col-md-8">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Last Signatures</h3>
            </div>
            <div class="panel-body">
                <form method="get" action="${pageContext.request.contextPath}/activation/detail/${activationId}" class="form-inline">
                    <div class="input-group w100">
                        <span class="input-group-addon">From</span>
                        <input type="text" name="fromDate" class="form-control" placeholder="yyyy/MM/dd hh:mm:ss" value="<c:out value="${fromDate}"/>">
                        <span class="input-group-addon">To</span>
                        <input type="text" name="toDate" class="form-control" placeholder="yyyy/DD/dd hh:mm:ss" value="<c:out value="${toDate}"/>">
                        <span class="input-group-btn">
                            <input type="submit" class="btn btn-default w100" value="Filter" />
                        </span>
                    </div>
                </form>
            </div>
            <table class="table w100">
                <tbody>
                <c:choose>
                    <c:when test="${fn:length(signatures) == 0}">
                        <tr class="code gray text-center">
                            <td colspan="4">
                                <p class="padder20">No signatures in the selected interval</p>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${signatures}" var="item">
                            <tr class="code">
                                <td class="gray" style="width: 270px;">
                                    <p>
                                        Transaction ID<br>
                                        <span class="black"><c:out value="${item.id}"/></span>
                                    </p>
                                    <p>
                                        Date<br>
                                        <span class="black">
                                            <fmt:formatDate type="both" pattern="yyyy/MM/dd hh:mm:ss" value="${item.timestampCreated.toGregorianCalendar().time}"/>
                                        </span>
                                    </p>
                                    <p>
                                        Value<br>
                                        <span class="black"><c:out value="${item.signature}"/></span>
                                    </p>
                                    <p>
                                        Type<br>
                                        <span class="black"><c:out value="${item.signatureType}"/></span>
                                    </p>
                                    <p>
                                        Result<br>
                                        <span class="black">
                                            <c:choose>
                                                <c:when test="${item.valid}"><span class="green">OK</span>:</c:when>
                                                <c:otherwise><span class="red">NOK</span>:</c:otherwise>
                                            </c:choose>
                                            <c:out value="${item.note}"/>
                                        </span>
                                    </p>
                                    <table class="w100">
                                        <tr>
                                            <td>
                                                Activation<br>
                                                <span class="black">
                                                    <jsp:include page="activationStatusSnippet.jsp">
                                                        <jsp:param value="${item.activationStatus}" name="status"/>
                                                    </jsp:include>
                                                </span>
                                            </td>
                                            <td>
                                                Counter<br>
                                                <span class="black"><c:out value="${item.activationCounter}"/></span>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <p class="wrap gray">
                                        Signed Data<br>
                                        <span class="black"><c:out value="${item.dataBase64}"/></span>
                                    </p>
                                    <c:if test="${not empty item.additionalInfo.entry}">
                                        <p class="wrap gray">
                                            Additional Information<br>
                                            <c:forEach var="entry" items="${item.additionalInfo.entry}">
                                                <span class="black">
                                                    <c:out value="${entry.key}"/>: <c:out value="${entry.value}"/>
                                                </span>
                                                <br/>
                                            </c:forEach>
                                        </p>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>

</div>

<jsp:include page="footer.jsp"/>