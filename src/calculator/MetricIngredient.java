package calculator;


public class MetricIngredient {
    private String name;
    private double stdAmount;
    private double metricAmount;
    private String stdUnit;
    private double rate;

    public MetricIngredient(String n, double sa, String su, Double ma) {
        name = n;
        stdAmount = sa;
        stdUnit = su;
        metricAmount = ma;
        Double tempStd = 0.0;


        if(stdUnit.equals("Cup")){
            tempStd = stdAmount * 8;
        }
        else if(stdUnit.equals("Tbsp")){
            tempStd = stdAmount * 0.5;
        }
        else if(stdUnit.equals("Tsp")){
            tempStd = stdAmount * (1/6);
        }
        //Std amount now in fl oz
        rate = (metricAmount/tempStd);
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

    public Double StdtoGrams(String u, double a){
        Double amount = 0.0;
        if(u.equals("Cup")){
            amount = a * 8;
        }
        else if(u.equals("Tbsp")){
            amount = a * 0.5;
        }
        else if(u.equals("Tsp")){
            amount = a * (1/6);
        }
        return (amount * rate);
    }

    public String getTextInfo(){
        return (name + "," + stdAmount + "," + stdUnit + "," + String.valueOf(metricAmount).substring(0, String.valueOf(metricAmount).length() - 2));
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

    public Double getMetricAmnt(){
        return metricAmount;
    }

}
