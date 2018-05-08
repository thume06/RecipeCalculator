package calculator;

import java.io.Serializable;

public class Ingredient implements Serializable{

   private String name;
   private double amount;
   private String unit;
   private String info;
   private String fracAmnt;
   private int decimalPos;
   private Double fracNum;
   private String fracUnit;
   private String strAmount;
   private int wholeNum;
   private String rationalVal;
   private int num;
   private int denom;
   private Double doubleSave;
   
   public Ingredient(String n, double a, String u){
      name = n;
      amount = a;
      unit = u;
   }

   public Ingredient() {
        name = "Null";
        amount = 0;
        unit = "Null";
   }

   private void convert(double amnt) {
       //Convert cups to tablespoons
       if (unit.equals("Cup") && (amnt < .25)) {
           unit = "Tbsp";
           amount = amount * 16.0;
       }
       //Converts tablespoons to cups
       else if (unit.equals("Tbsp") && amnt >= 4) {
           unit = "Cup";
           amount = amount / 16.0;
       }
       //Converts teaspoons to tablespoons
       else if (unit.equals("Tsp") && amnt >= 1.5) {
           unit = "Tbsp";
           amount = amount / 3.0;
       }
       //Converts tablespoons to teaspoons
       else if (unit.equals("Tbsp") && amnt < .5) {
           unit = "Tsp";
           amount = amount * 3.0;
       }
   }

    private void convertFrac(double amnt){
       fracUnit = unit;
        //Convert cups to tablespoons
        if(unit.equals("Cup") && (amnt < .25)){
            fracUnit = "Tbsp";
            fracNum = amnt * 16.0;
        }
        //Converts tablespoons to cups
        else if(unit.equals("Tbsp") && (amnt >= 4)){
            fracUnit = "Cup";
            fracNum = amnt/16.0;
        }
        //Converts teaspoons to tablespoons
        else if(unit.equals("Tsp") && amnt >= 1.5){
            fracUnit = "Tbsp";
            fracNum = amnt/3.0;
        }
        //Converts tablespoons to teaspoons
        else if(unit.equals("Tbsp") && amnt < .5){
            fracUnit = "Tsp";
            fracNum = amnt*3.0;
        }
   }

   public String getInfo(){
       convert(amount);
       Rational(amount);
       fracAmnt = toString();


       if(amount > 1) {
           strAmount = String.valueOf(amount);
           for (int i = 0; i < strAmount.length(); i++) {
               if (strAmount.charAt(i) == '.') {
                   decimalPos = i;
               }
           }
           wholeNum = Integer.valueOf(strAmount.substring(0, decimalPos));
           fracNum = Double.valueOf(strAmount.substring(decimalPos, strAmount.length()));

           convertFrac(fracNum);
           Rational(fracNum);
           rationalVal = toString();

           if(fracNum != 0) {
               //if fraction and and whole have same unit
               if(fracUnit.equals(unit)){
                   //for special cases where certain fraction values must be converted
                   if(rationalVal.equals("3/8") && unit.equals("Cup")){
                       info = (name + ": " + wholeNum + " and 1/4 " + unit + " and 2 Tbsp");
                   }
                   else if(rationalVal.equals("5/8") && unit.equals("Cup")){
                       info = (name + ": " + wholeNum + " and 1/2 " + unit + " and 2 Tbsp");
                   }
                   else if(rationalVal.equals("7/8") && unit.equals("Cup")){
                       info = (name + ": " + wholeNum + " and 3/4 " + unit + " and 2 Tbsp");
                   }
                   else if(rationalVal.equals("5/8") && unit.equals("Tbsp")){
                       info = (name + ": " + wholeNum + " " + unit + " and 1 and 7/8 Tsp");
                   }
                   else if(rationalVal.equals("7/8") && unit.equals("Tbsp")){
                       info = (name + ": " + wholeNum + " " + unit + " and 2 and 5/8 Tsp");
                   }
                   else if(unit.equals("Tbsp") && rationalVal.equals("2/3")){
                       info = (name + ": " + wholeNum + " " + unit + " and 2 Tsp");
                   }
                   else if(unit.equals("Tbsp") && rationalVal.equals("3/4")){
                       info = (name + ": " + wholeNum + " " + unit + " and 2 and 1/4 Tsp");
                   }
                   //Non-special cases
                   else {
                       if(CalculatorScreen.getRound()){
                           String roundAmnt = Round(rationalVal, unit);
                           //If it is rounded to one, just add that to the whole amount
                           if(roundAmnt.equals("1")){
                               wholeNum = wholeNum + 1;
                               info = (name + ": " + wholeNum + " " + unit);
                           }
                           else{
                               info = (name + ": " + wholeNum + " and " + Round(rationalVal, unit) + " " + unit);
                           }
                       }
                       else{
                           info = (name + ": " + wholeNum + " and " + rationalVal + " " + unit);
                       }
                   }
                //end if fraction and whole have the same unit
               }
               //executes if fraction and whole have different units
               else {
                   if(CalculatorScreen.getRound()){
                       info = (name + ": " + wholeNum + " " + unit + " and " + Round(rationalVal, fracUnit) + " " + fracUnit);
                   }
                   else{
                       info = (name + ": " + wholeNum + " " + unit + " and " + rationalVal + " " + fracUnit);
                   }
               }
           }
           //executes if fraction equals zero
           else{
               info = (name + ": " + fracAmnt + " " + unit);
           }
       }
       //executes if the total amount is less than or equal to one
       else{
           //Special Cases
           if(unit.equals("Cup") && fracAmnt.equals("3/8")){
               info = (name + ": 1/4 " + unit + " and 2 Tbsp");
           }
           else if(unit.equals("Cup") && fracAmnt.equals("5/8")){
               info = (name + ": 1/2 " + unit + " and 2 Tbsp");
           }
           else if (unit.equals("Cup") && fracAmnt.equals("7/8")){
               info = (name + ": 3/4 " + unit + "and 2 Tbsp");
           }
           else if(unit.equals("Tbsp") && fracAmnt.equals("5/8")){
               info = (name + ": 1 and 7/8 Tsp");
           }
           else if(unit.equals("Tbsp") && fracAmnt.equals("7/8")){
               info = (name + ": 2 and 5/8 Tsp");
           }
           else if(unit.equals("Tbsp") && fracAmnt.equals("2/3")){
               info = (name + ": 2 Tsp");
           }
           else if(unit.equals("Tbsp") && fracAmnt.equals("3/4")){
               info = (name + ": 2 and 1/4 Tsp");
           }
           //Normal cases
           else{
               if(CalculatorScreen.getRound()){
                   info = (name + ": " + Round(fracAmnt, unit) + " " + unit);
               }
               else{
                   info = (name + ": " + fracAmnt + " " + unit);
               }
           }
           /*else {
               info = (name + ": " + fracAmnt + " " + unit);
           }*/
       }
       if(amount > 500){
           info = "TOO LARGE!";
       }
       return info;
   }

   public void xtwo(){
      amount = amount*2.0;
   }
   
   public void half(){
      amount = amount / 2.0;
   }

   public void triple() {
       amount = amount*3.0;
   }

   public void third(){
       amount = amount / 3.0;
   }
   
   public void quad(){
      amount = amount*4.0;
   }
   
   public void quarter(){
      amount = amount / 4.0;
   }



   //Getters and Setters
   public void setName(String n){
      name = n;
   }
   
   public void setAmount(double a){
      amount = a;
   }
   
   public void setUnit(String u){
      unit = u;
   }
   
   public String getName(){
      return name;
   }
   
   public double getAmount(){
      return amount;
   }
   
   public String getUnit(){
      return unit;
   }




   //RATIONAL VALUES-------------------------------------------------------------------
   public void Rational(double d) {
       doubleSave = d;
       String s = String.valueOf(d);
       int digitsDec = s.length() - 1 - s.indexOf('.');
       int denom = 1;
       for (int i = 0; i < digitsDec; i++) {
           d *= 10;
           denom *= 10;
       }

       int num = (int) Math.round(d);
       int g = gcd(num, denom);
       this.num = num / g;
       this.denom = denom /g;
   }

    public void Rational(int num, int denom) {
        this.num = num;
        this.denom = denom;
    }

    public String toString() {
        String strNum = String.valueOf(num);
        String strDenom = String.valueOf(denom);
        if (strDenom.equals("1")){
            return strNum;
        }
        if (strNum.equals("624973141") && strDenom.equals("1874919424")){
            return ("1/3");
        }
        if (strNum.equals("1249946281") && strDenom.equals("1874919424")){
            return ("2/3");
        }
        if (strNum.equals("585050797") && strDenom.equals("-784662528")){
            return ("1/6");
        }
        if (strNum.equals("2925253985") && strDenom.equals("-784662528")){
            return ("5/6");
        }
        if ((strNum + strDenom).length() > 2){
            String decVal = String.valueOf(doubleSave);
            decVal = decVal.substring(0, Math.min(decVal.length(), 4));
            return decVal;
        }

        return strNum + "/" + strDenom;
    }

    public static int gcd(int num1, int denom1) {
        if(denom1 == 0){
            return num1;
        }
        return gcd(denom1, num1%denom1);
    }

    public String Round(String s, String u){
       if (s.contains("/")){
           return s;
        }
       Double toRound = Double.valueOf(s);
       String unit = u;

        if(unit == "Cup" && toRound > .875){
            return "1";
        }
        else if(unit == "Cup" && toRound > .7083){
            return "3/4";
        }
        else if(unit == "Cup" && toRound > .583){
            return "2/3";
        }
        else if(unit == "Cup" && toRound > .4167){
            return "1/2";
        }
        else if(unit == "Cup" && toRound > .2917){
            return "1/3";
        }
        else if(unit == "Cup" && toRound > .125){
            return "1/4";
        }
        else if(unit == "Cup"){
            return "less than 1/4";
        }
        else if(unit == "Tbsp" && toRound > .75){
            return "1";
        }
        else if(unit == "Tbsp" && toRound >.25){
            return "1/2";
        }
        else if(unit == "Tsp" && toRound > .9375){
            return "1";
        }
        else if(unit == "Tsp" && toRound > .8125){
            return "7/8";
        }
        else if(unit == "Tsp" && toRound > .6875){
            return "3/4";
        }
        else if(unit == "Tsp" && toRound > .5625){
            return "5/8";
        }
        else if(unit == "Tsp" && toRound > .4375){
            return "1/2";
        }
        else if(unit == "Tsp" && toRound > .3125){
            return "3/8";
        }
        else if(unit == "Tsp" && toRound > .1875){
            return "1/4";
        }
        else if(unit == "Tsp" && toRound > .0625){
            return "1/8";
        }
        else if(unit == "Tsp" && toRound <= .0625){
            return "less than 1/8";
        }
        else{
            return s;
        }

    }

}