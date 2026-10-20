<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String ctx = request.getContextPath();
  String path = request.getRequestURI();
  if (path.startsWith(ctx)) { path = path.substring(ctx.length()); }
  
  // Recuperamos los datos inyectados por el AuthFilter
  String nombreUsuario = (String) request.getAttribute("usuarioLogueado");
  String rolUsuario = (String) request.getAttribute("rolUsuario");
  
  if (nombreUsuario == null) nombreUsuario = "Empleado";
  if (rolUsuario == null) rolUsuario = "";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>MONSTER INC. · Panel Administrativo</title>
<link rel="preconnect" href="https://rsms.me/">
<link rel="stylesheet" href="https://rsms.me/inter/inter.css">
<link rel="stylesheet" href="<%= ctx %>/css/app.css">
<script src="https://unpkg.com/lucide@latest/dist/umd/lucide.js" defer></script>
</head>
<body>
<header class="app-header">
  <a href="<%= ctx %>/dashboard" class="app-logo">MONSTER INC. · Panel Administrativo</a>
  
  <!-- Mostramos el nombre real y el rol del usuario logueado -->
  <div>
      <span class="app-user"><%= nombreUsuario %> (<%= rolUsuario.toUpperCase() %>)</span>
      <a href="<%= ctx %>/Logout" style="color: #ff4444; text-decoration: none; margin-left: 15px; font-size: 0.9rem;">
          <i data-lucide="log-out" class="icon-sm" style="vertical-align: middle;"></i> Salir
      </a>
  </div>
</header>
<nav class="app-nav">
  <ul class="app-nav-list">
    <!-- El dashboard (Inicio) lo ven todos -->
    <li>
      <a href="<%= ctx %>/dashboard" class="app-nav-link <%= path.equals("/dashboard") ? "active" : "" %>">
        <i data-lucide="layout-dashboard" class="icon-sm"></i> Inicio
      </a>
    </li>

    <!-- Solo ADMIN -->
    <% if (rolUsuario.equals("admin")) { %>
    <li>
      <a href="<%= ctx %>/monster" class="app-nav-link <%= path.equals("/monster") ? "active" : "" %>">
        <i data-lucide="users" class="icon-sm"></i> Monstruos
      </a>
    </li>
    <li>
      <a href="<%= ctx %>/puesto" class="app-nav-link <%= path.equals("/puesto") ? "active" : "" %>">
        <i data-lucide="briefcase" class="icon-sm"></i> Puestos
      </a>
    </li>
    <li>
      <a href="<%= ctx %>/resultado" class="app-nav-link <%= path.equals("/resultado") ? "active" : "" %>">
        <i data-lucide="file-text" class="icon-sm"></i> Resultados
      </a>
    </li>
    <li>
      <a href="<%= ctx %>/miedo" class="app-nav-link <%= path.equals("/miedo") ? "active" : "" %>">
        <i data-lucide="file-text" class="icon-sm"></i> Miedos
      </a>
    </li>
    <% } %>

    <!-- ADMIN y OPERARIO -->
    <% if (rolUsuario.equals("admin") || rolUsuario.equals("operario")) { %>
    <li>
      <a href="<%= ctx %>/puesto-asignado" class="app-nav-link <%= path.equals("/puesto-asignado") ? "active" : "" %>">
        <i data-lucide="calendar-check" class="icon-sm"></i> Puestos asignados
      </a>
    </li>
    <li>
      <a href="<%= ctx %>/puerta" class="app-nav-link <%= path.equals("/puerta") ? "active" : "" %>">
        <i data-lucide="door-open" class="icon-sm"></i> Puertas
      </a>
    </li>
    <li>
      <a href="<%= ctx %>/puerta-asignada" class="app-nav-link <%= path.equals("/puerta-asignada") ? "active" : "" %>">
        <i data-lucide="calendar-clock" class="icon-sm"></i> Puertas asignadas
      </a>
    </li>
    <% } %>

 
    <% if (rolUsuario.equals("monstruo")) { %>
    <li>
      <a href="<%= ctx %>/resultado" class="app-nav-link <%= path.equals("/resultado") ? "active" : "" %>">
        <i data-lucide="file-text" class="icon-sm"></i> Resultados
      </a>
    </li>
    <% } %>
    
  </ul>
</nav>
<main class="app-main">