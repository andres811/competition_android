package tecno.competitionplatform.entities;

import java.util.Date;

/**
 * Created by Andres on 13/10/2015.
 */
public class Subscriber {

    private Integer subscriberId;
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private Date dob;
    private String token;
    private Country countryId;

    //Constructors
    public Subscriber(Integer subscriberId, String email, String firstname, String token) {
        this.subscriberId = subscriberId;
        this.email = email;
        this.firstname = firstname;
        this.token = token;
    }

    public Subscriber(String email, String firstname, String lastname, String password, Date dob, Country countryId) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.dob = dob;
        this.countryId = countryId;
    }

    //getters and setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Country getCountry() {
        return countryId;
    }

    public void setCountry(Country country) {
        this.countryId = country;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
