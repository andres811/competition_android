package tecno.competitionplatform.entities;

import java.util.Date;

/**
 * Created by Andres on 10/12/2015.
 */
public class Post {

    private int postId;
    private Date createdDate;
    private String description;
    private String title;
    private MainCompetition mainCompetitionId;
    private Admin adminId;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MainCompetition getMainCompetitionId() {
        return mainCompetitionId;
    }

    public void setMainCompetitionId(MainCompetition mainCompetitionId) {
        this.mainCompetitionId = mainCompetitionId;
    }

    public Admin getAdmin() {
        return adminId;
    }

    public void setAdmin(Admin admin) {
        this.adminId = admin;
    }
}
