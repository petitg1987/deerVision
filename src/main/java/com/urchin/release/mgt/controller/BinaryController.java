package com.urchin.release.mgt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/binaries")
public class BinaryController {

    @ModelAttribute
    public void pageIdAttribute(Model model) {
        model.addAttribute("pageId", "binaries");
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String listBooks(Model model) {
        return "binaries.html";
    }

}
