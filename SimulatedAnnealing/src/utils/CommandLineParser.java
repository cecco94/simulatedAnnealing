package utils;

import lombok.Data;
import java.util.ArrayList;

@Data
public class CommandLineParser{


    String[] m_args;
    ArrayList<String> m_params = new ArrayList<>();

	
    public CommandLineParser(String[] args)   {
        this.m_args = args;
        if ( this.m_args != null ) {
            for ( int i = 0; i < args.length; ++i ) {
                this.m_params.add(isNullReplace(args[i]).trim());
            }
        }
    }
    
    
    public int getNumParameters() {
        return this.m_params.size();
    }

    
    public boolean getFlag(String strFlag) {
        return this.m_params.contains(isNullReplace(strFlag).trim());
    }

    
    public String getSwitchValue(String strSwitch) {
        return getSwitchValue(strSwitch, "");
    }


    public String getSwitchValue(String strSwitch, String defaultValue)    {
        String strValue = defaultValue;
        String flag = isNullReplace(strSwitch).trim();
        
        if ( getFlag(flag) ) {
            int index = this.m_params.indexOf(flag);
            if ( this.m_params.size() >= (index + 1) ) {
                strValue = this.m_params.get(index + 1);
            }
        }
        return strValue;
    }


    public String getPositionalParameter(int index) {
        String strValue = "";
        int size = this.m_params.size();
        if ( size >= (index) ) {
            strValue = isNullReplace(this.m_params.get(index - 1)).trim();
        }

        return strValue;
    }


    public String getListParameters() {
        String strParams = "";
        if ( this.m_args != null ) {
            for ( int i = 0; i < this.m_args.length; ++i ) {
                strParams += (i > 0 ? " " : "") + isNullReplace(this.m_args[i]).trim();
            }
        }
        return strParams;
    }
    
    
    public static String isNullReplace(String s)    {
        return isNullReplace(s, "");
    }

    
    public static String isNullReplace(String s, String replacement) {
        return ((s == null) || (s.trim().length() == 0)) ? replacement : s;
    }

}
