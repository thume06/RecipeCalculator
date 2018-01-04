package calculator;


public class MetricIngredient {
    private String name;
    private double stdAmount;
    private double metricAmount;
    private String stdUnit;
    private double rate;

    public MetricIngredient(String n, double sa, String su, double ma) {
        name = n;
        stdAmount = sa;
        stdUnit = su;
        metricAmount = ma;
        double initAmount = sa;

        if(stdUnit.equals("Cup")){
            stdAmount = stdAmount * 8;
        }
        else if(stdUnit.equals("Tbsp")){
            stdAmount = stdAmount * 0.5;
        }
        else if(stdUnit.equals("Tsp")){
            stdAmount = stdAmount * (1/6);
        }
        //Std amount now in fl oz
        rate = (metricAmount/stdAmount);
    }

    public void GramstoStd(double g){
        double grams = g;
        double oz = grams/rate;
        if(oz >= 2){
            stdUnit = "Cup";
            stdAmount = oz/8;
        }
        else if(oz >= .25){
            stdUnit = "Tbsp";
            stdAmount = oz*2;
        }
        else if(oz >= 0.02){
            stdUnit = "Tsp";
            stdAmount = oz*6;
        }
    }

    public String getName(){
        return name;
    }

    public String getStdUnit(){
        return stdUnit;
    }

    public Double getStdAmnt(){
        return stdAmount;
    }

}
