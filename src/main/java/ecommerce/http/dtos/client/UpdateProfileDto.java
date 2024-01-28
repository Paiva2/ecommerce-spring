package ecommerce.http.dtos.client;

import ecommerce.http.entities.Client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UpdateProfileDto {
    @Email(message = "Invalid email")
    private String email;

    @Size(min = 3, max = 100, message = "Must be of 3 - 100 characters")
    private String name;

    @Size(min = 6, max = 9999, message = "password must have at least 6 characters")
    private String password;

    @Size(min = 6, max = 9999, message = "newPassword must have at least 6 characters")
    private String newPassword;

    private String privateQuestion;

    private String newPrivateQuestion;

    private String privateAnswer;

    private String newPrivateAnswer;

    public Client toClient() {
        return new Client(name, email, password, privateQuestion, privateAnswer, newPassword,
                newPrivateQuestion,
                newPrivateAnswer);
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPrivateQuestion() {
        return newPrivateQuestion;
    }

    public void setNewPrivateQuestion(String newPrivateQuestion) {
        this.newPrivateQuestion = newPrivateQuestion;
    }

    public String getNewPrivateAnswer() {
        return newPrivateAnswer;
    }

    public void setNewPrivateAnswer(String newPrivateAnswer) {
        this.newPrivateAnswer = newPrivateAnswer;
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
