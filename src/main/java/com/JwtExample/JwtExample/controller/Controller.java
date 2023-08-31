package com.JwtExample.JwtExample.controller;

import com.JwtExample.JwtExample.model.TokenReqRes;
import com.JwtExample.JwtExample.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class Controller {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/generate-token")
    public ResponseEntity<Object> generateToken(@RequestBody TokenReqRes tokenReqRes){
        if(tokenReqRes.getUsername().equals("techwithden@gmail.com") && tokenReqRes.getPassword().equals("tech123")){
            String token = jwtTokenUtil.generateToken(tokenReqRes.getUsername());
            TokenReqRes tokenResponse = new TokenReqRes();
            tokenResponse.setToken(token);
            tokenResponse.setExpirationTime("60 sec");
            return ResponseEntity.ok(tokenResponse);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Object> validateToken(@RequestBody TokenReqRes tokenReqRes){
        return ResponseEntity.ok(jwtTokenUtil.validateToken(tokenReqRes.getToken()));
    }

    @GetMapping("/get-fruits")
        public ResponseEntity<?> getAllFruits(@RequestHeader(value = "Authorization", required = false) String token){

        if (token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized. Token is required");
        }else{
            String realToken = token.substring(7);
            String tokenCheckResult = jwtTokenUtil.validateToken(realToken);
            if (tokenCheckResult.equalsIgnoreCase("valid")){
                List<String> fruits = List.of("Oranges", "Banana","Apple","Watermellon","Berries");
                return new ResponseEntity<>(fruits, HttpStatus.OK);
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized due to: " + tokenCheckResult);
            }
        }
    }
}
