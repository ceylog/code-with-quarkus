package com.wg.quarkus.entity;


import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.vertx.mutiny.sqlclient.Row;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_user")
public class User extends PanacheEntity {


    private String name;
    @Column(name="create_time")
    private LocalDateTime createTime;
    private String password;
    private String email;
    private String roles;

    public User() {
        super();
    }

    public User(Long id, String name) {
        super.id=id;
        this.name = name;
    }

    public User(Long id, String name, LocalDateTime createTime,String password,String email,String roles) {
        super.id = id;
        this.name = name;
        this.createTime = createTime;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public static User from(Row row) {
        return new User(row.getLong("id"), row.getString("name"), row.getLocalDateTime("create_time"),row.getString("password"),row.getString("email"),row.getString("roles"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        super.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}