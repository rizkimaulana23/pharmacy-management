package apap.ti.pharmacy2206814425.controller;

import apap.ti.pharmacy2206814425.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    HomeService homeService;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("dataDTO", homeService.getHomeData());
        return "home";
    }
}
