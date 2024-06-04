package sk.tuke.gamestudio.database.restclients;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.database.interfaces.PlayerService;
import sk.tuke.gamestudio.entity.checkers.Player;
import sk.tuke.gamestudio.server.dto.RegistrationRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class PlayerControllerClient implements PlayerService {

    private static final Logger log = LoggerFactory.getLogger(PlayerControllerClient.class);
    @Value("${remote.server.api}")
    private String url;
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private @NotNull HttpEntity<MultiValueMap<String, String>> getRequest(String nickname, String password) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("nickname", nickname);
        map.add("password", password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(map, headers);
    }

    @Override
    public Player logIn(@NotNull RegistrationRequest registrationRequest) {
        log.info("Sending login request for nickname: {}", registrationRequest.getNickname());
        log.info("Sending login request with password: {}", registrationRequest.getPassword());
        val request = this.getRequest(registrationRequest.getNickname(), registrationRequest.getPassword());
        ResponseEntity<Player> response = restTemplate.postForEntity(url + "/player/checkers/log-in", request, Player.class);
        log.info("Received response: {}", response.getBody());
        return response.getBody();
    }

    @Override
    public String checkForExistingPlayer(@NotNull String nickname) {
        return this.restTemplate.getForEntity(url + "/players/checkers/exists", String.class, nickname).getBody();
    }

    @Override
    public Player registerNew(@NotNull RegistrationRequest registrationRequest) {
        val request = this.getRequest(registrationRequest.getNickname(), registrationRequest.getPassword());
        ResponseEntity<Player> response = restTemplate.postForEntity(url + "/player/checkers/sign-up", request, Player.class);
        return response.getBody();
    }

    @Override
    public List<Player> getTopTenPlayers() {
        return Arrays.asList(Objects.requireNonNull(this.restTemplate.getForEntity(url + "/player/checkers/get-top", Player[].class).getBody()));
    }
}
