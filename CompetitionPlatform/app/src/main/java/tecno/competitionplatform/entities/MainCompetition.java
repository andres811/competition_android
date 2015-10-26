package tecno.competitionplatform.entities;

import java.util.Date;

/**
 * Created by Andres on 22/10/2015.
 */
public class MainCompetition {

    private Integer mainCompetitionId;

    public Integer getMainCompetitionId() {
        return mainCompetitionId;
    }

    public void setMainCompetitionId(Integer mainCompetitionId) {
        this.mainCompetitionId = mainCompetitionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStyleType() {
        return styleType;
    }

    public void setStyleType(int styleType) {
        this.styleType = styleType;
    }

    /*
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

*/

    private String description;

    private String name;

    private int styleType;

    //private Date startDate;

    //private Date endDate;

    //private Collection<Competition> competitionCollection;

    //private Tag tag;
}
