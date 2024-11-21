package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
@RestController
public class MainController {

    @GetMapping("/")
    public String mainPage() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(name + " " + authentication);

        String role = authentication.getAuthorities().iterator().next().getAuthority();

        return "Main Page " + name + " " + role;
    }
}
