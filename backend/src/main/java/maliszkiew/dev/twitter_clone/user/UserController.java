package maliszkiew.dev.twitter_clone.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserLoginRegisterDto userLoginRegisterDto){
        userService.saveUser(userLoginRegisterDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRegisterDto userLoginRegisterDto) {
        userService.loginUser(userLoginRegisterDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/session")
    public ResponseEntity<?> session(HttpSession session) {
        if(session.getAttribute("username").toString() == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

}
