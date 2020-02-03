package com.bmdb.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bmdb.business.JsonResponse;
import com.bmdb.business.Movie;
import com.bmdb.db.MovieRepository;

@RestController
@CrossOrigin
@RequestMapping("/movies")
public class MovieController {
	@Autowired
	private MovieRepository movieRepo;

	@GetMapping("/")
	public JsonResponse list() {
		return JsonResponse.getInstance(movieRepo.findAll());
	}
	
	// get - return 1 stuff for the given id
	@GetMapping("/{id}")
	public JsonResponse getMovie(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(movieRepo.findById(id));
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

//	@GetMapping("/list-movies-for-rating")
//	public JsonResponse listMoviesForRating(@RequestParam String rating) {
//		JsonResponse jr = null;
//		try {
//			jr = JsonResponse.getInstance(movieRepo.findByRating(rating));
//		}
//		catch (Exception e) {
//			jr = JsonResponse.getInstance(e);
//			e.printStackTrace();
//		}
//		return jr;
//	}

	// add - adds a new Movie
	@PostMapping("/")
	public JsonResponse addMovie(@RequestBody Movie m) {
		// add a new movie
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(movieRepo.save(m));
		}
		catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	// update - update a Movie
	@PutMapping("/")
	public JsonResponse updateMovie(@RequestBody Movie m) {
		// update a movie
		JsonResponse jr = null;
		try {
			if (movieRepo.existsById(m.getId())) {
				jr = JsonResponse.getInstance(movieRepo.save(m));
			}
			else {
				// record doesn't exist
				jr = JsonResponse.getInstance("Error updating Movie.  id: "+
											m.getId() + " doesn't exist!");
			}
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	// delete - delete a Movie
	@DeleteMapping("/{id}")
	public JsonResponse deleteMovie(@PathVariable int id) {
		// delete a movie
		JsonResponse jr = null;
		
		try {
			if (movieRepo.existsById(id)) {
				movieRepo.deleteById(id);
				jr = JsonResponse.getInstance("Delete successful!");
			}
			else {
				// record doesn't exist
				jr = JsonResponse.getInstance("Error deleting Movie.  id: "+
											id + " doesn't exist!");
			}
		}
		catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
}
