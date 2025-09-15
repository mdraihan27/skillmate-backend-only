package com.example.skillmateai.user.services;


import com.example.skillmateai.user.entities.UserEntity;
import com.example.skillmateai.user.repositories.UserRepository;
import com.example.skillmateai.user.utilities.CreateResponseUtil;
import com.example.skillmateai.user.utilities.GenerateAndValidateStringUtil;
import com.example.skillmateai.user.utilities.GetAuthenticatedUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;


@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserVerificationService userVerificationService;

    @Autowired
    private GetAuthenticatedUserUtil getAuthenticatedUserUtil;



    @Autowired
    private CreateResponseUtil createResponseUtil;

    public ResponseEntity<UserEntity> findUser(String userinfo, String infoType) throws Exception {
        try{
            Optional<UserEntity> user;

            if(infoType.equals("email")){
                user = userRepository.findByEmail(userinfo);
            }else if(infoType.equals("id")){
                user = userRepository.findById(userinfo);
            }else{
                log.error("Invalid user info type provided: {}", infoType);
                throw new IllegalArgumentException("User info type is not valid");
            }

            if(user.isPresent()){
                return ResponseEntity.ok(user.get());
            }else{
                log.debug("User not found with {} = {}", infoType, userinfo);
                return ResponseEntity.notFound().build();
            }
        }catch(IllegalArgumentException e){
            log.error("Invalid argument in findUser: {}", e.getMessage());
            throw e;
        }catch(Exception e){
            log.error("Unexpected error occurred while finding user with {} = {}", infoType, userinfo, e);
            throw new RuntimeException("Failed to find user", e);
        }
    }

    @Transactional
    public UserEntity createUser(UserEntity userEntity, Boolean isVerified) throws Exception{
        try{
            log.info("Creating new user with email: {}", userEntity.getEmail());

            userEntity.setId(GenerateAndValidateStringUtil.generateUniqueString());
            userEntity.setAccountEnabled(true);
            userEntity.setVerified(isVerified);
            userEntity.setIsBlocked(false);
            userVerificationService.createUserVerificationEntity(userEntity.getEmail());
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setRoles(new ArrayList<>(Arrays.asList("USER")));
            userEntity.setCreatedAt(System.currentTimeMillis());
            userEntity.setUpdatedAt(System.currentTimeMillis());
            userEntity.setEnrolledCoursePaths(new ArrayList<>());
            userEntity.setCreatedCoursePaths(new ArrayList<>());


            UserEntity createdUser = userRepository.save(userEntity);
            log.info("Successfully created user with email: {}", userEntity.getEmail());
            
            // Create default user preferences
            try {
                log.info("Successfully created default preferences for user: {}", userEntity.getEmail());
            } catch (Exception e) {
                log.error("Failed to create default preferences for user: {}, but user creation was successful", userEntity.getEmail(), e);
                // Don't fail user creation if preferences creation fails
            }
            
            return createdUser;
        }catch(Exception e){
            log.error("Failed to create user with email: {}", userEntity.getEmail(), e);
            throw new RuntimeException("User creation failed", e);
        }
    }

    @Transactional
    public ResponseEntity<Object> resetPasswordWithPreviousPassword(String oldPassword, String newPassword) throws Exception {
        try{
            
            if(oldPassword == null || oldPassword.isEmpty()){
                log.warn("Password reset failed - old password is null or empty");
                return ResponseEntity.badRequest()
                        .body(createResponseUtil.createResponseBody(false, "Old password is required"));
            }
            
            if(newPassword == null || newPassword.isEmpty()){
                log.warn("Password reset failed - new password is null or empty");
                return ResponseEntity.badRequest()
                        .body(createResponseUtil.createResponseBody(false, "New password is required"));
            }
            
            UserEntity authenticatedUser = getAuthenticatedUserUtil.getAuthenticatedUser();
            log.info("Password reset attempt for user: {}", authenticatedUser.getEmail());

            if(!passwordEncoder.matches(oldPassword, authenticatedUser.getPassword())){
                log.warn("Password reset failed - old password mismatch for user: {}", authenticatedUser.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createResponseUtil.createResponseBody(false, "Previous password did not match"));

            }else if(newPassword.length() < 8 || newPassword.length() > 50){
                log.warn("Password reset failed - invalid password length for user: {}", authenticatedUser.getEmail());
                return ResponseEntity.badRequest()
                        .body(createResponseUtil.createResponseBody(false, "Password must be between 8 and 50 characters"));

            }else if(newPassword.equals(oldPassword)){
                log.warn("Password reset failed - new password same as old for user: {}", authenticatedUser.getEmail());
                return ResponseEntity.badRequest()
                        .body(createResponseUtil.createResponseBody(false, "New password cannot be same as your previous password"));
            }else{
                authenticatedUser.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(authenticatedUser);
                log.info("Password successfully reset for user: {}", authenticatedUser.getEmail());
                return ResponseEntity.ok().body(createResponseUtil.createResponseBody(true, "Password successfully changed"));
            }

        }catch (Exception e){
            log.error("Unexpected error occurred during password reset", e);
            throw new RuntimeException("Password reset failed", e);
        }
    }

    @Transactional
    public ResponseEntity<Object> updateName(String newFirstName, String newLastName) throws Exception {
        try{
            UserEntity authenticatedUser = getAuthenticatedUserUtil.getAuthenticatedUser();
            log.info("Name update attempt for user: {}", authenticatedUser.getEmail());

            if(newFirstName == null || newFirstName.trim().isEmpty()){
                log.warn("Name update failed - empty name provided for user: {}", authenticatedUser.getEmail());
                return ResponseEntity.badRequest()
                        .body(createResponseUtil.createResponseBody(false, "Name cannot be empty"));
            }else{
                String oldFirstName = authenticatedUser.getFirstName();
                String oldLastName = authenticatedUser.getLastName();

                authenticatedUser.setFirstName(newFirstName.trim());
                authenticatedUser.setLastName(newLastName.trim());
                userRepository.save(authenticatedUser);
                log.info("Name successfully updated for user: {} from '{}' to '{}'", 
                    authenticatedUser.getEmail(), oldFirstName, newFirstName.trim());
                return ResponseEntity.ok().body(createResponseUtil.createResponseBody(true, "Name successfully updated"));
            }

        }catch (Exception e){
            log.error("Unexpected error occurred during name update", e);
            throw new RuntimeException("Name update failed", e);
        }
    }



}
