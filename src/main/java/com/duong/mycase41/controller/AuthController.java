package com.duong.mycase41.controller;
import com.duong.mycase41.model.AppUser;
import com.duong.mycase41.model.DTO.JwtResponse;
import com.duong.mycase41.model.Teacher;
import com.duong.mycase41.service.appuser.IAppUserService;
import com.duong.mycase41.service.jwt.JwtService;
import com.duong.mycase41.service.teacher.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IAppUserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AppUser appUser) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(appUser.getName(), appUser.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AppUser currentUser = userService.findByName(appUser.getName());
        return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<String> admin () {
        return new ResponseEntity<>("Admin", HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<String> user () {
        return new ResponseEntity<>("User", HttpStatus.OK);
    }

    @PostMapping("/create-user")
    public ResponseEntity<AppUser> createUser(@ModelAttribute AppUser appUser) {
        return new ResponseEntity<>(userService.save(appUser), HttpStatus.CREATED);
    }
    @Autowired
    private ITeacherService teacherService;
    @PostMapping("/create-teacher")
    public ResponseEntity<Teacher> createTeacher(@ModelAttribute Teacher teacher) {
        return new ResponseEntity<>(teacherService.save(teacher), HttpStatus.CREATED);
    }
}
