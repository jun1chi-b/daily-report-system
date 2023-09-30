<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst, java.util.List, java.util.Date,  java.time.LocalDate, java.time.LocalTime" %>

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            <c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>
<fmt:parseDate value="${timecard.timecardDate}" pattern="yyyy-MM-dd" var="timecardDay" type="date" />
<label>日付</label><br />
<div><fmt:formatDate value='${timecardDay}' pattern='yyyy-MM-dd' /></div>
<br /><br />

<label>氏名</label><br />
<c:out value="${sessionScope.login_employee.name}" />
<br /><br />

<label for="${AttributeConst.TIM_CLOCK_IN_H.getValue()}">出勤</label><br />
<select name="${AttributeConst.TIM_CLOCK_IN_H.getValue()}">
<c:forEach begin="0" end="23" step="1" var="i">
    <option value="${i}"<c:if test="${i == timecard.clockIn.getHour()}"> selected </c:if>><c:if test="${i < 10}">0</c:if><c:out value="${i}"/></option>
</c:forEach>
</select>
:
<label for="${AttributeConst.TIM_CLOCK_IN_M.getValue()}"></label>
<select name="${AttributeConst.TIM_CLOCK_IN_M.getValue()}">
<c:forEach begin="0" end="55" step="5" var="i">
    <option value="${i}"<c:if test="${i == timecard.clockIn.getMinute()}"> selected </c:if>><c:if test="${i < 10}">0</c:if><c:out value="${i}"/></option>
</c:forEach>
</select>
<br /><br />

<label for="${AttributeConst.TIM_CLOCK_OUT_H.getValue()}">退勤</label><br />
<select name="${AttributeConst.TIM_CLOCK_OUT_H.getValue()}" id="${AttributeConst.TIM_CLOCK_OUT_H.getValue()}">
<c:forEach begin="0" end="23" step="1" var="i">
    <option value="${i}"<c:if test="${i == timecard.clockOut.getHour()}"> selected </c:if>><c:if test="${i < 10}">0</c:if><c:out value="${i}"/></option>
</c:forEach>
</select>
:
<label for="${AttributeConst.TIM_CLOCK_OUT_H.getValue()}"></label>
<select name="${AttributeConst.TIM_CLOCK_OUT_M.getValue()}" id="${AttributeConst.TIM_CLOCK_OUT_M.getValue()}">
<c:forEach begin="0" end="55" step="5" var="i">
    <option value="${i}"<c:if test="${i == timecard.clockOut.getMinute()}"> selected </c:if>><c:if test="${i < 10}">0</c:if><c:out value="${i}"/></option>
</c:forEach>
</select>
<br /><br />

<label for="${AttributeConst.TIM_WORKING_HOURS_H.getValue()}">勤務時間</label><br />
<select name="${AttributeConst.TIM_WORKING_HOURS_H.getValue()}" id="${AttributeConst.TIM_WORKING_HOURS_H.getValue()}">
<c:forEach begin="0" end="23" step="1" var="i">
    <option value="${i}"<c:if test="${i == timecard.workingHours.getHour()}"> selected </c:if>><c:if test="${i < 10}">0</c:if><c:out value="${i}"/></option>
</c:forEach>
</select>
:
<label for="${AttributeConst.TIM_WORKING_HOURS_H.getValue()}"></label>
<select name="${AttributeConst.TIM_WORKING_HOURS_M.getValue()}" id="${AttributeConst.TIM_WORKING_HOURS_M.getValue()}">
<c:forEach begin="0" end="55" step="5" var="i">
    <option value="${i}"<c:if test="${i == timecard.workingHours.getMinute()}"> selected </c:if>><c:if test="${i < 10}">0</c:if><c:out value="${i}"/></option>
</c:forEach>
</select>
<br /><br />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<input type="hidden" name="${AttributeConst.TIM_DATE.getValue()}" value="${timecard.timecardDate}">
<button type="submit">投稿</button>