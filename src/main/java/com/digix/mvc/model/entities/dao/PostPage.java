package com.digix.mvc.model.entities.dao;

import com.digix.mvc.model.entities.dao.contents.Content;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrei on 6/4/2016.
 */
public class PostPage {

    private List<Content> contentList = new ArrayList<>();
    private PagedList<Post> currentPage;


    public List<Content> getContentList() {
        return contentList;
    }

    public void setContentList(List<Content> contentList) {
        this.contentList = contentList;
    }

    public PagedList<Post> getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(PagedList<Post> currentPage) {
        this.currentPage = currentPage;
    }

}
