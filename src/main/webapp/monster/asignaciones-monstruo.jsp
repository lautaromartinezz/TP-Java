<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
  <h1 class="page-title">Mis Asignaciones</h1>
</div>

<div class="card">
  <header style="border-bottom: 1px solid var(--color-border); padding-bottom: 15px; margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center;">

    <div style="display: flex; gap: 10px; align-items: center;">
      <label style="color: var(--color-text-2); font-weight: 500;">Filtrar por estado:</label>
      <select id="filtroEstado" class="form-select" style="width: 200px;" onchange="filtrarAsignaciones()">
        <option value="Todas">Mostrar Todas</option>
        <option value="Pendiente">Solo Pendientes (No hechas)</option>
        <option value="Completada">Solo Completadas (Hechas)</option>
      </select>
    </div>
    <div style="display: flex; gap: 10px; align-items: center;">
      <label style="color: var(--color-text-2); font-weight: 500;">Ordenar por:</label>
      <select id="ordenTabla" class="form-select" style="width: 200px;" onchange="ordenarAsignaciones()">
        <option value="fechaDesc">Más recientes primero</option>
        <option value="fechaAsc">Más antiguas primero</option>
        <option value="energiaDesc">Mayor energía generada</option>
        <option value="energiaAsc">Menor energía generada</option>
      </select>
    </div>

  </header>
  
  <div style="overflow-x: auto;">
    <table class="table" id="tablaAsignaciones">
      <thead>
        <tr>
          <th>Estado</th>
          <th>Nº Puerta</th>
          <th>Puesto</th>
          <th>Fecha (Susto)</th>
          <th style="text-align: right;">Energía / Acción</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="asig" items="${listaAsignaciones}">
          <tr class="fila-asignacion" 
              data-estado="${asig.estado}" 
              data-fecha="${not empty asig.fecha ? asig.fecha.time : 0}" 
              data-energia="${not empty asig.energiaGenerada ? asig.energiaGenerada : 0}">
            
            <!-- Columna Estado (Píldora visual) -->
            <td>
              <c:choose>
                <c:when test="${asig.estado == 'Completada'}">
                  <span style="background: rgba(0, 255, 102, 0.1); color: var(--color-accent); padding: 4px 8px; border-radius: 4px; font-size: 0.75rem; font-weight: 600;">COMPLETADA</span>
                </c:when>
                <c:otherwise>
                  <span style="background: rgba(255, 170, 0, 0.1); color: #ffaa00; padding: 4px 8px; border-radius: 4px; font-size: 0.75rem; font-weight: 600;">PENDIENTE</span>
                </c:otherwise>
              </c:choose>
            </td>

            <td style="color: var(--color-text); font-weight: 500;">${asig.numeroPuerta}</td>
            <td style="color: var(--color-text-2);">${asig.nombrePuesto}</td>
            <td style="color: var(--color-text-2);">${not empty asig.fecha ? asig.fecha : '-'}</td>
            
            <!-- Columna Energía / Botón Modal -->
            <td style="text-align: right;">
              <c:choose>
                <c:when test="${asig.estado == 'Completada'}">
                  <span style="color: var(--color-accent); font-weight: bold; font-family: monospace; font-size: 1.1rem;">
                    ${asig.energiaGenerada} kW
                  </span>
                </c:when>
                <c:otherwise>
                  <button type="button" onclick="abrirModalSusto('${asig.idAsignacion}', '${asig.numeroPuerta}')" class="btn btn-primary" style="padding: 4px 10px; font-size: 0.75rem;">
                    <i data-lucide="zap" class="icon-sm" style="margin-right: 4px;"></i> Cargar Susto
                  </button>
                </c:otherwise>
              </c:choose>
            </td>

          </tr>
        </c:forEach>
        
        <c:if test="${empty listaAsignaciones}">
          <tr><td colspan="5" class="empty-state">No tienes puertas asignadas actualmente.</td></tr>
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
      <form action="<%= request.getContextPath() %>/resultado" method="POST" class="form">
        <input type="hidden" name="action" value="insert">
        <input type="hidden" name="id_puerta_asignada" id="modal_id_asignacion">
        <p style="margin-bottom: 20px; color: var(--color-text-2); font-size: 0.9rem;">
          Ingresa los kilovatios extraídos en la <strong id="modal_txt_puerta" style="color: var(--color-accent);"></strong>.
        </p>
        <div class="form-group">
          <label class="form-label">Energía Recolectada (kW)</label>
          <input type="number" name="energia_generada" class="form-input" required min="1">
        </div>
        <div class="form-actions mt-4" style="justify-content: flex-end;">
          <button type="button" class="btn btn-ghost" onclick="cerrarModalSusto()">Cancelar</button>
          <button type="submit" class="btn btn-primary">Guardar Susto</button>
        </div>
      </form>
    </div>
  </div>
</div>


<script>
  // Lógica del Filtro
  function filtrarAsignaciones() {
    const filtro = document.getElementById('filtroEstado').value;
    const filas = document.querySelectorAll('.fila-asignacion');

    filas.forEach(fila => {
      const estadoFila = fila.getAttribute('data-estado');
      if (filtro === 'Todas' || filtro === estadoFila) {
        fila.style.display = '';
      } else {
        fila.style.display = 'none';
      }
    });
  }

  // Lógica del Modal
  function abrirModalSusto(idAsignacion, numeroPuerta) {
    document.getElementById('modal_id_asignacion').value = idAsignacion;
    document.getElementById('modal_txt_puerta').textContent = 'Puerta Nº ' + numeroPuerta;
    document.getElementById('modalSusto').classList.add('active');
  }

  function cerrarModalSusto() {
    document.getElementById('modalSusto').classList.remove('active');
  }
  
  function ordenarAsignaciones() {
	    const criterio = document.getElementById('ordenTabla').value;
	    const tbody = document.querySelector('#tablaAsignaciones tbody');
	    const filas = Array.from(tbody.querySelectorAll('.fila-asignacion'));

	    filas.sort((a, b) => {
	      const energiaA = parseInt(a.getAttribute('data-energia')) || 0;
	      const energiaB = parseInt(b.getAttribute('data-energia')) || 0;
	      
	      const fechaA = parseInt(a.getAttribute('data-fecha')) || 0;
	      const fechaB = parseInt(b.getAttribute('data-fecha')) || 0;

	      switch(criterio) {
	        case 'energiaDesc': return energiaB - energiaA; 
	        case 'energiaAsc':  return energiaA - energiaB; 
	        case 'fechaAsc':    return fechaA - fechaB;   
	        case 'fechaDesc':   return fechaB - fechaA;    
	        default:            return fechaB - fechaA;
	      }
	    });
	    filas.forEach(fila => tbody.appendChild(fila));
	  }
	  document.addEventListener("DOMContentLoaded", function() {
	    ordenarAsignaciones();
	  });
</script>


<jsp:include page="/WEB-INF/includes/footer.jsp" />