package com.urchin.release.mgt.controller;

import com.urchin.release.mgt.model.BinaryType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binary")
public class BinaryRestController {

    @GetMapping(value="/version")
    public String getVersion(){
        return "1.0.0"; //TODO impl
    }

    @GetMapping(value="/")
    public String getBinary(@RequestParam(name = "type") BinaryType binaryType){
        return "1.0.0"; //TODO impl
    }
}
