package ru.ispras.wtpractice.videorent.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ispras.wtpractice.videorent.dao.*;
import ru.ispras.wtpractice.videorent.entity.Movie;

import java.time.LocalDate;

@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieDAO movieDAO;
    private final ExemplarDAO exemplarDAO;
    private final TransactionDAO transactionDAO;

    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(required = false) String by,
                       Model model) {

        var movies = (search == null || search.isBlank())
                ? movieDAO.findAll()
                : switch (by != null ? by : "") {
                    case "director" -> movieDAO.findByDirectorContainingIgnoreCase(search);
                    case "company"  -> movieDAO.findByCompanyContainingIgnoreCase(search);
                    default         -> movieDAO.findByNameContainingIgnoreCase(search);
                };
        model.addAttribute("movies", movies);
        model.addAttribute("search", search);
        model.addAttribute("by", by);
        return "movies/list";
    }

    @GetMapping("/{id}")
    public String card(@PathVariable Long id, Model model) {
        Movie movie = movieDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found: " + id));

        model.addAttribute("movie", movie);
        model.addAttribute("exemplars", exemplarDAO.findByMovieId(id));
        model.addAttribute("transactions", transactionDAO.findByExemplarMovieIdOrderByTimeDesc(id));
        model.addAttribute("totalCount", exemplarDAO.countByMovieId(id));
        model.addAttribute("availableCount", exemplarDAO.countByMovieIdAndAvailableIsTrue(id));
        return "movies/card";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "movies/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Movie movie = movieDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found " + id));

        model.addAttribute("movie", movie);
        return "movies/form";
    }

    @PostMapping
    public String create(@ModelAttribute Movie movie) {
        movieDAO.save(movie);
        return "redirect:/movies/" + movie.getId();
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam String director,
                         @RequestParam String company,
                         @RequestParam LocalDate releaseDate) {
        Movie movie = movieDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found: " + id));

        movie.setName(name);
        movie.setDirector(director);
        movie.setCompany(company);
        movie.setReleaseDate(releaseDate);

        movieDAO.save(movie);
        return "redirect:/movies/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        movieDAO.deleteById(id);
        return "redirect:/movies";
    }
}
