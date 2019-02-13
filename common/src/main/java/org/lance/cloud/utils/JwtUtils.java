package org.lance.cloud.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.lance.cloud.api.request.AuthRequest;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.exception.enums.ErrorType;

import java.util.Date;

public class JwtUtils {

    /**
     * 校验token
     * @param token
     * @return
     */
    public static HttpResult<String> check(String token){
        try {
            if (StringUtils.isEmpty(token))
                return HttpResultBuilder.fail(ErrorType.ILLEGAL_REQUEST.getCode(), "缺少认证信息");

            String subject = Jwts.parser().setSigningKey(AuthRequest.DEFAULT_PUBLIC_KEY)
                    .parseClaimsJws(token).getBody().getSubject();
            return HttpResultBuilder.ok(subject);

        } catch (ExpiredJwtException e) {
            return HttpResultBuilder.fail(ErrorType.SYSTEM.getCode(), "Token已过期");
        } catch (Exception e) {
            return HttpResultBuilder.fail(ErrorType.ILLEGAL_REQUEST);
        }
    }

    /**
     * token签名
     * @param auth
     * @return
     */
    public static String sign(AuthRequest auth){
        return sign(auth, DateUtils.addMinutes(new Date(), 1));
    }

    /**
     * token签名
     * @param auth
     * @param expiration
     * @return
     */
    public static String sign(AuthRequest auth, Date expiration) {
        return Jwts.builder().setSubject(auth.getAuthId()).setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, auth.getKey()).compact();
    }
}
