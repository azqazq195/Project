package com.project.foret.response;

import com.project.foret.model.BoardModel;
import lombok.Data;

import java.util.List;

@Data
public class BoardResponse {
    private int total;
    private List<BoardModel> boards;

    public BoardResponse(){
    }

    public BoardResponse(List<BoardModel> boards){
        this.boards = boards;
        this.total = boards.size();
    }
}
