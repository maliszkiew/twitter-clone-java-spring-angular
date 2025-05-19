package maliszkiew.dev.twitter_clone.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserLoginRegisterDto userLoginRegisterDto){
        userService.saveUser(userLoginRegisterDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRegisterDto userLoginRegisterDto, HttpSession session) {
        userService.loginUser(userLoginRegisterDto);
        session.setAttribute("user", userLoginRegisterDto.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus(HttpSession session) {
        String username = (String) session.getAttribute("user");
        if (username != null) {
            return ResponseEntity.ok().body(Map.of("authenticated", true, "username", username));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("authenticated", false));
        }
    }


}