package sg.nus.vttp.day27workshop.models;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.json.JsonObject;

@Document(collection= "reviews")
public class Review {

    @Id
    private Long review_id;
    public Long getReview_id() {
        return review_id;
    }

    public void setReview_id(Long review_id) {
        this.review_id = review_id;
    }
    private String name;
    private Integer rating;
    private String comment;
    private Integer gid;
    private Date posted;
    private String gameName;
    private List<JsonObject> edits;

    public List<JsonObject> getEdits() {
        return edits;
    }

    public void setEdits(List<JsonObject> edits) {
        this.edits = edits;
    }

    @Override
    public String toString() {
        return "Review [review_id=" + review_id + ", name=" + name + ", rating=" + rating + ", comment=" + comment
                + ", gid=" + gid + ", posted=" + posted + ", gameName=" + gameName + ", edits=" + edits + "]";
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Review(){
        
    }
    
    public Review(String name, Integer rating, String comment, Integer gid) {
        this.name = name;
        this.rating = rating;
        this.comment = comment;
        this.gid = gid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Integer getGid() {
        return gid;
    }
    public void setGid(Integer gid) {
        this.gid = gid;
    }
    
}
