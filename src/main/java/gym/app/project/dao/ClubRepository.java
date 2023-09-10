package gym.app.project.dao;

import gym.app.project.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Integer>
{
    public Optional<Club> findByClubName(String clubName);
    public Optional<Club> findById(int id);
}
