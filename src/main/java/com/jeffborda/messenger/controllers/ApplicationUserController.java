package com.jeffborda.messenger.controllers;

import com.jeffborda.messenger.models.ApplicationUser;
import com.jeffborda.messenger.models.ApplicationUserRepository;
import com.jeffborda.messenger.models.BlogPost;
import com.jeffborda.messenger.models.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.ManyToOne;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class ApplicationUserController {

    @Autowired
    ApplicationUserRepository applicationUserRepo;

    @Autowired
    BlogPostRepository blogPostRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public RedirectView createNewUser(String username, String password) {
        ApplicationUser u = new ApplicationUser(username, passwordEncoder.encode(password));
        applicationUserRepo.save(u);

        // To automatically log user in when registering
        Authentication authentication = new UsernamePasswordAuthenticationToken(u, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new RedirectView("/");
    }

    @GetMapping("/login")
    public String getLoginPage(Principal p, Model m) {
        if(p != null) {
            // For nav fragment
            m.addAttribute("username", p.getName());
        }
        return "login";
    }

    @GetMapping("/my-profile")
    public String getMyProfile(Principal p, Model m) {
        if(p != null) {
            // For nav fragment
            m.addAttribute("username", p.getName());
        }
        // Create an AppUser to send to the front end
        ApplicationUser user = applicationUserRepo.findUserByUsername(p.getName());
        m.addAttribute("user", user);

        return "user-profile";
    }

    @PostMapping("/blogpost")
    public RedirectView addBlogPost(Principal p, String post_body) {
        ApplicationUser user = applicationUserRepo.findUserByUsername(p.getName());
        BlogPost blogPost = new BlogPost(post_body, user);
        blogPostRepo.save(blogPost);
        return new RedirectView("/my-profile");
    }

    @GetMapping("/users/all")
    public String getUserIndex(Principal p, Model m) {
        if(p != null) {
            // For nav fragment
            m.addAttribute("username", p.getName());
        }
        ApplicationUser loggedInUser = applicationUserRepo.findUserByUsername(p.getName());
        List<ApplicationUser> users = applicationUserRepo.findAll();
        users.remove(loggedInUser);
        m.addAttribute("users", users);
        return "user-index";
    }

    // This is the route for one user viewing another user's page.
    //  Does this really need to be a separate page??
    @GetMapping("/users/{id}")
    public String getOtherUserProfile(@PathVariable long id, Principal p, Model m) {
        if(p != null) {
            // For nav fragment
            m.addAttribute("username", p.getName());
        }
        // Create an AppUser to send to the front end
        ApplicationUser viewedUser = applicationUserRepo.findUserById(id);
        m.addAttribute("viewedUser", viewedUser);

        return "view-user";
    }

    // This is called when the 'follow' button is pressed
    @PostMapping("/users/{id}")
    public RedirectView setFollowingUser(@PathVariable long id, Principal p, Model m) {
        if(p != null) {
            ApplicationUser loggedInUser = applicationUserRepo.findUserByUsername(p.getName());
            ApplicationUser viewedUser = applicationUserRepo.findUserById(id);
            // Add the viewedUser to the Set of following users
            loggedInUser.addUserToFollow(viewedUser);
            // Then update user in the database
            applicationUserRepo.save(loggedInUser);
            m.addAttribute("loggedInUser", loggedInUser);
            m.addAttribute("viewedUser", viewedUser);
        }
        return new RedirectView("/users/{id}");
    }
    // Or should this be something like ("users/{id}/feed") ?
    @GetMapping("/my-profile/feed")
    public String getUserFeed(Principal p, Model m) {
        if(p != null) {
            ApplicationUser loggedInUser = applicationUserRepo.findUserByUsername(p.getName());
            // Gets the logged in user's Set of user's he/she is following
            m.addAttribute("loggedInUser", loggedInUser);
            // For nav fragment
            m.addAttribute("username", p.getName());
        }
        return "user-feed";
    }
}
