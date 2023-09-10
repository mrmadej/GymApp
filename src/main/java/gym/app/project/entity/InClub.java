package gym.app.project.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "InClub")
public class InClub
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "users_count_currently_in_club")
    private int usersCountCurrentlyInClub;

    @Column(name = "hour_of_download")
    private LocalDateTime hourOfDownload;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "club_id")
    private Club club;

    public InClub()
    {

    }
    public InClub(int usersCountCurrentlyInClub, LocalDateTime hourOfDownload)
    {
        this.usersCountCurrentlyInClub = usersCountCurrentlyInClub;
        this.hourOfDownload = hourOfDownload;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getUsersCountCurrentlyInClub()
    {
        return usersCountCurrentlyInClub;
    }

    public void setUsersCountCurrentlyInClub(int usersCountCurrentlyInClub)
    {
        this.usersCountCurrentlyInClub = usersCountCurrentlyInClub;
    }

    public LocalDateTime getHourOfDownload()
    {
        return hourOfDownload;
    }

    public void setHourOfDownload(LocalDateTime hourOfDownload)
    {
        this.hourOfDownload = hourOfDownload;
    }

    public Club getClub()
    {
        return club;
    }

    public void setClub(Club club)
    {
        this.club = club;
    }

    @Override
    public String toString()
    {
        return "InClub{" +
                "id=" + id +
                ", usersCountCurrentlyInClub=" + usersCountCurrentlyInClub +
                ", hourOfDownload=" + hourOfDownload +
                ", club=" + club +
                '}';
    }
}
