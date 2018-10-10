package icesi.i2t.leishmaniasisst.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import icesi.i2t.leishmaniasisst.calendario.DayOnGridModel;
import icesi.i2t.leishmaniasisst.dialogs.AmpollasAdministradasDialog;
import icesi.i2t.leishmaniasisst.dialogs.PastillasTomadasDialog;
import icesi.i2t.leishmaniasisst.model.*;


/**
 * Created by Laboratorioi2t on 08/02/2016.
 */
public class ManejadorBD extends SQLiteOpenHelper {

    // Errores
    public static final int SUCCESS = 1;
    public static final int RECORD_EXIST = 2;
    public static final int UNKNOWN_ERROR = 99;

    // Base de datos
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "leishmaniasis2";

    // UUID
    private static final String UUID = "uuid";

    // Paciente
    private static final String TABLE_PATIENTS = "patients";
    private static final String PATIENT_NAME = "name";
    private static final String PATIENT_LAST_NAME = "last_name";
    private static final String PATIENT_BIRTHDAY = "birthday"; // dd/MM/aaaa
    private static final String PATIENT_CEDULA = "cedula";
    private static final String PATIENT_DOCUMENT_TYPE = "document_type";
    private static final String PATIENT_GENRE = "genre";

    // Evaluador
    private static final String TABLE_EVALUADOR = "evaluadores";
    private static final String EVALUADOR_NAME = "name";
    private static final String EVALUADOR_LAST_NAME = "last_name";
    private static final String EVALUADOR_CEDULA = "cedula";
    private static final String EVALUADOR_LAST_LOGIN = "last_login"; // dd/MM/aaaa

    // Schema
    private static final String TABLE_SCHEMA = "schemas";
    private static final String SCHEMA_DATE_START = "date_start";
    private static final String SCHEMA_DATE_END = "date_end";
    private static final String SCHEMA_ACTIVE = "is_active";
    //private static final String SCHEMA_PATIENT = "patientUUID";
    //private static final String SCHEMA_EVALUADOR = "evaluadorUUID";

    // DailySchema
    private static final String TABLE_DAILY_SCHEMA = "daily_schemas";
    private static final String DAILY_SCHEMA_DAY = "day_of_treatment";
    private static final String DAILY_SCHEMA_DATE = "date_of_treatment";
    private static final String DAILY_SCHEMA_FLAG = "flag";
    //private static final String DAILY_SCHEMA_SCHEMA = "schemaUUID";

    // Antecedentes. NO VA EN APP
    //TODO: CAMBIAR CAMPOS DE LA DB POR LOS PARAMETROS NUEVOS DE ANTECEDENTE
    private static final String TABLE_ANTECEDENTES = "antecedentes";
    private static final String ANTECEDENTES_NAME = "name";
    private static final String MEDICINE_NAME = "medicine_name";

    // Prescripcion
    private static final String TABLE_PRESCRIPCION = "prescripciones";
    private static final String PRESCRIPCION_NAME = "name";
    private static final String PRESCRIPCION_DOSIS = "dosis";
    private static final String PRESCRIPCION_NUMERO_LOTE = "numero_lote";
    private static final String PRESCRIPCION_COMENTARIOS = "comentarios";

    // Develop Symptom
    private static final String TABLE_DEVELOPED_DISEASE = "develop_symptom";
    private static final String DEVELOPED_DISEASE_NAME = "name";
    private static final String DEVELOPED_DISEASE_ANSWER = "answer";
    private static final String DEVELOPED_DISEASE_MEDICINE_NAME = "medicine_name";
    private static final String DEVELOPED_DISEASE_IS_ACTIVE = "is_active";
    private static final String DEVELOPED_DISEASE_INITIAL_DATE = "date_start";
    private static final String DEVELOPED_DISEASE_REMOVE_DATE = "date_end";

    // Schedule Taking
    private static final String TABLE_SCHEDULE_TAKING = "schedule_taking";
    private static final String SCHEDULE_TAKING_TIME = "time";
    private static final String SCHEDULE_TAKING_DATE = "date";

    // Basic adverse effect
    private static final String TABLE_BASIC_ADVERSE_EFFECT = "basic_adverse_effect";
    private static final String BASIC_ADVERSE_EFFECT_NAME = "name";
    private static final String BASIC_ADVERSE_EFFECT_DOLOR_SITIO = "dolor_sitio";
    private static final String BASIC_ADVERSE_EFFECT_INFECCION_SITIO = "infeccion_sitio";
    private static final String BASIC_ADVERSE_EFFECT_MALESTAR_GENERAL = "malestar_general";
    private static final String BASIC_ADVERSE_EFFECT_DOLOR_MUSCULAR = "dolor_muscular";
    private static final String BASIC_ADVERSE_EFFECT_DOLOR_ARTICULACIONES = "dolor_articulaciones";
    private static final String BASIC_ADVERSE_EFFECT_DOLOR_CABEZA = "dolor_cabeza";
    private static final String BASIC_ADVERSE_EFFECT_FIEBRE = "fiebre";
    private static final String BASIC_ADVERSE_EFFECT_NAUSEAS = "nauseas";
    private static final String BASIC_ADVERSE_EFFECT_VOMITO = "vomito";
    private static final String BASIC_ADVERSE_EFFECT_DIARREA = "diarrea";
    private static final String BASIC_ADVERSE_EFFECT_DOLOR_ABDOMINAL = "dolor_abdominal";
    private static final String BASIC_ADVERSE_EFFECT_PERDIDA_APETITO = "perdida_apetito";
    private static final String BASIC_ADVERSE_EFFECT_MAREO = "mareo";
    private static final String BASIC_ADVERSE_EFFECT_PALPITACIONES = "palpitaciones";

    // UIcerForm
    private static final String TABLE_UICERFORM = "uicer_form";
    private static final String UICERFORM_NOTE_GENERAL = "note_general";
    private static final String UICERFORM_DATE = "date";

    // UIcerImg
    private static final String TABLE_UICERIMG = "uicer_img";
    private static final String UICERIMG_BODY_LOCATION = "body_location";
    private static final String UICERIMG_IMG_DATE = "img_date";
    private static final String UICERIMG_IMG_FORMAT = "img_format";
    private static final String UICERIMG_IMGUUID = "imgUUID";
    private static final String UIINJURIES_LOCATION = "injuries_location";

    // SynAudict
    private static final String TABLE_SYNAUDICT = "syn_audict";
    private static final String SYNAUDICT_DATA_ENTITY = "data_entity";
    private static final String SYNAUDICT_ISSUES = "issues";
    private static final String SYNAUDICT_DATE = "date";
    private static final String SYNAUDICT_STATE = "state";

    // Claves foráneas
    private static final String FK_PACIENTE_EVALUADOR = "evaluadorUUID";
    private static final String FK_SCHEMA_EVALUADOR = "evaluadorUUID";

    private static final String FK_SCHEMA_PACIENTE = "pacienteUUID";
    private static final String FK_DAILY_SCHEMA_SCHEMA = "schemaUUID";
    private static final String FK_DEVELOPED_DISEASE_SCHEMA = "schemaUUID";
    private static final String FK_ANTECEDENTES_SCHEMA = "daily_schemaUUID";
    private static final String FK_PRESCRIPCION_DAILY_SCHEMA = "daily_schemaUUID";
    private static final String FK_SCHEDULE_TAKING_PRESCRIPTIONUUID = "prescriptionUUID";
    private static final String FK_PRESCRIPCION_BASIC_ADVERSE_EVENT = "basic_adverse_eventUUID";
    private static final String FK_BASIC_ADVERSE_EFFECT_PRESCRIPTIONUUID = "prescriptionUUID";
    private static final String FK_UICERFORM_DAILY_SCHEM = "daily_schemaUUID";
    private static final String FK_UICERIMG_UICERFORM = "uicer_formUUID";

    public ManejadorBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* Creates the SQLite database */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENTS_TABLE = "CREATE TABLE " + TABLE_PATIENTS + "("
                + UUID + " TEXT PRIMARY KEY,"
                + PATIENT_NAME + " TEXT,"
                + PATIENT_LAST_NAME + " TEXT,"
                + PATIENT_BIRTHDAY + " DATE,"
                + PATIENT_CEDULA + " TEXT,"
                + PATIENT_DOCUMENT_TYPE + " TEXT,"
                + PATIENT_GENRE + " TEXT,"
                + FK_PACIENTE_EVALUADOR + " TEXT," + " FOREIGN KEY (" + FK_PACIENTE_EVALUADOR + ") REFERENCES " + TABLE_EVALUADOR + "(" + UUID + "))";

        String CREATE_EVALUADOR_TABLE = "CREATE TABLE " + TABLE_EVALUADOR + "("
                + UUID + " TEXT PRIMARY KEY,"
                + EVALUADOR_NAME + " TEXT,"
                + EVALUADOR_LAST_NAME + " TEXT,"
                + EVALUADOR_CEDULA + " TEXT,"
                + EVALUADOR_LAST_LOGIN + " DATE"
                + ")";

        String CREATE_SCHEMA_TABLE = "CREATE TABLE " + TABLE_SCHEMA + "("
                + UUID + " TEXT PRIMARY KEY,"
                + SCHEMA_DATE_START + " DATE,"
                + SCHEMA_DATE_END + " DATE,"
                + SCHEMA_ACTIVE + " INTEGER,"
                + FK_SCHEMA_EVALUADOR + " TEXT,"
                + FK_SCHEMA_PACIENTE + " TEXT,"
                + " FOREIGN KEY (" + FK_SCHEMA_EVALUADOR + ") REFERENCES " + TABLE_EVALUADOR + "(" + UUID + "),"
                + " FOREIGN KEY (" + FK_SCHEMA_PACIENTE + ") REFERENCES " + TABLE_PATIENTS + "(" + UUID + "))";

        String CREATE_DAILY_SCHEMA_TABLE = "CREATE TABLE " + TABLE_DAILY_SCHEMA + "("
                + UUID + " TEXT PRIMARY KEY,"
                + DAILY_SCHEMA_DAY + " INTEGER,"
                + DAILY_SCHEMA_DATE + " DATE,"
                + DAILY_SCHEMA_FLAG + " INTEGER,"
                + FK_DAILY_SCHEMA_SCHEMA + " TEXT,"
                + " FOREIGN KEY (" + FK_DAILY_SCHEMA_SCHEMA + ") REFERENCES " + TABLE_SCHEMA + "(" + UUID + "))";

        String CREATE_ANTECEDENTES = "CREATE TABLE " + TABLE_ANTECEDENTES + "("
                + UUID + " TEXT PRIMARY KEY,"
                + ANTECEDENTES_NAME + " TEXT,"
                + MEDICINE_NAME + " TEXT,"
                + FK_ANTECEDENTES_SCHEMA + " TEXT," + " FOREIGN KEY (" + FK_ANTECEDENTES_SCHEMA + ") REFERENCES " + TABLE_SCHEMA + "(" + UUID + "))";

        String CREATE_TABLE_PRESCRIPCIONES = "CREATE TABLE " + TABLE_PRESCRIPCION + "("
                + UUID + " TEXT PRIMARY KEY,"
                + PRESCRIPCION_NAME + " TEXT,"
                + PRESCRIPCION_DOSIS + " TEXT,"
                + PRESCRIPCION_NUMERO_LOTE + " TEXT,"
                + PRESCRIPCION_COMENTARIOS + " TEXT,"
                + FK_PRESCRIPCION_DAILY_SCHEMA + " TEXT," + " FOREIGN KEY (" + FK_PRESCRIPCION_DAILY_SCHEMA + ") REFERENCES " + TABLE_DAILY_SCHEMA + "(" + UUID + "))";
        //+ FK_PRESCRIPCION_BASIC_ADVERSE_EVENT + " TEXT," + " FOREIGN KEY ("+FK_PRESCRIPCION_BASIC_ADVERSE_EVENT+") REFERENCES "+TABLE_BASIC_ADVERSE_EFFECT+"("+UUID+"))";

        String CREATE_TABLE_DEVELOPED_DISEASE = "CREATE TABLE " + TABLE_DEVELOPED_DISEASE + "("
                + UUID + " TEXT PRIMARY KEY,"
                + DEVELOPED_DISEASE_NAME + " TEXT,"
                + DEVELOPED_DISEASE_ANSWER + " TEXT,"
                + DEVELOPED_DISEASE_MEDICINE_NAME + " TEXT,"
                + DEVELOPED_DISEASE_IS_ACTIVE + " INTEGER," //SQLLite no tiene para booleans
                + DEVELOPED_DISEASE_INITIAL_DATE + " DATE,"
                + DEVELOPED_DISEASE_REMOVE_DATE + " DATE,"
                + FK_DEVELOPED_DISEASE_SCHEMA + " TEXT," + " FOREIGN KEY (" + FK_DEVELOPED_DISEASE_SCHEMA + ") REFERENCES " + TABLE_SCHEMA + "(" + UUID + "))";

        String CREATE_TABLE_SCHEDULE_TAKING = "CREATE TABLE " + TABLE_SCHEDULE_TAKING + "("
                + UUID + " TEXT PRIMARY KEY,"
                + SCHEDULE_TAKING_TIME + " TEXT,"
                + SCHEDULE_TAKING_DATE + " DATE,"
                + FK_SCHEDULE_TAKING_PRESCRIPTIONUUID + " TEXT,"
                + " FOREIGN KEY (" + FK_SCHEDULE_TAKING_PRESCRIPTIONUUID + ") REFERENCES " + TABLE_PRESCRIPCION + "(" + UUID + "))";

        String CREATE_TABLE_BASIC_ADVERSE_EFFECT = "CREATE TABLE " + TABLE_BASIC_ADVERSE_EFFECT + "("
                + UUID + " TEXT PRIMARY KEY,"
                + BASIC_ADVERSE_EFFECT_NAME + " TEXT,"
                + BASIC_ADVERSE_EFFECT_DOLOR_SITIO + " INTEGER,"
                + BASIC_ADVERSE_EFFECT_INFECCION_SITIO + " INTEGER,"
                + BASIC_ADVERSE_EFFECT_MALESTAR_GENERAL + " INTEGER,"
                + BASIC_ADVERSE_EFFECT_DOLOR_MUSCULAR + " INTEGER,"
                + BASIC_ADVERSE_EFFECT_DOLOR_ARTICULACIONES + " INTEGER,"
                + BASIC_ADVERSE_EFFECT_DOLOR_CABEZA + " INTEGER,"
                + BASIC_ADVERSE_EFFECT_FIEBRE + " INTEGER,"
                + BASIC_ADVERSE_EFFECT_NAUSEAS + " INTEGER,"
                + BASIC_ADVERSE_EFFECT_VOMITO + " INTEGER,"
                + BASIC_ADVERSE_EFFECT_DIARREA + " INTEGER,"
                + BASIC_ADVERSE_EFFECT_DOLOR_ABDOMINAL + " INTEGER,"
                + BASIC_ADVERSE_EFFECT_PERDIDA_APETITO + " INTEGER,"
                + BASIC_ADVERSE_EFFECT_MAREO + " INTEGER,"
                + BASIC_ADVERSE_EFFECT_PALPITACIONES + " INTEGER,"
                + FK_BASIC_ADVERSE_EFFECT_PRESCRIPTIONUUID + " TEXT,"
                + " FOREIGN KEY (" + FK_BASIC_ADVERSE_EFFECT_PRESCRIPTIONUUID + ") REFERENCES " + TABLE_PRESCRIPCION + "(" + UUID + "))";

        String CREATE_TABLE_UICERFORM = "CREATE TABLE " + TABLE_UICERFORM + "("
                + UUID + " TEXT PRIMARY KEY,"
                + UICERFORM_DATE + " DATE,"
                + UICERFORM_NOTE_GENERAL + " TEXT,"
                + FK_UICERFORM_DAILY_SCHEM + " TEXT,"
                + " FOREIGN KEY (" + FK_UICERFORM_DAILY_SCHEM + ") REFERENCES " + TABLE_DAILY_SCHEMA + "(" + UUID + "))";

        String CREATE_TABLE_UICERIMG = "CREATE TABLE " + TABLE_UICERIMG + "("
                + UUID + " TEXT PRIMARY KEY,"
                + UICERIMG_IMGUUID + " TEXT,"
                + UICERIMG_BODY_LOCATION + " TEXT,"
                + UICERIMG_IMG_DATE + " DATE,"
                + UIINJURIES_LOCATION + " TEXT,"
                + UICERIMG_IMG_FORMAT + " TEXT,"
                + FK_UICERIMG_UICERFORM + " TEXT,"
                + " FOREIGN KEY (" + FK_UICERIMG_UICERFORM + ") REFERENCES " + TABLE_UICERFORM + "(" + UUID + "))";

        String CREATE_SYNAUDICT_TABLE = "CREATE TABLE " + TABLE_SYNAUDICT + "("
                + UUID + " TEXT PRIMARY KEY,"
                + SYNAUDICT_DATA_ENTITY + " TEXT,"
                + SYNAUDICT_DATE + " DATE,"
                + SYNAUDICT_ISSUES + " TEXT,"
                + SYNAUDICT_STATE + " INTEGER"
                + ")";


        db.execSQL(CREATE_EVALUADOR_TABLE);
        db.execSQL(CREATE_PATIENTS_TABLE);
        db.execSQL(CREATE_SCHEMA_TABLE);
        db.execSQL(CREATE_DAILY_SCHEMA_TABLE);
        db.execSQL(CREATE_ANTECEDENTES);
        db.execSQL(CREATE_TABLE_PRESCRIPCIONES);
        db.execSQL(CREATE_TABLE_DEVELOPED_DISEASE);
        db.execSQL(CREATE_TABLE_SCHEDULE_TAKING);
        db.execSQL(CREATE_TABLE_BASIC_ADVERSE_EFFECT);
        db.execSQL(CREATE_TABLE_UICERFORM);
        db.execSQL(CREATE_TABLE_UICERIMG);
        db.execSQL(CREATE_SYNAUDICT_TABLE);

    }

    /* Handles the changes in the model */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVALUADOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEMA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_SCHEMA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANTECEDENTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESCRIPCION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVELOPED_DISEASE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE_TAKING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BASIC_ADVERSE_EFFECT);
        onCreate(db);
    }

    /* DEFAULT CRUD */

    /* Agregar fila a tabla */
    public int add(String tableName, ContentValues values) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            long rowId = db.insertOrThrow(tableName, null, values);
            db.close();
            int out = rowId == -1 ? this.UNKNOWN_ERROR : this.SUCCESS;

            return out;
        } catch (SQLiteConstraintException e) {
            db.close();
            int out = this.RECORD_EXIST;

            return out;
        }
    }

    /* Edit an item in a table */
    public int edit(String tableName, ContentValues values, String UUID) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            long rowId = db.update(tableName, values, this.UUID + "=?", new String[]{UUID});
            db.close();
            return rowId == 0 ? DatabaseHandler.UNKNOWN_ERROR : DatabaseHandler.SUCCESS;
        } catch (SQLiteConstraintException e) {
            db.close();
            return DatabaseHandler.RECORD_EXIST;
        }
    }

    // Borrar registros de una tabla por el nombre
    public int delete(String tableName) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            long rowId = db.delete(tableName, null, null);
            db.close();
            return rowId == 0 ? DatabaseHandler.UNKNOWN_ERROR : DatabaseHandler.SUCCESS;
        } catch (SQLiteConstraintException e) {
            db.close();
            return DatabaseHandler.RECORD_EXIST;
        }
    }

    // Borra un registro de una tabla por su id y nombre de tabla
    public int deleteRow(String tableName, String uuid) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            long rowId = db.delete(tableName, UUID + "='" + uuid + "'", null);
            db.close();
            return rowId == 0 ? DatabaseHandler.UNKNOWN_ERROR : DatabaseHandler.SUCCESS;
        } catch (SQLiteConstraintException e) {
            db.close();
            return DatabaseHandler.RECORD_EXIST;
        }
    }

    //Valida si hay que sobreescribir la información en bd
    public boolean validarEvaluadorExistente(Evaluador fullRater) {
        Evaluador viejo = buscarEvaluador(fullRater.getUuid());
        if (viejo != null) {
            if (fullRater.getPacienteLista().getPacientes().size() != viejo.getPacienteLista().getPacientes().size()) {
                return false;
            }
        }
        if (fullRater.getPacienteLista().getPacientes().size() > 0) {
            for (Paciente pac : fullRater.getPacienteLista().getPacientes()) {
                if (pac.getListaSchemas().getSchemas().size() > 0) {
                    for (Schema schema : pac.getListaSchemas().getSchemas()) {
                        if (buscarSchema(schema.getUuid()) == null) return false;
                    }
                }
            }
        }
        return true;
    }

    // Llena toda la base de datos con los objetos contenidos dentro de Evaluador
    public void setFullRater(Evaluador fullRater) {
        deleteDataRater(fullRater.getUuid());
        agregarEvaluador(fullRater);
        if (fullRater.getPacienteLista().getPacientes().size() > 0) {
            for (Paciente pac : fullRater.getPacienteLista().getPacientes()) {
                agregarPacienteCambiandoRaterId(pac, fullRater.getUuid());
                if (pac.getListaSchemas().getSchemas().size() > 0) {
                    for (Schema schema : pac.getListaSchemas().getSchemas()) {
                        agregarSchema(schema);
                        if (schema.getAntecedentes().getAntecedentes().size() > 0) {
                            for (AntecedenteXml ant : schema.getAntecedentes().getAntecedentes()) {
                                agregarAntecedentes(ant);
                            }
                        }
                        if (schema.getDevelopSymptoms().getSintomas().size() > 0) {
                            for (DevelopSymptom dev : schema.getDevelopSymptoms().getSintomas()) {
                                agregarDevelopedDisease(dev);
                            }
                        }
                        if (schema.getListaDailySchemas().getDailySchemas().size() > 0) {
                            for (DailySchema dailySchema : schema.getListaDailySchemas().getDailySchemas()) {
                                agregarDailySchema(dailySchema);
                                if (dailySchema.getPrescripciones().getPrescripciones().size() > 0) {
                                    for (Prescripcion prescripcion : dailySchema.getPrescripciones().getPrescripciones()) {
                                        agregarPrescripcion(prescripcion);
                                        if (prescripcion.getDosisTomadas().getSchedulesTaking().size() > 0) {
                                            for (ScheduleTaking dosis : prescripcion.getDosisTomadas().getSchedulesTaking()) {
                                                agregarScheduleTaking(dosis);
                                            }
                                        }
                                        if (prescripcion.getEventosBasicos() != null) {
                                            agregarBasicAdverseEffect(prescripcion.getEventosBasicos());
                                        }
                                    }
                                }
                                if (dailySchema.getImagenes().getUlcerForms().size() > 0) {
                                    for (UlcerForm formImg : dailySchema.getImagenes().getUlcerForms()) {
                                        agregarUIcerForm(formImg);
                                        if (formImg.getUlcerImages().getUlcerImages().size() > 0) {
                                            for (UIcerImg img : formImg.getUlcerImages().getUlcerImages()) {
                                                agregarUIcerImg(img);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }


    private void agregarPacienteCambiandoRaterId(Paciente pac, String rater_id) {
        Paciente p = buscarPaciente(pac.getCedula());
        if (p != null) {
            p.setEvaluadorId(rater_id);
            editarPaciente(p);
        } else agregarPaciente(pac);
    }

    /* Obtiene Prescripcion, ScheduleTaking, BasicAdverseEvent, DevelopSymptom completos
    de enlazados al evaluador por los UUIDs de los demás objetos.
     */

    /*
    public Evaluador getFullRater(Evaluador rater_db) {

        Evaluador evaluador = getRater(rater_db.getCedula());
        ArrayList<Paciente> pacientes = new ArrayList<Paciente>();
        ArrayList<Schema> schemas = new ArrayList<Schema>();
        ArrayList<DevelopSymptom> developSymptoms = new ArrayList<DevelopSymptom>();
        ArrayList<DailySchema> dSchemas = new ArrayList<DailySchema>();
        ArrayList<Prescripcion> prescripciones = new ArrayList<Prescripcion>();
        ArrayList<UlcerForm> formsImgs = new ArrayList<UlcerForm>();

        for(Paciente pac : getListaPacientes(evaluador.getUuid())){
            Paciente pacN = new Paciente();
            pacN.setUuid(pac.getUuid());
            pacN.setName(pac.getName());
            pacN.setLastName(pac.getLastName());
            pacN.setCedula(pac.getCedula());
            pacN.setDocumentType(pac.getDocumentType());
            pacN.setGenre(pac.getGenre());

            pacN.setEvaluadorId(evaluador.getUuid());
            for(Schema schema : getListaSchemas(pac.getUuid())){
                Schema schemaN = new Schema();
                schemaN.setUuid(schema.getUuid());
                schemaN.setEvaluadorId(evaluador.getUuid());
                schemaN.setPatientId(pac.getUuid());
                for(DevelopSymptom devDis : getListDevelopedDisease(schema.getUuid())){
                    DevelopSymptom sintN = new DevelopSymptom();
                    sintN.setUuid(devDis.getUuid());
                    sintN.setSchemaId(schema.getUuid());
                    developSymptoms.add(sintN);
                }
                ListaSintomas listaSintomas = new ListaSintomas();
                listaSintomas.setSintomas(developSymptoms);

                schemaN.setDevelopSymptoms(listaSintomas);
                for (DailySchema dailySchema : getListaDailySchemas(schema.getUuid())){
                    DailySchema dailySchemaN = new DailySchema();
                    dailySchemaN.setUuid(dailySchema.getUuid());
                    dailySchemaN.setSchemaId(schema.getUuid());
                    for(Prescripcion presc : getListaPrescripciones(dailySchema.getUuid())){
                        presc = buscarPrescripcionCompleta(presc.getUuid());
                        prescripciones.add(presc);
                    }
                    ListaPrescripciones listaPrescripciones = new ListaPrescripciones();
                    listaPrescripciones.setPrescripciones(prescripciones);

                    dailySchemaN.setPrescripciones(listaPrescripciones);
                    for(UlcerForm ulcerForm : getListaImagenesForm(dailySchema.getUuid())) {
                        formsImgs.add(ulcerForm);
                    }
                    ListaUlcerForms listaUlcerForms = new ListaUlcerForms();
                    listaUlcerForms.setUlcerForms(formsImgs);
                    dailySchemaN.setImagenes(listaUlcerForms);

                    dSchemas.add(dailySchemaN);
                    ListaDailySchemas listaDailySchemas = new ListaDailySchemas();
                    listaDailySchemas.setDailySchemas(dSchemas);
                    schemaN.setListaDailySchemas(listaDailySchemas);
                }
                schemas.add(schemaN);
                ListaSchemas listaSchemas = new ListaSchemas();
                listaSchemas.setSchemas(schemas);
                pacN.setListaSchemas(listaSchemas);
            }
            pacientes.add(pacN);
            //ToDO: Preguntar a Aguirre
            //evaluador.addAllPacientes(pacientes);
            ListaPacientes listaPacientes = new ListaPacientes();
            listaPacientes.setPacientes(pacientes);
            evaluador.setPacienteLista(listaPacientes);
        }
        return evaluador;
    }
    */


    public Evaluador getFullRater(Evaluador rater_db) {

        Evaluador evaluador = getRater(rater_db.getCedula());
        Log.e("EVAL", "Evaluador " + evaluador);
        if (evaluador != null) {
            List<Schema> listaSchemasEvaluador = getListaSchemas(evaluador.getUuid());
            ListaSchemas ls = new ListaSchemas();
            ls.setSchemas(listaSchemasEvaluador);

            List<Paciente> pacientes = getListaPacientes(evaluador.getUuid());
            ListaPacientes lp = new ListaPacientes();
            lp.setPacientes(pacientes);

            //TODO: ¿Esta lista es nula para Leishmaniasis?
            //evaluador.setListaSchemas(ls);
            evaluador.setPacienteLista(lp);

            for (Paciente p : evaluador.getPacienteLista().getPacientes()) {
                List<Schema> listSchemaPaciente = getListaSchemas(p.getUuid());
                ListaSchemas listaSchemasPaciente = new ListaSchemas();
                listaSchemasPaciente.setSchemas(listSchemaPaciente);

                p.setListaSchemas(listaSchemasPaciente);
                for (Schema s : p.getListaSchemas().getSchemas()) {
                    //ANTECEDENTES
                    List<AntecedenteXml> lista_antecedentes = getListaAntecedentes(s.getUuid());
                    Antecedentes antecedentes = new Antecedentes();
                    antecedentes.setAntecedentes(lista_antecedentes);
                    s.setAntecedentes(antecedentes);

                    //DEVELOPSYMPTOMS
                    List<DevelopSymptom> lista_develop_symptom = getListaSintomas(s.getUuid());
                    ListaSintomas listaSintomas = new ListaSintomas();
                    listaSintomas.setSintomas(lista_develop_symptom);
                    s.setDevelopSymptoms(listaSintomas);

                    //DAYLYSCHEMAS
                    List<DailySchema> lista_daily_schemas = getListaDailySchemas(s.getUuid());
                    ListaDailySchemas listads = new ListaDailySchemas();
                    listads.setDailySchemas(lista_daily_schemas);
                    s.setListaDailySchemas(listads);

                    for (DailySchema d : s.getListaDailySchemas().getDailySchemas()) {
                        List<Prescripcion> listPrescripcion = getListaPrescripciones(d.getUuid());
                        ListaPrescripciones lista_prescripciones = new ListaPrescripciones();
                        lista_prescripciones.setPrescripciones(listPrescripcion);
                        d.setPrescripciones(lista_prescripciones);

                        for (Prescripcion pr : d.getPrescripciones().getPrescripciones()) {
                            BasicAdverseEvent bae = buscarBasicAdverseEvent(pr.getUuid());
                            pr.setEventosBasicos(bae);

                            List<ScheduleTaking> lista_schedule = getListaSheduleTaking(pr.getUuid());
                            ListaScheduleTaking listaScheduleTaking = new ListaScheduleTaking();
                            listaScheduleTaking.setSchedulesTaking(lista_schedule);
                            pr.setDosisTomadas(listaScheduleTaking);
                        }

                        List<UlcerForm> listUlcerForm = getListaImagenesForm(d.getUuid());
                        ListaUlcerForms lista_ulcerF = new ListaUlcerForms();
                        lista_ulcerF.setUlcerForms(listUlcerForm);
                        d.setImagenes(lista_ulcerF);

                        for (UlcerForm uf : d.getImagenes().getUlcerForms()) {
                            List<UIcerImg> listUlcerIMG = getListaImagenes(uf.getUiid());
                            ListaUlcerImages listaUlcerImages = new ListaUlcerImages();
                            listaUlcerImages.setUlcerImages(listUlcerIMG);
                            uf.setUlcerImages(listaUlcerImages);
                        }
                    }
                }
            }
        }
        return evaluador;
    }


    // Elimina todos los datos asociados al Evaluador
    public void deleteDataRater(String evaluadorId) {
        for (Paciente pac : getListaPacientes(evaluadorId)) {
            for (Schema schema : getListaSchemas(pac.getUuid())) {
                for (DevelopSymptom devDis : getListDevelopedDisease(schema.getUuid())) {
                    deleteRow(TABLE_DEVELOPED_DISEASE, devDis.getUuid());
                }
                for (AntecedenteXml ante : getListaAntecedentes(schema.getUuid())) {
                    deleteRow(TABLE_ANTECEDENTES, ante.getUuid());
                }
                for (DailySchema dailySchema : getListaDailySchemas(schema.getUuid())) {
                    for (Prescripcion presc : getListaPrescripciones(dailySchema.getUuid())) {
                        presc = buscarPrescripcionCompleta(presc.getUuid());
                        for (ScheduleTaking scheduleTaking : presc.getDosisTomadas().getSchedulesTaking()) {
                            deleteRow(TABLE_SCHEDULE_TAKING, scheduleTaking.getUuid());
                        }
                        if (presc.getEventosBasicos() != null) {
                            deleteRow(TABLE_BASIC_ADVERSE_EFFECT, presc.getEventosBasicos().getUuid());
                        }
                        deleteRow(TABLE_PRESCRIPCION, presc.getUuid());
                    }
                    for (UlcerForm uIcerForm : getListaImagenesForm(dailySchema.getUuid())) {
                        if (uIcerForm != null) {
                            for (UIcerImg uIcerImg : uIcerForm.getUlcerImages().getUlcerImages()) {
                                deleteRow(TABLE_UICERIMG, uIcerImg.getUuid());
                            }
                            deleteRow(TABLE_UICERFORM, uIcerForm.getUiid());
                        }
                    }
                    deleteRow(TABLE_DAILY_SCHEMA, dailySchema.getUuid());
                }
                deleteRow(TABLE_SCHEMA, schema.getUuid());
            }
            deleteRow(TABLE_PATIENTS, pac.getUuid());
        }
        deleteRow(TABLE_PATIENTS, evaluadorId);
    }

    public Evaluador buscarEvaluadorDePrescripcion(String prescripcionId) {
        String query = "SELECT e.uuid FROM evaluadores e, schemas s, daily_schemas d, prescripciones p" +
                " WHERE p.uuid=? AND p.daily_schemaUUID=d.uuid AND d.schemaUUID=s.uuid" +
                " AND s.evaluadorUUID=e.uuid";
        Evaluador e = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{prescripcionId});
        if (cursor != null && cursor.moveToFirst()) {
            e = buscarEvaluador(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return e;
    }

    public Schema buscarSchemaDePrescripcion(String prescripcionId) {
        String query = "SELECT s.uuid FROM schemas s, daily_schemas d, prescripciones p" +
                " WHERE p.uuid=? AND p.daily_schemaUUID=d.uuid AND d.schemaUUID=s.uuid";
        Schema e = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{prescripcionId});
        if (cursor != null && cursor.moveToFirst()) {
            e = buscarSchema(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return e;
    }

    /* Agregar Paciente */
    public int agregarPaciente(Paciente paciente) {
        ContentValues values = new ContentValues();
        values.put(UUID, paciente.getUuid());
        values.put(PATIENT_NAME, paciente.getName());
        values.put(PATIENT_LAST_NAME, paciente.getLastName());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(PATIENT_BIRTHDAY, formato.format(paciente.getBirthday()));
        values.put(PATIENT_CEDULA, paciente.getCedula());
        values.put(PATIENT_DOCUMENT_TYPE, paciente.getDocumentType());
        values.put(PATIENT_GENRE, paciente.getGenre());
        values.put(FK_PACIENTE_EVALUADOR, paciente.getEvaluadorId());
        return add(TABLE_PATIENTS, values);
    }

    /* Buscar paciente */
    public Paciente buscarPaciente(String id) {
        Paciente paciente = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS,
                new String[]{UUID, PATIENT_NAME, PATIENT_LAST_NAME, PATIENT_BIRTHDAY, PATIENT_CEDULA, PATIENT_DOCUMENT_TYPE, PATIENT_GENRE, FK_PACIENTE_EVALUADOR},
                PATIENT_CEDULA + "=?", new String[]{id}, null,
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                paciente = new Paciente(cursor.getString(0), cursor.getString(1), cursor.getString(2), format.parse(cursor.getString(3)), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), null);
                ListaSchemas listaSchemas = new ListaSchemas();
                listaSchemas.setSchemas(getListaSchemas(paciente.getUuid()));
                paciente.setListaSchemas(listaSchemas);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //if(getListaSchemas(paciente.getUuid()).size()>0)
            //paciente.setListaSchemas(getListaSchemas(paciente.getUuid()));
        }

        cursor.close();
        db.close();
        return paciente;
    }

    /* Editar Paciente */
    public int editarPaciente(Paciente paciente) {
        ContentValues values = new ContentValues();
        values.put(UUID, paciente.getUuid());
        values.put(PATIENT_NAME, paciente.getName());
        values.put(PATIENT_LAST_NAME, paciente.getLastName());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(PATIENT_BIRTHDAY, formato.format(paciente.getBirthday()));
        values.put(PATIENT_CEDULA, paciente.getCedula());
        values.put(PATIENT_DOCUMENT_TYPE, paciente.getDocumentType());
        values.put(PATIENT_GENRE, paciente.getGenre());
        values.put(FK_PACIENTE_EVALUADOR, paciente.getEvaluadorId());
        return edit(TABLE_PATIENTS, values, paciente.getUuid());
    }

    /* Agregar Evaluador */
    public int agregarEvaluador(Evaluador evaluador) {
        ContentValues values = new ContentValues();
        values.put(UUID, evaluador.getUuid());
        values.put(EVALUADOR_NAME, evaluador.getName());
        values.put(EVALUADOR_LAST_NAME, evaluador.getLastName());
        values.put(EVALUADOR_CEDULA, evaluador.getCedula());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(EVALUADOR_LAST_LOGIN, formato.format(evaluador.getLastLogin()));
        return add(TABLE_EVALUADOR, values);

    }

    /* Buscar evaluador */
    public Evaluador buscarEvaluador(String id) {
        Evaluador evaluador = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVALUADOR,
                new String[]{UUID, EVALUADOR_NAME, EVALUADOR_LAST_NAME, EVALUADOR_CEDULA, EVALUADOR_LAST_LOGIN},
                UUID + "=?", new String[]{id}, null,
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                evaluador = new Evaluador(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), format.parse(cursor.getString(4)));
            } catch (Exception e) {
                Log.e("ERROR DB", e.getLocalizedMessage());
            }

            ListaPacientes listaPacientes = new ListaPacientes();
            listaPacientes.setPacientes(getListaPacientes(evaluador.getUuid()));
            evaluador.setPacienteLista(listaPacientes);
            //evaluador.addAllPacientes();
        }

        cursor.close();
        db.close();
        return evaluador;
    }


    /* Returns the user with the specified id, only with its name and identification. */
    public Evaluador getMinimizedUser(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVALUADOR,
                new String[]{UUID, EVALUADOR_NAME, EVALUADOR_LAST_NAME, EVALUADOR_CEDULA, EVALUADOR_LAST_LOGIN},
                EVALUADOR_CEDULA + "=?", new String[]{id},
                null, null, null, null);

        Evaluador evaluador = null;
        if (cursor != null && cursor.moveToFirst()) {

            //   public Evaluador(String uuid, String name, String last_name, String cedula, Date last_login) {
            Log.d("--> getMinimizedUser", cursor.getString(4));
            evaluador = new Evaluador(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), null);
        }
        cursor.close();
        db.close();
        return evaluador;
    }

    public Evaluador getRater(String cedula) {
        Log.e("RESULTADO", "RESULTADO " + cedula);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVALUADOR,
                new String[]{UUID, EVALUADOR_NAME, EVALUADOR_LAST_NAME, EVALUADOR_CEDULA, EVALUADOR_LAST_LOGIN},
                EVALUADOR_CEDULA + "=?", new String[]{cedula},
                null, null, null, null);

        Evaluador evaluador = null;
        if (cursor != null && cursor.moveToFirst()) {

            try {
                evaluador = new Evaluador(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), format.parse(cursor.getString(4)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return evaluador;
    }

    public Evaluador getRaterByUUID(String uuid) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVALUADOR,
                new String[]{UUID, EVALUADOR_NAME, EVALUADOR_LAST_NAME, EVALUADOR_CEDULA, EVALUADOR_LAST_LOGIN},
                UUID + "=?", new String[]{uuid},
                null, null, null, null);

        Evaluador evaluador = null;
        if (cursor != null && cursor.moveToFirst()) {
            try {
                evaluador = new Evaluador(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), format.parse(cursor.getString(4)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return evaluador;
    }


    /* Editar Evaluador */
    public int editarEvaluador(Evaluador evaluador) {
        ContentValues values = new ContentValues();
        values.put(UUID, evaluador.getUuid());
        values.put(EVALUADOR_NAME, evaluador.getName());
        values.put(EVALUADOR_LAST_NAME, evaluador.getLastName());
        values.put(EVALUADOR_CEDULA, evaluador.getCedula());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(EVALUADOR_LAST_LOGIN, formato.format(evaluador.getLastLogin()));
        return edit(TABLE_EVALUADOR, values, evaluador.getUuid());

    }

    /* Editar DailySchema */
    public int editarDailySchema(DailySchema dailySchema) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        ContentValues values = new ContentValues();
        values.put(UUID, dailySchema.getUuid());
        values.put(DAILY_SCHEMA_DAY, dailySchema.getDayOfTreatment());
        values.put(DAILY_SCHEMA_DATE, formato.format(dailySchema.getDateOfTreatment()));
        values.put(DAILY_SCHEMA_FLAG, "" + dailySchema.isFlag());
        values.put(FK_DAILY_SCHEMA_SCHEMA, dailySchema.getSchemaId());
        return edit(TABLE_DAILY_SCHEMA, values, dailySchema.getUuid());
    }

    /* Retorna la lista de pacientes de un evaluador. Cada uno con id, nombre, apellido y cédula */
    public List<Paciente> getListaPacientes(String evaluadorId) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        List<Paciente> patients = new ArrayList<Paciente>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS, new String[]{UUID, PATIENT_NAME, PATIENT_LAST_NAME, PATIENT_CEDULA, PATIENT_BIRTHDAY, PATIENT_DOCUMENT_TYPE, PATIENT_GENRE, FK_PACIENTE_EVALUADOR}, FK_PACIENTE_EVALUADOR + "=?", new String[]{evaluadorId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {// agregar validacion de tener schemas activos
                Paciente patient = null;
                try {
                    patient = new Paciente(cursor.getString(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), format.parse(cursor.getString(4)), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                patients.add(patient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return patients;
    }

    /* Agregar Schema */
    public int agregarSchema(Schema schema) {
        ContentValues values = new ContentValues();
        values.put(UUID, schema.getUuid());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(SCHEMA_DATE_START, formato.format(schema.getDateStart()));
        values.put(SCHEMA_DATE_END, formato.format(schema.getDateEnd()));
        values.put(SCHEMA_ACTIVE, "" + schema.isActive());
        values.put(FK_SCHEMA_EVALUADOR, schema.getEvaluadorId());
        values.put(FK_SCHEMA_PACIENTE, schema.getPatientId());
        return add(TABLE_SCHEMA, values);
    }

    /* Buscar Schema */
    public Schema buscarSchema(String id) {
        Schema schema = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCHEMA,
                new String[]{UUID, SCHEMA_DATE_START, SCHEMA_DATE_END, SCHEMA_ACTIVE, FK_SCHEMA_EVALUADOR, FK_SCHEMA_PACIENTE},
                UUID + "=?", new String[]{id}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            //if(cursor.getString(3).equals("true")){
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                schema = new Schema(cursor.getString(0), format.parse(cursor.getString(1)), format.parse(cursor.getString(2)),
                        cursor.getString(3).equals("true") ? true : false, cursor.getString(4), cursor.getString(5));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            ListaDailySchemas listaDailySchemas = new ListaDailySchemas();
            listaDailySchemas.setDailySchemas(getListaDailySchemas(schema.getUuid()));
            schema.setListaDailySchemas(listaDailySchemas);

            ListaSintomas listaSintomas = new ListaSintomas();
            listaSintomas.setSintomas(getListDevelopedDisease(schema.getUuid()));
            schema.setDevelopSymptoms(listaSintomas);


            Antecedentes antecedentes = new Antecedentes();
            antecedentes.setAntecedentes(getListaAntecedentes(schema.getUuid()));
            schema.setAntecedentes(antecedentes);
            //}
        }

        cursor.close();
        db.close();
        return schema;
    }

    /* Retorna la lista de schemas de un paciente. */
    public List<Schema> getListaSchemas(String pacienteId) {
        List<Schema> schemas = new ArrayList<Schema>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCHEMA,
                new String[]{UUID, SCHEMA_DATE_START, SCHEMA_DATE_END, SCHEMA_ACTIVE, FK_SCHEMA_EVALUADOR, FK_SCHEMA_PACIENTE},
                FK_SCHEMA_PACIENTE + "=?", new String[]{pacienteId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {//validar que el schema esté activo
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                Schema schema = null;
                try {
                    schema = new Schema(cursor.getString(0), formato.parse(cursor.getString(1)), formato.parse(cursor.getString(2)),
                            cursor.getString(3).equals("true") ? true : false, cursor.getString(4), cursor.getString(5));

                    schemas.add(schema);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return schemas;
    }

    /* Editar Schema */
    public int editarSchema(Schema schema) {
        ContentValues values = new ContentValues();
        values.put(UUID, schema.getUuid());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(SCHEMA_DATE_START, formato.format(schema.getDateStart()));
        values.put(SCHEMA_DATE_END, formato.format(schema.getDateEnd()));
        values.put(SCHEMA_ACTIVE, "" + schema.isActive());
        values.put(FK_SCHEMA_EVALUADOR, schema.getEvaluadorId());
        values.put(FK_SCHEMA_PACIENTE, schema.getPatientId());
        return edit(TABLE_SCHEMA, values, schema.getUuid());

    }

    // Agregar DailySchema
    public int agregarDailySchema(DailySchema dailySchema) {
        ContentValues values = new ContentValues();
        values.put(UUID, dailySchema.getUuid());
        values.put(DAILY_SCHEMA_DAY, dailySchema.getDayOfTreatment());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(DAILY_SCHEMA_DATE, formato.format(dailySchema.getDateOfTreatment()));
        values.put(DAILY_SCHEMA_FLAG, "" + dailySchema.isFlag());
        values.put(FK_DAILY_SCHEMA_SCHEMA, dailySchema.getSchemaId());
        return add(TABLE_DAILY_SCHEMA, values);

    }

    /* Retorna la lista de dailySchemas de un Schema. */
    public List<DailySchema> getListaDailySchemas(String schemaId) {
        List<DailySchema> dailySchemas = new ArrayList<DailySchema>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DAILY_SCHEMA,
                new String[]{UUID, DAILY_SCHEMA_DAY, DAILY_SCHEMA_DATE, DAILY_SCHEMA_FLAG, FK_DAILY_SCHEMA_SCHEMA},
                FK_DAILY_SCHEMA_SCHEMA + "=?", new String[]{schemaId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                DailySchema dailySchema = null;
                try {
                    dailySchema = new DailySchema(cursor.getString(0), cursor.getString(1),
                            formato.parse(cursor.getString(2)), cursor.getString(3).equals("true") ? true : false, cursor.getString(4));
                    dailySchemas.add(dailySchema);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dailySchemas;
    }

    // Editar DailySchema
    public int editrDailySchema(DailySchema dailySchema) {
        ContentValues values = new ContentValues();
        values.put(UUID, dailySchema.getUuid());
        values.put(DAILY_SCHEMA_DAY, dailySchema.getDayOfTreatment());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(DAILY_SCHEMA_DATE, formato.format(dailySchema.getDateOfTreatment()));
        values.put(DAILY_SCHEMA_FLAG, "" + dailySchema.isFlag());
        values.put(FK_DAILY_SCHEMA_SCHEMA, dailySchema.getSchemaId());
        return edit(TABLE_DAILY_SCHEMA, values, dailySchema.getUuid());
    }

    /* Buscar DailySchema por schema y día*/
    public DailySchema buscarDailySchema(String schemaId, Date date) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = formato.format(date);
        DailySchema dailySchema = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DAILY_SCHEMA,
                new String[]{UUID, DAILY_SCHEMA_DAY, DAILY_SCHEMA_DATE, DAILY_SCHEMA_FLAG, FK_DAILY_SCHEMA_SCHEMA},
                FK_DAILY_SCHEMA_SCHEMA + "=? AND " + DAILY_SCHEMA_DATE + "=?", new String[]{schemaId, fecha}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            //if(cursor.getString(3).equals("true")){
            try {
                dailySchema = new DailySchema(cursor.getString(0), cursor.getString(1),
                        formato.parse(cursor.getString(2)), cursor.getString(3).equals("true") ? true : false, cursor.getString(4));

                ListaPrescripciones listaPrescripciones = new ListaPrescripciones();
                listaPrescripciones.setPrescripciones(getListaPrescripciones(dailySchema.getUuid()));
                dailySchema.setPrescripciones(listaPrescripciones);

                ListaUlcerForms listaUlcerForms = new ListaUlcerForms();
                listaUlcerForms.setUlcerForms(getListaImagenesForm(dailySchema.getUuid()));
                dailySchema.setImagenes(listaUlcerForms);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //*** agregar Prescripciones, ulcerImg para validar

            //}
        }

        cursor.close();
        db.close();
        return dailySchema;
    }

    // Agregar Antecedentes
    public int agregarAntecedentes(AntecedenteXml antecedente) {
        ContentValues values = new ContentValues();
        values.put(UUID, antecedente.getUuid());
        values.put(ANTECEDENTES_NAME, antecedente.getName());
        values.put(MEDICINE_NAME, antecedente.getMedicineName());
        values.put(FK_ANTECEDENTES_SCHEMA, antecedente.getSchemaId());
        return add(TABLE_ANTECEDENTES, values);
    }

    /* Buscar Antecedente por schema y día*/
    public AntecedenteXml buscarAntecedentes(String schemaId) {
        AntecedenteXml antecedente = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ANTECEDENTES,
                new String[]{UUID, ANTECEDENTES_NAME, MEDICINE_NAME, FK_ANTECEDENTES_SCHEMA},
                UUID + "=?", new String[]{schemaId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            //if(cursor.getString(3).equals("true")){
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            antecedente = new AntecedenteXml(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3));
            //dailySchema.setListaDailySchemas(getListaDailySchemas(schema.getUuid()));
            //}
        }
        cursor.close();
        db.close();
        return antecedente;
    }

    // Editar Antecedentes
    public int editarAntecedentes(AntecedenteXml antecedente) {
        ContentValues values = new ContentValues();
        values.put(UUID, antecedente.getUuid());
        values.put(ANTECEDENTES_NAME, antecedente.getName());
        values.put(MEDICINE_NAME, antecedente.getMedicineName());
        values.put(FK_ANTECEDENTES_SCHEMA, antecedente.getSchemaId());
        return edit(TABLE_ANTECEDENTES, values, antecedente.getUuid());
    }

    /* Retorna la lista de antecedentes de un DailySchema. */
    public List<AntecedenteXml> getListaAntecedentes(String schemaId) {
        List<AntecedenteXml> antecedentes = new ArrayList<AntecedenteXml>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ANTECEDENTES,
                new String[]{UUID, ANTECEDENTES_NAME, MEDICINE_NAME, FK_ANTECEDENTES_SCHEMA},
                FK_ANTECEDENTES_SCHEMA + "=?", new String[]{schemaId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                AntecedenteXml antecedente = null;

                antecedente = new AntecedenteXml(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));

                antecedentes.add(antecedente);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return antecedentes;
    }

    // Agregar Prescripcion
    public int agregarPrescripcion(Prescripcion prescripcion) {
        ContentValues values = new ContentValues();
        values.put(UUID, prescripcion.getUuid());
        values.put(PRESCRIPCION_NAME, prescripcion.getName());
        values.put(PRESCRIPCION_DOSIS, prescripcion.getDosis());
        values.put(PRESCRIPCION_NUMERO_LOTE, prescripcion.getNumeroLote());
        values.put(PRESCRIPCION_COMENTARIOS, prescripcion.getComentarios());
        values.put(FK_PRESCRIPCION_DAILY_SCHEMA, prescripcion.getDailySchemaId());
        return add(TABLE_PRESCRIPCION, values);
    }

    /* Retorna la lista de prescripciones de un DailySchema. */
    public List<Prescripcion> getListaPrescripciones(String dailySchemaId) {
        List<Prescripcion> prescripciones = new ArrayList<Prescripcion>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRESCRIPCION,
                new String[]{UUID, PRESCRIPCION_NAME, PRESCRIPCION_DOSIS, PRESCRIPCION_NUMERO_LOTE, PRESCRIPCION_COMENTARIOS, FK_PRESCRIPCION_DAILY_SCHEMA},
                FK_PRESCRIPCION_DAILY_SCHEMA + "=?", new String[]{dailySchemaId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Prescripcion prescripcion = new Prescripcion(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));

                prescripciones.add(prescripcion);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return prescripciones;
    }

    public BasicAdverseEvent getBasicEventsByPrescriptionID(String prescriptionID) {
        SQLiteDatabase db = this.getReadableDatabase();
        BasicAdverseEvent basicAdverseEvent = null;
        Cursor cursor = db.query(TABLE_BASIC_ADVERSE_EFFECT,
                new String[]{UUID, BASIC_ADVERSE_EFFECT_NAME,
                        BASIC_ADVERSE_EFFECT_DOLOR_SITIO, BASIC_ADVERSE_EFFECT_INFECCION_SITIO,
                        BASIC_ADVERSE_EFFECT_MALESTAR_GENERAL, BASIC_ADVERSE_EFFECT_DOLOR_MUSCULAR,
                        BASIC_ADVERSE_EFFECT_DOLOR_ARTICULACIONES, BASIC_ADVERSE_EFFECT_DOLOR_CABEZA, BASIC_ADVERSE_EFFECT_FIEBRE,
                        BASIC_ADVERSE_EFFECT_NAUSEAS, BASIC_ADVERSE_EFFECT_VOMITO, BASIC_ADVERSE_EFFECT_DIARREA,
                        BASIC_ADVERSE_EFFECT_DOLOR_ABDOMINAL, BASIC_ADVERSE_EFFECT_PERDIDA_APETITO, BASIC_ADVERSE_EFFECT_MAREO,
                        BASIC_ADVERSE_EFFECT_PALPITACIONES, FK_BASIC_ADVERSE_EFFECT_PRESCRIPTIONUUID},
                FK_BASIC_ADVERSE_EFFECT_PRESCRIPTIONUUID + "=?", new String[]{prescriptionID}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            basicAdverseEvent = new BasicAdverseEvent(cursor.getString(0), cursor.getString(1)
                    , cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5),
                    cursor.getInt(6), cursor.getInt(7), cursor.getInt(8), cursor.getInt(9),
                    cursor.getInt(10), cursor.getInt(11), cursor.getInt(12), cursor.getInt(13)
                    , cursor.getInt(14), cursor.getInt(15), cursor.getString(16));
        }
        cursor.close();
        db.close();
        return basicAdverseEvent;
    }

    /* Buscar prescripcion. */
    public Prescripcion buscarPrescripcionCompleta(String prescripcionId) {
        Prescripcion prescripcion = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRESCRIPCION,
                new String[]{UUID, PRESCRIPCION_NAME, PRESCRIPCION_DOSIS, PRESCRIPCION_NUMERO_LOTE, PRESCRIPCION_COMENTARIOS, FK_PRESCRIPCION_DAILY_SCHEMA},
                UUID + "=?", new String[]{prescripcionId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                prescripcion = new Prescripcion(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5));

                prescripcion.setEventosBasicos(getBasicEventsByPrescriptionID(prescripcion.getUuid()));

                ListaScheduleTaking listaScheduleTaking = new ListaScheduleTaking();
                listaScheduleTaking.setSchedulesTaking(getListaSheduleTaking(prescripcion.getUuid()));
                prescripcion.setDosisTomadas(listaScheduleTaking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return prescripcion;
    }

    // Editar Prescripcion
    public int editarPrescripcion(Prescripcion prescripcion) {
        ContentValues values = new ContentValues();
        values.put(UUID, prescripcion.getUuid());
        values.put(PRESCRIPCION_NAME, prescripcion.getName());
        values.put(PRESCRIPCION_DOSIS, prescripcion.getDosis());
        values.put(PRESCRIPCION_NUMERO_LOTE, prescripcion.getNumeroLote());
        values.put(PRESCRIPCION_COMENTARIOS, prescripcion.getComentarios());
        values.put(FK_PRESCRIPCION_DAILY_SCHEMA, prescripcion.getDailySchemaId());
        return edit(TABLE_PRESCRIPCION, values, prescripcion.getUuid());
    }

    // Agregar Developed disease
    public int agregarDevelopedDisease(DevelopSymptom developSymptom) {
        ContentValues values = new ContentValues();
        values.put(UUID, developSymptom.getUuid());
        values.put(DEVELOPED_DISEASE_NAME, developSymptom.getName());
        values.put(DEVELOPED_DISEASE_MEDICINE_NAME, developSymptom.getMedicineName());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        values.put(DEVELOPED_DISEASE_IS_ACTIVE, "" + developSymptom.isActive());

        String fecha_inicio = formato.format(developSymptom.getDate_start());
        values.put(DEVELOPED_DISEASE_INITIAL_DATE, fecha_inicio);
        if (developSymptom.getDate_end() != null) {
            values.put(DEVELOPED_DISEASE_REMOVE_DATE, formato.format(developSymptom.getDate_end()));
        }
        values.put(FK_DEVELOPED_DISEASE_SCHEMA, developSymptom.getSchemaId());
        return add(TABLE_DEVELOPED_DISEASE, values);
    }

    /* Buscar developed disease. */ // ******** Deberia buscarse por schema -- YA LO HICIMOS!!
    public DevelopSymptom buscarDevelopedDisease(String schemaId) {
        DevelopSymptom developSymptom = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEVELOPED_DISEASE,
                new String[]{UUID,
                        DEVELOPED_DISEASE_NAME,
                        DEVELOPED_DISEASE_ANSWER,
                        DEVELOPED_DISEASE_MEDICINE_NAME,
                        DEVELOPED_DISEASE_IS_ACTIVE,
                        DEVELOPED_DISEASE_INITIAL_DATE,
                        DEVELOPED_DISEASE_REMOVE_DATE,
                        FK_DEVELOPED_DISEASE_SCHEMA},
                FK_DEVELOPED_DISEASE_SCHEMA + "=?", new String[]{schemaId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    if (cursor.getString(6) == null) {
                        developSymptom = new DevelopSymptom(cursor.getString(0), cursor.getString(1),
                                cursor.getString(2), cursor.getString(3), cursor.getString(4).equals("true") ? true : false,
                                formato.parse(cursor.getString(5)), formato.parse(cursor.getString(6)), cursor.getString(7));
                    } else {
                        developSymptom = new DevelopSymptom(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                                cursor.getString(3), cursor.getString(4).equals("true") ? true : false,
                                formato.parse(cursor.getString(5)), formato.parse(cursor.getString(6)), cursor.getString(7));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //prescripciones.add(prescripcion);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return developSymptom;
    }

    /* Lista de developed disease de un schema. */
    public List<DevelopSymptom> getListDevelopedDisease(String schemaId) {
        ArrayList<DevelopSymptom> sintomas = new ArrayList<DevelopSymptom>();
        DevelopSymptom developSymptom = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEVELOPED_DISEASE,
                new String[]{UUID,
                        DEVELOPED_DISEASE_NAME,
                        DEVELOPED_DISEASE_ANSWER,
                        DEVELOPED_DISEASE_MEDICINE_NAME,
                        DEVELOPED_DISEASE_IS_ACTIVE,
                        DEVELOPED_DISEASE_INITIAL_DATE,
                        DEVELOPED_DISEASE_REMOVE_DATE,
                        FK_DEVELOPED_DISEASE_SCHEMA},
                FK_DEVELOPED_DISEASE_SCHEMA + "=?", new String[]{schemaId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    if (cursor.getString(6) == null) {
                        developSymptom = new DevelopSymptom(cursor.getString(0), cursor.getString(1),
                                cursor.getString(2), cursor.getString(3), cursor.getString(4).equals("true") ? true : false,
                                formato.parse(cursor.getString(5)), null, cursor.getString(7));
                    } else {
                        developSymptom = new DevelopSymptom(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                                cursor.getString(3), cursor.getString(4).equals("true") ? true : false,
                                formato.parse(cursor.getString(5)), formato.parse(cursor.getString(6)), cursor.getString(7));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                sintomas.add(developSymptom);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sintomas;
    }

    // Editar Developed disease
    public int editarDevelopedDisease(DevelopSymptom developSymptom) {
        ContentValues values = new ContentValues();
        values.put(UUID, developSymptom.getUuid());
        values.put(DEVELOPED_DISEASE_NAME, developSymptom.getName());
        values.put(DEVELOPED_DISEASE_ANSWER, developSymptom.getAnswer());
        values.put(DEVELOPED_DISEASE_MEDICINE_NAME, developSymptom.getMedicineName());
        values.put(DEVELOPED_DISEASE_IS_ACTIVE, "" + developSymptom.isActive());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(DEVELOPED_DISEASE_INITIAL_DATE, formato.format(developSymptom.getDate_start()));

        if (developSymptom.getDate_end() != null) {
            values.put(DEVELOPED_DISEASE_REMOVE_DATE, formato.format(developSymptom.getDate_end()));
        }
        values.put(FK_DEVELOPED_DISEASE_SCHEMA, developSymptom.getSchemaId());
        return edit(TABLE_DEVELOPED_DISEASE, values, developSymptom.getUuid());

    }

    // Agregar Schedule Taking
    public int agregarScheduleTaking(ScheduleTaking scheduleTaking) {
        ContentValues values = new ContentValues();
        values.put(UUID, scheduleTaking.getUuid());
        values.put(SCHEDULE_TAKING_TIME, scheduleTaking.getTime());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(SCHEDULE_TAKING_DATE, formato.format(scheduleTaking.getDate()));
        values.put(FK_SCHEDULE_TAKING_PRESCRIPTIONUUID, scheduleTaking.getPrescriptionId());
        return add(TABLE_SCHEDULE_TAKING, values);
    }

    /* Buscar schedule taking. */ // ******** Deberia buscarse por prescripcion
    public ScheduleTaking buscarScheduleTaking(String scheduleTakingId) {
        ScheduleTaking scheduleTaking = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCHEDULE_TAKING,
                new String[]{UUID, SCHEDULE_TAKING_TIME, SCHEDULE_TAKING_DATE, FK_SCHEDULE_TAKING_PRESCRIPTIONUUID},
                UUID + "=?", new String[]{scheduleTakingId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    scheduleTaking = new ScheduleTaking(cursor.getString(0), cursor.getString(1), formato.parse(cursor.getString(2)),
                            cursor.getString(3));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //prescripciones.add(prescripcion);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scheduleTaking;
    }

    /* Buscar schedule takings de una prescripcion. */
    public List<ScheduleTaking> getListaSheduleTaking(String prescripcionId) {
        ArrayList<ScheduleTaking> takings = null;
        try {
            takings = new ArrayList<ScheduleTaking>();
            ScheduleTaking scheduleTaking = null;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_SCHEDULE_TAKING,
                    new String[]{UUID, SCHEDULE_TAKING_TIME, SCHEDULE_TAKING_DATE, FK_SCHEDULE_TAKING_PRESCRIPTIONUUID},
                    FK_SCHEDULE_TAKING_PRESCRIPTIONUUID + "=?", new String[]{prescripcionId}, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        scheduleTaking = new ScheduleTaking(cursor.getString(0), cursor.getString(1), formato.parse(cursor.getString(2)),
                                cursor.getString(3));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    takings.add(scheduleTaking);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("ERROR DESCONOCIDO", e.getLocalizedMessage());
        }
        return takings;
    }

    // Editar Schedule Taking
    public int editarScheduleTaking(ScheduleTaking scheduleTaking) {
        ContentValues values = new ContentValues();
        values.put(UUID, scheduleTaking.getUuid());
        values.put(SCHEDULE_TAKING_TIME, scheduleTaking.getTime());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(SCHEDULE_TAKING_DATE, formato.format(scheduleTaking.getDate()));
        values.put(FK_SCHEDULE_TAKING_PRESCRIPTIONUUID, scheduleTaking.getPrescriptionId());
        return edit(TABLE_SCHEDULE_TAKING, values, scheduleTaking.getUuid());
    }

    // Agregar Basic Adverse Effect
    public int agregarBasicAdverseEffect(BasicAdverseEvent basicAdverseEvent) {
        ContentValues values = new ContentValues();
        values.put(UUID, basicAdverseEvent.getUuid());
        values.put(BASIC_ADVERSE_EFFECT_NAME, basicAdverseEvent.getName());
        values.put(BASIC_ADVERSE_EFFECT_DOLOR_SITIO, basicAdverseEvent.getDolorSitio());
        values.put(BASIC_ADVERSE_EFFECT_INFECCION_SITIO, basicAdverseEvent.getInfeccionSitio());
        values.put(BASIC_ADVERSE_EFFECT_MALESTAR_GENERAL, basicAdverseEvent.getMalestarGeneral());
        values.put(BASIC_ADVERSE_EFFECT_DOLOR_MUSCULAR, basicAdverseEvent.getDolorMuscular());
        values.put(BASIC_ADVERSE_EFFECT_DOLOR_ARTICULACIONES, basicAdverseEvent.getDolorArticulaciones());
        values.put(BASIC_ADVERSE_EFFECT_DOLOR_CABEZA, basicAdverseEvent.getDolorCabeza());
        values.put(BASIC_ADVERSE_EFFECT_FIEBRE, basicAdverseEvent.getFiebre());
        values.put(BASIC_ADVERSE_EFFECT_NAUSEAS, basicAdverseEvent.getNauseas());
        values.put(BASIC_ADVERSE_EFFECT_VOMITO, basicAdverseEvent.getVomito());
        values.put(BASIC_ADVERSE_EFFECT_DIARREA, basicAdverseEvent.getDiarrea());
        values.put(BASIC_ADVERSE_EFFECT_DOLOR_ABDOMINAL, basicAdverseEvent.getDolorAbdominal());
        values.put(BASIC_ADVERSE_EFFECT_PERDIDA_APETITO, basicAdverseEvent.getPerdidaApetito());
        values.put(BASIC_ADVERSE_EFFECT_MAREO, basicAdverseEvent.getMareo());
        values.put(BASIC_ADVERSE_EFFECT_PALPITACIONES, basicAdverseEvent.getPalpitaciones());
        values.put(FK_BASIC_ADVERSE_EFFECT_PRESCRIPTIONUUID, basicAdverseEvent.getPrescriptionId());
        return add(TABLE_BASIC_ADVERSE_EFFECT, values);
    }

    /* Obtiene el basic adverse event de una prescripcion. */
    public BasicAdverseEvent buscarBasicAdverseEvent(String prescripcionId) {
        BasicAdverseEvent basicAdverseEvent = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BASIC_ADVERSE_EFFECT,
                new String[]{UUID, BASIC_ADVERSE_EFFECT_NAME, BASIC_ADVERSE_EFFECT_DOLOR_SITIO,
                        BASIC_ADVERSE_EFFECT_INFECCION_SITIO, BASIC_ADVERSE_EFFECT_MALESTAR_GENERAL,
                        BASIC_ADVERSE_EFFECT_DOLOR_MUSCULAR, BASIC_ADVERSE_EFFECT_DOLOR_ARTICULACIONES,
                        BASIC_ADVERSE_EFFECT_DOLOR_CABEZA, BASIC_ADVERSE_EFFECT_FIEBRE,
                        BASIC_ADVERSE_EFFECT_NAUSEAS, BASIC_ADVERSE_EFFECT_VOMITO,
                        BASIC_ADVERSE_EFFECT_DIARREA, BASIC_ADVERSE_EFFECT_DOLOR_ABDOMINAL,
                        BASIC_ADVERSE_EFFECT_PERDIDA_APETITO, BASIC_ADVERSE_EFFECT_MAREO,
                        BASIC_ADVERSE_EFFECT_PALPITACIONES, FK_BASIC_ADVERSE_EFFECT_PRESCRIPTIONUUID
                },
                FK_BASIC_ADVERSE_EFFECT_PRESCRIPTIONUUID + "=?", new String[]{prescripcionId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                basicAdverseEvent = new BasicAdverseEvent(cursor.getString(0), cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),
                        Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)),
                        Integer.parseInt(cursor.getString(10)), Integer.parseInt(cursor.getString(11)),
                        Integer.parseInt(cursor.getString(12)), Integer.parseInt(cursor.getString(13)),
                        Integer.parseInt(cursor.getString(14)), Integer.parseInt(cursor.getString(15)),
                        cursor.getString(16));
                //basicAdverseEvent.setEventosAdversos(getListaAdverseEvent(basicAdverseEvent.getUuid()));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return basicAdverseEvent;
    }

    /* Obtiene el basic adverse event a partir de su id. */
    public BasicAdverseEvent buscarBasicAdverseEventById(String basicId) {
        BasicAdverseEvent basicAdverseEvent = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BASIC_ADVERSE_EFFECT,
                new String[]{UUID, BASIC_ADVERSE_EFFECT_NAME, BASIC_ADVERSE_EFFECT_DOLOR_SITIO,
                        BASIC_ADVERSE_EFFECT_INFECCION_SITIO, BASIC_ADVERSE_EFFECT_MALESTAR_GENERAL,
                        BASIC_ADVERSE_EFFECT_DOLOR_MUSCULAR, BASIC_ADVERSE_EFFECT_DOLOR_ARTICULACIONES,
                        BASIC_ADVERSE_EFFECT_DOLOR_CABEZA, BASIC_ADVERSE_EFFECT_FIEBRE,
                        BASIC_ADVERSE_EFFECT_NAUSEAS, BASIC_ADVERSE_EFFECT_VOMITO,
                        BASIC_ADVERSE_EFFECT_DIARREA, BASIC_ADVERSE_EFFECT_DOLOR_ABDOMINAL,
                        BASIC_ADVERSE_EFFECT_PERDIDA_APETITO, BASIC_ADVERSE_EFFECT_MAREO,
                        BASIC_ADVERSE_EFFECT_PALPITACIONES, FK_BASIC_ADVERSE_EFFECT_PRESCRIPTIONUUID
                },
                UUID + "=?", new String[]{basicId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                basicAdverseEvent = new BasicAdverseEvent(cursor.getString(0), cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),
                        Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)),
                        Integer.parseInt(cursor.getString(10)), Integer.parseInt(cursor.getString(11)),
                        Integer.parseInt(cursor.getString(12)), Integer.parseInt(cursor.getString(13)),
                        Integer.parseInt(cursor.getString(14)), Integer.parseInt(cursor.getString(15)),
                        cursor.getString(16));
                //basicAdverseEvent.setEventosAdversos(getListaAdverseEvent(basicAdverseEvent.getUuid()));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return basicAdverseEvent;
    }

    // Editar Basic Adverse Event
    public int editarBasicAdverseEvent(BasicAdverseEvent basicAdverseEvent) {
        ContentValues values = new ContentValues();
        values.put(UUID, basicAdverseEvent.getUuid());
        values.put(BASIC_ADVERSE_EFFECT_NAME, basicAdverseEvent.getName());
        values.put(BASIC_ADVERSE_EFFECT_DOLOR_SITIO, basicAdverseEvent.getDolorSitio());
        values.put(BASIC_ADVERSE_EFFECT_INFECCION_SITIO, basicAdverseEvent.getInfeccionSitio());
        values.put(BASIC_ADVERSE_EFFECT_MALESTAR_GENERAL, basicAdverseEvent.getMalestarGeneral());
        values.put(BASIC_ADVERSE_EFFECT_DOLOR_MUSCULAR, basicAdverseEvent.getDolorMuscular());
        values.put(BASIC_ADVERSE_EFFECT_DOLOR_ARTICULACIONES, basicAdverseEvent.getDolorArticulaciones());
        values.put(BASIC_ADVERSE_EFFECT_DOLOR_CABEZA, basicAdverseEvent.getDolorCabeza());
        values.put(BASIC_ADVERSE_EFFECT_FIEBRE, basicAdverseEvent.getFiebre());
        values.put(BASIC_ADVERSE_EFFECT_NAUSEAS, basicAdverseEvent.getNauseas());
        values.put(BASIC_ADVERSE_EFFECT_VOMITO, basicAdverseEvent.getVomito());
        values.put(BASIC_ADVERSE_EFFECT_DIARREA, basicAdverseEvent.getDiarrea());
        values.put(BASIC_ADVERSE_EFFECT_DOLOR_ABDOMINAL, basicAdverseEvent.getDolorAbdominal());
        values.put(BASIC_ADVERSE_EFFECT_PERDIDA_APETITO, basicAdverseEvent.getPerdidaApetito());
        values.put(BASIC_ADVERSE_EFFECT_MAREO, basicAdverseEvent.getMareo());
        values.put(BASIC_ADVERSE_EFFECT_PALPITACIONES, basicAdverseEvent.getPalpitaciones());
        values.put(FK_BASIC_ADVERSE_EFFECT_PRESCRIPTIONUUID, basicAdverseEvent.getPrescriptionId());
        return edit(TABLE_BASIC_ADVERSE_EFFECT, values, basicAdverseEvent.getUuid());
    }

    // Agregar UIcerForm
    public int agregarUIcerForm(UlcerForm ulcerForm) {
        ContentValues values = new ContentValues();
        values.put(UUID, ulcerForm.getUiid());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(UICERFORM_DATE, formato.format(ulcerForm.getDate()));
        values.put(UICERFORM_NOTE_GENERAL, ulcerForm.getNoteGeneral());
        values.put(FK_UICERFORM_DAILY_SCHEM, ulcerForm.getDailySchemaId());
        return add(TABLE_UICERFORM, values);
    }

    /* Retorna la lista de uiForms de un DailySchema. */
    public List<UlcerForm> getListaImagenesForm(String dailySchemaId) {
        List<UlcerForm> uIcerForms = new ArrayList<UlcerForm>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_UICERFORM,
                new String[]{UUID, UICERFORM_NOTE_GENERAL, UICERFORM_DATE, FK_UICERFORM_DAILY_SCHEM},
                FK_UICERFORM_DAILY_SCHEM + "=?", new String[]{dailySchemaId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                UlcerForm uIcerForm = null;
                try {

                    uIcerForm = new UlcerForm(cursor.getString(0), cursor.getString(1),
                            formato.parse(cursor.getString(2)), cursor.getString(3));

                    ListaUlcerImages listaUlcerImages = new ListaUlcerImages();
                    listaUlcerImages.setUlcerImages(getListaImagenes(uIcerForm.getUiid()));
                    uIcerForm.setUlcerImages(listaUlcerImages);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                uIcerForms.add(uIcerForm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return uIcerForms;
    }

    // Editar UIcerForm
    public int editarUIcerForm(UlcerForm ulcerForm) {
        ContentValues values = new ContentValues();
        values.put(UUID, ulcerForm.getUiid());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(UICERFORM_DATE, formato.format(ulcerForm.getDate()));
        values.put(UICERFORM_NOTE_GENERAL, ulcerForm.getNoteGeneral());
        values.put(FK_UICERFORM_DAILY_SCHEM, ulcerForm.getDailySchemaId());
        return edit(TABLE_UICERFORM, values, ulcerForm.getUiid());
    }

    // Agregar UIcerImg
    public int agregarUIcerImg(UIcerImg uIcerImg) {
        ContentValues values = new ContentValues();
        values.put(UUID, uIcerImg.getUuid());
        values.put(UICERIMG_IMGUUID, uIcerImg.getImgUUID());
        values.put(UICERIMG_BODY_LOCATION, uIcerImg.getBodyLocation());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(UICERIMG_IMG_DATE, formato.format(uIcerImg.getImgDate()));
        values.put(UIINJURIES_LOCATION, uIcerImg.getInjuriesPerLocation());
        values.put(UICERIMG_IMG_FORMAT, uIcerImg.getImgFormat());
        values.put(FK_UICERIMG_UICERFORM, uIcerImg.getUIcerFormId());
        return add(TABLE_UICERIMG, values);
    }

    /* Retorna la lista de uiImg de un UIForm. */
    public List<UIcerImg> getListaImagenes(String ulcerFormId) {
        List<UIcerImg> uIcerImgs = new ArrayList<UIcerImg>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_UICERIMG,
                new String[]{UUID, UICERIMG_IMGUUID, UICERIMG_BODY_LOCATION, UICERIMG_IMG_DATE,
                        UIINJURIES_LOCATION, UICERIMG_IMG_FORMAT, FK_UICERIMG_UICERFORM},
                FK_UICERIMG_UICERFORM + "=?", new String[]{ulcerFormId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                UIcerImg uIcerImg = null;


                try {
                    uIcerImg = new UIcerImg(cursor.getString(0), cursor.getString(2), formato.parse(cursor.getString(3)), cursor.getString(5), cursor.getString(1), cursor.getString(4), cursor.getString(6));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                uIcerImgs.add(uIcerImg);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return uIcerImgs;
    }


    // Editar UIcerImg
    public int editarUIcerImg(UIcerImg uIcerImg) {
        ContentValues values = new ContentValues();
        values.put(UUID, uIcerImg.getUuid());
        values.put(UICERIMG_IMGUUID, uIcerImg.getImgUUID());
        values.put(UICERIMG_BODY_LOCATION, uIcerImg.getBodyLocation());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        values.put(UICERIMG_IMG_DATE, formato.format(uIcerImg.getImgDate()));
        values.put(UIINJURIES_LOCATION, formato.format(uIcerImg.getImgDate()));
        values.put(UICERIMG_IMG_FORMAT, uIcerImg.getImgFormat());
        values.put(FK_UICERIMG_UICERFORM, uIcerImg.getUIcerFormId());
        return edit(TABLE_UICERIMG, values, uIcerImg.getUuid());
    }
    /*
    // SynAudict
    private static final String TABLE_SYNAUDICT = "syn_audict";
    private static final String SYNAUDICT_DATA_ENTITY = "data_entity";
    private static final String SYNAUDICT_ISSUES = "issues";
    private static final String SYNAUDICT_DATE = "date";
    private static final String SYNAUDICT_STATE = "state";

    // Claves foráneas*/


    public Paciente buscarPacientePorCedula(String cedula) {
        Paciente p = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS,
                new String[]{UUID, PATIENT_NAME, PATIENT_LAST_NAME, PATIENT_CEDULA, PATIENT_BIRTHDAY, PATIENT_DOCUMENT_TYPE, PATIENT_GENRE, FK_PACIENTE_EVALUADOR},
                PATIENT_CEDULA + "=?", new String[]{cedula}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    p = new Paciente(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), formato.parse(cursor.getString(4)), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return p;
    }


    public String buscarMedicamentosDelPaciente(String uuid) {
        try {
            List<Schema> schemas = getListaSchemas(uuid);
            Schema schema_activo = null;
            for (Schema s : schemas) {
                if (s.isActive()) {
                    schema_activo = s;
                    break;
                }
            }

            if (schema_activo == null) return "SIN_MEDICAMENTO";

            List<DailySchema> dailySchemas = getListaDailySchemas(schema_activo.getUuid());

            if (dailySchemas.size() == 0) return "SIN_MEDICAMENTO";

            List<Prescripcion> listaPrescripcion = null;
            for (int i = 0; i < dailySchemas.size(); i++) {
                listaPrescripcion = getListaPrescripciones(dailySchemas.get(i).getUuid());
                if (listaPrescripcion.size() == 0) continue;
                else break;
            }


            String medicina = "";
            for (int i = 0; i < listaPrescripcion.size(); i++) {
                medicina = listaPrescripcion.get(i).getName();
                if (medicina == null) continue;
                else break;
            }
            if (medicina == null) medicina = "SIN_MEDICAMENTO";
            return medicina;
        } catch (Exception e) {
            Log.e("ERROR MANEJADOR", e.getLocalizedMessage());
        }
        return "SIN_MEDICAMENTO";
    }

    private Schema buscarSchemaDelPaciente(String uuid) {
        Schema schema = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCHEMA,
                new String[]{UUID, SCHEMA_DATE_START, SCHEMA_DATE_END, SCHEMA_ACTIVE, FK_SCHEMA_EVALUADOR, FK_SCHEMA_PACIENTE},
                FK_SCHEMA_PACIENTE + "=?", new String[]{uuid}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                schema = new Schema(cursor.getString(0), format.parse(cursor.getString(1)), format.parse(cursor.getString(2)),
                        cursor.getString(3).equals("true") ? true : false, cursor.getString(4), cursor.getString(5));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return schema;
    }


    public Date getFechaDeInicioPaciente(String uuid) {
        List<Schema> schemas = getListaSchemas(uuid);
        Schema schema_activo = null;
        for (Schema s : schemas) {
            if (s.isActive()) {
                schema_activo = s;
                break;
            }
        }

        if (schema_activo == null) return null;

        List<DailySchema> dailySchemas = getListaDailySchemas(schema_activo.getUuid());
        return dailySchemas.get(0).getDateOfTreatment();

    }

    public String getDosisPaciente(String uuid) {
        List<Schema> schemas = getListaSchemas(uuid);
        Schema schema_activo = null;
        for (Schema s : schemas) {
            if (s.isActive()) {
                schema_activo = s;
                break;
            }
        }

        if (schema_activo == null) return "No especificada";

        List<DailySchema> dailySchemas = getListaDailySchemas(schema_activo.getUuid());

        List<Prescripcion> listaPresc;
        for (int i = 0; i < dailySchemas.size(); i++) {
            listaPresc = getListaPrescripciones(dailySchemas.get(i).getUuid());
            for (int j = 0; j < listaPresc.size(); j++) {
                if (listaPresc.get(j).getDosis() != null) return listaPresc.get(j).getDosis();
            }
        }

        return "No especificado";

    }


    public String getTotalDailySchemas(Paciente p) {
        List<Schema> schemas = getListaSchemas(p.getUuid());
        Schema schema_activo = null;
        for (Schema s : schemas) {
            if (s.isActive()) {
                schema_activo = s;
                break;
            }
        }

        List<DailySchema> dailySchemas = getListaDailySchemas(schema_activo.getUuid());
        return "" + dailySchemas.size();
    }

    public String getTotalDiasEvaluacionPorMedicamento(Paciente p) {
        String medicina = buscarMedicamentosDelPaciente(p.getUuid());
        if (medicina == null) return "28";
        if (medicina.equals("Miltefosine")) return "28";
        else if (medicina.equals("Glucantime")) return "20";
        return "SIN_MEDICAMENTO";

    }

    public String getDayofTreatment(Paciente p) {
        Calendar c = Calendar.getInstance();
        List<Schema> schemas = getListaSchemas(p.getUuid());
        Schema schema_activo = null;
        for (Schema s : schemas) {
            if (s.isActive()) {
                schema_activo = s;
                break;
            }
        }

        //BUSCAR DAILY POR FECHA
        DailySchema ds = buscarDailySchema(schema_activo.getUuid(), Calendar.getInstance().getTime());
        if (ds == null) return "-";
        else return ds.getDayOfTreatment();
    }

    public boolean isToday(Calendar c) {
        Calendar hoy = Calendar.getInstance();
        return (c.get(Calendar.YEAR) == hoy.get(Calendar.YEAR)) && (c.get(Calendar.MONTH) == hoy.get(Calendar.MONTH)) && (c.get(Calendar.DAY_OF_MONTH) == hoy.get(Calendar.DAY_OF_MONTH));
    }

    public Schema buscarSchemaActivoDelPaciente(Paciente p) {
        Schema schema_activo = null;
        for (Schema s : getListaSchemas(p.getUuid())) {
            if (s.isActive()) {
                schema_activo = s;
                break;
            }
        }
        return schema_activo;
    }


    public boolean isPacienteActivo(Paciente p) {
        Schema schema_activo = null;
        for (Schema s : getListaSchemas(p.getUuid())) {
            if (s.isActive()) {
                schema_activo = s;
                return true;
            }
        }
        return false;
    }


    public int setSintomasDesarrollados(DevelopSymptom developSymptom) {
        ContentValues values = new ContentValues();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        values.put(UUID, developSymptom.getUuid());
        values.put(DEVELOPED_DISEASE_ANSWER, developSymptom.getAnswer());
        values.put(DEVELOPED_DISEASE_INITIAL_DATE, formato.format(developSymptom.getDate_start()));
        values.put(DEVELOPED_DISEASE_REMOVE_DATE, formato.format(developSymptom.getDate_end()));
        values.put(DEVELOPED_DISEASE_IS_ACTIVE, developSymptom.isActive());
        values.put(DEVELOPED_DISEASE_MEDICINE_NAME, developSymptom.getMedicineName());
        values.put(DEVELOPED_DISEASE_NAME, developSymptom.getName());

        values.put(FK_DEVELOPED_DISEASE_SCHEMA, developSymptom.getSchemaId());

        return add(TABLE_DEVELOPED_DISEASE, values);
    }

    public DevelopSymptom getSintomaDesarrollado(String uuid) {
        DevelopSymptom developSymptom = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEVELOPED_DISEASE,
                new String[]{UUID, DEVELOPED_DISEASE_ANSWER, DEVELOPED_DISEASE_INITIAL_DATE, DEVELOPED_DISEASE_REMOVE_DATE, DEVELOPED_DISEASE_IS_ACTIVE, DEVELOPED_DISEASE_MEDICINE_NAME, DEVELOPED_DISEASE_NAME, FK_DEVELOPED_DISEASE_SCHEMA},
                FK_SCHEMA_PACIENTE + "=?", new String[]{uuid}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                developSymptom = new DevelopSymptom(cursor.getString(0), cursor.getString(6), cursor.getString(1), cursor.getString(5),
                        cursor.getString(4).equals("true") ? true : false, format.parse(cursor.getString(2)), format.parse(cursor.getString(3)), cursor.getString(7));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return developSymptom;

    }

    /* Buscar schedule takings de una prescripcion. */
    public List<DevelopSymptom> getListaSintomas(String schema_id) {
        ArrayList<DevelopSymptom> developSymptoms = new ArrayList<DevelopSymptom>();
        DevelopSymptom developSymptom = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEVELOPED_DISEASE,
                new String[]{UUID, DEVELOPED_DISEASE_ANSWER, DEVELOPED_DISEASE_INITIAL_DATE, DEVELOPED_DISEASE_REMOVE_DATE, DEVELOPED_DISEASE_IS_ACTIVE, DEVELOPED_DISEASE_MEDICINE_NAME, DEVELOPED_DISEASE_NAME, FK_DEVELOPED_DISEASE_SCHEMA},
                FK_DEVELOPED_DISEASE_SCHEMA + "=?", new String[]{schema_id}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    String fecha_fin_string = cursor.getString(3);

                    Date fecha_fin = null;
                    if (fecha_fin_string == null) {
                        fecha_fin = format.parse("00/00/0000");
                    } else {
                        fecha_fin = format.parse(cursor.getString(3));
                    }

                    developSymptom = new DevelopSymptom(cursor.getString(0), cursor.getString(6), cursor.getString(1), cursor.getString(5), cursor.getString(4).equals("true") ? true : false, format.parse(cursor.getString(2)), fecha_fin, cursor.getString(7));
                } catch (ParseException e) {
                    Log.e("ERROR Parsing", e.getLocalizedMessage());

                }
                developSymptoms.add(developSymptom);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return developSymptoms;
    }

    public void setBasicAdverseEvents(String id_dailySchema) {

    }


    public void setScheduleTakingOnDailySchema(DailySchema d, ScheduleTaking st) {
        agregarScheduleTaking(st);
    }

    //PREGUNTAR AL FLACO COMO CAMBIO LA BANDERA DEL DS A TRUE
    public void setDailySchemaFlag(String uuid) {
        DailySchema daily_schema = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DAILY_SCHEMA,
                new String[]{UUID, DAILY_SCHEMA_DAY, DAILY_SCHEMA_DATE, DAILY_SCHEMA_FLAG, FK_DAILY_SCHEMA_SCHEMA},
                UUID + "=?", new String[]{uuid}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                daily_schema = new DailySchema(cursor.getString(0), cursor.getString(1), format.parse(cursor.getString(2)), true, cursor.getString(4));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (daily_schema != null) editarDailySchema(daily_schema);
        else {
            Log.e("ERROR", "UNO DE LOS DAILYSCHEMAS ESTA NULO");
        }
        cursor.close();
        db.close();
    }


    public void deleteScheduleTaking(String uuid) {
        deleteRow(TABLE_SCHEDULE_TAKING, uuid);
    }

    public void deleteDevelopSymptom(String uuid) {

        deleteRow(TABLE_DEVELOPED_DISEASE, uuid);

    }

    public ArrayList<DailySchema> getDailySchemaListByDates(String schemaID, String inicio, String fin) {
        ArrayList<DailySchema> lista_dailySchemas = new ArrayList<>();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date_inicio = simpleDateFormat.parse(inicio);
            Date date_fin = simpleDateFormat.parse(fin);
            Calendar cal_inicio = Calendar.getInstance();
            cal_inicio.setTime(date_inicio);
            Calendar cal_fin = Calendar.getInstance();
            cal_fin.setTime(date_fin);
            int count = 0;
            while (count <= 182) {
                if (simpleDateFormat.format(cal_inicio.getTime()).equals(simpleDateFormat.format(cal_fin.getTime()))) {
                    DailySchema d = buscarDailySchema(schemaID, cal_inicio.getTime());
                    lista_dailySchemas.add(d);
                    break;
                }
                DailySchema d = buscarDailySchema(schemaID, cal_inicio.getTime());
                lista_dailySchemas.add(d);
                cal_inicio.add(Calendar.DAY_OF_MONTH, 1);
                count++;
            }
            return lista_dailySchemas;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lista_dailySchemas;
    }

    public static final int CABEZA = 0;
    public static final int BRAZO_DERECHO = 1;
    public static final int BRAZO_IZQUIERDO = 2;
    public static final int TRONCO = 3;
    public static final int PIERNA_DERECHA = 4;
    public static final int PIERNA_IZQUIERDA = 5;
    public static final int MANO_DERECHA = 6;
    public static final int MANO_IZQUIERDA = 7;
    public static final int ALL_PARTS = 8;

    public List<Integer> getListBodyLocation(Paciente p, Date time, int part) {

        List<Integer> bodyLocations = new ArrayList<>();
        List<Integer> bodyLocations_c = new ArrayList<>();
        List<Integer> bodyLocations_bd = new ArrayList<>();
        List<Integer> bodyLocations_bi = new ArrayList<>();
        List<Integer> bodyLocations_t = new ArrayList<>();
        List<Integer> bodyLocations_pd = new ArrayList<>();
        List<Integer> bodyLocations_pi = new ArrayList<>();
        List<Integer> bodyLocations_md = new ArrayList<>();
        List<Integer> bodyLocations_mi = new ArrayList<>();

        Schema activo = buscarSchemaActivoDelPaciente(p);
        DailySchema ds = buscarDailySchema(activo.getUuid(), time);

        if(ds == null) return new ArrayList<>();

        List<UlcerForm> listaUlcerForms = getListaImagenesForm(ds.getUuid());
        if (listaUlcerForms == null) return new ArrayList<>();
        ;
        UlcerForm ulcerForm = listaUlcerForms.get(0);
        List<UIcerImg> imagenes = getListaImagenes(ulcerForm.getUiid());
        if (imagenes == null) return new ArrayList<>();

        for (int i = 0; i < imagenes.size(); i++) {
            String str_bl = imagenes.get(i).getBodyLocation();
            int bl = Integer.parseInt(str_bl);

            if (bodyLocations.contains(bl)) continue;

            bodyLocations.add(bl);
            if (bl >= 1 && bl <= 18)
                bodyLocations_c.add(bl);

            else if (bl >= 19 && bl <= 34)
                bodyLocations_bd.add(bl);

            else if (bl >= 35 && bl <= 50)
                bodyLocations_bi.add(bl);

            else if (bl >= 51 && bl <= 74)
                bodyLocations_t.add(bl);

            else if (bl >= 75 && bl <= 90)
                bodyLocations_pd.add(bl);

            else if (bl >= 91 && bl <= 106)
                bodyLocations_pi.add(bl);

            else if (bl >= 107 && bl <= 120)
                bodyLocations_md.add(bl);

            else if (bl >= 121 && bl <= 134)
                bodyLocations_mi.add(bl);

        }

        switch (part) {
            case CABEZA:
                return bodyLocations_c;
            case BRAZO_DERECHO:
                return bodyLocations_bd;
            case BRAZO_IZQUIERDO:
                return bodyLocations_bi;
            case TRONCO:
                return bodyLocations_t;
            case PIERNA_DERECHA:
                return bodyLocations_pd;
            case PIERNA_IZQUIERDA:
                return bodyLocations_pi;
            case MANO_DERECHA:
                return bodyLocations_md;
            case MANO_IZQUIERDA:
                return bodyLocations_mi;
            case ALL_PARTS:
                return bodyLocations;
        }

        return new ArrayList<>();
    }

    /*Retorna todas las imagenes contenidas en un UlcerForm dado y una zona del cuerpo dada*/
    public List<UIcerImg> getListaImagenesPorBodyLocation(String ulcerFormId, int id_zona) {
        List<UIcerImg> imagenes = getListaImagenes(ulcerFormId);
        List<UIcerImg> salida = new ArrayList<>();
        for (UIcerImg i : imagenes) {
            String str_bl = i.getBodyLocation();
            int bl = Integer.parseInt(str_bl);
            if (bl == id_zona) salida.add(i);
        }
        return salida;
    }

    public void deleteUlcerImage(UIcerImg i) {
        deleteRow(TABLE_UICERIMG, i.getUuid());
    }

    public boolean pacienteTieneFotosPendientes(Paciente paciente) {
        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fecha_prueba = format.format(Calendar.getInstance().getTime());
            Schema s = buscarSchemaActivoDelPaciente(paciente);
            if (s == null) return false;
            DailySchema ds = buscarDailySchema(s.getUuid(), format.parse(fecha_prueba));
            if (ds == null) return false;
            UlcerForm ulcerForm = getListaImagenesForm(ds.getUuid()).get(0);
            if (ulcerForm == null) return false;

                //List<UIcerImg> imgs = getListaImagenes(ulcerForm.getUiid());
                //if(imgs == null) return false;
                //if(imgs.size() == 0) return false;
            else return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean thereIsEvaluationToday(Paciente p) {
        boolean respuesta = false;
        try {
            Calendar c = Calendar.getInstance();
            Date today = c.getTime();
            Schema activo = buscarSchemaActivoDelPaciente(p);
            DailySchema ds = buscarDailySchema(activo.getUuid(), today);
            List<Prescripcion> lista_p = getListaPrescripciones(ds.getUuid());
            if (lista_p == null) respuesta = false;
            if (lista_p.size() == 0) respuesta = false;
            if (lista_p.get(0) == null) respuesta = false;
            else respuesta = true;
        } catch (Exception e) {
            respuesta = false;
        }
        return respuesta;
    }

    public boolean isEvaluationComplete(Paciente p) {
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        Schema activo = buscarSchemaActivoDelPaciente(p);
        Log.e("SCHEMA", ""+activo);Log.e("SCHEMA", ""+activo);
        DailySchema ds = buscarDailySchema(activo.getUuid(), today);
        if (ds == null) return false;
        Log.e("DAYSCHEMA", ""+ds);
        List<Prescripcion> lista_p = getListaPrescripciones(ds.getUuid());
        if (lista_p == null) return false;
        if (lista_p.size() == 0) return false;
        if (lista_p.get(0) == null) return false;
        Prescripcion presc = lista_p.get(0);
        if (presc.getNumeroLote().equals("NULL")) return false;
        else return true;
    }

    public List<Paciente> buscarListaPacientes() {
        List<Paciente> pacientes = new ArrayList<Paciente>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS,
                new String[]{UUID, PATIENT_NAME, PATIENT_LAST_NAME, PATIENT_CEDULA, PATIENT_DOCUMENT_TYPE, PATIENT_GENRE, PATIENT_BIRTHDAY, FK_PACIENTE_EVALUADOR},
                null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {//validar que el schema esté activo
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                Paciente paciente = null;
                try {
                    paciente = new Paciente(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), formato.parse(cursor.getString(6)), cursor.getString(4), cursor.getString(5), cursor.getString(7));
                    pacientes.add(paciente);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return pacientes;
    }


    public int getTotalDays(Paciente paciente) {
        try {
            String[] fechas = new String[2];
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

            Schema activo = buscarSchemaActivoDelPaciente(paciente);
            List<DailySchema> lista = getListaDailySchemas(activo.getUuid());

            int index_cut = 0;

            for (int i = 0; i < lista.size(); i++) {
                DailySchema ds = lista.get(i);
                List<Prescripcion> p = getListaPrescripciones(ds.getUuid());
                if (p.size() == 0) continue;
                else {
                    fechas[0] = formato.format(ds.getDateOfTreatment());
                    index_cut = i;
                    break;
                }
            }

            for (int i = index_cut; i < lista.size(); i++) {
                DailySchema ds = lista.get(i);
                List<Prescripcion> p = getListaPrescripciones(ds.getUuid());
                if (p.size() != 0) continue;
                else {
                    fechas[1] = formato.format(ds.getDateOfTreatment());
                    break;
                }
            }

            Date inicio = formato.parse(fechas[0]);
            Calendar c_inicio = Calendar.getInstance();
            c_inicio.setTime(inicio);

            Date fin = formato.parse(fechas[1]);
            Calendar c_fin = Calendar.getInstance();
            c_fin.setTime(fin);

            int dias = 0;
            while (dias < 100) {
                c_inicio.add(Calendar.DAY_OF_MONTH, 1);
                dias++;
                if (c_inicio.get(Calendar.YEAR) == c_fin.get(Calendar.YEAR)
                        && c_inicio.get(Calendar.MONTH) == c_fin.get(Calendar.MONTH)
                        && c_inicio.get(Calendar.DAY_OF_MONTH) == c_fin.get(Calendar.DAY_OF_MONTH)) {
                    break;
                }
            }
            return dias + 1;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public ArrayList<UlcerForm> getVentanaForms(Paciente paciente) {
        ArrayList<UlcerForm> forms = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Schema s = buscarSchemaActivoDelPaciente(paciente);
        UlcerForm ulcerForm = null;
        while (true) {
            DailySchema ds = buscarDailySchema(s.getUuid(), calendar.getTime());
            if (ds == null) break;
            List<UlcerForm> lista = getListaImagenesForm(ds.getUuid());
            if (lista.size() == 0) break;
            ulcerForm = lista.get(0);
            if (ulcerForm == null) break;
            forms.add(ulcerForm);
            break;
            //calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        /*
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        while(true) {
            DailySchema ds = buscarDailySchema(s.getUuid(), calendar.getTime());
            if(ds == null) break;
            List<UlcerForm> lista = getListaImagenesForm(ds.getUuid());
            if(lista.size() == 0) break;
            ulcerForm = lista.get(0);
            if(ulcerForm == null) break;
            forms.add(ulcerForm);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        */
        return forms;
    }

    public String getTotalDiasDeFotos(Paciente p) {
        int contador = 0;
        Schema activo = buscarSchemaActivoDelPaciente(p);
        List<DailySchema> dailySchemas = getListaDailySchemas(activo.getUuid());
        for (DailySchema s : dailySchemas) {
            List<UlcerForm> ulcerForms = getListaImagenesForm(s.getUuid());
            if (ulcerForms.size() > 0) contador++;
        }
        return "" + contador;
    }


    public String getNumeroUlcerFormsEnDailySchemas(Paciente p) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Date> list = new ArrayList<>();
        Schema activo = buscarSchemaActivoDelPaciente(p);
        List<DailySchema> dailySchemas = getListaDailySchemas(activo.getUuid());
        for (DailySchema s : dailySchemas) {
            List<UlcerForm> ulcerForms = getListaImagenesForm(s.getUuid());
            if (ulcerForms.size() > 0) {
                list.add(s.getDateOfTreatment());
                Log.e("PRUEBA",""+format.format(s.getDateOfTreatment()));
            }
        }

        Calendar c = Calendar.getInstance();
        Date hoy = c.getTime();


        for (int i = 0; i < list.size(); i++) {
            if (hoy.before(list.get(i)) || mismoDia(hoy, list.get(i))) return "" + i;
        }
        return "-";
    }

    private boolean mismoDia(Date hoy, Date date) {
        Calendar hoy_c = Calendar.getInstance();
        hoy_c.setTime(hoy);

        Calendar date_c = Calendar.getInstance();
        date_c.setTime(date);

        boolean a = hoy_c.get(Calendar.YEAR) == date_c.get(Calendar.YEAR);
        boolean b = hoy_c.get(Calendar.MONTH) == date_c.get(Calendar.MONTH);
        boolean c = hoy_c.get(Calendar.DAY_OF_MONTH) == date_c.get(Calendar.DAY_OF_MONTH);

        return a && b && c;

    }

    public Paciente getFullPaciente(Paciente p) {


        for (Schema s : p.getListaSchemas().getSchemas()) {
            //ANTECEDENTES
            List<AntecedenteXml> lista_antecedentes = getListaAntecedentes(s.getUuid());
            Antecedentes antecedentes = new Antecedentes();
            antecedentes.setAntecedentes(lista_antecedentes);
            s.setAntecedentes(antecedentes);

            //DEVELOPSYMPTOMS
            List<DevelopSymptom> lista_develop_symptom = getListaSintomas(s.getUuid());
            ListaSintomas listaSintomas = new ListaSintomas();
            listaSintomas.setSintomas(lista_develop_symptom);
            s.setDevelopSymptoms(listaSintomas);

            //DAYLYSCHEMAS
            List<DailySchema> lista_daily_schemas = getListaDailySchemas(s.getUuid());
            ListaDailySchemas listads = new ListaDailySchemas();
            listads.setDailySchemas(lista_daily_schemas);
            s.setListaDailySchemas(listads);

            for (DailySchema d : s.getListaDailySchemas().getDailySchemas()) {
                List<Prescripcion> listPrescripcion = getListaPrescripciones(d.getUuid());
                ListaPrescripciones lista_prescripciones = new ListaPrescripciones();
                lista_prescripciones.setPrescripciones(listPrescripcion);
                d.setPrescripciones(lista_prescripciones);

                for (Prescripcion pr : d.getPrescripciones().getPrescripciones()) {
                    BasicAdverseEvent bae = buscarBasicAdverseEvent(pr.getUuid());
                    pr.setEventosBasicos(bae);

                    List<ScheduleTaking> lista_schedule = getListaSheduleTaking(pr.getUuid());
                    ListaScheduleTaking listaScheduleTaking = new ListaScheduleTaking();
                    listaScheduleTaking.setSchedulesTaking(lista_schedule);
                    pr.setDosisTomadas(listaScheduleTaking);
                }

                List<UlcerForm> listUlcerForm = getListaImagenesForm(d.getUuid());
                ListaUlcerForms lista_ulcerF = new ListaUlcerForms();
                lista_ulcerF.setUlcerForms(listUlcerForm);
                d.setImagenes(lista_ulcerF);

                for (UlcerForm uf : d.getImagenes().getUlcerForms()) {
                    List<UIcerImg> listUlcerIMG = getListaImagenes(uf.getUiid());
                    ListaUlcerImages listaUlcerImages = new ListaUlcerImages();
                    listaUlcerImages.setUlcerImages(listUlcerIMG);
                    uf.setUlcerImages(listaUlcerImages);
                }
            }

        }


        return p;
    }

    public void printAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVALUADOR, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Log.e("ULCERS>", "USUARIO " + cursor.getString(cursor.getColumnIndex(EVALUADOR_CEDULA)) + ", " + cursor.getString(cursor.getColumnIndex(EVALUADOR_NAME)));
            } while (cursor.moveToNext());
        }
        db.close();

    }

    public int getDaysSinceLastDayOfTreatment(Paciente p) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Date> list = new ArrayList<>();
        Schema activo = buscarSchemaActivoDelPaciente(p);
        List<DailySchema> dailySchemas = getListaDailySchemas(activo.getUuid());
        for (DailySchema s : dailySchemas) {
            List<UlcerForm> ulcerForms = getListaImagenesForm(s.getUuid());
            if (ulcerForms.size() > 0) {
                list.add(s.getDateOfTreatment());
            }
        }

        Calendar c = Calendar.getInstance();
        Date d = c.getTime();

        long endTime = d.getTime();
        long startTime = list.get(list.size()-1).getTime();
        long diffTime = endTime - startTime;
        long diffDays = diffTime / (1000 * 60 * 60 * 24);

        return (int) diffDays;
    }

    public String getLastDayOfTreatment(Paciente p) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Date> list = new ArrayList<>();
        Schema activo = buscarSchemaActivoDelPaciente(p);
        List<DailySchema> dailySchemas = getListaDailySchemas(activo.getUuid());
        return dailySchemas.get(dailySchemas.size()-1).getDayOfTreatment();
    }
}

