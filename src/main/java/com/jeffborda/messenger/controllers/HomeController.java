package com.jeffborda.messenger.controllers;

import com.jeffborda.messenger.models.ApplicationUser;
import com.jeffborda.messenger.models.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    ApplicationUserRepository applicationUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String getHome(Principal p, Model m) {
        if(p != null) {
            m.addAttribute("username", p.getName());
        }
        return "home";
    }

    @GetMapping("/signup")
    public RedirectView getSignup() {
        return new RedirectView("/");
    }

    @PostMapping("/signup")
    public RedirectView createNewUser(String username, String password) {
        ApplicationUser newUser = new ApplicationUser(username, passwordEncoder.encode(password));
        // This is a more efficient way to check if the username is already taken, use SQL:
        ApplicationUser usernameUniqueIfNull = applicationUserRepo.findUserByUsername(username);
        if(usernameUniqueIfNull == null) {
            // Save the new username
            applicationUserRepo.save(newUser);
            // Automatically log user in after registering:
            Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        else {
            return new RedirectView("/login/username-taken");
        }
        // User sent to their profile page after registering for the first time
        return new RedirectView("/my-profile");
    }

    @GetMapping("/login/username-taken")
    public String getUsernameTaken(Principal p, Model m) {
        if(p != null) {
            // For nav fragment
            m.addAttribute("username", p.getName());
        }
        return "login-username-taken";
    }

    @GetMapping("/login")
    public String getLoginPage(Principal p, Model m) {
        if(p != null) {
            // For nav fragment
            m.addAttribute("username", p.getName());
        }
        return "login";
    }

}
