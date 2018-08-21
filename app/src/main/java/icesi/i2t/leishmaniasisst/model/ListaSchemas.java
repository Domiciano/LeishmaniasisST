package icesi.i2t.leishmaniasisst.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domiciano on 11/04/2016.
 */
@Root(name = "Schemas", strict = false)
public class ListaSchemas extends DataXml{

    @ElementList(name="SchemaXml", required = false, inline = true)
    List<Schema> schemas = new ArrayList<Schema>();

    public List<Schema> getSchemas() {
        return schemas;
    }

    public void setSchemas(List<Schema> pacientes) {
        this.schemas = pacientes;
    }

    @Override
    public void ParseAttributes() {

    }
}
