package org.nadaeliba.studentTranscript.controllers;

import org.nadaeliba.studentTranscript.services.StudentTranscriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("")
public class StudentTranscriptController {

    @Autowired
    private StudentTranscriptService studentTranscriptService;

    @RequestMapping(method = RequestMethod.GET, value = "/getTranscript")
    public ResponseEntity<String> getTranscript(@RequestParam Integer studentID){
        return ResponseEntity
                .ok(studentTranscriptService.getTranscript(studentID));
    }

}
