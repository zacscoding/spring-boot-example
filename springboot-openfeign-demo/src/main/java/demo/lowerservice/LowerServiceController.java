package demo.lowerservice;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import demo.common.MessageRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LowerServiceController {

    @PostMapping("/lowercase/message")
    public String convertLowerCaseMessage(@RequestBody MessageRequest request) {
        logger.info("[LowerService] message : {}", request.getMessage());
        return StringUtils.hasText(request.getMessage()) ? request.getMessage().toLowerCase() : "null";
    }

    @GetMapping("/alive")
    public ResponseEntity<Boolean> isAlive() {
        logger.info("Check alive ==> true");
        return ResponseEntity.ok(true);
//        boolean isAlive = new Random().nextInt(10) == 0;
//        logger.info("Check alive ==> {}", isAlive);
//
//        if (isAlive) {
//            return ResponseEntity.ok(true);
//        }
//
//        return ResponseEntity.notFound().build();
    }
}
