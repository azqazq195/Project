package com.project.foret.controller;

import com.project.foret.entity.Foret;
import com.project.foret.model.ForetModel;
import com.project.foret.response.CreateResponse;
import com.project.foret.response.ForetResponse;
import com.project.foret.service.ForetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/foret")
public class ForetController {

    private ForetService foretService;

//    @PostMapping("/photo")
//    public ResponseEntity<Object> photo(
//            @RequestPart @Nullable Foret foret,
//            MultipartFile[] files) throws Exception {
//        return foretService.photo(files, foret);
//    }

    @PostMapping("/create")
    public ResponseEntity<Object> createForet(
            @RequestPart Foret foret,
            MultipartFile[] files
    ) throws Exception {
        return foretService.createForet(foret, files);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Object> updateForet(
            @PathVariable Long id,
            @RequestParam Long member_id,
            Foret foret,
            MultipartFile[] files) throws Exception {
        return foretService.updateForet(id, member_id, foret, files);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteForet(@PathVariable Long id) {
        return foretService.deleteForet(id);
    }

    @GetMapping("/details/{id}")
    public ForetModel getForet(@PathVariable Long id){
        return foretService.getForet(id);
    }

    @GetMapping("/all")
    public ForetResponse getForets(){
        return foretService.getForets();
    }

    @GetMapping("/myForets")
    public ForetResponse getMyForets(@RequestParam Long member_id){
        return foretService.getMyForets(member_id);
    }

    @GetMapping("/page")
    public ForetResponse getForetsByPage(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return foretService.getForetsByPage(page, size);
    }

    @PostMapping("/signUp")
    public CreateResponse signUpForet(
            @RequestParam Long foret_id,
            @RequestParam Long member_id
    ) {
        return foretService.signUpForet(foret_id, member_id);
    }

    @GetMapping("/search")
    public ForetResponse searchForet(
            @RequestParam String name
    ) {
        return foretService.searchForet(name);
    }
}
