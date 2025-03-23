package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.MedicalNotes;
import com.sdgp.MediPass.service.MedicalNotesService;
import com.sdgp.MediPass.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/medipass/medical-notes")
@Api(value = "Medical Notes", description = "Managing medical notes uploaded by the doctor")
public class MedicalNotesController {

    @Autowired
    private MedicalNotesService mediNotesService;

    @Autowired
    private JwtUtil jwtUtil;

    private String validateToken(String token) throws Exception {
        if(token == null || !token.startsWith("Bearer ")){
            throw new Exception("Missing or invalid Token");
        }
        String actualToken = token.substring(7);
        String mediId = jwtUtil.extractMediId(actualToken);
        if(mediId == null){
            throw new Exception("Invalid or expired token");
        }
        return mediId;
    }

    @ApiOperation(value = "Store the medical noted of the patient in DB")
    @PostMapping("/add-records")
    public ResponseEntity<?> addMedicalNotes(@RequestHeader("Authorization") String token, @RequestParam Long mediId,
                                             @RequestParam(required = false) String textContent,
                                             @RequestParam(required = false)MultipartFile file){
        try{
            String extractedMediId = validateToken(token);

            if(!extractedMediId.equals(mediId)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired Token");
            }
            //save medical notes if the token is valid
            MedicalNotes savedNotes = mediNotesService.saveNotes(mediId, textContent,file);
            return ResponseEntity.ok(savedNotes);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("File upload error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //ResponseEntity<?> is a generic return type to send HTTP responses (HTTP status code, Response body, Headers)
    @ApiOperation(value = "Retrieving the medical notes")
    @GetMapping("/{mediID}")
    public ResponseEntity<?> getMedicalNotes(@RequestHeader("Authorization") String token,@PathVariable Long mediId){
        try{
            String extractedToken = validateToken(token);

            if(!extractedToken.equals(mediId)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired Token");
            }

            return ResponseEntity.ok(mediNotesService.getNotes(mediId));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
