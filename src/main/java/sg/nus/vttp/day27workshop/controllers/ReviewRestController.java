package sg.nus.vttp.day27workshop.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.nus.vttp.day27workshop.Exceptions.ReviewNotFoundException;
import sg.nus.vttp.day27workshop.models.Review;
import sg.nus.vttp.day27workshop.services.ReviewService;

@RestController
public class ReviewRestController {
    @Autowired
    ReviewService reviewService;

    @PostMapping(path = "/review")
    public ResponseEntity<String> getReview(@RequestBody MultiValueMap<String, Object> mvm, Model model){
        try{
            String name = mvm.getFirst("name").toString();
            Integer rating = Integer.parseInt(mvm.getFirst("rating").toString());
            String commnet = mvm.getFirst("comment").toString();
            Integer gid = Integer.parseInt(mvm.getFirst("gid").toString());
            Review review = new Review();
            review.setName(name);
            review.setRating(rating);
            review.setComment(commnet);
            review.setGid(gid);
        String reviewsaved = reviewService.getGameName(review);

        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                                            .body(reviewsaved);
        
        return response;
        }
        catch(RuntimeException ex){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.TEXT_PLAIN)
                                                .body("Game not found.");
            return response;
        }
        
    }

    @PutMapping(path = "/review/{review_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editReview(@PathVariable(name = "review_id") int review_id,
    @RequestBody Edit edit){
        edit.setEdited(new Date());
        Review review = reviewService.getReview(review_id);
        reviewService.editReview(review_id, review, edit);
        ResponseEntity response = ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("Review updated.");
        return response;
    }

    @GetMapping(path = "/review/{review_id}")
    public ResponseEntity<String> getReview(@PathVariable(name = "review_id") int review_id){
        try{
        Review review = reviewService.getReview(review_id);
        JsonObject jsonObject = reviewService.reviewJson(review);
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                                                .body(jsonObject.toString());
        return response;
        }
        catch(ReviewNotFoundException ex){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_PLAIN)
                                                .body("Not found.");
            return response;                            
        }
        
    }

    @GetMapping(path = "/review/{review_id}/history")
    public ResponseEntity<String> getEditHistory(@PathVariable(name = "review_id") int review_id){
        Review review = reviewService.getReview(review_id);
        JsonObject responseBody = reviewService.reviewJsonWithEdits(review);
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .body(responseBody.toString());
        return response;
    }
    
}
