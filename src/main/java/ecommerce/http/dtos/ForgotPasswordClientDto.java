package ecommerce.http.dtos;

import ecommerce.http.entities.Client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ForgotPasswordClientDto {
    @NotBlank(message = "email can't be empty")
    @NotNull(message = "email can't be null")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "newPassword can't be empty")
    @NotNull(message = "newPassword can't be null")
    @Size(min = 6, max = 9999, message = "newPassword must have at least 6 characters")
    private String newPassword;

    public Client toClient() {
        return new Client(email, newPassword);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
