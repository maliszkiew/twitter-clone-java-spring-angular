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
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserLoginRegisterDto userLoginRegisterDto){
        UserDto userDto = userMapper.toDto(userService.saveUser(userLoginRegisterDto));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRegisterDto userLoginRegisterDto, HttpSession session) {
        UserDto userDto = userMapper.toDto(userService.loginUser(userLoginRegisterDto));
        session.setAttribute("user", userLoginRegisterDto.getUsername());
        return ResponseEntity.ok(userDto);
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
            UserDto userDto = userMapper.toDto(userService.getUserByUsername(username));
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("authenticated", false));
        }
    }
}
