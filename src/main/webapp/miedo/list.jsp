<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<jsp:include page="/WEB-INF/includes/header.jsp" />

<div class="page-header" style="display: flex; justify-content: space-between; align-items: center;">
  <h1 class="page-title">Gestión de Miedos</h1>
  <a href="<%= request.getContextPath() %>/miedo?new" class="btn btn-primary">
    <i data-lucide="plus" class="icon-sm" style="margin-right: 8px;"></i> Nuevo Miedo
  </a>
</div>

<c:if test="${not empty error}">
  <div style="background: rgba(255, 50, 50, 0.1); border: 1px solid red; color: #ff5555; padding: 15px; border-radius: 6px; margin-bottom: 20px;">
    ${error}
  </div>
</c:if>

<div class="card">
  <div style="overflow-x: auto;">
    <table class="table">
      <thead>
        <tr>
          <th style="width: 80px;">ID</th>
          <th>Nombre del Miedo</th>
          <th style="text-align: right;">Acciones</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="miedo" items="${miedos}">
          <tr>
            <td style="color: var(--color-text-2);">${miedo.id}</td>
            <td style="color: var(--color-text); font-weight: 500;">${miedo.nombre}</td>
            <td style="text-align: right; display: flex; justify-content: flex-end; gap: 10px;">
              
              <!-- Botón Editar -->
              <a href="<%= request.getContextPath() %>/miedo?id=${miedo.id}" class="btn btn-ghost" style="padding: 4px 8px;">
                <i data-lucide="edit" class="icon-sm"></i>
              </a>
              
              <!-- Formulario Eliminar -->
              <form action="<%= request.getContextPath() %>/miedo" method="POST" style="display: inline;" onsubmit="return confirm('¿Estás seguro de eliminar el miedo: ${miedo.nombre}?');">
                <input type="hidden" name="_method" value="DELETE">
                <input type="hidden" name="id" value="${miedo.id}">
                <button type="submit" class="btn btn-ghost" style="padding: 4px 8px; color: #ff5555;">
                  <i data-lucide="trash" class="icon-sm"></i>
                </button>
              </form>
              
            </td>
          </tr>
        </c:forEach>
        
        <c:if test="${empty miedos}">
          <tr><td colspan="3" class="empty-state">No hay miedos registrados en el sistema.</td></tr>
        </c:if>
      </tbody>
    </table>
  </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
