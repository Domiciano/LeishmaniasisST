package icesi.i2t.leishmaniasisst.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Domiciano on 06/09/2016.
 */
public class GeneralUtils {
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }


    public static Set<String> arrayList2Set(ArrayList<String> array){
        Set<String> out = new HashSet<String>(array);
        return out;
    }

    public static ArrayList<String> set2ArrayList(Set<String> set){
        ArrayList<String> out = new ArrayList<String>(set);
        return out;
    }
}
