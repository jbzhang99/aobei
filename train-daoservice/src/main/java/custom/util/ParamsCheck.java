package custom.util;

import java.util.regex.Pattern;

public class ParamsCheck {




    public static boolean checkNull(Object object){
        return  object==null;
    }

    public static boolean checkNumAndLength(String param,int length){

        if(checkNull(param)){
            return false;
        }
        Pattern pattern = Pattern.compile("^\\d{"+length+"}$", Pattern.DOTALL);
        return  pattern.matcher(param).find();
    }

    public static boolean checkIntNum(String param){
        if(checkNull(param)){
            return false;
        }
        Pattern pattern = Pattern.compile("^\\d+$", Pattern.DOTALL);
        return  pattern.matcher(param).find();
    }

    public  static boolean checkNum(String param){
        if(checkNull(param)){
            return false;
        }
        Pattern pattern = Pattern.compile("^\\d+\\.*\\d+$", Pattern.DOTALL);
        return  pattern.matcher(param).find();
    }
    public static boolean checkStrAndLength(String param,int length){
        if(checkNull(param)){
            return false;
        }
        if (param.getBytes().length>length){
            return false;
        }
        return true;
    }
    public static void main(String[] args) {

        String param  = "01";

        System.out.println( checkNum(param));


    }
}
