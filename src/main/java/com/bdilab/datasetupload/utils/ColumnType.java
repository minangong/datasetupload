package com.bdilab.datasetupload.utils;

public class ColumnType {

    public static String getColumnType(String Columnvalue){
        if(isDouble(Columnvalue)) {
            if(Columnvalue.length()>7){
                return"Float64";
            }else{
                return "Float32";
            }
        }else if(isInt(Columnvalue)){
            Long value = Math.abs(Long.parseLong(Columnvalue));
            if(value<=127){
                return "Int8";
            }else if(value <=32767){
                return "Int16";
            }else if(value <=2147483647){
                return "Int32";
            }else{
                return "Int64";
            }
        }else if(isDate(Columnvalue)){
            //DateTime64？
            if(Columnvalue.contains(":")){
                return "DateTime";
            }else{
                return "Date";
            }
        }
        return "String";
    }

    public static boolean isDouble(String s){
        String regex = "[+-]?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$)";
        if(s.matches(regex)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isInt(String s){
        String regex = "[+-]?[0-9]*$";
        if(s.matches(regex)){
            return true;
        }else{
            return false;
        }
    }
    public static boolean isDate(String s){
        String regex = "^\\d{4}-\\d{1,2}-\\d{1,2} *(\\d{2}:\\d{2}:\\d{2})?";
        if(s.matches(regex)){
            return true;
        }else{
            return false;
        }
    }
}
