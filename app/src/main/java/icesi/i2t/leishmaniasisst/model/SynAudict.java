package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;

/**
 * Created by Andres Aguirre on 09/02/2016.
 * Represents an BasicAdverseEvent. Has the required annotations to serialize the data and send it to the server.
 */

@Root(name = "data")
public class SynAudict extends DataXml {

    private static final long serialVersionUID = 1L;
    @Attribute(name = "xsi:type")
    private String classname = "SynAudictXml";
    @Element(name = "SynAudictId")
    private String uuid;
    @Element(name = "DataEntity")
    private String dataEntity;
    @Element(name = "Issues")
    private String issues;
    @Element(name = "Date")
    private Date date;
    @Element(name = "State")
    private boolean state;

    @Override
    public void ParseAttributes() {

    }

}
