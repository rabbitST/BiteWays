package com.codecool.biteways.service;

import com.codecool.biteways.exceptions.RecordNotFoundException;
import com.codecool.biteways.model.Chef;
import com.codecool.biteways.model.ChefDto;
import com.codecool.biteways.repository.ChefRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChefService {

    private final ChefRepository chefRepository;
    private final ModelMapper modelMapper;

    public ChefService(ChefRepository chefRepository,
                       ModelMapper modelMapper) {
        this.chefRepository = chefRepository;
        this.modelMapper = modelMapper;
    }

    public ChefDto saveChef(ChefDto chefDto) {
        Chef chef = modelMapper.map(chefDto, Chef.class);
        chefRepository.save(chef);
        return new ModelMapper().map(chef, ChefDto.class);
    }

    public List<ChefDto> findAllChef() {
        return chefRepository.findAll().stream().map((element) -> modelMapper.map(element, ChefDto.class)).toList();
    }

    public ChefDto findChefById(Long id) {
        return modelMapper.map(chefRepository.findById(id), ChefDto.class);
    }

    @Transactional
    public ChefDto updateChef(Long id, ChefDto chefDto) throws RecordNotFoundException {
        Chef chef = chefRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("Requested ID: %s not found!", id)));
        chef.setName(chefDto.getName());
        chef.setRecipeList(chefDto.getRecipeList());
        return modelMapper.map(chef, ChefDto.class);
    }

    public void deleteChefById(Long id) {
        chefRepository.deleteById(id);
    }
}
