package ecommerce.http.dtos;

import ecommerce.http.entities.Client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterClientDto {
    @NotBlank(message = "email can't be empty")
    @NotNull(message = "email can't be null")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "name can't be empty")
    @NotNull(message = "name can't be null")
    @Size(min = 3, max = 100, message = "Must be of 3 - 100 characters")
    private String name;

    @NotBlank(message = "password can't be empty")
    @NotNull(message = "password can't be null")
    @Size(min = 6, max = 9999, message = "password must have at least 6 characters")
    private String password;

    @NotBlank(message = "privateQuestion can't be empty")
    @NotNull(message = "privateQuestion can't be null")
    private String privateQuestion;

    @NotBlank(message = "privateAnswer can't be empty")
    @NotNull(message = "privateAnswer can't be null")
    private String privateAnswer;

    public Client toClient() {
        return new Client(name, email, password, privateQuestion, privateAnswer);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateQuestion() {
        return privateQuestion;
    }

    public void setPrivateQuestion(String privateQuestion) {
        this.privateQuestion = privateQuestion;
    }

    public String getPrivateAnswer() {
        return privateAnswer;
    }

    public void setPrivateAnswer(String privateAnswer) {
        this.privateAnswer = privateAnswer;
    }
}
