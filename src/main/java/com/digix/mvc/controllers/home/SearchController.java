package com.digix.mvc.controllers.home;

import com.digix.mvc.model.entities.dao.contents.Content;
import com.digix.mvc.model.entities.dao.SearchFilter;
import com.digix.mvc.model.services.home.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Daniel Moniry on 18.05.2016.
 */

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/results", method = RequestMethod.GET)
    public ModelAndView getResults(@RequestParam(value = "query") String query) {

        ModelAndView model = new ModelAndView("results");
        SearchFilter filter = new SearchFilter(query);

        String statement = searchService.getStatement(filter);
        List<Content> contentList = searchService.getContents(statement);

        model.addObject("contentList", contentList);

        return model;
    }
}
