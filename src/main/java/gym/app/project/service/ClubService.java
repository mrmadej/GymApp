package gym.app.project.service;

import gym.app.project.dao.ClubRepository;
import gym.app.project.entity.Club;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClubService
{
    private ClubRepository clubRepository;

    @Autowired
    ClubService(ClubRepository clubRepository)
    {
        this.clubRepository = clubRepository;
    }

    public Club FindByClubName(String clubName)
    {
        return clubRepository.findByClubName(clubName).orElse(null);
    }
    @Transactional
    public void save(Club club)
    {
        clubRepository.save(club);
    }

    public List<Club> findAll()
    {
        return clubRepository.findAll();
    }

    public Club findById(int id)
    {
        return clubRepository.findById(id).orElse(null);
    }
}
