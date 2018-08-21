package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by Juan David.
 * Base class to perform the serialization of data for synchronization.
 */
//xmlns="http://schemas.datacontract.org/2004/07/SND.Library.Factories" xmlns:i="http://www.w3.org/2001/XMLSchema-instance"
@Root(name = "data")
@NamespaceList({
        @Namespace(prefix = "", reference = "http://schemas.datacontract.org/2004/07/SND.Library.Factories.GuaralST.XML"),
        @Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "i")})
public abstract class DataXml implements Serializable {

    private static final long serialVersionUID = 1L;

    public DataXml() {
    }

    public abstract void ParseAttributes();

}
