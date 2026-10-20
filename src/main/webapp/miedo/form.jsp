<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header">
  <h1 class="page-title">
    <c:choose>
      <c:when test="${not empty miedo}">Editar Miedo #${miedo.id}</c:when>
      <c:otherwise>Registrar Nuevo Miedo</c:otherwise>
    </c:choose>
  </h1>
</div>

<div class="card" style="max-width: 600px; margin: 0 auto; padding: 30px;">
  <form action="<%= request.getContextPath() %>/miedo" method="POST" class="form">
    
    <c:if test="${not empty miedo}">
      <!-- Si estamos editando, inyectamos el método PUT y el ID -->
      <input type="hidden" name="_method" value="PUT">
      <input type="hidden" name="id" value="${miedo.id}">
    </c:if>

    <div class="form-group">
      <label class="form-label" for="nombre">Nombre del Miedo</label>
      <input type="text" 
             id="nombre" 
             name="nombre" 
             class="form-input" 
             required 
             placeholder="Ej: Oscuridad, Payasos, Alturas..." 
             value="${not empty miedo ? miedo.nombre : ''}">
      <p style="color: var(--color-text-2); font-size: 0.8rem; margin-top: 5px;">
        Define el nombre de la fobia o temor para luego cruzarlo con las puertas y monstruos.
      </p>
    </div>

    <div class="form-actions mt-4" style="justify-content: flex-end; gap: 15px; display: flex;">
      <a href="<%= request.getContextPath() %>/miedo" class="btn btn-ghost">Cancelar</a>
      <button type="submit" class="btn btn-primary">
        <c:choose>
          <c:when test="${not empty miedo}">Actualizar Miedo</c:when>
          <c:otherwise>Guardar Miedo</c:otherwise>
        </c:choose>
      </button>
    </div>
    
  </form>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />