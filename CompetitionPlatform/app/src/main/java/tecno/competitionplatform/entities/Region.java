package tecno.competitionplatform.entities;

/**
 * Created by Andres on 31/10/2015.
 */
public class Region {

    private Integer regionId;
    private String name;
    private Country countryId;

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return countryId;
    }

    public void setCountry(Country country) {
        this.countryId = country;
    }
}
