package icesi.i2t.leishmaniasisst.bodylocations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import icesi.i2t.leishmaniasisst.model.UIcerImg;

public class FotoEvaluation {

    private List<UIcerImg> ulcerList;

    public FotoEvaluation(){
        ulcerList = new ArrayList<>();
    }

    public List<UIcerImg> getUlcerList() {
        return ulcerList;
    }

    public void setUlcerList(List<UIcerImg> ulcerList) {
        this.ulcerList = ulcerList;
    }

    public void addUlcer(UIcerImg img){
        this.ulcerList.add(img);
    }

    public boolean addAllUlcerImg(Collection<? extends UIcerImg> ulcerImgs){
        return ulcerList.addAll(ulcerImgs);
    }
}
