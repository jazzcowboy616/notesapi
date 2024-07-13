//package org.speer.assessment.controllers;
//
//import com.fasterxml.jackson.annotation.JsonView;
//import org.speer.assessment.entities.User;
//import org.speer.assessment.exceptions.CredentialInvalidException;
//import org.speer.assessment.exceptions.UserExistingException;
//import org.speer.assessment.exceptions.UserNotFoundException;
//import org.speer.assessment.exceptions.UserPasswordNotConsistentException;
//import org.speer.assessment.repositories.UserRepository;
//import org.speer.assessment.responses.CommonResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.ExampleMatcher;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Base64;
//import java.util.stream.Collectors;
//
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    @Autowired
//    UserRepository repo;
//
//    @PostMapping("/signup")
//    public void signup(@RequestBody @Validated User user){
//        if(!user.getPassword().equals(user.getRepeatPassword()))
//            throw new UserPasswordNotConsistentException();
//
//        ExampleMatcher matcher = ExampleMatcher.matchingAny();
//        User exuser = new User();
//        exuser.setUsername(user.getUsername());
//        Example<User> example = Example.of(exuser, matcher);
//        long cnt = repo.count(example);
//        if(cnt > 0)
//            throw new UserExistingException(user.getUsername());
//
//        String enPass = encrypt(user.getPassword());
//        user.setPassword(enPass);
//        repo.save(user);
//    }
//
//    @PostMapping("/login")
//    @ResponseStatus(code = HttpStatus.OK)
//    public void login(@RequestBody User user){
//        ExampleMatcher matcher = ExampleMatcher.matchingAny()
//                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.exact());
//
//        Example<User> example = Example.of(user, matcher);
//        User userInDB = repo.findOne(example).orElseThrow(CredentialInvalidException::new);
//
//        if(!encrypt(user.getPassword()).equals(userInDB.getPassword()))
//            throw new CredentialInvalidException();
//    }
//
//    @GetMapping("{id}")
//    @JsonView(User.NameJsonView.class)
//    public User getById(@PathVariable Long id){
//        return repo.findById(id).get();
//    }
//
//    @ResponseBody
//    @ExceptionHandler
//    public ResponseEntity<CommonResponse> handle(RuntimeException ex) {
//        if(ex instanceof UserExistingException)
//            return new ResponseEntity<>(new CommonResponse(HttpStatus.CONFLICT.toString(), ex.getMessage()), HttpStatus.OK);
//        else if(ex instanceof CredentialInvalidException)
//            return new ResponseEntity<>(new CommonResponse(HttpStatus.UNAUTHORIZED.toString(), ex.getMessage()), HttpStatus.OK);
//        else if(ex instanceof UserNotFoundException)
//            return new ResponseEntity<>(new CommonResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage()), HttpStatus.OK);
//        else if(ex instanceof UserPasswordNotConsistentException)
//            return new ResponseEntity<>(new CommonResponse(HttpStatus.BAD_REQUEST.toString(), ex.getMessage()), HttpStatus.OK);
//        return new ResponseEntity<>(new CommonResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage()), HttpStatus.OK);
//    }
//
//    @ResponseBody
//    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
//    public ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        BindingResult bindingResult = ex.getBindingResult();
//        StringBuilder sb = new StringBuilder("Argument validation failed:");
//        sb.append(bindingResult.getFieldErrors().stream().map(m ->m.getField()+":"+m.getDefaultMessage()).collect(Collectors.joining(",")));
//        String msg = sb.toString();
//        return new ResponseEntity<>(new CommonResponse(HttpStatus.BAD_REQUEST.toString(), msg), HttpStatus.OK);
//    }
//
//    public String encrypt(String str){
//        if(str == null || str.isEmpty())
//            return str;
//        return Base64.getEncoder().encodeToString(str.getBytes());
//    }
//}
