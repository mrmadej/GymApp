package gym.app.project.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "Club")
public class Club
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "club_name")
    private String clubName;

    @Column(name = "club_address")
    private String clubAddress;

    @OneToMany(mappedBy = "club",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    private List<InClub> inClubList;

    public Club()
    {

    }
    public Club(String clubName, String clubAddress)
    {
        this.clubName = clubName;
        this.clubAddress = clubAddress;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getClubName()
    {
        return clubName;
    }

    public void setClubName(String clubName)
    {
        this.clubName = clubName;
    }

    public String getClubAddress()
    {
        return clubAddress;
    }

    public void setClubAddress(String clubAddress)
    {
        this.clubAddress = clubAddress;
    }

    public List<InClub> getInClubList()
    {
        return inClubList;
    }
    public int getLastInClub()
    {
        List<InClub> inClub = getInClubList();
        inClub.sort(Comparator.comparing(InClub::getHourOfDownload).reversed());

        if(!inClub.isEmpty())
        {
            return inClub.get(0).getUsersCountCurrentlyInClub();
        }

        return 0;
    }

    public void setInClubList(List<InClub> inClubList)
    {
        this.inClubList = inClubList;
    }
    public void add(InClub inClub)
    {
        if(inClubList == null)
        {
            inClubList = new ArrayList<>();
        }
        inClubList.add(inClub);

        inClub.setClub(this);
    }

    @Override
    public String toString()
    {
        return "Club{" +
                "id=" + id +
                ", clubName='" + clubName + '\'' +
                ", clubAddress='" + clubAddress + '\'' +
                '}';
    }
}
