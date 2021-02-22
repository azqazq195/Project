package com.project.foret.service;

import com.project.foret.entity.Board;
import com.project.foret.entity.Comment;
import com.project.foret.entity.Foret;
import com.project.foret.entity.Member;
import com.project.foret.model.CommentModel;
import com.project.foret.model.ForetModel;
import com.project.foret.model.MemberModel;
import com.project.foret.repository.BoardRepository;
import com.project.foret.repository.CommentRepository;
import com.project.foret.repository.MemberRepository;
import com.project.foret.response.CommentResponse;
import com.project.foret.response.ForetResponse;
import com.project.foret.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    CommentRepository commentRepository;
    BoardRepository boardRepository;
    MemberRepository memberRepository;
    MemberService memberService;

    public ResponseEntity<Object> create(Comment comment) {
        HashMap<String, String> hashMap = new HashMap<>();
        Comment saveComment = commentRepository.save(comment);
        if (commentRepository.findById(saveComment.getId()).isPresent()) {
            if(comment.getGroup_id() == null){
                saveComment.setGroup_id(saveComment.getId());
            }
            commentRepository.save(saveComment);
            hashMap.put("result", "OK");
            return ResponseEntity.ok(hashMap);
        } else {
            hashMap.put("result", "FAIL");
            return ResponseEntity.unprocessableEntity().body(hashMap);
        }
    }

    public ResponseEntity<Object> update(Long id, Long member_id, Comment comment) {
        if (commentRepository.findById(id).isPresent()) {
            Comment updateComment = commentRepository.findById(id).get();
            if (updateComment.getMember().getId().equals(member_id)) {
                updateComment.setContent(comment.getContent());
                Comment savedComment = commentRepository.save(updateComment);
                if (commentRepository.findById(savedComment.getId()).isPresent()) {
                    return ResponseEntity.ok("댓글수정 성공");
                } else return ResponseEntity.unprocessableEntity().body("댓글수정 실패");
            } else return ResponseEntity.unprocessableEntity().body("댓글 수정권한이 없습니다.");
        } else return ResponseEntity.unprocessableEntity().body("댓글을 찾을 수 없습니다.");
    }

    public ResponseEntity<Object> delete(Long id) {
        Response response = new Response();
        if (commentRepository.findById(id).isPresent()) {
            commentRepository.deleteById(id);
            if (commentRepository.findById(id).isPresent()){
                response.setResult("FAIL");
                response.setMessage("댓글삭제 실패");
                return ResponseEntity.unprocessableEntity().body("댓글삭제 실패");
            }
            else {
                response.setResult("OK");
                response.setMessage("댓글삭제 성공");
                return ResponseEntity.ok().body(response);
            }
        } else {
            response.setMessage("댓글을 찾을 수 없습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    public CommentModel getComment(Long id) {
        if (commentRepository.findById(id).isPresent()) {
            Comment comment = commentRepository.findById(id).get();
            CommentModel commentModel = new CommentModel();
            commentModel.setId(comment.getId());
            commentModel.setContent(comment.getContent());
            commentModel.setReg_date(comment.getReg_date());
            commentModel.setGroup_id(comment.getGroup_id());
            commentModel.setMember(getMember(comment));
            return commentModel;
        } else return null;
    }

    public CommentResponse getComments(Long board_id) {
        List<Comment> commentList = commentRepository.findAllByBoardIdOrderByGroupIdAscIdAsc(board_id);
        if (commentList.size() > 0) {
            List<CommentModel> commentModels = new ArrayList<>();
            for (Comment comment : commentList) {
                CommentModel commentModel = new CommentModel();
                commentModel.setId(comment.getId());
                commentModel.setContent(comment.getContent());
                commentModel.setReg_date(comment.getReg_date());
                commentModel.setGroup_id(comment.getGroup_id());
                commentModel.setMember(getMember(comment));
                commentModels.add(commentModel);
            }
            return new CommentResponse(commentModels);
        } else return new CommentResponse();
    }

    private MemberModel getMember(Comment comment) {
        // return memberService.getMember(comment.getMember().getId());
        MemberModel member = memberService.getMember(comment.getMember().getId());
        MemberModel newMember = new MemberModel();
        newMember.setId(member.getId());
        newMember.setName(member.getName());
        newMember.setNickname(member.getNickname());
        return newMember;
    }

}
