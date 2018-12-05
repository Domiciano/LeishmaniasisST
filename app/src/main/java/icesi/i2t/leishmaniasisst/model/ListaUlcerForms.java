package icesi.i2t.leishmaniasisst.model;

import java.util.ArrayList;
import java.util.List;

public class ListaUlcerForms extends DataXml{

   
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
