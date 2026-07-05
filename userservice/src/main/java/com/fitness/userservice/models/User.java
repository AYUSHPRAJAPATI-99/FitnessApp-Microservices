package com.fitness.userservice.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="users")   // user db me ek table na name bhi use root user....
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private String id ;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false )
    private String email;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING) // isse 0,1(pos)  nahi exact USER,ADMIN store of DB me
    private UserRole role = UserRole.USER;
    @Enumerated(EnumType.STRING)
    private AuthProvider provider = AuthProvider.LOCAL;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
