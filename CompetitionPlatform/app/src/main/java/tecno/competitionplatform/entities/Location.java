package tecno.competitionplatform.entities;




/**
 * Created by Andres on 29/10/2015.
 */
public class Location {

    private Integer locationId;
    private String name;
    private Integer capacity;
    private Double longitude;
    private Double latitude;
    private Region regionId;

    public Integer getLocation() {
        return locationId;
    }

    public void setLocation(Integer location) {
        this.locationId = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Region getRegion() {
        return regionId;
    }

    public void setRegion(Region region) {
        this.regionId = region;
    }
}
