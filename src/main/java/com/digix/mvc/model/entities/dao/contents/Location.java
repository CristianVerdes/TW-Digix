package com.digix.mvc.model.entities.dao.contents;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Location {

    private String contentID;
    private String country;
    private String city;
    private String street;
    private String latitude;
    private String longitude;

    public Location() {

    }

    public Location(ResultSet resultSet) throws SQLException {
        this.contentID = resultSet.getString(1);
        this.country = resultSet.getString(2);
        this.city = resultSet.getString(3);
        this.street = resultSet.getString(4);
        this.latitude = resultSet.getString(5);
        this.longitude = resultSet.getString(6);
    }

    public Location(org.springframework.social.facebook.api.Location location) {
        this.country = location.getCountry();
        this.city = location.getCity();
        this.street = location.getStreet();
        this.latitude = String.valueOf(location.getLatitude());
        this.longitude = String.valueOf(location.getLongitude());
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "contentID='" + contentID + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
