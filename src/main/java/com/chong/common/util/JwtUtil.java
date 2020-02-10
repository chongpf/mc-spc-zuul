package com.chong.common.util;

import com.chong.common.entity.JwtResult;
import com.chong.mcspczuul.message.MessageEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    public static String getToken(String uid) {
        return getToken(uid, "", "");
    }

    public static String getToken(String uid, String uname, String urole) {

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "hs256");
        JwtBuilder jwtBuilder = Jwts.builder()
                .setHeader(header)
                .setId(uid)
                .setSubject(uname)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 60 * 1000))
                .claim("role", urole)
                .signWith(SignatureAlgorithm.HS256, "myKeySalt");
        System.out.println("JwtToken：" + jwtBuilder.compact());
        return jwtBuilder.compact();
    }

    public static JwtResult checkToken(String jwttoken) {
        JwtResult jwtResult = new JwtResult();
        jwtResult.setSuccess(false);
        try {
            Claims claims = getClaims(jwttoken);
            jwtResult.setSuccess(true);
            jwtResult.setClaims(claims);
        } catch (ExpiredJwtException e) {
            jwtResult.setErrCode(MessageEnum.ERROR_JWT_EXPIRE.getCode());
        } catch (SignatureException e) {
            jwtResult.setErrCode(MessageEnum.ERROR_JWT_SIGNATURE.getCode());
        } catch (Exception e) {
            jwtResult.setErrCode(MessageEnum.ERROR_JWT_PARSE.getCode());
        }
        return jwtResult;
    }

    private static Claims getClaims(String jwttoken) {
        return Jwts.parser()
                .setSigningKey("myKeySalt")
                .parseClaimsJws(jwttoken)
                .getBody();
    }

    public static void testDecode() {
        String jwttoken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODg4ODgiLCJzdWIiOiLlpKfluIgiLCJpYXQiOjE1ODEwODIzOTMsImV4cCI6MTU4MTA4MjQ1Mywicm9sZSI6ImFkbWluaXN0cmF0b3IifQ.-XKvLmDZ99hLjBNxhgRnrnCGX1UEBSTgeZ7m3yF1vCg";
        Claims claims = Jwts.parser()
                .setSigningKey("myKeySalt")
                .parseClaimsJws(jwttoken)
                .getBody();
        String sign = Jwts.parser()
                .setSigningKey("myKeySalt")
                .parseClaimsJws(jwttoken)
                .getSignature();
        Header header = Jwts.parser()
                .setSigningKey("myKeySalt")
                .parseClaimsJws(jwttoken)
                .getHeader();
        ObjectMapper mapper = new ObjectMapper();
        try {
            //jwttoken header:{"typ":"JWT","alg":"HS256"}
            System.out.println("jwttoken header:" + mapper.writeValueAsString(header));

            //jwttoken body:{"jti":"888888","sub":"大师","iat":1581082393,"exp":1581082453,"role":"administrator"}
            System.out.println("jwttoken body:" + mapper.writeValueAsString(claims));

            //jwttoken signature:-XKvLmDZ99hLjBNxhgRnrnCGX1UEBSTgeZ7m3yF1vCg
            System.out.println("jwttoken signature:" + sign);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
