<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Importamos JSTL para poder iterar la lista de puertas -->
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
  <h1 class="page-title">Panel de Control: Nivel de Sustos</h1>
</div>

<!-- ========================================== -->
<!-- SECCIÓN 1: KPIs DEL MONSTRUO               -->
<!-- ========================================== -->
<div class="card-grid">
  
  <!-- KPI 1: Rendimiento Crítico -->
  <article class="card card-metric">
    <header class="card-metric-header">
      <h2 class="card-metric-title">Sustos bajo la media</h2>
      <i data-lucide="trending-down" class="icon-md" style="color: #ff4444;" aria-hidden="true"></i>
    </header>
    <div class="card-metric-value">${sustosBajos != null ? sustosBajos : 0}</div>
    <footer class="card-metric-footer">
      <span class="link" style="color: #8b949e; cursor: default;">
        Requieren mejora inmediata
      </span>
    </footer>
  </article>

  <!-- KPI 2: Carga Operativa -->
  <article class="card card-metric">
    <header class="card-metric-header">
      <h2 class="card-metric-title">Puertas pendientes (Hoy)</h2>
      <i data-lucide="door-closed" class="icon-md" style="color: #00ff66;" aria-hidden="true"></i>
    </header>
    <div class="card-metric-value">${puertasHoy != null ? puertasHoy : 0}</div>
    <footer class="card-metric-footer">
      <a href="<%= request.getContextPath() %>/resultado?action=mis_sustos" class="link link-with-icon">
        Ver todas las asignaciones
        <i data-lucide="arrow-right" class="icon-sm" aria-hidden="true"></i>
      </a>
    </footer>
  </article>

  <!-- KPI 3: Impacto en el Negocio -->
  <article class="card card-metric">
    <header class="card-metric-header">
      <h2 class="card-metric-title">Energía generada</h2>
      <i data-lucide="battery-charging" class="icon-md" style="color: #e3b341;" aria-hidden="true"></i>
    </header>
    <div class="card-metric-value">${energiaTotal != null ? energiaTotal : 0} <span style="font-size: 1rem; color: #8b949e;">kW</span></div>
    <footer class="card-metric-footer">
      <span class="link" style="color: #8b949e; cursor: default;">
        Acumulado histórico
      </span>
    </footer>
  </article>

</div>

<!-- ========================================== -->
<!-- SECCIÓN 2: LISTA DE PRÓXIMAS 3 PUERTAS     -->
<!-- ========================================== -->
<div class="card" style="margin-top: 30px; padding: 25px;">
  <header style="border-bottom: 1px solid #30363d; padding-bottom: 15px; margin-bottom: 20px; display: flex; align-items: center;">
    <i data-lucide="list-todo" class="icon-md" style="margin-right: 10px; color: #c9d1d9;"></i>
    <h2 style="margin: 0; font-size: 1.25rem; color: #fff;">Próximos Objetivos (Sustos Pendientes)</h2>
  </header>
  
  <div style="overflow-x: auto;">
    <table style="width: 100%; border-collapse: collapse; text-align: left;">
      <thead>
        <tr style="color: #8b949e; border-bottom: 1px solid #30363d;">
          <th style="padding: 12px 10px; font-weight: 600;">Nº Puerta</th>
          <th style="padding: 12px 10px; font-weight: 600;">DNI Objetivo</th>
          <th style="padding: 12px 10px; font-weight: 600;">Puesto / Estación</th>
          <th style="padding: 12px 10px; font-weight: 600; text-align: right;">Acción</th>
        </tr>
      </thead>
      <tbody>
        <!-- Iteramos sobre la lista inyectada por el Servlet -->
        <c:forEach var="puerta" items="${listaPuertasPendientes}">
          <tr style="border-bottom: 1px solid #21262d; transition: background-color 0.2s;">
            <td style="padding: 15px 10px; color: #fff; font-weight: bold;">
              <i data-lucide="door-open" class="icon-sm" style="vertical-align: middle; margin-right: 5px; color: #00ff66;"></i>
              ${puerta.numeroPuerta}
            </td>
            <td style="padding: 15px 10px; color: #c9d1d9;">${puerta.dniNino}</td>
            <td style="padding: 15px 10px; color: #c9d1d9;">${puerta.nombrePuesto}</td>
            <td style="padding: 15px 10px; text-align: right;">
              <button type="button" onclick="abrirModalSusto('${puerta.idAsignacion}', '${puerta.numeroPuerta}')" class="btn btn-primary">
				  <i data-lucide="zap" class="icon-sm" style="margin-right: 5px;"></i> Cargar Susto
				</button>
            </td>
          </tr>
        </c:forEach>
        
        <!-- Mensaje por si no hay asignaciones pendientes -->
        <c:if test="${empty listaPuertasPendientes}">
          <tr>
            <td colspan="4" style="padding: 30px 10px; text-align: center; color: #8b949e;">
              <i data-lucide="check-circle" class="icon-md" style="display: block; margin: 0 auto 10px auto; color: #00ff66;"></i>
              No tienes puertas asignadas pendientes para hoy. ¡Buen trabajo!
            </td>
          </tr>
        </c:if>
      </tbody>
    </table>
  </div>
</div>
<div id="modalSusto" class="modal-overlay">
  <div class="modal-content">
    
    <div class="modal-header">
      <h3 class="modal-title">Registrar Energía Generada</h3>
      <button type="button" class="btn-close" onclick="cerrarModalSusto()">
        <i data-lucide="x" class="icon-md"></i>
      </button>
    </div>

    <div class="modal-body">
      <!-- El form apunta a tu ResultadoServlet. Asegúrate de manejar el POST ahí -->
      <form action="<%= request.getContextPath() %>/resultado" method="POST" class="form">
        <!-- Parámetro oculto para decirle al Servlet qué acción ejecutar -->
        <input type="hidden" name="action" value="insert">
        
        <!-- Este input oculto guardará dinámicamente el ID de la asignación -->
        <input type="hidden" name="id_puerta_asignada" id="modal_id_asignacion">

        <p style="margin-bottom: 20px; color: var(--color-text-2); font-size: 0.9rem;">
          Ingresa los kilovatios (kW) extraídos del objetivo en la <strong id="modal_txt_puerta" style="color: var(--color-accent);"></strong>.
        </p>

        <div class="form-group">
          <label for="energia" class="form-label">Energía Recolectada (kW)</label>
          <input type="number" id="energia" name="energia_generada" class="form-input" required min="1" placeholder="Ej: 150">
        </div>

        <div class="form-actions mt-4" style="justify-content: flex-end;">
          <button type="button" class="btn btn-ghost" onclick="cerrarModalSusto()">Cancelar</button>
          <button type="submit" class="btn btn-primary">
            <i data-lucide="save" class="icon-sm"></i> Guardar Susto
          </button>
        </div>
      </form>
    </div>

  </div>
</div>

<!-- ========================================== -->
<!-- SECCIÓN 3: ÚLTIMOS 3 SUSTOS (RESUMEN)      -->
<!-- ========================================== -->
<div class="card" style="margin-top: 30px; padding: 25px;">
  <header style="border-bottom: 1px solid var(--color-border); padding-bottom: 15px; margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center;">
    <h2 style="margin: 0; font-size: 1.25rem; color: #fff;">
      <i data-lucide="history" class="icon-sm" style="margin-right: 8px;"></i> Últimos sustos realizados
    </h2>
    <a href="<%= request.getContextPath() %>/resultado" class="link link-with-icon">
      Ver historial completo <i data-lucide="arrow-right" class="icon-sm"></i>
    </a>
  </header>
  
  <table class="table">
    <thead>
      <tr>
        <th>Fecha</th>
        <th>Nº Puerta</th>
        <th>Puesto</th>
        <th style="text-align: right;">Energía (kW)</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="susto" items="${ultimosSustos}">
        <tr>
          <td style="color: var(--color-text-2);">${susto.fecha}</td>
          <td style="color: var(--color-text); font-weight: 500;">${susto.numeroPuerta}</td>
          <td style="color: var(--color-text-2);">${susto.nombrePuesto}</td>
          <td style="text-align: right; color: var(--color-accent); font-weight: bold;">${susto.energiaGenerada}</td>
        </tr>
      </c:forEach>
      <c:if test="${empty ultimosSustos}">
        <tr><td colspan="4" class="empty-state">Aún no has registrado sustos.</td></tr>
      </c:if>
    </tbody>
  </table>
</div>
<script>
  function abrirModalSusto(idAsignacion, numeroPuerta) {
    // 1. Inyectamos los datos en el modal
    document.getElementById('modal_id_asignacion').value = idAsignacion;
    document.getElementById('modal_txt_puerta').textContent = 'Puerta Nº ' + numeroPuerta;
    
    // 2. Mostramos el modal
    document.getElementById('modalSusto').classList.add('active');
  }

  function cerrarModalSusto() {
    // Ocultamos el modal y limpiamos el campo numérico
    document.getElementById('modalSusto').classList.remove('active');
    document.getElementById('energia').value = ''; 
  }

  // Permite cerrar el modal si el monstruo hace clic afuera de la caja
  document.getElementById('modalSusto').addEventListener('click', function(e) {
    if (e.target === this) {
      cerrarModalSusto();
    }
  });
</script>
<jsp:include page="/WEB-INF/includes/footer.jsp" />