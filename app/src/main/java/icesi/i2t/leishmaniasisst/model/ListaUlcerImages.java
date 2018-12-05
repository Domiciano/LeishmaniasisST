package icesi.i2t.leishmaniasisst.model;

import java.util.ArrayList;
import java.util.List;


public class ListaUlcerImages extends DataXml{

    List<UIcerImg> UlcerImages = new ArrayList<UIcerImg>();

    public List<UIcerImg> getUlcerImages() {
        return UlcerImages;
    }

    public void setUlcerImages(List<UIcerImg> ulcerImages) {
        UlcerImages = ulcerImages;
    }

    @Override
    public void ParseAttributes() {

    }
}
