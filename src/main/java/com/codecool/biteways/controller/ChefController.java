package com.codecool.biteways.controller;

import com.codecool.biteways.exceptions.RecordNotFoundException;
import com.codecool.biteways.model.Chef;
import com.codecool.biteways.model.ChefDto;
import com.codecool.biteways.service.ChefService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/chef")
public class ChefController {

    private final ChefService chefService;

    public ChefController(ChefService chefService) {
        this.chefService = chefService;
    }

    @PostMapping
    public ResponseEntity<?> saveChef(
            @Valid @RequestBody ChefDto chefDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided data is incorrect!");
        return ResponseEntity.status(HttpStatus.OK).body(chefService.saveChef(chefDto));
    }

    @GetMapping
    public List<ChefDto> findAllChef() {
        return chefService.findAllChef();
    }

    @GetMapping(value = "/{id}")
    public ChefDto findChefById(
            @PathVariable("id") Long id
    ) {
        return chefService.findChefById(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateChef(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChefDto chefDto,
            BindingResult bindingResult
    ) throws RecordNotFoundException {
        if (id == chefDto.getId() && !bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.OK).body(chefService.updateChef(id, chefDto));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided data is incorrect");
    }


    @DeleteMapping(value = "/{id}")
    public void deleteChefById(
            @PathVariable("id") Long id
    ) {
        chefService.deleteChefById(id);
    }

}
