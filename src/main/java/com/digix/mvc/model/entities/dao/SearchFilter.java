package com.digix.mvc.model.entities.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Daniel Moniry on 18.05.2016.
 */
public class SearchFilter {

    private String username;
    private int contentType;
    private String year;
    private int month;
    private int minAge;
    private int maxAge;
    private String location;
    private String relatives;
    private List<String> tags= new ArrayList<>();

    public SearchFilter(String query) {
        try {
            String[] splited = query.split(" ");
            if (splited.length != 0) {
                switch (splited[0]) {
                    case "photos":
                        this.setContentType(1);
                        break;
                    case "videos":
                        this.setContentType(2);
                        break;
                    case "documents":
                        this.setContentType(3);
                        break;
                    case "contents":
                        this.setContentType(0);
                        break;
                }
            }
            List<String> months = Arrays.asList("january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december");
            for (int i = 1; i < splited.length; i += 2) {
                switch (splited[i]) {
                    case "of": {
                        if(splited[i+1].equals("my")) {
                            this.setRelatives(splited[i + 2]);
                            i++;
                        } else {
                        this.setUsername(splited[i + 1]);
                        }
                    }
                        break;
                    case "in": {
                        String field = splited[i + 1];
                        if (org.apache.commons.lang.math.NumberUtils.isNumber(field))
                            this.setYear(field);
                        else if (months.contains(field))
                            month = months.indexOf(field) + 1;
                        else this.setLocation(field);
                    }
                    break;
                    case "with": {
                        getTags().add(splited[i + 1]);
                        while (splited.length > i + 3 && splited[i + 2].toLowerCase().equals("and")) {
                            i += 2;
                            getTags().add(splited[i + 1]);
                        }
                    } break;
                    case "from": {
                        if(splited[i+1].toLowerCase().equals("childhood")){
                            this.setMinAge(0);
                            this.setMaxAge(12);
                        }
                        if(splited[i+1].toLowerCase().equals("teenage")&&splited[i+2].toLowerCase().equals("years")){
                            this.setMinAge(12);
                            this.setMaxAge(20);
                            i++;
                        }
                        if(splited[i+1].toLowerCase().equals("highschool")){
                            this.setMinAge(14);
                            this.setMaxAge(18);
                        }
                        if(splited[i+1].toLowerCase().equals("college")){
                            this.setMinAge(18);
                            this.setMaxAge(24);
                        }
                    }

                }
            }
        } catch (IndexOutOfBoundsException e) {

        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getRelatives() {
        return relatives;
    }

    public void setRelatives(String relatives) {
        this.relatives = relatives;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public String toString() {
        return "SearchFilter{" +
                "username='" + username + '\'' +
                ", contentType=" + contentType +
                ", year='" + year + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
