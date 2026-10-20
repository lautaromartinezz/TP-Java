<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Monsters Inc. - Acceso al Nivel de Sustos</title>
    <!-- Importamos la fuente Creepster para el toque Spooky -->
    <link href="https://fonts.googleapis.com/css2?family=Creepster&display=swap" rel="stylesheet">
    
    <style>
        body {
            background-color: #0d1117; /* Fondo oscuro industrial */
            color: #ffffff;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            overflow: hidden;
        }

        .login-container {
            background: #161b22;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 0 20px rgba(0, 255, 102, 0.1);
            border: 1px solid #30363d;
            width: 100%;
            max-width: 380px;
            text-align: center;
        }

        .login-container h1 {
            font-family: 'Creepster', system-ui;
            font-size: 3rem;
            color: #00ff66; /* Verde Mike Wazowski */
            margin-bottom: 5px;
            margin-top: 0;
            text-shadow: 0 0 10px rgba(0, 255, 102, 0.5);
            letter-spacing: 2px;
        }

        .login-container p.subtitle {
            color: #8b949e;
            margin-bottom: 30px;
            font-size: 0.9rem;
        }

        .input-group {
            margin-bottom: 20px;
            text-align: left;
        }

        .input-group label {
            display: block;
            margin-bottom: 8px;
            color: #c9d1d9;
            font-size: 0.9rem;
            font-weight: bold;
        }

        .input-group input {
            width: 100%;
            padding: 12px;
            background-color: #0d1117;
            border: 1px solid #30363d;
            border-radius: 6px;
            color: #ffffff;
            font-size: 1rem;
            box-sizing: border-box;
            transition: border-color 0.3s, box-shadow 0.3s;
        }

        .input-group input:focus {
            outline: none;
            border-color: #00ff66;
            box-shadow: 0 0 8px rgba(0, 255, 102, 0.3);
        }

        .btn-submit {
            width: 100%;
            padding: 12px;
            background-color: #00ff66;
            color: #0d1117;
            font-weight: bold;
            font-size: 1.1rem;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.3s, transform 0.1s;
            margin-top: 10px;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .btn-submit:hover {
            background-color: #00cc52;
        }

        .btn-submit:active {
            transform: scale(0.98);
        }

        .error-msg {
            color: #ff4444;
            background: rgba(255, 68, 68, 0.1);
            border: 1px solid #ff4444;
            padding: 10px;
            border-radius: 6px;
            margin-bottom: 20px;
            font-size: 0.9rem;
        }
    </style>
</head>
<body>

    <div class="login-container">
        <h1>Monsters Inc.</h1>
        <p class="subtitle">Portal del Nivel de Sustos</p>

        <%-- Lógica para mostrar mensajes de error atrapados de la URL --%>
        <%
            String error = request.getParameter("error");
            if (error != null) {
                if (error.equals("1")) {
                    out.print("<div class='error-msg'>Legajo o contraseña incorrectos.</div>");
                } else if (error.equals("formato_invalido")) {
                    out.print("<div class='error-msg'>El legajo debe ser un número válido.</div>");
                } else if (error.equals("token_invalido")) {
                    out.print("<div class='error-msg'>Sesión expirada. Vuelve a identificarte.</div>");
                }
            }
        %>

        <!-- El form apunta a tu Servlet mapeado en /Login -->
        <form action="Login" method="POST">
            <div class="input-group">
                <label for="legajo">N° de Legajo</label>
                <!-- type="number" fuerza a que el usuario solo pueda escribir números -->
                <input type="number" id="legajo" name="legajo" placeholder="Ej: 1000" required>
            </div>
            
            <div class="input-group">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="••••••••" required>
            </div>
            
            <button type="submit" class="btn-submit">Entrar al Sistema</button>
        </form>
    </div>

</body>
</html>