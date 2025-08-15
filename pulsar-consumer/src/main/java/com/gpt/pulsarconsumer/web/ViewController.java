package com.gpt.pulsarconsumer.web;


import com.gpt.pulsarconsumer.config.PulsarProperties;
import com.gpt.pulsarconsumer.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController
{

    private final MetricsService metrics;
    private final PulsarProperties pulsarProperties;

    @GetMapping("/")
    public String index(Model model)
    {
        model.addAttribute("counts", metrics.snapshot());
        model.addAttribute("iteration", pulsarProperties.getIteration());

        return "index";
    }
}
