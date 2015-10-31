package tecno.competitionplatform.entities;

/**
 * Created by Andres on 31/10/2015.
 */
public class Participation {

    private Athlete athleteId;
    private Competition competitionId;
    private Integer position;

    public Athlete getAthlete() {
        return athleteId;
    }

    public void setAthlete(Athlete athlete) {
        this.athleteId = athlete;
    }

    public Competition getCompetition() {
        return competitionId;
    }

    public void setCompetition(Competition competition) {
        this.competitionId = competition;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
