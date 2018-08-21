package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domiciano on 12/04/2016.
 */
@Root(name = "DailySchemas", strict = false)
public class ListaDailySchemas extends DataXml{

    @ElementList(name = "DailySchemaXml", required = false, inline = true)
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
