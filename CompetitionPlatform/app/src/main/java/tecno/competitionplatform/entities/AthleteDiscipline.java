package tecno.competitionplatform.entities;

/**
 * Created by Andres on 31/10/2015.
 */
public class AthleteDiscipline {

    private Athlete athleteId;
    private Discipline disciplineId;

    public Athlete getAthlete() {
        return athleteId;
    }

    public void setAthlete(Athlete athlete) {
        this.athleteId = athlete;
    }

    public Discipline getDiscipline() {
        return disciplineId;
    }

    public void setDiscipline(Discipline discipline) {
        this.disciplineId = discipline;
    }
}
