package tecno.competitionplatform.entities;

/**
 * Created by Andres on 31/10/2015.
 */
public class Tag {

    private MainCompetition mainCompetitionId;
    private String tag;
    private Integer tagId;

    public MainCompetition getMainCompetition() {
        return mainCompetitionId;
    }

    public void setMainCompetition(MainCompetition mainCompetitionId) {
        this.mainCompetitionId = mainCompetitionId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}
