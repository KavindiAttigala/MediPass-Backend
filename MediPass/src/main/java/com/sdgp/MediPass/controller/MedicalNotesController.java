package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.MedicalNotes;
import com.sdgp.MediPass.service.MedicalNotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/medical-notes")
@RequiredArgsConstructor
public class MedicalNotesController {
    private final MedicalNotesService mediNotesService;

    @PostMapping("/add-records")
    public ResponseEntity<?> addMedicalNotes(@RequestParam Long mediId, @RequestParam(required = false) String textContent, @RequestParam(required = false)MultipartFile file){
        try{
            MedicalNotes savedNotes = mediNotesService.saveNotes(mediId,textContent,file);
            return ResponseEntity.ok(savedNotes);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("File upload error: " + e.getMessage());
        }
    }

    //ResponseEntity<?> is a generic return type to send HTTP responses (HTTP status code, Response body, Headers)
    
    @GetMapping("/{mediID}")
    public ResponseEntity<?> getMedicalNotes(@PathVariable Long mediId){
        try{
            return ResponseEntity.ok(mediNotesService.getNotes(mediId));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
