package tecno.competitionplatform.entities;

/**
 * Created by Andres on 31/10/2015.
 */
public class Country {

    private Integer countryId;
    private String sortname;
    private String name;


    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
