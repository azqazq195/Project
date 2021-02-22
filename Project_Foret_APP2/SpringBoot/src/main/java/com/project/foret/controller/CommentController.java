package com.project.foret.controller;

import com.project.foret.entity.Comment;
import com.project.foret.model.CommentModel;
import com.project.foret.response.CommentResponse;
import com.project.foret.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(
            @RequestBody Comment comment
    ) {
        return commentService.create(comment);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(
            @PathVariable Long id,
            @RequestParam Long member_id,
            Comment comment
    ) {
        return commentService.update(id, member_id, comment);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(
            @PathVariable Long id
    ) {
        return commentService.delete(id);
    }

    @GetMapping("/details/{id}")
    public CommentModel getComment(
            @PathVariable Long id
    ) {
        return commentService.getComment(id);
    }

    @GetMapping("/getComments")
    public CommentResponse getComments(
            @RequestParam Long board_id
    ) {
        return commentService.getComments(board_id);
    }
}
