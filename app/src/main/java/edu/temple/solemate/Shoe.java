package edu.temple.solemate;

/**
 * Created by xxnoa_000 on 4/2/2018.
 */

public class Shoe {
    String shoeName, shoeDescription, shoePrice;

    public Shoe(String name, String description, String price) {
        // This constructor has one parameter, name.
        shoeName= name;
        shoeDescription= description;
        shoePrice= price;
    }

    public String getName() {
        return shoeName;
    }


    public String getPrice( ) {
        return shoePrice;
    }


    public String getDesc() {
        return shoeDescription;
    }
}
