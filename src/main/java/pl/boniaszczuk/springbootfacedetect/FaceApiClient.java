package pl.boniaszczuk.springbootfacedetect;


import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import pl.boniaszczuk.springbootfacedetect.model.FaceObject;
import pl.boniaszczuk.springbootfacedetect.model.ImageUrl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

@Controller
public class FaceApiClient {
    @Value("${apiKey}")
    private String apiKey;
    private static final String FACE_BASE_API_URL = "https://francecentral.api.cognitive.microsoft.com/face/v1.0/detect?";

    @EventListener(ApplicationReadyEvent.class)
    public void get(){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ImageUrl> imageUrlHttpEntity = getHttpEntity("https://cdn.now.howstuffworks.com/media-content/0b7f4e9b-f59c-4024-9f06-b3dc12850ab7-1920-1080.jpg");
        ResponseEntity<FaceObject[]> exchange = restTemplate.exchange(getApiUrl(), HttpMethod.POST, imageUrlHttpEntity, FaceObject[].class);
        Stream.of(exchange.getBody()).forEach(System.out::println);
    }
    private HttpEntity<ImageUrl> getHttpEntity(String url) {
        ImageUrl imageUrl = new ImageUrl(url);
        HttpHeaders httpHeaders = getHttpHeaders();
        return new HttpEntity<>(imageUrl, httpHeaders);
    }
    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Ocp-Apim-Subscription-Key",apiKey);
        return httpHeaders;
    }
    private URI getApiUrl(){
        URI uri = null;
        try{
            URIBuilder uriBuilder = new URIBuilder(FACE_BASE_API_URL);
            uriBuilder.addParameter("returnFaceId", "true");
            uriBuilder.addParameter("returnFaceLandmarks", "false");
            uriBuilder.addParameter("returnFaceAttributes", "age,gender,smile,facialHair,glasses,emotion," +
                    "hair,accessories,blur,exposure,noise");
            uriBuilder.addParameter("recognitionModel", "recognition_01");
            uriBuilder.addParameter("returnRecognitionModel", "false");
            uriBuilder.addParameter("detectionModel", "detection_01");
          uri =  uriBuilder.build();
        } catch (URISyntaxException e){
            e.printStackTrace();
        }
        return uri;
    }

}
