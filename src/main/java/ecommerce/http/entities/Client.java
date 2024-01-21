package ecommerce.http.entities;

import java.util.UUID;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client {
    @SequenceGenerator(name = "client_sequence", sequenceName = "client_sequence",
            allocationSize = 1)

    @GeneratedValue(generator = "uuid2")
    @Id
    private UUID id;

    @Column(unique = true)
    private String email;

    @Nonnull
    private String name;

    @Nonnull
    private String password;

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

    public String getPassword() {
        return this.password;
    }

}
