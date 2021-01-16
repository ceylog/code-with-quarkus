package com.wg.quarkus.resource;

import com.wg.quarkus.PBKDF2Encoder;
import com.wg.quarkus.model.AuthRequest;
import com.wg.quarkus.model.AuthResponse;
import com.wg.quarkus.utils.TokenUtils;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/user")
public class AuthenticationResource {

    @Inject
    PBKDF2Encoder passwordEncoder;

    @ConfigProperty(name = "com.wg.quarkusjwt.jwt.duration")
    public Long duration;
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    public String issuer;

    @PermitAll
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> login(AuthRequest authRequest) {

        Uni<com.wg.quarkus.entity.User> name = com.wg.quarkus.entity.User.find("name", authRequest.username).singleResult();
        return name.map(u -> {
            if (u != null && u.getPassword().equals(passwordEncoder.encode(authRequest.password))) {
                try {
                    Set<String> roles = u.getRoles() == null ? null : Set.of(u.getRoles().split(","));
                    return Response.ok(new AuthResponse(TokenUtils.generateJWTToken(u.getName(), roles, duration, issuer))).build();
                } catch (Exception e) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        });

        /*User u = User.findByUsername(authRequest.username);
        if (u != null && u.password.equals(passwordEncoder.encode(authRequest.password))) {
            try {
                return Response.ok(new AuthResponse(TokenUtils.generateJWTToken(u.username, u.roles, duration, issuer))).build();
            } catch (Exception e) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }*/
    }

}