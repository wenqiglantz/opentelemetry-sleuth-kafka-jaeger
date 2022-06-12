package com.github.wenqiglantz.service.customerservicebff.service;

import com.github.wenqiglantz.service.customerservicebff.data.CustomerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerService {

    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${services.customer-service.url}")
    private String apiUrl;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public CustomerVO saveCustomer(CustomerVO customerVO) throws Exception {
        log.info("saveCustomer: apiUrl: {} customerVO: {}", apiUrl, customerVO);
        ResponseEntity<CustomerVO> responseEntity =
                restTemplate(restTemplateBuilder).postForEntity(apiUrl, customerVO, CustomerVO.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            return responseEntity.getBody();
        }
        throw new ResponseStatusException(responseEntity.getStatusCode(), "Customer creation failed");
    }

    public CustomerVO getCustomer(String customerId) {
        log.info("getCustomer: apiUrl: {} ", apiUrl);
        ResponseEntity<CustomerVO> responseEntity =
                restTemplate(restTemplateBuilder).getForEntity(apiUrl + "/" + customerId, CustomerVO.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            return responseEntity.getBody();
        }
        throw new ResponseStatusException(responseEntity.getStatusCode(), "Customer retrieval failed");
    }

    public List<CustomerVO> getCustomers() {
        log.info("getCustomers: apiUrl: {} ", apiUrl);
        ResponseEntity<List> responseEntity =
                restTemplate(restTemplateBuilder).getForEntity(apiUrl, List.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            return responseEntity.getBody();
        }
        throw new ResponseStatusException(responseEntity.getStatusCode(), "Customer retrieval failed");
    }

    public void updateCustomer(String customerId, CustomerVO customerVO) throws Exception {
        log.info("updateCustomer: apiUrl: {} customerVO {} ", apiUrl, customerVO);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<CustomerVO> request = new HttpEntity<>(customerVO, headers);
        restTemplate(restTemplateBuilder)
                .exchange(apiUrl + "/" + customerId, HttpMethod.PUT, request, Void.class);
    }

    public void deleteCustomer(String customerId) throws Exception {
        log.info("deleteCustomer: apiUrl: {} theaterId {} ", apiUrl, customerId);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> request = new HttpEntity<>(customerId, headers);
        restTemplate(restTemplateBuilder)
                .exchange(apiUrl + "/" + customerId, HttpMethod.DELETE, request, Void.class);
    }
}
