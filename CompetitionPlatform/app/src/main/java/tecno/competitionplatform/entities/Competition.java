package tecno.competitionplatform.entities;

/**
 * Created by Andres on 25/10/2015.
 */
public class Competition {

    private Integer competitionId;
    private MainCompetition mainCompetitionId;
    private Location location;
    private String name;
    private String description;
    private Discipline discipline;

    public Integer getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Integer competitionId) {
        this.competitionId = competitionId;
    }

    public MainCompetition getMainCompetition() {
        return mainCompetitionId;
    }

    public void setMainCompetition(MainCompetition mainCompetition) {
        this.mainCompetitionId = mainCompetition;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
}
