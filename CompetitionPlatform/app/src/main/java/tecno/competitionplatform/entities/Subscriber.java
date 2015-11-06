package tecno.competitionplatform.entities;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Andres on 13/10/2015.
 */
public class Subscriber {

    private Integer subscriberId;
    private String email;
    private String firstname;
    private Date dob;
    //private Date tokenExpiresAt;
    private String token;


    public Subscriber(Integer subscriberId, String email, String firstname, String token) {
        this.subscriberId = subscriberId;
        this.email = email;
        this.firstname = firstname;
        this.token = token;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Integer subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


}
