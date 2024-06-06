package com.passGuardian.passGuardian.controller;

import com.passGuardian.passGuardian.dtos.LoginDto;
import com.passGuardian.passGuardian.dtos.ResetPasswordDto;
import com.passGuardian.passGuardian.dtos.UpdatePasswordDto;
import com.passGuardian.passGuardian.dtos.UsersRecordDto;
import com.passGuardian.passGuardian.models.UsersModel;
import com.passGuardian.passGuardian.repository.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.passGuardian.passGuardian.service.PasswordResetService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/create")
    public ResponseEntity<Object> saveUsers(@RequestBody @Valid UsersRecordDto usersRecordDto){
        boolean existsUser = usersRepository.findByUsername(usersRecordDto.username()).isPresent();
        if (existsUser){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        var usersModel = new UsersModel();
        BeanUtils.copyProperties(usersRecordDto, usersModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(usersRepository.save(usersModel));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto){
        var user = usersRepository.findByUsername(loginDto.username());
        if (user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        var encoder = new BCryptPasswordEncoder();
        boolean isPasswordMatch = encoder.matches(loginDto.password(), user.get().getPassword());
        if (!isPasswordMatch){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Login successful");
    }

    @GetMapping
    public ResponseEntity<List<UsersModel>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(usersRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable Long id) {
        Optional<UsersModel> usersModel = usersRepository.findById(id);
        if (usersModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(usersModel);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Object> getOneUserByUsername(@PathVariable String username) {
        var usersModel = usersRepository.findByUsername(username);
        System.out.println("usersModel" + usersModel);
        if (usersModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(usersModel);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UsersRecordDto usersRecordDto) {
        Optional<UsersModel> usersModel = usersRepository.findById(id);
        if (usersModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else {
            var user = usersModel.get();
            BeanUtils.copyProperties(usersRecordDto, user);
            return ResponseEntity.status(HttpStatus.OK).body(usersRepository.save(user));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        Optional<UsersModel> usersModel = usersRepository.findById(id);
        if (usersModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else {
            usersRepository.delete(usersModel.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
        }
    }

    @PostMapping("/reset-password")
    public  ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPassword) {
        try{
            passwordResetService.sendPasswordResetToken(resetPassword.username());
            return ResponseEntity.status(HttpStatus.OK).body("Token sent to email");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error sending email");
        }
    }

    @GetMapping("/validate-token/{token}")
    public ResponseEntity<String> validateToken(@PathVariable String token) {
        if (passwordResetService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.OK).body("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is invalid");
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) {
        try {
            passwordResetService.resetPassword(updatePasswordDto.token(), updatePasswordDto.password());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Password updated");
    }

}
