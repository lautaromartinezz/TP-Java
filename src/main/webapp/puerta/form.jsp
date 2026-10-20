<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="entities.Puerta" %>
<%@ page import="util.HtmlUtil" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
	Puerta puerta = (Puerta) request.getAttribute("puerta");
	boolean edit = puerta != null && puerta.getId() > 0;
	String ctx = request.getContextPath();
	String numero = edit ? puerta.getNumero() : "";
%>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title"><%= edit ? "Editar puerta #" + puerta.getId() : "Nueva puerta" %></h1>
</div>

<div class="card card-form">
	<form id="form" class="form" method="post" action="<%= ctx %>/puerta">
		<div class="form-group">
			<label class="form-label" for="numero">Numero</label>
			<input class="form-input" type="text" id="numero" name="numero" value="<%= HtmlUtil.escapeHtml(numero) %>" required>
		</div>
		<div class="alert alert-error" id="error" hidden></div>
		<div class="form-group" style="margin-top: 20px;">
			<p class="form-label" style="margin-bottom: 8px;">Miedos que requiere esta Puerta</p>
			<div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(180px, 1fr)); gap: 12px; background: rgba(0,0,0,0.2); padding: 20px; border-radius: 8px; border: 1px solid var(--color-border);">
				
				<c:forEach var="miedo" items="${listaMiedos}">
					<label style="display: flex; align-items: center; gap: 8px; color: var(--color-text); cursor: pointer; font-size: 0.95rem;">
						<input type="checkbox" name="miedos" value="${miedo.id}" 
       ${miedosAsignados != null && miedosAsignados.contains(miedo.id) ? 'checked' : ''} 
       style="width: 18px; height: 18px; accent-color: var(--color-accent);">
						${miedo.nombre}
					</label>
				</c:forEach>
				
				<c:if test="${empty listaMiedos}">
					<span style="color: #ff5555; font-size: 0.9rem;">No hay miedos creados en el sistema.</span>
				</c:if>
			</div>
		</div>
		<div class="form-actions">
			<button type="submit" class="btn btn-primary">
				<i data-lucide="save" class="icon-sm"></i>
				<%= edit ? "Guardar" : "Crear" %>
			</button>
			<a href="<%= ctx %>/puerta" class="btn btn-ghost">Volver</a>
		</div>
	</form>
</div>

<script>
	var ctx = '<%= ctx %>';
	var edit = <%= edit %>;
	var id = <%= edit ? puerta.getId() : 0 %>;

	document.getElementById('form').addEventListener('submit', function(e) {
		e.preventDefault();
		
		var url = ctx + '/puerta';
		if (edit) {
			url += '?id=' + id;
		}
		var formData = new FormData(this);
		var parametros = new URLSearchParams(formData).toString();

		fetch(url, {
			method: edit ? 'PUT' : 'POST',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			body: parametros 
		}).then(function(res) {
			if (res.ok || res.redirected) {
				location.href = ctx + '/puerta';
			} else {
				return res.text().then(function(msg) {
					var error = document.getElementById('error');
					error.textContent = msg;
					error.hidden = false;
				});
			}
		});
	});
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
