package com.wg.quarkus.resource;

import com.wg.quarkus.entity.User;
import com.wg.quarkus.model.AuthRequest;
import com.wg.quarkus.model.AuthResponse;
import com.wg.quarkus.utils.TokenUtils;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.mindrot.jbcrypt.BCrypt;

import javax.annotation.security.PermitAll;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/user")
public class AuthenticationResource {

    @ConfigProperty(name = "com.wg.quarkusjwt.jwt.duration")
    public Long duration;
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    public String issuer;

    @PermitAll
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> login(AuthRequest authRequest) {

        Uni<User> user = User.find("name", authRequest.username).singleResult();
        return user.map(u -> {
            if (u != null && BCrypt.checkpw(authRequest.password, u.getPassword())) {
                try {
                    Set<String> roles = u.getRoles() == null ? null : Set.of(u.getRoles().split(","));
                    return Response.ok(new AuthResponse(TokenUtils.generateJWTToken(u.getName(), roles, duration, issuer))).build();
                } catch (Exception e) {
                    e.printStackTrace();
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
            }
            return Response.status(Response.Status.UNAUTHORIZED).build();
        });
    }

}