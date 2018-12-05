package icesi.i2t.leishmaniasisst.model;


import java.util.ArrayList;
import java.util.List;

public class ListaSchemas extends DataXml{

   
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
