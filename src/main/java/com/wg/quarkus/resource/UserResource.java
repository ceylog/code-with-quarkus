package com.wg.quarkus.resource;

import com.wg.quarkus.model.Message;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/resource")
public class UserResource {

    @Inject
    JsonWebToken jwt;
    @Inject
    @Claim(standard = Claims.exp)
    long exp;


    @RolesAllowed("USER")
    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response user() {
        return Response.ok(new Message("Content for user")).build();
    }

    @RolesAllowed("ADMIN")
    @GET
    @Path("/admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response admin() {
        System.out.println(exp);
        Set<String> claimNames = jwt.getClaimNames();
        System.out.println("claimNames = " + claimNames);
        claimNames.forEach(x-> System.out.println(x+" = " +jwt.getClaim(x)));
        return Response.ok(new Message("Content for admin")).build();
    }

    @RolesAllowed({"USER", "ADMIN"})
    @GET
    @Path("/user-or-admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userOrAdmin() {
        return Response.ok(new Message("Content for user or admin")).build();
    }
}