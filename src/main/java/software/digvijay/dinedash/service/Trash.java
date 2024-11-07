package software.digvijay.dinedash.service;

import software.digvijay.dinedash.entity.Location;

public class Trash {

    public static void main(String[] args) {
        Location location=new Location(1.0130131,1.3131314);
        String s=location.getLatitude()+","+ location.getLongitude();
        System.out.println(s);
    }
}
