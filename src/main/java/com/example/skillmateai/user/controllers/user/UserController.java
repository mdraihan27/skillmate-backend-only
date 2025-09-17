package com.example.skillmateai.user.controllers.user;

import com.example.skillmateai.user.dtos.ResetPasswordRequest;
import com.example.skillmateai.user.dtos.UpdateNameRequest;
import com.example.skillmateai.user.dtos.UpdateUserPreferencesRequest;
import com.example.skillmateai.user.entities.UserEntity;
import com.example.skillmateai.user.entities.UserVerificationEntity;
import com.example.skillmateai.user.repositories.UserRepository;
import com.example.skillmateai.user.repositories.UserVerificationRepository;
import com.example.skillmateai.user.services.UserService;
import com.example.skillmateai.user.utilities.CreateResponseUtil;
import com.example.skillmateai.user.utilities.GetAuthenticatedUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private CreateResponseUtil createResponseUtil;

    @Autowired
    private GetAuthenticatedUserUtil getAuthenticatedUserUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserVerificationRepository userVerificationRepository;
    
    @Autowired
    private UserService userService;



    @GetMapping("info")
    public ResponseEntity<Object> getLoggedInUserInfo() {
        try {
            UserEntity authenticatedUser = getAuthenticatedUserUtil.getAuthenticatedUser();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(createResponseUtil.createResponseBody(true, "User found", "userInfo", createResponseUtil.createUserInfoMap(authenticatedUser)));

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while fetching user"));
        }
    }

    @GetMapping("profile")
    public ResponseEntity<Object> getLoggedInUserProfile() {
        try {
            UserEntity authenticatedUser = getAuthenticatedUserUtil.getAuthenticatedUser();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(createResponseUtil.createResponseBody(true, "User profile found", "userProfile", createResponseUtil.createExtendedUserInfoMap(authenticatedUser)));

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while fetching user profile"));
        }
    }

    @Transactional
    @DeleteMapping("delete")
    public ResponseEntity<Object> deleteLoggedInUser() {
        try {
            UserEntity authenticatedUser = getAuthenticatedUserUtil.getAuthenticatedUser();

            String email =authenticatedUser.getEmail();

//            userVerificationRepository.deleteByUserEmail(authenticatedUser.getEmail());
            userRepository.deleteById(authenticatedUser.getId());
            UserEntity userByEmail = userRepository.findByEmail(email).orElse(null);

//            UserVerificationEntity userVerificationEntity = userVerificationRepository.findByUserEmail(authenticatedUser.getEmail()).orElse(null);

            if (userByEmail == null) {

                return ResponseEntity.status(HttpStatus.OK)
                        .body(createResponseUtil.createResponseBody(true, "User successfully deleted"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(createResponseUtil.createResponseBody(false, "User deletion has concluded partial or unsuccessful"));
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while deleting user"));
        }
    }

    @PatchMapping("update/name")
    public ResponseEntity<Object> updateName(@RequestBody UpdateNameRequest request) {
        try {
            
            if(request == null){
                return ResponseEntity.badRequest()
                        .body(createResponseUtil.createResponseBody(false, "Request body is required"));
            }
            
            return userService.updateName(request.getNewFirstName(), request.getNewLastName());

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while updating name"));
        }
    }

    @PatchMapping("update/password")
    public ResponseEntity<Object> resetPasswordWithPreviousPassword(@RequestBody ResetPasswordRequest request) {
        try {
            
            if(request == null){
                return ResponseEntity.badRequest()
                        .body(createResponseUtil.createResponseBody(false, "Request body is required"));
            }
            
            return userService.resetPasswordWithPreviousPassword(request.getOldPassword(), request.getNewPassword());

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while resetting password"));
        }
    }



}
