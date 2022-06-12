package com.github.wenqiglantz.service.orderservice.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wenqiglantz.service.orderservice.data.CustomerVO;
import com.github.wenqiglantz.service.orderservice.data.event.CustomerWasCreated;
import com.github.wenqiglantz.service.orderservice.data.event.CustomerWasDeleted;
import com.github.wenqiglantz.service.orderservice.data.event.CustomerWasUpdated;
import com.github.wenqiglantz.service.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "subscriber", description = "Operations pertaining to order service event consumption")
public class SubscriberController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OrderService orderService;

    @Operation(summary = "Consume customer CRUD Event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully consumed customer CRUD event"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Unexpected system exception"),
            @ApiResponse(responseCode = "502", description = "An error has occurred with an upstream service")
    })

    @KafkaListener(topics = "customers-topic", groupId = "customers-group")
    @PostMapping(path = "/customer-operations")
    public void consumeCustomerCrudEvent(@RequestBody(required = false) CustomerVO event) {
        log.info("Received Message: " + event);
        try {
            String stringValue = OBJECT_MAPPER.writeValueAsString(event);
            log.info("received event {}", OBJECT_MAPPER.writeValueAsString(event));
            if (stringValue.contains("CREATED")) {
                orderService.consumeCustomerWasCreated(
                        OBJECT_MAPPER.convertValue(event, CustomerWasCreated.class));
            } else if (stringValue.contains("UPDATED")) {
                orderService.consumeCustomerWasUpdated(
                        OBJECT_MAPPER.convertValue(event, CustomerWasUpdated.class));
            } else if (stringValue.contains("DELETED")) {
                orderService.consumeCustomerWasDeleted(
                        OBJECT_MAPPER.convertValue(event, CustomerWasDeleted.class));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
