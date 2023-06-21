package com.app.kokonut.common.realcomponent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.cglib.core.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-06-12
 * Time :
 * Remark : Whois 호출 유틸리티
 */
@Slf4j
@Service
@SuppressWarnings("unchecked")
public class WhoisUtil implements Constants {

    @Value("${kokonut.whois.secretKey}")
    public String whoisKey;

    public String whoisAPI(String publicIP) {

        String apiUri = "https://apis.data.go.kr/B551505/whois/ipas_country_code";
        List<NameValuePair> nameValuePairs= new ArrayList<>();

        nameValuePairs.add(new BasicNameValuePair("query",publicIP));
        nameValuePairs.add(new BasicNameValuePair("serviceKey",whoisKey));
        nameValuePairs.add(new BasicNameValuePair("answer","JSON"));

        HttpGet httpGet = new HttpGet(apiUri);

        URI uri;
        ObjectMapper objectMapper;

        try {
            uri = new URIBuilder(httpGet.getURI())
                    .addParameters(nameValuePairs)
                    .build();

            httpGet.setURI(uri);

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            CloseableHttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();

            if(statusCode == HttpStatus.OK.value()) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                objectMapper = new ObjectMapper();

                Map<String, Object> map = objectMapper.readValue(json, new TypeReference<>() {});
                Map<String, Object> responseMap = (Map<String, Object>) map.get("response");
                Map<String, String> whois = (Map<String, String>) responseMap.get("whois");

//                log.info("response : "+ map.toString());
//                log.info("countryCode :"+whois.get("countryCode"));

                return whois.get("countryCode");
            }
        } catch (URISyntaxException | IOException e) {
            log.error("해외차단 서비스 예외발생");
            log.error("예외처리 : "+e);
			log.error("예외처리 메세지 : "+e.getMessage());
        }

        return null;
    }

}
