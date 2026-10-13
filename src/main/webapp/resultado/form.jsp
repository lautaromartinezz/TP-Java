<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="entities.PuestoAsignado" %>
<%@ page import="entities.PuertaAsignada" %>
<%@ page import="entities.Resultado" %>
<%@ page import="util.HtmlUtil" %>
<%
	Resultado resultado = (Resultado) request.getAttribute("resultado");
	boolean edit = resultado != null && resultado.getId() > 0;
	boolean asustar = Boolean.TRUE.equals(request.getAttribute("asustar")) && !edit;
	String ctx = request.getContextPath();

	String fechaValor;
	if (edit) {
		fechaValor = resultado.getFechaInput();
	} else {
		fechaValor = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date());
	}
	String nombreNino = edit ? resultado.getNombreNino() : "";
	int idPuertaAsignadaActual = edit && resultado.getPuertaAsignada() != null ? resultado.getPuertaAsignada().getId() : 0;
	int energiaActual = edit ? resultado.getEnergiaGenerada() : 0;

	LinkedList<PuertaAsignada> puertasAsignadas = (LinkedList<PuertaAsignada>) request.getAttribute("puertasAsignadas");

	String titulo;
	if (edit) { titulo = "Editar resultado #" + resultado.getId(); }
	else if (asustar) { titulo = "Asustar a un nino"; }
	else { titulo = "Nuevo resultado"; }
%>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
	<h1 class="page-title"><%= titulo %></h1>
</div>

<div class="card card-form">
	<% if (asustar) { %>
		<p class="alert mb-3">La energia generada se calculara automaticamente al asustar.</p>
	<% } %>

	<form id="form" class="form" method="post" action="<%= ctx %>/resultado">
		<div class="form-group">
			<label class="form-label" for="fecha">Fecha y hora</label>
			<input class="form-input" type="datetime-local" id="fecha" name="fecha" value="<%= fechaValor %>" required>
		</div>
		<div class="form-group">
			<label class="form-label" for="nombre_nino">Nombre del nino</label>
			<input class="form-input" type="text" id="nombre_nino" name="nombre_nino" value="<%= HtmlUtil.escapeHtml(nombreNino) %>" required>
		</div>
		<div class="form-group">
			<label class="form-label" for="id_puerta_asignada">Puerta asignada (monstruo - puesto - puerta - fecha)</label>
			<select class="form-select" id="id_puerta_asignada" name="id_puerta_asignada" required>
				<option value="">-- Seleccione --</option>
				<%
					if (puertasAsignadas != null) {
						for (PuertaAsignada da : puertasAsignadas) {
							String etiqueta = "";
							PuestoAsignado pa = da.getPuestoAsignado();
							if (pa != null) {
								if (pa.getMonster() != null) { etiqueta += pa.getMonster().getNombre(); }
								if (pa.getPuesto() != null) { etiqueta += " - " + pa.getPuesto().getNombre(); }
							}
							if (da.getPuerta() != null) { etiqueta += " - " + da.getPuerta().getNumero(); }
							if (pa != null) { etiqueta += " - " + pa.getFechaFormateada(); }
				%>
				<option value="<%= da.getId() %>" <%= (da.getId() == idPuertaAsignadaActual ? "selected" : "") %>><%= HtmlUtil.escapeHtml(etiqueta) %></option>
				<%
						}
					}
				%>
			</select>
		</div>
		<% if (!asustar) { %>
		<div class="form-group">
			<label class="form-label" for="energia_generada">Energia generada</label>
			<input class="form-input" type="number" id="energia_generada" name="energia_generada" min="0" value="<%= energiaActual %>">
		</div>
		<% } %>
		<div class="alert alert-error" id="error" hidden></div>
		<div class="form-actions">
			<button type="submit" class="btn btn-primary">
				<i data-lucide="<%= asustar ? "ghost" : "save" %>" class="icon-sm"></i>
				<%= edit ? "Guardar" : (asustar ? "Asustar" : "Crear") %>
			</button>
			<a href="<%= ctx %>/resultado" class="btn btn-ghost">Volver</a>
		</div>
	</form>
</div>

<script>
	var ctx = '<%= ctx %>';
	var edit = <%= edit %>;
	var asustar = <%= asustar %>;
	var id = <%= edit ? resultado.getId() : 0 %>;

	document.getElementById('form').addEventListener('submit', function(e) {
		e.preventDefault();
		var fecha = document.getElementById('fecha').value;
		var nombreNino = document.getElementById('nombre_nino').value;
		var idPuertaAsignada = document.getElementById('id_puerta_asignada').value;
		if (!idPuertaAsignada) {
			var error = document.getElementById('error');
			error.textContent = 'Seleccione una puerta asignada';
			error.hidden = false;
			return;
		}
		var body = 'fecha=' + encodeURIComponent(fecha)
			+ '&nombre_nino=' + encodeURIComponent(nombreNino)
			+ '&id_puerta_asignada=' + encodeURIComponent(idPuertaAsignada);
		if (!asustar) {
			body += '&energia_generada=' + encodeURIComponent(document.getElementById('energia_generada').value);
		}
		var url = ctx + '/resultado';
		if (edit) {
			url += '?id=' + id;
		}
		fetch(url, {
			method: edit ? 'PUT' : 'POST',
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			body: body
		}).then(function(res) {
			if (res.ok || res.redirected) {
				location.href = ctx + '/resultado';
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
