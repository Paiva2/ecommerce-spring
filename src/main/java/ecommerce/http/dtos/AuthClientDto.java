package ecommerce.http.dtos;

import ecommerce.http.entities.Client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AuthClientDto {
    @NotBlank(message = "email can't be empty")
    @NotNull(message = "email can't be null")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "password can't be empty")
    @NotNull(message = "password can't be null")
    @Size(min = 6, max = 9999, message = "password must have at least 6 characters")
    private String password;

    public Client toClient() {
        return new Client(email, password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
