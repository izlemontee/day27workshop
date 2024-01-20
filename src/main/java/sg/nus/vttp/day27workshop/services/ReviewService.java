package sg.nus.vttp.day27workshop.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import sg.nus.vttp.day27workshop.Exceptions.ReviewNotFoundException;
import sg.nus.vttp.day27workshop.controllers.Edit;
import sg.nus.vttp.day27workshop.models.Review;
import sg.nus.vttp.day27workshop.repositories.GameRepo;

@Service
public class ReviewService {
    
    @Autowired
    private GameRepo gameRepo;

    public String getGameName(Review review){
        Integer gid = review.getGid();
        System.out.println("Game exists: "+gameRepo.gameExists(gid));
        if(gameRepo.gameExists(gid)){
      
            Document document = gameRepo.getSingleGame(gid);
            String name = document.get("name").toString();
            review.setPosted(new Date());
            review.setGameName(name);
    
            Review reviewsaved = gameRepo.saveReview(review);
            System.out.println("reached here");
            System.out.println(reviewsaved.toString());
  
            return reviewsaved.toString();
        }
        throw new RuntimeException("Game not found");
    }


    public Review getReview (int review_id){
        if(gameRepo.reviewExists(review_id)){
            Document document = gameRepo.getReview(review_id);
            Review review = new Review();
            // private String name;
            // private Integer rating;
            // private String comment;
            // private Integer gid;
            // private Date posted;
            // private String gameName;
            // private List<JsonObject> edits;
            review.setName(document.getString("name"));
            review.setRating(document.getInteger("rating"));
            review.setComment(document.getString("comment"));
            review.setGid(document.getInteger("gid"));
            review.setPosted(new Date(document.getLong("posted")));
            review.setGameName(document.getString("gameName"));
            List<Document> editsRaw = (List<Document>)document.get("edits");
            List<JsonObject> edits = new ArrayList<>();
            for(Document d:editsRaw){
                System.out.println("Document: "+d.toString());
                Document commentDoc = (Document)d.get("comment");
                Document ratingDoc = (Document)d.get("rating");
                Document editedDoc = (Document)d.get("edited");
                String comment = commentDoc.getString("value");
                Long editedMils = editedDoc.getLong("num");
                Integer rating = ratingDoc.getInteger("num");
                System.out.println("rating: "+rating.toString());
                JsonObjectBuilder JOB = Json.createObjectBuilder();
                JsonObject JSO = JOB.add("comment",comment)
                    .add("rating",rating)
                    .add("edited",(new Date(editedMils)).toString())
                    .build();
                edits.add(JSO);
            
            }
            review.setEdits(edits);
            return review;
        }
        throw new ReviewNotFoundException();
     
    }

    public JsonObject reviewJson(Review review){
        JsonObjectBuilder JOB = Json.createObjectBuilder();
        JOB.add("user",review.getName())
            .add("rating",review.getRating())
            .add("comment",review.getComment())
            .add("ID",review.getGid())
            .add("posted",review.getPosted().toString())
            .add("name",review.getGameName())
            .add("edited",(review.getEdits().size()>0))
            .add("timestamp",new Date().toString());
        return JOB.build();
    }

    public JsonObject reviewJsonWithEdits(Review review){
        JsonObjectBuilder JOB = Json.createObjectBuilder();
        JOB.add("user",review.getName())
            .add("rating",review.getRating())
            .add("comment",review.getComment())
            .add("ID",review.getGid())
            .add("posted",review.getPosted().toString())
            .add("name",review.getGameName());
        JsonArrayBuilder JAB = Json.createArrayBuilder();
        List<JsonObject> edits = review.getEdits();
        for(int i = 0; i< edits.size();i++ ){
            System.out.println(edits.get(i));
            JsonObject JSO = edits.get(i);
            JAB.add(JSO);
        }
        
            JOB.add("edited",JAB.build())
            .add("timestamp",new Date().toString());
        return JOB.build();
    }


    public void editReview(int review_id, Review review, Edit edit){
        List<JsonObject> edits;
        if(review.getEdits() == null){
            edits = new ArrayList<>();
        }
        else{
            edits = review.getEdits();
        }
        edits.add(edit.toJson());
        review.setEdits(edits);
        review.setComment(edit.getComment());
        review.setPosted(edit.getEdited());
        review.setRating(edit.getRating());
        long updatedCount = gameRepo.updateReview(review_id, review);
   

    }
    
}
