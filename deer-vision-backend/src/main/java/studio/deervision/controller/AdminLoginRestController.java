package studio.deervision.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import studio.deervision.config.properties.AdminProperties;
import studio.deervision.dto.TokenDto;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AdminLoginRestController {

    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    @Autowired
    public AdminLoginRestController(AdminProperties adminProperties, PasswordEncoder passwordEncoder) {
        this.adminProperties = adminProperties;
        this.passwordEncoder = passwordEncoder;
    }

    //curl -X POST http://localhost:5000/api/admin/login?password=dev
    @PostMapping("/admin/login")
    public TokenDto login(@RequestParam("password") String password) {
        TokenDto tokenDto = new TokenDto();
        if (!passwordEncoder.matches(password, adminProperties.getPassword())) {
            tokenDto.setValue("");
        } else {
            tokenDto.setValue(getJWTToken());
        }
        return tokenDto;
    }

    private String getJWTToken() {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
        SecretKey signingKey = Keys.hmacShaKeyFor(adminProperties.getJwtSecret().getBytes());
        return Jwts
                .builder()
                .id("dvsJWT")
                .subject("admin")
                .claim("authorities", grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60L * 60L * 24L * 365L * 10L))
                .signWith(signingKey, Jwts.SIG.HS512).compact();
    }
}