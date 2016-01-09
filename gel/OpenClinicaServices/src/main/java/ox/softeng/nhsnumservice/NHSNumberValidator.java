package ox.softeng.nhsnumservice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NHSNumberValidator {

	
	public static boolean validNHSNum(String nhsNo){
        String NHSNO_PATTERN = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(NHSNO_PATTERN);
        Matcher matcher = pattern.matcher(nhsNo);
        
        if(nhsNo.equals(""))
        {
                return false;
        }
        
        if(!nhsNo.equals("") && !matcher.matches())
        {
                return false;
        }
        
        int[] digits = new int[10];
        int[] values = new int[10];
        int sum = 0;
        for(int i=0;i<9;i++)
        {
                digits[i] = Integer.parseInt(nhsNo.substring(i, i+1));
                values[i] = digits[i] * (10-i);
                sum += values[i];                       
        }
        //System.out.println("Sum : " + sum);
        digits[9] = Integer.parseInt(nhsNo.substring(9, 10));
        int rem = 11 - (sum % 11);
        //System.out.println("Rem : " + rem);
        if(rem == 11)
        {
                rem = 0;
        }
        else if(rem == 10)
        {
                return false;
        }
        if(digits[9] != rem)
        {
                return false;
        }
        
        return true;
	}
	
}
