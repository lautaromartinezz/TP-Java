<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/includes/header.jsp" />
<div class="page-header">
  <h1 class="page-title">Panel de control</h1>
</div>
<div class="card-grid">
  <article class="card card-metric">
    <header class="card-metric-header">
      <h2 class="card-metric-title">Monstruos</h2>
      <i data-lucide="users" class="icon-md" aria-hidden="true"></i>
    </header>
    <div class="card-metric-value">${totalMonstruos != null ? totalMonstruos : 0}</div>
    <footer class="card-metric-footer">
      <a href="<%= request.getContextPath() %>/monster" class="link link-with-icon">
        Ver listado
        <i data-lucide="arrow-right" class="icon-sm" aria-hidden="true"></i>
      </a>
    </footer>
  </article>
  <article class="card card-metric">
    <header class="card-metric-header">
      <h2 class="card-metric-title">Puertas</h2>
      <i data-lucide="door-open" class="icon-md" aria-hidden="true"></i>
    </header>
    <div class="card-metric-value">${totalPuertas != null ? totalPuertas : 0}</div>
    <footer class="card-metric-footer">
      <a href="<%= request.getContextPath() %>/puerta" class="link link-with-icon">
        Ver listado
        <i data-lucide="arrow-right" class="icon-sm" aria-hidden="true"></i>
      </a>
    </footer>
  </article>
  <article class="card card-metric">
    <header class="card-metric-header">
      <h2 class="card-metric-title">Puestos</h2>
      <i data-lucide="briefcase" class="icon-md" aria-hidden="true"></i>
    </header>
    <div class="card-metric-value">${totalPuestos != null ? totalPuestos : 0}</div>
    <footer class="card-metric-footer">
      <a href="<%= request.getContextPath() %>/puesto" class="link link-with-icon">
        Ver listado
        <i data-lucide="arrow-right" class="icon-sm" aria-hidden="true"></i>
      </a>
    </footer>
  </article>
  <article class="card card-metric">
    <header class="card-metric-header">
      <h2 class="card-metric-title">Puestos asignados</h2>
      <i data-lucide="calendar-check" class="icon-md" aria-hidden="true"></i>
    </header>
    <div class="card-metric-value">${totalPuestosAsignados != null ? totalPuestosAsignados : 0}</div>
    <footer class="card-metric-footer">
      <a href="<%= request.getContextPath() %>/puesto-asignado" class="link link-with-icon">
        Ver listado
        <i data-lucide="arrow-right" class="icon-sm" aria-hidden="true"></i>
      </a>
    </footer>
  </article>
  <article class="card card-metric">
    <header class="card-metric-header">
      <h2 class="card-metric-title">Puertas asignadas</h2>
      <i data-lucide="calendar-clock" class="icon-md" aria-hidden="true"></i>
    </header>
    <div class="card-metric-value">${totalPuertasAsignadas != null ? totalPuertasAsignadas : 0}</div>
    <footer class="card-metric-footer">
      <a href="<%= request.getContextPath() %>/puerta-asignada" class="link link-with-icon">
        Ver listado
        <i data-lucide="arrow-right" class="icon-sm" aria-hidden="true"></i>
      </a>
    </footer>
  </article>
  <article class="card card-metric">
    <header class="card-metric-header">
      <h2 class="card-metric-title">Resultados</h2>
      <i data-lucide="file-text" class="icon-md" aria-hidden="true"></i>
    </header>
    <div class="card-metric-value">${totalResultados != null ? totalResultados : 0}</div>
    <footer class="card-metric-footer">
      <a href="<%= request.getContextPath() %>/resultado" class="link link-with-icon">
        Ver listado
        <i data-lucide="arrow-right" class="icon-sm" aria-hidden="true"></i>
      </a>
    </footer>
  </article>
</div>
<jsp:include page="/WEB-INF/includes/footer.jsp" />
