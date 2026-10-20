<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Monsters Inc. - Bienvenidos</title>
    <link href="https://fonts.googleapis.com/css2?family=Creepster&display=swap" rel="stylesheet">
    <style>
        body {
            background-color: #0d1117;
            color: #fff;
            font-family: 'Segoe UI', sans-serif;
            margin: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            text-align: center;
        }
        h1 {
            font-family: 'Creepster', system-ui;
            font-size: 5rem;
            color: #00ff66;
            text-shadow: 0 0 20px rgba(0, 255, 102, 0.4);
            margin-bottom: 10px;
        }
        p { color: #8b949e; font-size: 1.2rem; max-width: 600px; margin-bottom: 40px; }
        .btn-login {
            padding: 15px 30px;
            background-color: transparent;
            color: #00ff66;
            border: 2px solid #00ff66;
            border-radius: 8px;
            font-size: 1.2rem;
            text-decoration: none;
            font-weight: bold;
            transition: all 0.3s;
        }
        .btn-login:hover {
            background-color: #00ff66;
            color: #0d1117;
            box-shadow: 0 0 15px rgba(0, 255, 102, 0.5);
        }
    </style>
</head>
<body>
    <h1>Monsters Inc.</h1>
    <p>Energía limpia, segura y confiable. Liderando la industria de recolección de gritos infantiles para iluminar Monstruópolis.</p>
    <a href="login.jsp" class="btn-login">Acceso a Empleados</a>
</body>
</html>