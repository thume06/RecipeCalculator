package calculator;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    String name;
    ArrayList<Ingredient> recipe;

    public Recipe(String n, ArrayList r){
        name = n;
        recipe = r;
    }


    public String getName(){
        return name;
    }

    public ArrayList<Ingredient> getRecipe(){
        return recipe;
    }
}
