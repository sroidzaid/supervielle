package com.challenge.supervielle.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.challenge.supervielle.exception.JwtException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
public class JwtService {

    public String createToken(String usuario, List<String> roles){

        return JWT.create()
                .withIssuer("supervielle-token")
                .withIssuedAt(new Date())
                .withNotBefore(new Date())
                //.withExpiresAt(new Date(System.currentTimeMillis() + 360000))
                .withClaim("user", usuario)
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .sign(Algorithm.HMAC256("clave-secreta-supervielle"));


    }

    public boolean isBearer(String authorization){
        return authorization != null
                && authorization.startsWith("Bearer ")
                && authorization.split("\\.").length == 3;
    }

    public String user(String authorization) throws JwtException {
        return this.verify(authorization).getClaim("user").asString();
    }

    private DecodedJWT verify(String authorization) throws JwtException{

        if(!this.isBearer(authorization)){
            throw new JwtException("no es bearer");
        }

        try {
            return JWT.require(Algorithm.HMAC256("clave-secreta-supervielle"))
                    .withIssuer("supervielle-token").build()
                    .verify(authorization.substring("Bearer ".length()));
        }catch (Exception e) {
            throw new JwtException("JWT es erroneo " + e.getMessage());
        }
    }

    public List<String> roles(String authorization) throws JwtException{
        return Arrays.asList(this.verify(authorization).getClaim("roles").asArray(String.class));
    }

}
