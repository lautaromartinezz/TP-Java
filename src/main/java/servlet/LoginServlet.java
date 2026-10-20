package servlet;

import controller.ControllerMonster;
import entities.Monster;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.Key;
import java.util.Date;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int legajo = 0;
        try {
            legajo = Integer.parseInt(request.getParameter("legajo"));
        } catch (NumberFormatException e) {
            response.sendRedirect("login.jsp?error=formato_invalido");
            return;
        }
        String password = request.getParameter("password");
        ControllerMonster cm = new ControllerMonster();
        Monster monstruo = cm.login(legajo, password);

        if (monstruo != null) {
            String jwt = Jwts.builder()
                    .setSubject(monstruo.getNombre())
                    .claim("idMonstruo", monstruo.getId())
                    .claim("rol", monstruo.getRol())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora de sesión
                    .signWith(SECRET_KEY)
                    .compact();

            Cookie cookie = new Cookie("jwt_token", jwt);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600); // 1 hora en segundos
            response.addCookie(cookie);

            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            response.sendRedirect("login.jsp?error=1");
        }
    }
}
