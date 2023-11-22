package studio.deervision.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    //curl -X GET http://localhost:5000/api/test/deploy
    //curl -X GET https://backend.deervision.studio/api/test/deploy
    @GetMapping(value = "/deploy")
    public ResponseEntity<String> deployTest() {
        return ResponseEntity.ok("deployed");
    }

}
