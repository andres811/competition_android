package tecno.competitionplatform.entities;

/**
 * Created by Andres on 31/10/2015.
 */
public class Tag {

    private MainCompetition mainCompetitionId;
    private String tag;

    public MainCompetition getMainCompetition() {
        return mainCompetitionId;
    }

    public void setMainCompetition(MainCompetition mainCompetition) {
        this.mainCompetitionId = mainCompetition;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
