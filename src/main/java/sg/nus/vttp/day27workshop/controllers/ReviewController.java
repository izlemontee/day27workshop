package sg.nus.vttp.day27workshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import sg.nus.vttp.day27workshop.models.Review;

@Controller
public class ReviewController {

    @GetMapping(path = "/")
    public String getLandingPage(Model model){
        model.addAttribute("review", new Review());
        return "index";
    }
    
}
