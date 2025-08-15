package com.gpt.pulsarproducer.web;


import com.gpt.pulsarproducer.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stations")
public class StationController
{
    private final StationService service;

    @PostMapping("/add")
    public ResponseEntity<Void> add()
    {
        service.addRandom();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete()
    {
        service.deleteRandom();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> update()
    {
        service.updateRandom();
        return ResponseEntity.noContent().build();
    }
}
