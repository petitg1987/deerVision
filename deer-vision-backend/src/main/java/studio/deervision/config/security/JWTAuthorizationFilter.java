package studio.deervision.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import studio.deervision.config.properties.AdminProperties;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter implements Filter {

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    private final AdminProperties adminProperties;

    public JWTAuthorizationFilter(AdminProperties adminProperties) {
        this.adminProperties = adminProperties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            try {
                if (checkJWTToken(request)) {
                    Claims claims = validateToken(request);
                    if (claims.get("authorities") != null) {
                        setUpSpringAuthentication(claims);
                    } else {
                        SecurityContextHolder.clearContext();
                    }
                } else {
                    SecurityContextHolder.clearContext();
                }
                chain.doFilter(request, response);
            } catch (JwtException e) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(401);
                httpResponse.getWriter().write("Invalid JWT: " + e.getMessage());
            }
        }
    }

    private Claims validateToken(ServletRequest request) {
        String jwtToken = ((HttpServletRequest) request).getHeader(HEADER).replace(PREFIX, "");
        return Jwts.parser().setSigningKey(adminProperties.getJwtSecret().getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    @SuppressWarnings({"unchecked"})
    private void setUpSpringAuthentication(Claims claims) {
        List<String> authorities = (List<String>) claims.get("authorities");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private boolean checkJWTToken(ServletRequest request) {
        String authenticationHeader = ((HttpServletRequest) request).getHeader(HEADER);
        return authenticationHeader != null && authenticationHeader.startsWith(PREFIX);
    }

}
