package gym.app.project.dao;

import gym.app.project.entity.InClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface InClubRepository extends JpaRepository<InClub, Integer>
{
    @Query("SELECT AVG(i.usersCountCurrentlyInClub) FROM InClub i WHERE i.club.id = :clubId AND i.hourOfDownload BETWEEN :startTime AND :endTime")
    Double findAverageUsersCountForHourOfDayInClub(
            @Param("clubId") int clubId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT AVG(i.usersCountCurrentlyInClub) FROM InClub i WHERE i.club.id = :clubId " +
            "AND DAYOFWEEK(i.hourOfDownload) = :dayOfWeek " +
            "AND EXTRACT(HOUR FROM i.hourOfDownload) = :startTimeHour " +
            "AND EXTRACT(MINUTE FROM i.hourOfDownload) <= 59" +
            "AND EXTRACT(SECOND FROM i.hourOfDownload) <= 59"
    )
    Double findAverageUsersCountForHourOfWeekInClub(
            @Param("clubId") int clubId,
            @Param("dayOfWeek") int dayOfWeek,
            @Param("startTimeHour") int startTimeHour
    );
}
