package sg.nus.vttp.day27workshop.repositories;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import jakarta.json.JsonObject;
import sg.nus.vttp.day27workshop.models.Review;

@Repository
public class GameRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean gameExists(Integer gid){
        Query query = Query.query(Criteria.where("gid").is(gid));
        System.out.println("gid: "+gid);
        System.out.println("collectionEXists" + mongoTemplate.collectionExists("game"));
        List<Document> result = mongoTemplate.find(query, Document.class, "game");
        //System.out.println("List: "+result);
        return (result.size()>0);
    }
    public Document getSingleGame(Integer gid){
        Query query = Query.query(Criteria.where("gid").is(gid)).limit(1);
        List<Document> result = mongoTemplate.find(query,Document.class,"game");
        return result.get(0);
    }

    public Review saveReview (Review review){
        Query query = new Query();
        Long reviewCount = mongoTemplate.count(query,"reviews");
        review.setReview_id(reviewCount+1);
        Review reviewsaved = mongoTemplate.insert(review, "reviews");
        //System.out.println("reached here in repo");
        return reviewsaved;
    }

    public boolean reviewExists(int review_id){
        Query query = Query.query(Criteria.where("_id").is(review_id));
        List<Document> result = mongoTemplate.find(query,Document.class,"reviews");
        return (result.size()>0);
    }

    public Document getReview(int review_id){
        Query query = Query.query(Criteria.where("_id").is(review_id)).limit(1);
        List<Document> result = mongoTemplate.find(query, Document.class, "reviews");
        System.out.println("no error");
        return result.get(0);
        //return result.get(0);
    }

    public long updateReview(int review_id, Review review){
        // edits, comment, edited, rating
        String comment = review.getComment();
        Date edited = review.getPosted();
        int rating = review.getRating();
        List<JsonObject> edits = review.getEdits();
        Update update = new Update().set("rating",rating)
                        .set("comment",comment)
                        .set("posted",edited.getTime())
                        .set("edits",edits);
        Query query = Query.query(Criteria.where("_id").is(review_id)).limit(1);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Review.class,"reviews");
        long updatedCount = updateResult.getModifiedCount();
        return updatedCount;
        //mongoTemplate.findAndModify(query, null, null, null, null)
    }
}
