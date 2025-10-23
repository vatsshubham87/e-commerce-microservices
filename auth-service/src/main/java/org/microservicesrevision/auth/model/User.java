package org.microservicesrevision.auth.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="users")
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private String name;
   private String password;
   private String email;
   @Column(name="phone_number")
   private String phoneNumber;
   private String country;
   private String state;

   // One user can have many roles
   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<UserRole> roles;

   @Column(name = "created_at", updatable = false)
   @CreatedDate
   private LocalDateTime createdAt;

   @Column(name = "updated_at")
   @LastModifiedDate
   private LocalDateTime updatedAt;

   @Column(name = "created_by", updatable = false)
   private String createdBy;

   @Column(name = "updated_by")
   private String updatedBy;

}
