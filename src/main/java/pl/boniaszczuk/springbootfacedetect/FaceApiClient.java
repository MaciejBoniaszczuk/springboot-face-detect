package pl.boniaszczuk.springbootfacedetect;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import pl.boniaszczuk.springbootfacedetect.model.ImageUrl;

@Controller
public class FaceApiClient {
    @Value("${apiKey}")
    private String apiKey;
    private static final String FACE_API_URL = "https://francecentral.api.cognitive.microsoft.com/face/v1.0/detect?returnFaceId=true&returnFaceLandmarks=false&returnFaceAttributes=age,gender,smile,facialHair,glasses,emotion,hair,accessories,blur,exposure,noise&recognitionModel=recognition_01&returnRecognitionModel=false&detectionModel=detection_01";

    @EventListener(ApplicationReadyEvent.class)
    public void get(){
        RestTemplate restTemplate = new RestTemplate();

        ImageUrl imageUrl = new ImageUrl("https://cdn.now.howstuffworks.com/media-content/0b7f4e9b-f59c-4024-9f06-b3dc12850ab7-1920-1080.jpg");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Ocp-Apim-Subscription-Key",apiKey);
        HttpEntity<ImageUrl> imageUrlHttpEntity = new HttpEntity<>(imageUrl, httpHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(FACE_API_URL, HttpMethod.POST, imageUrlHttpEntity, String.class);
        System.out.println(exchange.getBody());
    }

}
