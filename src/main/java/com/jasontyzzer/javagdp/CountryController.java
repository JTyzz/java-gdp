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
        nameList.sort(Comparator.comparing(Country::getCountry));
        return nameList;
    }

    @GetMapping("/economy")
    public List<Country> economy(){
        List<Country> economyList = countryRepository.findAll();
        economyList.sort((c1, c2) -> (int)(c2.getGdp() - c1.getGdp()));
        return economyList;
    }

    @GetMapping("/total")
    public ObjectNode total() {
        List<Country> countryList = countryRepository.findAll();
        Long total = 0L;
        for (Country c : countryList) {
            total += c.getGdp();
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode totalGDP = mapper.createObjectNode();
        totalGDP.put("id", 0);
        totalGDP.put("country", "total");
        totalGDP.put("gdp", total);

        return totalGDP;

    }
    @GetMapping("/gdp/{name}")
    public Country findCountry(@PathVariable String name) {
        List<Country> countryList = countryRepository.findAll();
        Country country = new Country("Not found", 0L);
        for (Country c : countryList){
            if (c.getCountry().equals(name)){
                country = c;
            }
        }
        CountryLog message = new CountryLog("Searched " +country.getCountry() +"'s GDP");
        rabbitTemplate.convertAndSend(JavaGdpApplication.QUEUE_NAME, message.toString());
        log.info("Message sent");
        return country;
    }

    @PostMapping("/gdp")
    public List<Country> loadGdpData(@RequestBody List<Country> newData){
        return countryRepository.saveAll(newData);
    }

}
