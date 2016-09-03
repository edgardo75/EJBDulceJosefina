package com.dulcejosefina.utils;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
public class ValidateClientandUserData {
    public ValidateClientandUserData() {
    }
    public boolean validate(String dataUser,String regExp){         
                Pattern p = Pattern.compile(regExp);
                Matcher m = p.matcher(dataUser);
                boolean b = m.matches();
          
       return b;
    }
    public boolean isValidEmailAddress(String email){
        boolean result = true;        
        try {
            InternetAddress emailAddr = new InternetAddress(email);
                emailAddr.validate();
        } catch (AddressException ex) {
            Logger.getLogger(ValidateClientandUserData.class.getName()).log(Level.SEVERE, null, ex);
            result = false;
        }         
        return result;
        }    
}