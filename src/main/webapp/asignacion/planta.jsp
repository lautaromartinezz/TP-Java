<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<% String ctx = request.getContextPath(); %>

<jsp:include page="/WEB-INF/includes/header.jsp" />

<style>
	/* Diseño base de la Planta */
	.planta-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 40px; padding: 20px 0; }
	.puesto-card { background: var(--color-bg, #1e1e2f); border: 1px solid var(--color-border, #3a3f58); border-radius: 12px; padding: 20px; position: relative; box-shadow: 0 4px 6px rgba(0,0,0,0.3); }
	
	/* Línea conectora entre tarjetas */
	.puesto-card:not(:nth-child(3n))::after { content: ''; position: absolute; top: 50%; right: -40px; width: 40px; height: 4px; background: var(--color-border, #3a3f58); z-index: 1; }
	
	/* Elementos visuales de las puertas */
	.lista-puertas { margin-top: 10px; padding: 15px; background: rgba(0,0,0,0.2); border: 1px solid var(--color-border, #3a3f58); border-radius: 6px; min-height: 70px; }
	.puerta-badge { display: inline-block; background: var(--color-accent, #6366f1); color: #fff; padding: 6px 10px; border-radius: 6px; margin: 3px; font-size: 0.85rem; font-weight: bold; }
	
	/* Modal Estilos */
	.modal-overlay { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.8); z-index: 1000; justify-content: center; align-items: center; }
	.modal-content { background: var(--color-bg, #1e1e2f); padding: 30px; border-radius: 12px; width: 400px; max-width: 90%; border: 1px solid var(--color-border, #3a3f58); box-shadow: 0 10px 25px rgba(0,0,0,0.5); }
	.modal-header { margin-top: 0; color: var(--color-accent, #6366f1); }
</style>

<!-- NUEVO ENCABEZADO CON SELECTOR DE FECHA -->
<div class="page-header" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 15px; border-bottom: 1px solid var(--color-border, #3a3f58); padding-bottom: 20px; margin-bottom: 20px;">
	<div style="display: flex; align-items: center; gap: 20px;">
		<h1 class="page-title" style="margin: 0;">Planta de Sustos</h1>
		
		<!-- Selector de Fecha interactivo -->
		<div style="display: flex; align-items: center; gap: 10px; background: rgba(255,255,255,0.05); padding: 8px 15px; border-radius: 8px; border: 1px solid var(--color-border, #3a3f58);">
			<i data-lucide="calendar" style="width: 18px; height: 18px; color: var(--color-accent, #6366f1);"></i>
			<input type="date" id="buscador-fecha" value="${fechaMostrada}" 
				   onchange="window.location.href='<%= ctx %>/asignacion?fecha=' + this.value"
				   style="background: transparent; border: none; color: #fff; font-family: inherit; font-size: 1rem; outline: none; cursor: pointer;">
		</div>
	</div>
	
	<!-- Badges de Estado Visuales -->
	<div>
		<c:choose>
			<c:when test="${esEditable}">
				<span style="background: rgba(16, 185, 129, 0.2); color: #10b981; border: 1px solid #10b981; padding: 6px 12px; border-radius: 6px; font-weight: bold; font-size: 0.9rem;">
					<i data-lucide="edit-3" style="width: 14px; height: 14px; margin-right: 4px;"></i> MODO EDICIÓN
				</span>
			</c:when>
			<c:otherwise>
				<span style="background: rgba(239, 68, 68, 0.2); color: #ef4444; border: 1px solid #ef4444; padding: 6px 12px; border-radius: 6px; font-weight: bold; font-size: 0.9rem;">
					<i data-lucide="lock" style="width: 14px; height: 14px; margin-right: 4px;"></i> HISTORIAL DE LECTURA
				</span>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<c:if test="${param.success == 'true'}">
	<div class="alert alert-success" style="margin-bottom: 20px; color: #4ade80;">Asignaciones registradas correctamente en la base de datos.</div>
</c:if>

<!-- BIFURCACIÓN DE LA PANTALLA SEGÚN LA FECHA -->
<c:choose>
	<c:when test="${esEditable}">
		<!-- === MODO HOY (EDITABLE) === -->
		<form id="form-planta" method="post" action="<%= ctx %>/asignacion">
			<div class="planta-grid">
				<c:forEach var="puesto" items="${puestos}">
					<div class="puesto-card">
						<input type="hidden" name="puestosActivos" value="${puesto.id}">
						<h3 style="margin-top: 0; color: var(--color-accent, #6366f1);">Estación #${puesto.id} - ${puesto.nombre}</h3>
						
						<div class="form-group" style="margin-top: 15px;">
							<label class="form-label">Asignar Monstruo:</label>
							<select id="select-monster-${puesto.id}" class="form-input select-monster" name="idMonster_${puesto.id}" data-puesto="${puesto.id}" onchange="actualizarMonstruos(this)" required>
								<option value="">-- Seleccionar --</option>
								<c:forEach var="monstruo" items="${monstruos}">
									<!-- MARCA EL MONSTRUO PRE-SELECCIONADO SI EXISTE EN BD -->
									<option value="${monstruo.id}" ${monstruosDelDia[puesto.id] == monstruo.id ? 'selected' : ''}>${monstruo.nombre}</option>
								</c:forEach>
							</select>
						</div>
						
						<div class="form-group" style="margin-top: 15px;">
							<button type="button" class="btn btn-ghost" onclick="abrirModal(${puesto.id})" style="width: 100%; margin-bottom: 10px; border: 1px dashed var(--color-border, #3a3f58);">
								<i data-lucide="plus"></i> Agregar Puertas
							</button>
							<div class="lista-puertas" id="lista-puertas-${puesto.id}">
								<c:choose>
									<c:when test="${not empty puertasDelDia[puesto.id]}">
										<c:forEach var="p" items="${puertasDelDia[puesto.id]}">
											<!-- Contenedor individual que agrupa la vista y el input -->
											<div class="puerta-wrapper" style="display: inline-block; margin: 3px;">
												<span class="puerta-badge" style="display: flex; align-items: center; gap: 5px;">
													<i data-lucide="door-closed" style="width: 12px; height: 12px;"></i> P#${p.numero}
													<!-- Botón de eliminación directa -->
													<span style="cursor: pointer; color: #ffaaaa; margin-left: 2px; font-weight: bold; padding: 0 3px;" onclick="removerPuerta(this, '${puesto.id}')" title="Quitar puerta">&times;</span>
												</span>
												<!-- Input que viaja al Servlet -->
												<input type="hidden" name="puertas_${puesto.id}" value="${p.id}">
											</div>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<span style="color: #888; font-size: 0.85rem;">Sin puertas asignadas...</span>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>

			<div style="text-align: right; margin-top: 30px; padding-top: 20px; border-top: 1px solid var(--color-border, #3a3f58);">
				<button type="submit" class="btn btn-primary" style="font-size: 1.1rem; padding: 12px 30px;">
					<i data-lucide="save"></i> Confirmar Asignaciones de Planta
				</button>
			</div>
		</form>

		<!-- El Modal se renderiza solo si es editable -->
		<div id="puertasModal" style="display: none; position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background: rgba(0,0,0,0.8); z-index: 999999; justify-content: center; align-items: center;">
			<div style="background: #1e1e2f; padding: 30px; border-radius: 12px; width: 400px; max-width: 90%; border: 1px solid #3a3f58; box-shadow: 0 10px 40px rgba(0,0,0,0.9);">
				<h3 style="margin-top: 0; color: #6366f1;">Seleccionar Puertas</h3>
				<p style="color: #888; font-size: 0.9rem; margin-bottom: 20px;">Solo se muestran puertas compatibles y disponibles para esta estación.</p>
				<div id="modal-puertas-container" style="max-height: 250px; overflow-y: auto; margin-bottom: 20px; padding-right: 10px;"></div>
				<div style="display: flex; gap: 10px; justify-content: flex-end;">
					<button type="button" class="btn btn-ghost" onclick="cerrarModal()">Cancelar</button>
					<button type="button" class="btn btn-primary" onclick="guardarPuertas()">
						<i data-lucide="check"></i> Confirmar Puertas
					</button>
				</div>
			</div>
		</div>
	</c:when>

	<c:otherwise>
		<!-- === MODO PASADO (SOLO LECTURA) === -->
		<div class="planta-grid">
			<c:forEach var="puesto" items="${puestos}">
				<div class="puesto-card" style="opacity: 0.7; border-color: #555;">
					<h3 style="margin-top: 0; color: #aaa;">Estación #${puesto.id} - ${puesto.nombre}</h3>
					
					<div class="form-group" style="margin-top: 15px;">
						<label class="form-label">Monstruo Asignado:</label>
						<!-- MUESTRA EL MONSTRUO ASIGNADO ESE DÍA -->
						<div style="padding: 10px; background: rgba(255,255,255,0.05); border-radius: 6px; color: #fff;">
							<c:choose>
								<c:when test="${not empty monstruosDelDia[puesto.id]}">
									<c:forEach var="m" items="${monstruos}">
										<c:if test="${m.id == monstruosDelDia[puesto.id]}"><i data-lucide="user" style="width: 16px; height: 16px; margin-right: 5px;"></i> ${m.nombre}</c:if>
									</c:forEach>
								</c:when>
								<c:otherwise><i data-lucide="user-x" style="width: 16px; height: 16px; margin-right: 5px;"></i> Sin Asignar</c:otherwise>
							</c:choose>
						</div>
					</div>
					
					<div class="form-group" style="margin-top: 15px;">
						<label class="form-label">Puertas Trabajadas:</label>
						<div class="lista-puertas">
							<!-- MUESTRA LAS PUERTAS DE ESE DÍA -->
							<c:choose>
								<c:when test="${not empty puertasDelDia[puesto.id]}">
									<c:forEach var="p" items="${puertasDelDia[puesto.id]}">
										<span class="puerta-badge"><i data-lucide="door-closed" style="width: 12px; height: 12px; margin-right: 4px;"></i> P#${p.numero}</span>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<span style="color: #888; font-size: 0.85rem;">Ninguna puerta asignada</span>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:otherwise>
</c:choose>

<script>
	const mapaCompatibles = {
		<c:forEach var="entry" items="${mapaCompatibles}">
			"${entry.key}": [
				<c:forEach var="puerta" items="${entry.value}">
					{ id: "${puerta.id}", numero: "${puerta.numero}" },
				</c:forEach>
			],
		</c:forEach>
	};

	let puestoActualModal = null;

	function actualizarMonstruos(selectCambiado) {
		const todosLosSelects = document.querySelectorAll('.select-monster');
		if(todosLosSelects.length === 0) return;

		const seleccionados = Array.from(todosLosSelects).map(s => s.value).filter(v => v !== "");

		todosLosSelects.forEach(select => {
			Array.from(select.options).forEach(option => {
				if (option.value === "") return;
				option.disabled = seleccionados.includes(option.value) && select.value !== option.value;
			});
		});

		if (selectCambiado) {
			const idPuesto = selectCambiado.getAttribute('data-puesto');
			document.getElementById('lista-puertas-' + idPuesto).innerHTML = '<span style="color: #888; font-size: 0.85rem;">Sin puertas asignadas...</span>';
		}
	}

	function abrirModal(idPuesto) {
		const selectMonster = document.getElementById('select-monster-' + idPuesto);
		if (!selectMonster) return;

		const idMonster = selectMonster.value;
		if (!idMonster) {
			alert("Primero debes seleccionar un monstruo para habilitar la elección de puertas.");
			return;
		}

		puestoActualModal = idPuesto;
		const contenedor = document.getElementById('modal-puertas-container');
		contenedor.innerHTML = "";

		const puertasDelMonstruo = mapaCompatibles[idMonster] || [];
		
		const inputsPuertas = document.querySelectorAll('input[name^="puertas_"]');
		const puertasEnUso = Array.from(inputsPuertas)
								.filter(input => !input.name.includes('puertas_' + idPuesto))
								.map(input => String(input.value)); // Forzamos a String

		const misPuertasInputs = document.querySelectorAll('input[name="puertas_' + idPuesto + '"]');
		const misPuertasActuales = Array.from(misPuertasInputs).map(i => String(i.value)); // Forzamos a String

		let mostradas = 0;
		puertasDelMonstruo.forEach(p => {
			const idPuertaStr = String(p.id); // Forzamos a String
			if (!puertasEnUso.includes(idPuertaStr)) {
				const isChecked = misPuertasActuales.includes(idPuertaStr) ? "checked" : "";
				contenedor.innerHTML += 
					'<label style="display: flex; align-items: center; gap: 10px; margin-bottom: 12px; cursor: pointer; color: var(--color-text, #e0e0e0);">' +
						'<input type="checkbox" class="chk-modal-puerta" value="' + idPuertaStr + '" data-numero="' + p.numero + '" ' + isChecked + ' style="width: 18px; height: 18px; accent-color: var(--color-accent, #6366f1);">' +
						'Puerta #' + p.numero +
					'</label>';
				mostradas++;
			}
		});

		if (mostradas === 0) {
			contenedor.innerHTML = "<p style='color:#ff5555; padding: 10px; background: rgba(255,85,85,0.1); border-radius: 6px;'>No hay puertas compatibles disponibles.</p>";
		}
		
		document.getElementById('puertasModal').style.display = 'flex';
	}

	function cerrarModal() {
		document.getElementById('puertasModal').style.display = 'none';
		puestoActualModal = null;
	}
	function guardarPuertas() {
		const checkboxes = document.querySelectorAll('.chk-modal-puerta:checked');
		const listaUI = document.getElementById('lista-puertas-' + puestoActualModal);

		listaUI.innerHTML = ""; 

		if (checkboxes.length === 0) {
			listaUI.innerHTML = '<span style="color: #888; font-size: 0.85rem;">Sin puertas asignadas...</span>';
		} else {
			let htmlTemporal = "";
			checkboxes.forEach(chk => {
				htmlTemporal += '<div class="puerta-wrapper" style="display: inline-block; margin: 3px;">';
				htmlTemporal += '<span class="puerta-badge" style="display: flex; align-items: center; gap: 5px;">';
				htmlTemporal += '<i data-lucide="door-closed" style="width: 12px; height: 12px;"></i> P#' + chk.getAttribute('data-numero');
				htmlTemporal += '<span style="cursor: pointer; color: #ffaaaa; margin-left: 2px; font-weight: bold; padding: 0 3px;" onclick="removerPuerta(this, \'' + puestoActualModal + '\')" title="Quitar puerta">&times;</span>';
				htmlTemporal += '</span>';
				htmlTemporal += '<input type="hidden" name="puertas_' + puestoActualModal + '" value="' + chk.value + '">';
				htmlTemporal += '</div>';
			});
			listaUI.innerHTML = htmlTemporal;
			if(typeof lucide !== 'undefined') lucide.createIcons();
		}
		cerrarModal();
	}

	// NUEVA FUNCIÓN: Aniquila la puerta del HTML al instante
	function removerPuerta(botonCruz, idPuesto) {
		const wrapper = botonCruz.closest('.puerta-wrapper');
		if (wrapper) {
			wrapper.remove(); // Borra el dibujito y el input oculto de un solo golpe
		}
		
		const listaUI = document.getElementById('lista-puertas-' + idPuesto);
		if(listaUI.querySelectorAll('.puerta-wrapper').length === 0) {
			listaUI.innerHTML = '<span style="color: #888; font-size: 0.85rem;">Sin puertas asignadas...</span>';
		}
	}
	document.addEventListener('DOMContentLoaded', function() {
		actualizarMonstruos();
		});
</script>
<jsp:include page="/WEB-INF/includes/footer.jsp" />