package filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlet.LoginServlet;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI();
        if (path.endsWith("login.jsp") || path.endsWith("/Login") ||path.endsWith("/Logout")|| path.contains("/css/") || 
        	    path.endsWith("index.jsp") || path.equals(request.getContextPath() + "/")){
            chain.doFilter(request, response);
            return;
        }

        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(LoginServlet.SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String rol = claims.get("rol", String.class);
            request.setAttribute("usuarioLogueado", claims.getSubject());
            request.setAttribute("rolUsuario", rol);
            request.setAttribute("idMonstruo", claims.get("idMonstruo", Integer.class));
            

            if ("admin".equalsIgnoreCase(rol)) {
                chain.doFilter(request, response);
                return;
            }

            if ("operario".equalsIgnoreCase(rol)) {
                if (path.contains("Puerta") || path.contains("Puesto") || path.endsWith("/dashboard") || path.endsWith("index.jsp")) {
                    chain.doFilter(request, response);
                    return;
                }
            }

            if ("monstruo".equalsIgnoreCase(rol)) {
                if (path.contains("Resultado") ||path.contains("/resultado") || path.endsWith("/dashboard") || path.endsWith("index.jsp"))  {
                    chain.doFilter(request, response);
                    return;
                }
            }

            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tu rol de " + rol + " no tiene permisos para esta zona.");

        } catch (Exception e) {
            Cookie deleteCookie = new Cookie("jwt_token", null);
            deleteCookie.setMaxAge(0);
            deleteCookie.setPath("/");
            response.addCookie(deleteCookie);
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=token_invalido");
        }
    }
}