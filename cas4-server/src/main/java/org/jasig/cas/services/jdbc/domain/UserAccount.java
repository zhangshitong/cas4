package org.jasig.cas.services.jdbc.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by stzhang on 2016/12/13.
 */
@Entity(name = "T_U_ACCOUNT")
public class UserAccount {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid.hex")
    @Column(name="id")
    private String userId;

    @Column(nullable = false)
    private String user_account;

    @Column(nullable = true)
    private String user_name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String salt = "salt";


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
