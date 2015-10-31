package tecno.competitionplatform.entities;

/**
 * Created by Andres on 31/10/2015.
 */
public class Delegation{

    private Integer delegationId;
    private String name;
    private Integer membersQty;
    private String imageUrl;
    private String telephone;
    private String email;
    private Country countryId;

    public Integer getDelegationId() {
        return delegationId;
    }

    public void setDelegationId(Integer delegationId) {
        this.delegationId = delegationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMembersQty() {
        return membersQty;
    }

    public void setMembersQty(Integer membersQty) {
        this.membersQty = membersQty;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Country getCountry() {
        return countryId;
    }

    public void setCountry(Country country) {
        this.countryId = country;
    }

}
