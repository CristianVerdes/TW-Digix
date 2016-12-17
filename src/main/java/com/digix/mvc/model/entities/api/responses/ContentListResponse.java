package com.digix.mvc.model.entities.api.responses;

import com.digix.mvc.model.entities.api.jsons.ContentJson;
import com.digix.mvc.model.entities.dao.contents.Content;

import java.util.ArrayList;
import java.util.List;

public class ContentListResponse {

    private String message;
    private List<ContentJson> contentList = new ArrayList<>();

    public ContentListResponse(String message, List<Content> contentList) {
        this.message = message;
        this.setContetList(contentList);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ContentJson> getContentList() {
        return contentList;
    }

    public void setContentList(List<ContentJson> contentList) {
        this.contentList = contentList;
    }

    public void setContetList(List<Content> contentList) {
        if(contentList != null) {
            for (Content content : contentList) {
                this.contentList.add(new ContentJson(content));
            }
        }
    }
}
