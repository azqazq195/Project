package com.project.foret.controller;

import com.project.foret.entity.Board;
import com.project.foret.entity.Foret;
import com.project.foret.model.BoardModel;
import com.project.foret.response.BoardResponse;
import com.project.foret.response.Response;
import com.project.foret.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private BoardService boardService;

    @PostMapping("/like")
    public ResponseEntity<Object> likeBoard(
            @RequestParam Long board_id,
            @RequestParam Long member_id
    ) {
        return boardService.likeBoard(board_id, member_id);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createBoard(
            @RequestPart Board board,
            MultipartFile[] files
    ) throws Exception {
        return boardService.createBoard(board, files);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Object> updateBoard(
            @PathVariable Long id,
            @RequestParam Long member_id,
            Board board,
            MultipartFile[] files
    ) throws Exception {
        return boardService.updateBoard(id, member_id, board, files);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteBoard(@PathVariable Long id) {
        return boardService.deleteBoard(id);
    }

    @GetMapping("/details/{id}")
    public BoardModel getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @GetMapping("/getHomeBoardList")
    public BoardResponse getHomeBoardList(
            @RequestParam Long foret_id
    ) {
        return boardService.getHomeBoardList(foret_id);
    }

    @GetMapping("/getForetBoardList")
    public BoardResponse getForetBoardList(
            @RequestParam Long foret_id,
            @RequestParam int type
    ) {
        return boardService.getForetBoardList(foret_id, type);
    }

    @GetMapping("/getAnonymousBoardList/{order}")
    public BoardResponse getAnonymousBoardList(@PathVariable int order
    ) {
        return boardService.getAnonymousBoardList(order);
    }


}
