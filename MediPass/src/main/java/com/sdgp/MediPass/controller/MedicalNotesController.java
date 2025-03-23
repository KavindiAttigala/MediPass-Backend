package com.sdgp.MediPass.controller;

import com.sdgp.MediPass.model.MedicalNotes;
import com.sdgp.MediPass.service.MedicalNotesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation(value = "Store the medical noted of the patient in DB")
    @PostMapping("/add-records")
    public ResponseEntity<?> addMedicalNotes(@RequestParam Long mediId,
                                             @RequestParam(required = false) String textContent,
                                             @RequestParam(required = false)MultipartFile file){
        try{
            MedicalNotes savedNotes = mediNotesService.saveNotes(mediId, textContent,file);
            return ResponseEntity.ok(savedNotes);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("File upload error: " + e.getMessage());
        }
    }

    //ResponseEntity<?> is a generic return type to send HTTP responses (HTTP status code, Response body, Headers)
    @ApiOperation(value = "Retrieving the medical notes")
    @GetMapping("/{mediID}")
    public ResponseEntity<?> getMedicalNotes(@PathVariable Long mediId){
        try{
            return ResponseEntity.ok(mediNotesService.getNotes(mediId));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
