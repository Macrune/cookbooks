package com.impal.CookBook.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.impal.CookBook.Model.Recipes;
import com.impal.CookBook.Model.RepiceRepository;

@Service
public class RecipeService {
    @Autowired
    private RepiceRepository repository;

    public List<Recipes> getAllRecipe() {
        System.out.println(repository.findAll());
        return repository.findAll();
    }

    public Optional<Recipes> findRecipeByImdbId(String imdbId) {
        return repository.findRecipeByImdbId(imdbId);
    }
}
