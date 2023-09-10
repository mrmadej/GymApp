package gym.app.project.service;

import gym.app.project.dao.InClubRepository;
import gym.app.project.entity.InClub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InClubService
{
    private InClubRepository inClubRepository;

    @Autowired
    public InClubService(InClubRepository inClubRepository)
    {
        this.inClubRepository = inClubRepository;
    }
    @Transactional
    public void save(InClub inClub)
    {
        inClubRepository.save(inClub);
    }

    public List<Integer> getAvg(LocalDateTime dateTime, int clubId)
    {
        List<Integer> averages = new ArrayList<Integer>();

        LocalDateTime startTime = dateTime.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endTime = dateTime.withHour(23).withMinute(59).withSecond(59);

        for(int hour = 0; hour < 24; hour++)
        {
            startTime = startTime.withHour(hour);
            endTime = endTime.withHour(hour);

            Double average = inClubRepository.findAverageUsersCountForHourOfDayInClub(clubId, startTime, endTime);

            if(average == null)
            {
                average = 0.0;
            }

            averages.add(average.intValue());
        }
        return averages;
    }

    public List<Integer> getAvgByDayOfTheWeek(int dayOfTheWeek, int clubId)
    {
        List<Integer> averages = new ArrayList<Integer>();

        for(int hour = 0; hour < 24; hour++)
        {
            Double average = inClubRepository.findAverageUsersCountForHourOfWeekInClub(clubId, dayOfTheWeek, hour);

            if(average == null)
            {
                average = 0.0;
            }
            averages.add(average.intValue());
        }
        return averages;
    }
}
