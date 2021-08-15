package com.project.moviecatalogservice.resource;

import com.project.moviecatalogservice.models.CatalogItem;
import com.project.moviecatalogservice.models.Movie;
import com.project.moviecatalogservice.models.Rating;
import com.project.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
//        List<Rating> ratings = Arrays.asList(
//                new Rating("1234",4),
//                new Rating("5678",3)
//        );
        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/"+userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating -> {
           Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
           return new CatalogItem(movie.getName(),"Test1",rating.getRating());
               }).collect(Collectors.toList());
    }
}
