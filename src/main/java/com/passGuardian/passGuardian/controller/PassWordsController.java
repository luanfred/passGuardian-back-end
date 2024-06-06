package com.passGuardian.passGuardian.controller;

import com.passGuardian.passGuardian.dtos.PassWordResponseDto;
import com.passGuardian.passGuardian.dtos.PassWordsDto;
import com.passGuardian.passGuardian.models.PassWordsModel;
import com.passGuardian.passGuardian.models.UsersModel;
import com.passGuardian.passGuardian.repository.PassWordsRepository;
import com.passGuardian.passGuardian.repository.UsersRepository;
import com.passGuardian.passGuardian.util.AESUtil;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/passWords")
public class PassWordsController {

    @Autowired
    private PassWordsRepository passWordsRepository;
    @Autowired
    private UsersRepository usersRepository;

    @PostMapping
    public ResponseEntity<Object> savePassWords(@RequestBody @Valid PassWordsDto passWordsDto) throws Exception {
        var user = usersRepository.findById(passWordsDto.user_id());
        if (user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        var passWordsModel = new PassWordsModel();
        BeanUtils.copyProperties(passWordsDto, passWordsModel);
        passWordsModel.setUserId(user.get());
        passWordsModel.setPassword(AESUtil.encrypt(passWordsModel.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(passWordsRepository.save(passWordsModel));
    }

    @GetMapping
    public ResponseEntity<Object> getAllPassWords() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersModel user = (UsersModel) authentication.getPrincipal();
        var passWordsModel = passWordsRepository.findByUserId(user);
        if (passWordsModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Password not found");
        }
        for (var passWord : passWordsModel) {
            passWord.setPassword(AESUtil.decrypt(passWord.getPassword()));
        }
        List<PassWordResponseDto> passWordsModelResponse = (List<PassWordResponseDto>) passWordsModel.stream().map(passWord -> new PassWordResponseDto(
                passWord.getPassword_id(),
                passWord.getTitle(),
                passWord.getUrl(),
                passWord.getEmail(),
                passWord.getPassword(),
                passWord.getFavorite()
        )).toList();

        return ResponseEntity.status(HttpStatus.OK).body(passWordsModelResponse);
    }

    @GetMapping("/favorites")
    public ResponseEntity<Object> getAllPassWordsFavorites() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersModel user = (UsersModel) authentication.getPrincipal();
        var passWordsModel = passWordsRepository.findByUserIdAndFavorite(user, "S");
        if (passWordsModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Password not found");
        }
        for (var passWord : passWordsModel) {
            passWord.setPassword(AESUtil.decrypt(passWord.getPassword()));
        }
        List<PassWordResponseDto> passWordsModelResponse = (List<PassWordResponseDto>) passWordsModel.stream().map(passWord -> new PassWordResponseDto(
                passWord.getPassword_id(),
                passWord.getTitle(),
                passWord.getUrl(),
                passWord.getEmail(),
                passWord.getPassword(),
                passWord.getFavorite()
        )).toList();

        return ResponseEntity.status(HttpStatus.OK).body(passWordsModelResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOnePassWord(@PathVariable Long id) throws Exception {
        var passWordsModel = passWordsRepository.findById(id);
        if (passWordsModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Password not found");
        }
        passWordsModel.get().setPassword(AESUtil.decrypt(passWordsModel.get().getPassword()));
        PassWordResponseDto passWordResponseDto = new PassWordResponseDto(
                passWordsModel.get().getPassword_id(),
                passWordsModel.get().getTitle(),
                passWordsModel.get().getUrl(),
                passWordsModel.get().getEmail(),
                passWordsModel.get().getPassword(),
                passWordsModel.get().getFavorite()
        );
        return ResponseEntity.status(HttpStatus.OK).body(passWordResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePassWord(@PathVariable Long id, @RequestBody PassWordsDto passWordsDto) throws Exception {
        var passWordsModel = passWordsRepository.findById(id);
        if (passWordsModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Password not found");
        } else {
            var passWord = passWordsModel.get();
            BeanUtils.copyProperties(passWordsDto, passWord);
            passWord.setPassword(AESUtil.encrypt(passWord.getPassword()));
            return ResponseEntity.status(HttpStatus.OK).body(passWordsRepository.save(passWord));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePassWord(@PathVariable Long id) {
        var passWordsModel = passWordsRepository.findById(id);
        if (passWordsModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Password not found");
        } else {
            passWordsRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Password deleted");
        }
    }
}
