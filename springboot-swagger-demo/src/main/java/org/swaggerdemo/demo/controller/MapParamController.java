package org.swaggerdemo.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zacconding
 * @Date 2018-12-23
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
@RequestMapping("/api/map")
public class MapParamController {

    private ObjectMapper objectMapper;

    @Autowired
    public MapParamController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // NOT WORKING!!
    @PostMapping
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "requestBody",
            dataType = "Person",
            examples = @Example(
                @ExampleProperty(
                    mediaType = "application/json",
                    value = "{ \"name\" : \"hivava\", \"age\" : 10 }"
                )
            )
        )
    })
    public ResponseEntity postMapping(@RequestBody Map<String, Object> params) {
        try {
            log.info("Request post mapping : {}", params);
            return ResponseEntity.ok(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
