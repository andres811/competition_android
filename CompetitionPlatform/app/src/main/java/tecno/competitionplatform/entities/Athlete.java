package tecno.competitionplatform.entities;

import java.util.Date;

/**
 * Created by Andres on 31/10/2015.
 */
public class Athlete {

    private Integer athleteId;
    private Delegation delegationId;
    private String name;
    private String lastname;
    private Date dob;
    private Boolean featured;

    public Integer getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(Integer athleteId) {
        this.athleteId = athleteId;
    }

    public Delegation getDelegation() {
        return delegationId;
    }

    public void setDelegation(Delegation delegation) {
        this.delegationId = delegation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }
}
