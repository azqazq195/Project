package com.project.foret.response;

import com.project.foret.model.ForetModel;
import lombok.Data;

import java.util.List;

@Data
public class ForetResponse {
    private int total;
    private List<ForetModel> forets;

    public ForetResponse(){
    }

    public ForetResponse(List<ForetModel> forets){
        this.forets = forets;
        this.total = forets.size();
    }
}
