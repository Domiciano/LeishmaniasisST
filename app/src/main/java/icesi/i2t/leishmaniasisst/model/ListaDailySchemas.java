package icesi.i2t.leishmaniasisst.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domiciano on 12/04/2016.
 */

public class ListaDailySchemas extends DataXml{

    
    List<DailySchema> dailySchemas = new ArrayList<DailySchema>();

    public List<DailySchema> getDailySchemas() {
        return dailySchemas;
    }

    public void setDailySchemas(List<DailySchema> dailySchemas) {
        this.dailySchemas = dailySchemas;
    }

    @Override
    public void ParseAttributes() {

    }
}
