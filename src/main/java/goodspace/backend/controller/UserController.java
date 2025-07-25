package goodspace.backend.controller;

import goodspace.backend.dto.UserMyPageDto;
import goodspace.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static java.lang.Long.parseLong;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private UserService userService;

    @PatchMapping("/updateMyPage")
    public ResponseEntity<String> updateMyPage(Principal principal, @RequestBody UserMyPageDto userMyPageDto){
        Long id = parseLong(principal.getName());
        return ResponseEntity.ok().body(userService.updateMyPage(id, userMyPageDto));
    }
}
