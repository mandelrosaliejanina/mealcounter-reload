package at.ac.htl.leonding.milfare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpPost;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.HttpClients;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TagLoaderOld {

    public  void sendData(String requestString, String id) throws IOException {
        String jsonString = "";

        //set card
        NfcCardDto nfcCardDto = new NfcCardDto();
        //Date input = new Date(nfcCardDto.registerDateTime);
        //long date = input.getTime();

        //localdate in long
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        long date = zdt.toInstant().toEpochMilli();


        nfcCardDto.nfcId = id;
        nfcCardDto.registerDateTime = date;



        //convert java obejct to json string
        //https://www.tabnine.com/blog/how-to-convert-a-java-object-into-a-json-string/
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonString = mapper.writeValueAsString(nfcCardDto);
            System.out.println("ResultingJSONstring = " + jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }



        //stackoverflow
        //https://stackoverflow.com/questions/12059278/how-to-post-json-request-using-apache-httpclient
        StringEntity requestEntity = new StringEntity(jsonString);
        HttpPost postMethod = new HttpPost(requestString);
        postMethod.setEntity(requestEntity);
        postMethod.setHeader("Accept", "application/json");
        postMethod.setHeader("Content-type", "application/json");




        //https://www.baeldung.com/httpclient-post-http-request
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(postMethod);
        if (response.getCode() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response);
        }
        client.close();


/*        //old
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestString))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(requestEntity)))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
        System.out.println("send");*/
    }

    public void checkData(String uri) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }

}
