package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.MedicalNotes;
import com.sdgp.MediPass.service.MedicalNotesService;
import com.sdgp.MediPass.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping("/medipass/medical-notes")
@Api(value = "Medical Notes", description = "Managing medical notes uploaded by the doctor")
public class MedicalNotesController {

    @Autowired
    private MedicalNotesService mediNotesService;

    @Autowired
    private JwtUtil jwtUtil;

    private Long validateToken(String token) throws Exception {
        if(token == null || !token.startsWith("Bearer ")){
            throw new Exception("Missing or invalid Token");
        }
        String actualToken = token.substring(7);
        String mediId = jwtUtil.extractMediId(actualToken);
        if(mediId == null){
            throw new Exception("Invalid or expired token");
        }
        return Long.parseLong(mediId);
    }

    @ApiOperation(value = "Store the medical noted of the patient in DB")
    @PostMapping("/add-notes")
    public ResponseEntity<?> addMedicalNotes(@RequestHeader("Authorization") String token,
                                             @RequestParam(required = false) String textContent){
        try{
            long extractedMediId = validateToken(token);
            //save medical notes if the token is valid
            MedicalNotes savedNotes = mediNotesService.saveNotes(extractedMediId, textContent);
            return ResponseEntity.ok(savedNotes);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body("Error in saving the note: "+e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "Retrieving the medical notes")
    @GetMapping("/get-notes")
    public ResponseEntity<?> getMedicalNotes(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "mediId", required = false) String mediIdParam) {
        try {
            // Extract mediId from the token
            long mediIdFromToken = validateToken(token);
            long mediIdLong;

            // If mediId is provided in the query parameter, use it;
            // Otherwise, fall back to the mediId extracted from the token.
            if (mediIdParam == null || mediIdParam.trim().isEmpty()) {
                mediIdLong = mediIdFromToken;
            } else {
                try {
                    mediIdLong = Long.parseLong(mediIdParam);
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("Invalid mediId format");
                }
                // Optional: Ensure that the provided mediId matches the token's mediId
                if (!Objects.equals(mediIdFromToken, mediIdLong)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Token mediId does not match provided mediId");
                }
            }

            return ResponseEntity.ok(mediNotesService.getNotes(mediIdLong));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
