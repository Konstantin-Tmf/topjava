package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@RestController
@RequestMapping(value = MealUIController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealUIController extends AbstractMealController {
    static final String URL = "/profile/meals";

    @GetMapping
    public List<MealTo> getAll(@RequestParam(required = false) String startDate,
                               @RequestParam(required = false) String startTime,
                               @RequestParam(required = false) String endDate,
                               @RequestParam(required = false) String endTime) {
        return super.getBetween(parseLocalDate(startDate), parseLocalTime(startTime),
                parseLocalDate(endDate), parseLocalTime(endTime));
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestParam String dateTime,
                       @RequestParam String description,
                       @RequestParam int calories) {
        super.create(new Meal(LocalDateTime.parse(dateTime), description, calories));
    }

}
