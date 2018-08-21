package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domiciano on 12/04/2016.
 */
@Root(name = "UlcerForms", strict = false)
public class ListaUlcerForms extends DataXml{

    @ElementList(name="UlcerFormXml", required = false, inline = true)
    List<UlcerForm> ulcerForms = new ArrayList<UlcerForm>();

    public List<UlcerForm> getUlcerForms() {
        return ulcerForms;
    }

    public void setUlcerForms(List<UlcerForm> ulcerForms) {
        this.ulcerForms = ulcerForms;
    }

    @Override
    public void ParseAttributes() {

    }
}
