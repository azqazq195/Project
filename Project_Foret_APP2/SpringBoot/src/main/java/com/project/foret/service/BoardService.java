package com.project.foret.service;

import com.project.foret.entity.*;
import com.project.foret.model.BoardModel;
import com.project.foret.model.MemberModel;
import com.project.foret.model.PhotoModel;
import com.project.foret.repository.*;
import com.project.foret.response.BoardResponse;
import com.project.foret.response.CreateResponse;
import com.project.foret.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {

    @Autowired
    ServletContext servletContext;

    private BoardRepository boardRepository;
    private BoardPhotoRepository boardPhotoRepository;
    private MemberRepository memberRepository;
    private ForetRepository foretRepository;

    private MemberService memberService;

    public ResponseEntity<Object> likeBoard(Long board_id, Long member_id){
        Response response = new Response();
        if(!memberRepository.findById(member_id).isPresent()){
            response.setMessage("사용자를 찾을 수 없습니다.");
            return ResponseEntity.ok(response);
        }
        if(!boardRepository.findById(board_id).isPresent()){
            response.setMessage("게시글을 찾을 수 없습니다.");
            return ResponseEntity.ok(response);
        }
        Member member = memberRepository.findById(member_id).get();
        Board board = boardRepository.findById(board_id).get();
        if(!member.getLikes().contains(board)){
            member.addLike(board);
            memberRepository.save(member);
            response.setMessage("좋아요");
        } else {
            member.removeLike(board);
            memberRepository.save(member);
            response.setMessage("싫어요");
        }
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Object> createBoard(Board board, MultipartFile[] files) throws Exception {
        CreateResponse response = new CreateResponse();
        if (files != null) {
            for (MultipartFile photo : files) {
                board.addPhoto(uploadPhoto(photo));
            }
        }
        Board savedBoard = boardRepository.save(board);
        if (boardRepository.findById(savedBoard.getId()).isPresent()) {
            response.setMessage("게시글 생성 성공");
            response.setId(savedBoard.getId());
            return ResponseEntity.ok(response);
        } else {
            response.setMessage("게시글 생성 실패");
            return ResponseEntity.unprocessableEntity().body(response);
        }
    }

    @Transactional
    public ResponseEntity<Object> updateBoard(Long id, Long member_id, Board board, MultipartFile[] files) throws Exception {
        if (boardRepository.findById(id).isPresent()) {
            Board updateBoard = boardRepository.findById(id).get();
            if (updateBoard.getMember().getId().equals(member_id)) {
                boardPhotoRepository.deleteByBoardId(id);

                updateBoard.setType(board.getType());
                updateBoard.setSubject(board.getSubject());
                updateBoard.setContent(board.getContent());
                updateBoard.setEdit_date(Timestamp.valueOf(LocalDateTime.now()));
                if (files != null) {
                    for (MultipartFile photo : files) {
                        updateBoard.addPhoto(uploadPhoto(photo));
                    }
                }
                Board savedBoard = boardRepository.save(updateBoard);
                if (boardRepository.findById(savedBoard.getId()).isPresent()) {
                    return ResponseEntity.ok("게시글수정 성공");
                } else return ResponseEntity.unprocessableEntity().body("게시글수정 실패");
            } else return ResponseEntity.unprocessableEntity().body("게시글 수정권한이 없습니다.");
        } else return ResponseEntity.unprocessableEntity().body("게시글을 찾을 수 없습니다.");
    }

    public ResponseEntity<Object> deleteBoard(Long id) {
        if (boardRepository.findById(id).isPresent()) {
            boardRepository.deleteById(id);
            if (boardRepository.findById(id).isPresent())
                return ResponseEntity.unprocessableEntity().body("게시글삭제 실패");
            else return ResponseEntity.ok().body("게시글삭제 성공");
        } else return ResponseEntity.badRequest().body("게시글을 찾을 수 없습니다.");
    }

    public BoardModel getBoard(Long id) {
        if (boardRepository.findById(id).isPresent()) {
            Board board = boardRepository.findById(id).get();
            BoardModel boardModel = new BoardModel();
            boardModel.setId(board.getId());
            if(board.getForet() != null){
                boardModel.setForet_id(board.getForet().getId());
            } else {
                boardModel.setForet_id(null);
            }
            boardModel.setSubject(board.getSubject());
            boardModel.setContent(board.getContent());
            boardModel.setType(board.getType());
            boardModel.setHit(board.getHit());
            boardModel.setReg_date(board.getReg_date());
            boardModel.setEdit_date(board.getEdit_date());
            boardModel.setPhotos(getPhotoList(board));
            boardModel.setMember(getMember(board));
            return boardModel;
        } else return null;
    }

    public BoardResponse getHomeBoardList(Long foret_id) {
        if (foretRepository.findById(foret_id).isPresent()) {
            List<Board> boardList = boardRepository.findTop5ByForetIdOrderByIdDesc(foret_id);
            if (boardList.size() > 0) {
                List<BoardModel> boardModels = new ArrayList<>();
                for (Board board : boardList) {
                    BoardModel boardModel = new BoardModel();
                    boardModel.setId(board.getId());
                    boardModel.setForet_id(board.getForet().getId());
                    boardModel.setSubject(board.getSubject());
                    boardModel.setContent(board.getContent());
                    boardModel.setType(board.getType());
                    boardModel.setHit(board.getHit());
                    boardModel.setReg_date(board.getReg_date());
                    boardModel.setEdit_date(board.getEdit_date());
                    // boardModel.setPhotos(getPhotoList(board));
                    boardModel.setMember(getMember(board));
                    boardModels.add(boardModel);
                }
                return new BoardResponse(boardModels);
            } else return new BoardResponse();
        } else return new BoardResponse();
    }

    public BoardResponse getForetBoardList(Long foret_id, int type) {
        if (foretRepository.findById(foret_id).isPresent()) {
            List<Board> boardList = boardRepository.findByForetIdAndTypeOrderByIdDesc(foret_id, type);
            if (boardList.size() > 0) {
                List<BoardModel> boardModels = new ArrayList<>();
                for (Board board : boardList) {
                    BoardModel boardModel = new BoardModel();
                    boardModel.setId(board.getId());
                    boardModel.setForet_id(board.getForet().getId());
                    boardModel.setSubject(board.getSubject());
                    boardModel.setContent(board.getContent());
                    boardModel.setType(board.getType());
                    boardModel.setHit(board.getHit());
                    boardModel.setReg_date(board.getReg_date());
                    boardModel.setEdit_date(board.getEdit_date());
                    // boardModel.setPhotos(getPhotoList(board));
                    boardModel.setMember(getMember(board));
                    boardModels.add(boardModel);
                }
                return new BoardResponse(boardModels);
            } else return new BoardResponse();
        } else return new BoardResponse();
    }

    public BoardResponse getAnonymousBoardList(int order) {
        List<Board> boardList = new ArrayList<>();
        if (order == 1) {
            boardList = boardRepository.findByTypeOrderByIdDesc(4);
        } else if (order == 2) {
            boardList = boardRepository.findByTypeOrderByCommentCount(4);
        }
        if (boardList.size() > 0) {
            List<BoardModel> boardModels = new ArrayList<>();
            for (Board board : boardList) {
                BoardModel boardModel = new BoardModel();
                boardModel.setId(board.getId());
//                boardModel.setForet_id(board.getForet().getId());
                boardModel.setSubject(board.getSubject());
                boardModel.setContent(board.getContent());
                boardModel.setType(board.getType());
                boardModel.setHit(board.getHit());
                boardModel.setReg_date(board.getReg_date());
                boardModel.setEdit_date(board.getEdit_date());
                // boardModel.setPhotos(getPhotoList(board));
                boardModel.setMember(getMember(board));
                boardModel.setComment_count(getCommentCount(board));
                boardModel.setLike_count(getLikeCount(board));
                boardModels.add(boardModel);
            }
            return new BoardResponse(boardModels);
        } else return new BoardResponse();
    }

    private List<PhotoModel> getPhotoList(Board board) {
        List<PhotoModel> photoList = new ArrayList<>();
        if (board.getPhotos() != null && board.getPhotos().size() != 0) {
            for (BoardPhoto boardPhoto : board.getPhotos()) {
                PhotoModel photoModel = new PhotoModel();
                photoModel.setId(boardPhoto.getId());
                photoModel.setDir(boardPhoto.getDir());
                photoModel.setFilename(boardPhoto.getFilename());
                photoModel.setOriginname(boardPhoto.getOriginname());
                photoModel.setFilesize(boardPhoto.getFilesize());
                photoModel.setFiletype(boardPhoto.getFiletype());
                photoModel.setReg_date(boardPhoto.getReg_date());
                photoList.add(photoModel);
            }
            return photoList;
        } else return null;
    }

    private BoardPhoto uploadPhoto(MultipartFile photo) throws Exception {
        String realPath = servletContext.getRealPath("/storage/board");
        String dir = "/storage/board";
        String originname = photo.getOriginalFilename();
        String filename = photo.getOriginalFilename();
        int lastIndex = originname.lastIndexOf(".");
        String filetype = originname.substring(lastIndex + 1);
        int filesize = (int) photo.getSize();
        if (!new File(realPath).exists()) {
            new File(realPath).mkdirs();
        }
        File file = new File(realPath, filename);
        FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));
        BoardPhoto boardPhoto = new BoardPhoto();
        boardPhoto.setDir(dir);
        boardPhoto.setOriginname(originname);
        boardPhoto.setFilename(filename);
        boardPhoto.setFiletype(filetype);
        boardPhoto.setFilesize(filesize);
        return boardPhoto;
    }

    private MemberModel getMember(Board board) {
        if (memberRepository.findById(board.getMember().getId()).isPresent()) {
            return memberService.getMember(board.getMember().getId());
        }
        return null;
    }

    private int getCommentCount(Board board) {
        if (board.getComments() != null && board.getComments().size() != 0) {
            return board.getComments().size();
        } else return 0;
    }

    private int getLikeCount(Board board){
        if(board.getMembers() != null && board.getMembers().size() != 0) {
            return board.getMembers().size();
        } else return 0;
    }
}
