package ecommerce.http.entities;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ecommerce.http.enums.UserRole;
import jakarta.persistence.CascadeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "clients")
public class Client implements UserDetails {
    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String privateQuestion;

    @Column(nullable = false)
    private String privateAnswer;

    @Column(nullable = false, updatable = true)
    private UserRole role = UserRole.USER;

    @OneToOne(cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private ClientWallet wallet;

    @Transient
    private String newPassword;

    @Transient
    private String newPrivateQuestion;

    @Transient
    private String newPrivateAnswer;

    public Client() {}

    // Register DTO
    public Client(String name, String email, String password, String privateQuestion,
            String privateAnswer, String newPassword, String newPrivateQuestion,
            String newPrivateAnswer) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.privateQuestion = privateQuestion;
        this.privateAnswer = privateAnswer;
        this.newPassword = newPassword;
        this.newPrivateQuestion = newPrivateQuestion;
        this.newPrivateAnswer = newPrivateAnswer;
    }

    // Update DTO
    public Client(String name, String email, String password, String privateQuestion,
            String privateAnswer) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.privateQuestion = privateQuestion;
        this.privateAnswer = privateAnswer;
    }

    // Forgot password DTO
    public Client(String email, String password, String privateAnswer) {
        this.email = email;
        this.password = password;
        this.privateAnswer = privateAnswer;
    }

    // Auth Client DTO
    public Client(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Client(UUID id, String name, String email, UserRole role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER"));
        }

        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @JsonIgnore
    public String getPrivateQuestion() {
        return privateQuestion;
    }

    public void setPrivateQuestion(String privateQuestion) {
        this.privateQuestion = privateQuestion;
    }

    @JsonIgnore
    public String getPrivateAnswer() {
        return privateAnswer;
    }

    public void setPrivateAnswer(String privateAnswer) {
        this.privateAnswer = privateAnswer;
    }

    @JsonIgnore
    public String getNewPrivateQuestion() {
        return newPrivateQuestion;
    }

    public void setNewPrivateQuestion(String newPrivateQuestion) {
        this.newPrivateQuestion = newPrivateQuestion;
    }

    @JsonIgnore
    public String getNewPrivateAnswer() {
        return newPrivateAnswer;
    }

    public void setNewPrivateAnswer(String newPrivateAnswer) {
        this.newPrivateAnswer = newPrivateAnswer;
    }

    public ClientWallet getWallet() {
        return wallet;
    }

    public void setWallet(ClientWallet wallet) {
        this.wallet = wallet;
    }
}
