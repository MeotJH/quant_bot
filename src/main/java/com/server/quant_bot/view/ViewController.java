package com.server.quant_bot.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {

    @GetMapping("/")
    public ModelAndView returnMainPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }

    @GetMapping("/view/{page}")
    public String returnOtherPage(@PathVariable String page){
        return getPage(page);
    }

    @GetMapping("/auth/{page}")
    public String returnAuthPage(@PathVariable String page){
        return getPage(page);
    }

    private String getPage(String page){
        if( page == null || page == ""){
            page = "index";
        }
        return page + ".html";
    }


}
