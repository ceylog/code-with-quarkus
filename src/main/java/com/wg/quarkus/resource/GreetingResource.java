package com.wg.quarkus.resource;

import com.wg.quarkus.PBKDF2Encoder;
import com.wg.quarkus.entity.User;
import io.quarkus.redis.client.RedisClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("/hello-resteasy")
@RequestScoped
public class GreetingResource {

    @Inject
    PgPool pgclient;

    @Inject
    RedisClient redisClient;

    @Inject
    PBKDF2Encoder passwordEncoder;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }


    @GET
    @Path("/querydb")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<User>> queryDb() {
        System.out.println("-----  hello ---");
        return User.listAll();
     /*   return pgclient.query("select id,name,email,create_time,password from t_user").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(User::from);*/
    }

    @GET
    @Path("/user/{name}")
    public Uni<User> getUser(@PathParam("name") String name){
        return User.find("name",name).singleResult();
     /*   return pgclient.preparedQuery("select id,name,email,create_time,password,roles from t_user where name = $1 ").execute(Tuple.of(name))
                .onItem().transform(rs-> User.from(rs.iterator().next()));*/
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    public  Uni<Void> addUser(User user){
        return User.persist(user);
   /*     return pgclient.preparedQuery("insert into t_user(name,email,password) values($1,$2,$3)")
                .execute(Tuple.of(user.getName(), user.getEmail(), passwordEncoder.encode(user.getPassword())))
                .onItem().transform(pgRowSet -> User.from(pgRowSet.iterator().next()));*/
    }




    @GET
    @Path("/helloredis")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRedis() {
        String hello = redisClient.get("hello").toString();
        if(null == hello){
            System.out.println("cache is null -- ");
            redisClient.set(Arrays.asList("hello","hello"));
        }
        return hello;
    }



}
