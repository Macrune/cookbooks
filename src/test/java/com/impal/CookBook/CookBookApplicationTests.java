package com.impal.CookBook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.impal.CookBook.Controller.HomepageController;
import com.impal.CookBook.Controller.UserController;
import com.impal.CookBook.Model.User;
import com.impal.CookBook.Payload.RecipeCardResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CookBookApplicationTests {


	@Autowired
	private UserController userController;

	@Autowired
	private HomepageController homepageController;

	@ParameterizedTest
	@CsvSource({
			"akhtarm",
			"nailyusr",
			"argogum",
			"nailyusra",
			"bian",
			"aryoooo"
	})
	void cekUser(String imdbId) {
		ResponseEntity<User> response = userController.apiGetUser(imdbId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getImdbId(), imdbId);
	
	}

	@ParameterizedTest
	@CsvSource({
			"67757596da314543537d22a1",
			"6776b8a3aa60721fd2c7e07b",
			"6777bec36afc107f0f7d1548"
	})
	void cekRecipes(String imdbId) {
		ResponseEntity<ArrayList<RecipeCardResponse>> response = homepageController.getFeaturedRecipe();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());

		ArrayList<String> listId = new ArrayList<>();
		for (RecipeCardResponse recipeCard : response.getBody()) {
			listId.add(recipeCard.getImdbId());
		}
		assertEquals(listId.contains(imdbId), true);

	}

}
