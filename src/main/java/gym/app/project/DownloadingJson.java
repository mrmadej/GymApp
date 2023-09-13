package gym.app.project;

import gym.app.project.entity.Club;
import gym.app.project.entity.InClub;
import gym.app.project.service.ClubService;
import gym.app.project.service.InClubService;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;

@EnableScheduling
@Component
public class DownloadingJson
{
    private ClubService clubService;
    private InClubService inClubService;
    private String clubName;
    private String clubAddress;
    private int usersCountCurrentlyInCLub;
    private LocalDateTime hourOfDownload;
    private String accessCookie="";
    @Value("${gym.login}")
    private String login;
    @Value("${gym.password}")
    private String password;

    @Autowired
    public DownloadingJson(ClubService clubService, InClubService inClubService)
    {
        this.clubService = clubService;
        this.inClubService = inClubService;
    }

    @Scheduled(fixedRate = 60000)
    public void myScheduleMethod() throws IOException
    {
        String url = "https://smartplatinium.perfectgym.pl/ClientPortal2/Clubs/Clubs/GetMembersInClubs"; // Twój URL JSON

        try
        {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            addHeaders(httpGet);
            httpGet.addHeader("Cookie",this.accessCookie);
            HttpResponse response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode()==200)
            {
                HttpEntity entity = response.getEntity();
                String jsonResponse = EntityUtils.toString(entity);

                if (jsonResponse != null && !jsonResponse.isEmpty())
                {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(jsonResponse).get("UsersInClubList");

                    if (jsonNode.isArray())
                    {
                        for (final JsonNode objNode : jsonNode)
                        {
                            usersCountCurrentlyInCLub = objNode.get("UsersCountCurrentlyInClub").asInt();
                            if(usersCountCurrentlyInCLub > 0)
                            {
                                clubName = objNode.get("ClubName").asText();
                                clubAddress = objNode.get("ClubAddress").asText();
                                hourOfDownload = LocalDateTime.now();

                                Club tempClub = clubService.FindByClubName(clubName);
                                if(tempClub == null)
                                {
                                    tempClub = new Club(clubName, clubAddress);
                                }

                                InClub tempInClub = new InClub(usersCountCurrentlyInCLub, hourOfDownload);

                                tempInClub.setClub(tempClub);
                                tempClub.add(tempInClub);

                                clubService.save(tempClub);
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Odpowiedź JSON nie jest obiektem.");
                    }
                }
                else
                {
                    System.out.println("Odpowiedź JSON jest pusta.");
                }
            }else if (response.getStatusLine().getStatusCode()==401)
            {
                System.out.println("Token wygasl potrzebne logowanie");
                JSONObject json = new JSONObject();
                json.put("RememberMe", false);
                json.put("Login", login);
                json.put("Password", password);
                HttpPost httpPost = new HttpPost("https://smartplatinium.perfectgym.pl/ClientPortal2/Auth/Login");
                addHeaders(httpPost);
                String jsonString = json.toString();
                StringEntity se = new StringEntity(jsonString);
                se.setContentType(new BasicHeader("Content-Type", "application/json"));
                httpPost.setEntity(se);
                HttpResponse loginResponse = httpClient.execute(httpPost);

                if (loginResponse != null)
                {
                    String cookie = loginResponse.getFirstHeader("Set-Cookie").getValue();
                    this.accessCookie = cookie;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void addHeaders(HttpRequestBase httpGet) {
        httpGet.addHeader("CP-LANG","pl");
        httpGet.addHeader("CP-MODE","mobile");
        httpGet.addHeader("Accept-Encoding","gzip, deflate, br");
        httpGet.addHeader("Accept-Language","en-US,en;q=0.5");
        httpGet.addHeader("Connection","keep-alive");
        httpGet.addHeader("Accept","application/json, text/plain, */*");
    }
}
