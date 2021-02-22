package com.project.foret.response;

import com.project.foret.model.CommentModel;
import lombok.Data;

import java.util.List;

@Data
public class CommentResponse {
    private int total;
    private List<CommentModel> comments;

    public CommentResponse(){
    }

    public CommentResponse(List<CommentModel> comments){
        this.comments = comments;
        this.total = comments.size();
    }
}
