package org.chat.wschat.Security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private long jwtExpiration;

    private Key getSigninKey(){
        byte []bytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmachShaKeyFor(bytes);
    }
}
