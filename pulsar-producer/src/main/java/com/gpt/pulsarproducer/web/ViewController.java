package com.gpt.pulsarproducer.web;


import com.gpt.pulsarproducer.service.StationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController
{
    private final StationService stationService;

    public ViewController(StationService stationService)
    {
        this.stationService = stationService;
    }

    @GetMapping("/")
    public String index(Model model)
    {
        model.addAttribute("counts", stationService.countsView());
        return "index";
    }
}
