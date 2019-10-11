package com.jeffborda.messenger.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String username;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<BlogPost> blogPosts;

    @ManyToMany
    @JoinTable(
            name="following_users",
            joinColumns ={ @JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="following_user_id")}
    )
    private Set<ApplicationUser> following;

    @ManyToMany(mappedBy = "following")
    private Set<ApplicationUser> followedBy;

    public ApplicationUser() {}

    public ApplicationUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return this.id;
    }

    public List<BlogPost> getBlogPosts() {
        return this.blogPosts;
    }

    public Set<ApplicationUser> getFollowing() {
        return this.following;
    }

    public void addUserToFollow(ApplicationUser u) {

        following.add(u);
    }

    public void removeUserToFollow(ApplicationUser u) {

        following.remove(u);
    }

    //Implement these automatically, then change return statements
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    // Next four methods are how to determine if user is allowed to login
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
