package tecno.competitionplatform.entities;

/**
 * Created by Andres on 25/10/2015.
 */
public class Competition {

    private Integer competitionId;
    private MainCompetition mainCompetitionId;
    private Location locationId;
    private String name;
    private String description;
    private Discipline disciplineId;

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
        return locationId;
    }

    public void setLocation(Location location) {
        this.locationId = location;
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
        return disciplineId;
    }

    public void setDiscipline(Discipline discipline) {
        this.disciplineId = discipline;
    }
}
