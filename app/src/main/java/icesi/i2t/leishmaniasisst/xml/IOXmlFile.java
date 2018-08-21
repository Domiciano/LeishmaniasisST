package icesi.i2t.leishmaniasisst.xml;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Locale;

import icesi.i2t.leishmaniasisst.model.DataXml;
import icesi.i2t.leishmaniasisst.model.DateFormatTransformer;


/**
 * Created by Juan.
 * Represents the temporal file in which the data is sent to the server.
 */
public class IOXmlFile {

    private String filename;
    private Serializer serializer;
    private Dictionary<Date, String> tableFilename;

    public IOXmlFile() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        RegistryMatcher m = new RegistryMatcher();
        m.bind(Date.class, new DateFormatTransformer(format));
        this.serializer = new Persister(m);
    }

    /**
     * @param serializer
     */
    public IOXmlFile(Serializer serializer) {
        super();
        this.serializer = serializer;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the tableFilename
     */
    public Dictionary<Date, String> getTableFilename() {
        return tableFilename;
    }

    /**
     * @param tableFilename the tableFilename to set
     */
    public void setTableFilename(Dictionary<Date, String> tableFilename) {
        this.tableFilename = tableFilename;
    }


    /**
     * Permite serializar un objeto tipo DataXml en un archivo XML en una
     * ubicación especifica.
     *
     * @param source
     * @param filename
     * @throws Throwable
     */
    public void WriteFileXml(DataXml source, File filename) throws Exception {
        this.filename = filename.getAbsolutePath();
        if (serializer != null) {
            this.serializer.write(source, filename);
        } else {
            this.serializer = new Persister();
            this.serializer.write(source, filename);
        }
    }

    /**
     * Permite deserializar un archivo XML en un objeto tipo DataXml en una
     * ubicación especifica.
     *
     * @param source
     * @param filename
     * @return
     * @throws Exception
     */
    public DataXml ReadFileXml(DataXml source, File filename) throws Exception {
        this.filename = filename.getAbsolutePath();
        return this.serializer.read(source, filename);
    }

}
