<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.Monster" %>
<%@ page import="util.HtmlUtil" %>
<%
	String ctx = request.getContextPath();
%>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title">Monstruos por energia generada</h1>
	<div class="page-actions">
		<a href="<%= ctx %>/monster" class="btn btn-ghost">
			<i data-lucide="arrow-left" class="icon-sm"></i>
			Volver al listado
		</a>
	</div>
</div>

<%
	LinkedList<Monster> monsters = (LinkedList<Monster>) request.getAttribute("monsters");
	if (monsters == null || monsters.isEmpty()) {
%>
	<div class="empty-state">
		<i data-lucide="trophy" class="empty-state-icon"></i>
		<p>No hay datos para rankear todavia.</p>
	</div>
<%
	} else {
%>
	<div class="table-wrapper mt-3">
		<table class="table">
			<thead>
				<tr>
					<th>#</th>
					<th>ID</th>
					<th>Nombre</th>
					<th>Energia total</th>
				</tr>
			</thead>
			<tbody>
				<%
					int pos = 1;
					for (Monster m : monsters) {
				%>
				<tr>
					<td><strong><%= pos++ %></strong></td>
					<td><%= m.getId() %></td>
					<td><%= HtmlUtil.escapeHtml(m.getNombre()) %></td>
					<td><%= m.getEnergiaTotal() %></td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
	</div>
<%
	}
%>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
