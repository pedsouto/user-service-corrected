package com.pedsouto.userservice.controller;

import com.pedsouto.userservice.dto.AddressDto;
import com.pedsouto.userservice.dto.PhoneDto;
import com.pedsouto.userservice.dto.UserDto;
import com.pedsouto.userservice.infra.security.JwtUtil;
import com.pedsouto.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userDto));
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.email(),
                        userDto.password()
                )
        );

        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    @GetMapping
    public ResponseEntity<UserDto> findByEmail(@RequestParam("email") String email,
                                               Authentication authentication) {
        return ResponseEntity.ok(userService.findByEmail(email, authentication.getName()));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteByEmail(@PathVariable("email") String email,
                                              Authentication authentication) {
        userService.deleteByEmail(email, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,
                                              Authentication authentication) {
        return ResponseEntity.ok(userService.updateUser(authentication.getName(), userDto));
    }

    @PutMapping("/address")
    public ResponseEntity<AddressDto> updateAddress(@Valid @RequestBody AddressDto addressDto,
                                                    @RequestParam("id") Long id,
                                                    Authentication authentication) {
        return ResponseEntity.ok(userService.updateAddress(id, addressDto, authentication.getName()));
    }

    @PutMapping("/phone")
    public ResponseEntity<PhoneDto> updatePhone(@Valid @RequestBody PhoneDto phoneDto,
                                                @RequestParam("id") Long id,
                                                Authentication authentication) {
        return ResponseEntity.ok(userService.updatePhone(id, phoneDto, authentication.getName()));
    }

    @PostMapping("/address")
    public ResponseEntity<AddressDto> addAddress(
            @Valid @RequestBody AddressDto dto,
            Authentication authentication) {
        return ResponseEntity.ok(userService.addAddress(authentication.getName(), dto));
    }

    @PostMapping("/phone")
    public ResponseEntity<PhoneDto> addPhone(
            @Valid @RequestBody PhoneDto dto,
            Authentication authentication) {
        return ResponseEntity.ok(userService.addPhone(authentication.getName(), dto));
    }
}
