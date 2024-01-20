package sg.nus.vttp.day27workshop.controllers;

import java.util.Date;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Edit {
    private String comment;
    private int rating;
    private Date edited;

    public JsonObject toJson(){
        JsonObjectBuilder JOB = Json.createObjectBuilder();
        JOB.add("comment",comment)
            .add("rating",rating)
            .add("edited",edited.getTime());
        return JOB.build();
      
    }


    @Override
    public String toString() {
        return "Edit [comment=" + comment + ", rating=" + rating + ", edited=" + edited + "]";
    }
    public Date getEdited() {
        return edited;
    }
    public void setEdited(Date edited) {
        this.edited = edited;
    }
    public Edit(String comment, int rating) {
        this.comment = comment;
        this.rating = rating;
    }
    public Edit(){
        
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    
}
