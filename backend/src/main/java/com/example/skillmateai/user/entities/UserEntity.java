package com.example.skillmateai.user.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "user")
@Getter
@Setter
@AllArgsConstructor
public class UserEntity {

    public UserEntity() {
        this.enrolledCoursePaths = new ArrayList<>();
        this.createdCoursePaths = new ArrayList<>();
    }

    @Id
    private String id;

    @NonNull
    @Indexed(unique = true)
    private String email;

    @NonNull
    private String password;

    @NonNull
    private String firstName;

    private String lastName;

    private boolean isVerified;
    private boolean isAccountEnabled;
    private Boolean isBlocked;
    private String profilePictureUrl;
    private String firebaseUid;
    private long createdAt;
    private long updatedAt;
    private ArrayList<String> roles;
    private ArrayList<String> enrolledCoursePaths; // Course path IDs user has enrolled in
    private ArrayList<String> createdCoursePaths; // Course path IDs user has created


}