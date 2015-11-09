package tecno.competitionplatform.entities;

/**
 * Created by Andres on 08/11/2015.
 */
public class SessionUser {

    private Integer userId;
    private String email;
    private String firstname;
    private String token;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //Constructor
    public SessionUser(Integer userId, String email, String firstname, String token) {
        this.userId = userId;
        this.email = email;
        this.firstname = firstname;
        this.token = token;
    }
}

