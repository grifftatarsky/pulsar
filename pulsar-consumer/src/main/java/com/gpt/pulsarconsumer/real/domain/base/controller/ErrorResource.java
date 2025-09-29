package com.gpt.pulsarconsumer.real.domain.base.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorResource implements ErrorController
{

    private static final String PATH = "error";

    @RequestMapping(PATH)
    public void handleError(HttpServletRequest request) throws Throwable
    {
        if (request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") != null)
        {
            throw (Throwable) request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }
    }

    public String getErrorPath()
    {
        return PATH;
    }
}