//package sk.tuke.gamestudio.web.get;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class GetTest {
//    private static final int port = 8080;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    public void testYourEndpoint() {
//        ResponseEntity<String> response = restTemplate.getForEntity(
//                "http://localhost:" + port + "/", String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).contains("expectedValue");
//    }
//}
