package com.jasontyzzer.javagdp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Slf4j
@RestController
public class CountryController {
    private final CountryRepository countryRepository;
    private final RabbitTemplate rabbitTemplate;

    public CountryController(CountryRepository countryRepository, RabbitTemplate rabbitTemplate){
        this.countryRepository = countryRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    @GetMapping("/names")
    public List<Country> getAll(){
        List<Country> nameList = countryRepository.findAll();
        nameList.sort(Comparator.comparing(Country::getName));
        return nameList;
    }

}
