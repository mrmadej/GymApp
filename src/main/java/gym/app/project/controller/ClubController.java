package gym.app.project.controller;

import gym.app.project.entity.Club;
import gym.app.project.entity.InClub;
import gym.app.project.service.ClubService;
import gym.app.project.service.InClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/Clubs")
public class ClubController
{
    private ClubService clubService;
    private InClubService inClubService;

    @Autowired
    public ClubController(ClubService clubService, InClubService inClubService)
    {
        this.clubService = clubService;
        this.inClubService = inClubService;
    }

    @GetMapping("/lastCount")
    public String getLastCount(Model model)
    {
        List<Club> clubs = clubService.findAll();

        model.addAttribute("clubs", clubs);

        return "club-list";
    }
    @GetMapping("/getAvg")
    public String getAvgByDay(@RequestParam int clubId, Model model)
    {
        List<Integer> inClubList = inClubService.getAvg(LocalDateTime.now(), clubId);

        Club club = clubService.findById(clubId);

        if(club == null)
        {
            return "club-list";
        }

        model.addAttribute("club", club);
        model.addAttribute("avg", inClubList);

        return "club-hour-list";
    }

    @GetMapping("/getAvgWeek")
    public String  getAvgOfEveryDayOfWeek(@RequestParam int clubId, Model model)
    {
        HashMap<Integer, List<Integer>> averagesOfWeek = new HashMap<>();
        List<Integer> inClubList;
        for(int day = 1; day < 8; day++)
        {
            inClubList = inClubService.getAvgByDayOfTheWeek(day, clubId);
            averagesOfWeek.put(day, inClubList);
            inClubList = null;
        }

        Club club = clubService.findById(clubId);

        model.addAttribute("club", club);
        model.addAttribute("averages", averagesOfWeek);

        System.out.println(averagesOfWeek);
        return "club-week-averages";
    }

    @GetMapping("/find")
    public String findView(@RequestParam int id, Model model)
    {
        model.addAttribute("id", id);

        return "find-specific-date";
    }
    @PostMapping("/find")
    public String findSpecificDate(@RequestParam("id") int id, @RequestParam("day") int day, @RequestParam("month") int month, @RequestParam("year") int year,Model model)
    {
        LocalDateTime date = LocalDateTime.now().withYear(year).withMonth(month).withDayOfMonth(day);

        List<Integer> inClubList = inClubService.getAvg(date, id);

        Club club = clubService.findById(id);

        if(club == null)
        {
            return "club-list";
        }

        model.addAttribute("club", club);
        model.addAttribute("avg", inClubList);

        return "club-hour-list";
    }
}
