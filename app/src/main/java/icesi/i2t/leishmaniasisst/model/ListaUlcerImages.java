package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;


@Root(name = "UlcerImages", strict = false)
public class ListaUlcerImages extends DataXml{

    @ElementList(name = "UlcerImgXml", required = false, inline = true)
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
