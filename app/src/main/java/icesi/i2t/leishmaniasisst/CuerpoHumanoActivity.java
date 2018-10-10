package icesi.i2t.leishmaniasisst;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import icesi.i2t.leishmaniasisst.bodylocations.BrazoDerechoActivity;
import icesi.i2t.leishmaniasisst.bodylocations.BrazoIzquierdoActvity;
import icesi.i2t.leishmaniasisst.bodylocations.CabezaActivity;
import icesi.i2t.leishmaniasisst.bodylocations.ManoDerechaActivity;
import icesi.i2t.leishmaniasisst.bodylocations.ManoIzquierdaActivity;
import icesi.i2t.leishmaniasisst.bodylocations.PiernaDerechaActivity;
import icesi.i2t.leishmaniasisst.bodylocations.PiernaIzquierdaActivity;
import icesi.i2t.leishmaniasisst.bodylocations.TroncoActivity;
import icesi.i2t.leishmaniasisst.data.ManejadorBD;
import icesi.i2t.leishmaniasisst.dialogs.BooleanAnswerDialog;
import icesi.i2t.leishmaniasisst.model.DailySchema;
import icesi.i2t.leishmaniasisst.model.Evaluador;
import icesi.i2t.leishmaniasisst.model.ListaUlcerForms;
import icesi.i2t.leishmaniasisst.model.ListaUlcerImages;
import icesi.i2t.leishmaniasisst.model.Paciente;
import icesi.i2t.leishmaniasisst.model.Schema;
import icesi.i2t.leishmaniasisst.model.UIcerImg;
import icesi.i2t.leishmaniasisst.model.UlcerForm;
import icesi.i2t.leishmaniasisst.util.GeneralUtils;


public class CuerpoHumanoActivity extends AppCompatActivity implements View.OnTouchListener {

    boolean focus = true;
    ImageView cuerpo;
    TextView subtitulo_cuerpo, label_cabeza, label_tronco, label_brazo_izquierdo;
    TextView label_brazo_derecho, label_pierna_izquierda, label_pierna_derecha;
    TextView label_mano_derecha, label_mano_izquierda;

    static final int BRAZO_DERECHO = 0;
    static final int BRAZO_IZQUIERDO = 1;
    static final int CABEZA = 2;
    static final int TRONCO = 3;
    static final int PIERNA_DERECHA = 4;
    static final int PIERNA_IZQUIERDA = 5;
    static final int MANO_DERECHA = 6;
    static final int MANO_IZQUIERDA = 7;

    String[] partes = new String[]{"BRAZO DERECHO", "BRAZO IZQUIERDO", "CABEZA", "TRONCO", "PIERNA DERECHA", "PIERNA IZQUIERDA", "NO_PART"};
    TextView[] labels = new TextView[8];

    int alto;
    int ancho;

    int display_height;
    int display_width;

    float text_label_size = 0;

    View stick1, stick2, stick3, stick4, stick5, stick6, stick7, stick8;

    TextView lesion_cabeza, lesion_tronco, lesion_brazo_der, lesion_brazo_izq, lesion_pierna_der, lesion_pierna_izq, lesion_mano_der, lesion_mano_izq;

    boolean modo_nueva_lesion = false;

    ManejadorBD db;
    Paciente paciente;

    public static DailySchema dailySchema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuerpo_humano_activity);

        List<Integer> bodyLocation = null;
        try {
            db = new ManejadorBD(this);
            String id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
            paciente = db.buscarPaciente(id);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String fecha_prueba = format.format(Calendar.getInstance().getTime());

            bodyLocation = db.getListBodyLocation(paciente, format.parse(fecha_prueba), ManejadorBD.ALL_PARTS);
            int numero = bodyLocation.size();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        display_height = displayMetrics.heightPixels;
        display_width = displayMetrics.widthPixels;

        focus = true;

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cuerpo_humano);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        cuerpo = (ImageView) findViewById(R.id.cuerpo);

        subtitulo_cuerpo = (TextView) findViewById(R.id.subtitulo_cuerpo);
        label_brazo_derecho = (TextView) findViewById(R.id.label_brazo_derecho);
        label_brazo_izquierdo = (TextView) findViewById(R.id.label_brazo_izquierdo);
        label_cabeza = (TextView) findViewById(R.id.label_cabeza);
        label_tronco = (TextView) findViewById(R.id.label_tronco);
        label_pierna_derecha = (TextView) findViewById(R.id.label_pierna_derecha);
        label_pierna_izquierda = (TextView) findViewById(R.id.label_pierna_izquierda);
        label_mano_derecha = (TextView) findViewById(R.id.label_mano_derecha);
        label_mano_izquierda = (TextView) findViewById(R.id.label_mano_izquierda);

        labels[0] = label_brazo_derecho;
        labels[1] = label_brazo_izquierdo;
        labels[2] = label_cabeza;
        labels[3] = label_tronco;
        labels[4] = label_pierna_derecha;
        labels[5] = label_pierna_izquierda;
        labels[6] = label_mano_derecha;
        labels[7] = label_mano_izquierda;

        cuerpo.setOnTouchListener(this);

        double escala = 0.5;
        int ancho = (int) (display_height * 0.3 * escala);
        int alto = (int) (display_height * escala);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho, alto);
        cuerpo.setLayoutParams(params);

        stick1 = findViewById(R.id.stick1);
        stick2 = findViewById(R.id.stick2);
        stick3 = findViewById(R.id.stick3);
        stick4 = findViewById(R.id.stick4);
        stick5 = findViewById(R.id.stick5);
        stick6 = findViewById(R.id.stick6);
        stick7 = findViewById(R.id.stick7);
        stick8 = findViewById(R.id.stick8);

        lesion_cabeza = (TextView) findViewById(R.id.lesion_cabeza);
        lesion_tronco = (TextView) findViewById(R.id.lesion_tronco);
        lesion_brazo_der = (TextView) findViewById(R.id.lesion_brazo_der);
        lesion_brazo_izq = (TextView) findViewById(R.id.lesion_brazo_izq);
        lesion_pierna_der = (TextView) findViewById(R.id.lesion_pierna_der);
        lesion_pierna_izq = (TextView) findViewById(R.id.lesion_pierna_izq);
        lesion_mano_der = (TextView) findViewById(R.id.lesion_mano_der);
        lesion_mano_izq = (TextView) findViewById(R.id.lesion_mano_izq);


        lesion_cabeza.setVisibility(View.GONE);
        lesion_tronco.setVisibility(View.GONE);
        lesion_brazo_der.setVisibility(View.GONE);
        lesion_brazo_izq.setVisibility(View.GONE);
        lesion_pierna_der.setVisibility(View.GONE);
        lesion_pierna_izq.setVisibility(View.GONE);
        lesion_mano_der.setVisibility(View.GONE);
        lesion_mano_izq.setVisibility(View.GONE);


        ponerIndicadoresDeLesion(bodyLocation);
        ponerIndicadoresDeLesionTerminada();

        hideButtons();

        if (dailySchema == null) {
            Schema schema = db.buscarSchemaActivoDelPaciente(paciente);
            Calendar hoy = Calendar.getInstance();
            DailySchema days = db.buscarDailySchema(schema.getUuid(), hoy.getTime());
            if (dailySchema != null) {
                dailySchema = days;
            } else {
                String dia_tratamiento = "" + (Integer.parseInt(db.getLastDayOfTreatment(paciente)) + db.getDaysSinceLastDayOfTreatment(paciente));
                dailySchema = new DailySchema(UUID.randomUUID().toString(), dia_tratamiento, hoy.getTime(), false, schema.getUuid());

                ListaUlcerImages listaUlcerImages = new ListaUlcerImages();
                UlcerForm ulcerForm = new UlcerForm(UUID.randomUUID().toString(), "", hoy.getTime(), dailySchema.getUuid());
                ulcerForm.setUlcerImages(listaUlcerImages);
                ListaUlcerForms listaUlcerForms = new ListaUlcerForms();
                listaUlcerForms.getUlcerForms().add(ulcerForm);
                dailySchema.setImagenes(listaUlcerForms);
            }
        }
        Gson gson = new Gson();
        Log.e("REMODELACION", "" + gson.toJson(dailySchema));
    }

    private void ponerIndicadoresDeLesionTerminada() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String fecha_prueba = format.format(Calendar.getInstance().getTime());
            DailySchema ds = db.buscarDailySchema(db.buscarSchemaActivoDelPaciente(paciente).getUuid(), format.parse(fecha_prueba));
            if (ds == null) return;
            List<UIcerImg> imagenes = db.getListaImagenes(db.getListaImagenesForm(ds.getUuid()).get(0).getUiid());
            for (int i = 0; i < imagenes.size(); i++) {
                if (!imagenes.get(i).getImgUUID().equals("00000000-0000-0000-0000-000000000000")) {
                    int bl = Integer.parseInt(imagenes.get(i).getBodyLocation());
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("BP" + bl, true).commit();
                    if (bl >= 1 && bl <= 18)
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putBoolean("c_ok", true).putString("parte_actual", "Lesiones cabeza").commit();
                    else if (bl >= 19 && bl <= 34)
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putBoolean("bd_ok", true).putString("parte_actual", "Lesiones brazo derecho").commit();
                    else if (bl >= 35 && bl <= 50)
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putBoolean("bi_ok", true).putString("parte_actual", "Lesiones brazo izquierda").commit();
                    else if (bl >= 51 && bl <= 74)
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putBoolean("t_ok", true).putString("parte_actual", "Lesiones tronco").commit();
                    else if (bl >= 75 && bl <= 90)
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putBoolean("pd_ok", true).putString("parte_actual", "Lesiones pierna derecha").commit();
                    else if (bl >= 91 && bl <= 106)
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putBoolean("pi_ok", true).putString("parte_actual", "Lesiones pierna izquierda").commit();
                    else if (bl >= 107 && bl <= 120)
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putBoolean("md_ok", true).putString("parte_actual", "Lesiones mano derecha").commit();
                    else if (bl >= 121 && bl <= 134)
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putBoolean("mi_ok", true).putString("parte_actual", "Lesiones mano izquierda").commit();
                }
            }
        } catch (ParseException e) {
            Log.e("ERROR", e.getLocalizedMessage());
        }
    }

    private void ponerIndicadoresDeLesion(List<Integer> bodyLocation) {
        for (Integer bl : bodyLocation) {
            if (bl >= 1 && bl <= 18)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("prueba_c", true).commit();
            else if (bl >= 19 && bl <= 34)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("prueba_bd", true).commit();
            else if (bl >= 35 && bl <= 50)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("prueba_bi", true).commit();
            else if (bl >= 51 && bl <= 74)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("prueba_t", true).commit();
            else if (bl >= 75 && bl <= 90)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("prueba_pd", true).commit();
            else if (bl >= 91 && bl <= 106)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("prueba_pi", true).commit();
            else if (bl >= 107 && bl <= 120)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("prueba_md", true).commit();
            else if (bl >= 121 && bl <= 134)
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("prueba_mi", true).commit();
        }
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prueba_bd", false))
            showLesion(BRAZO_DERECHO);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prueba_bi", false))
            showLesion(BRAZO_IZQUIERDO);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prueba_c", false))
            showLesion(CABEZA);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prueba_t", false))
            showLesion(TRONCO);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prueba_pd", false))
            showLesion(PIERNA_DERECHA);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prueba_pi", false))
            showLesion(PIERNA_IZQUIERDA);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prueba_md", false))
            showLesion(MANO_DERECHA);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prueba_mi", false))
            showLesion(MANO_IZQUIERDA);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_evaluacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cancel_eval) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            BooleanAnswerDialog dialog = BooleanAnswerDialog.newInstance("¿Está seguro que desea cancelar la evaluación en curso?");
            dialog.show(ft, "dialog_fiebre");
            dialog.setOnDialogResult(new BooleanAnswerDialog.OnMyDialogResult() {
                @Override
                public void finish(String salida) {
                    if (salida.equals("SI")) {
                        Intent intent = new Intent(getApplicationContext(), Evaluacion.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideButtons();
        focus = true;

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (focus) {
            focus = false;

            ancho = cuerpo.getWidth();
            alto = cuerpo.getHeight();
            adjustSticks();
            adjustLabels();
            initLesiones();
            comprobarLesiones();

            if (modo_nueva_lesion) activar_modo_nueva_lesion();
            else desactivar_modo_nueva_lesion();
        }
    }

    private void comprobarLesiones() {
        boolean bd_ok = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("bd_ok", false);
        boolean bi_ok = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("bi_ok", false);
        boolean c_ok = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("c_ok", false);
        boolean t_ok = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("t_ok", false);
        boolean pd_ok = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pd_ok", false);
        boolean pi_ok = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pi_ok", false);
        boolean md_ok = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("md_ok", false);
        boolean mi_ok = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("mi_ok", false);

        if (bd_ok) lesion_brazo_der.setBackgroundResource(R.drawable.bt_lesion_terminado);
        else lesion_brazo_der.setBackgroundResource(R.drawable.bt_lesion);
        if (bi_ok) lesion_brazo_izq.setBackgroundResource(R.drawable.bt_lesion_terminado);
        else lesion_brazo_izq.setBackgroundResource(R.drawable.bt_lesion);
        if (c_ok) lesion_cabeza.setBackgroundResource(R.drawable.bt_lesion_terminado);
        else lesion_cabeza.setBackgroundResource(R.drawable.bt_lesion);
        if (t_ok) lesion_tronco.setBackgroundResource(R.drawable.bt_lesion_terminado);
        else lesion_tronco.setBackgroundResource(R.drawable.bt_lesion);
        if (pd_ok) lesion_pierna_der.setBackgroundResource(R.drawable.bt_lesion_terminado);
        else lesion_pierna_der.setBackgroundResource(R.drawable.bt_lesion);
        if (pi_ok) lesion_pierna_izq.setBackgroundResource(R.drawable.bt_lesion_terminado);
        else lesion_pierna_izq.setBackgroundResource(R.drawable.bt_lesion);
        if (md_ok) lesion_mano_der.setBackgroundResource(R.drawable.bt_lesion_terminado);
        else lesion_mano_der.setBackgroundResource(R.drawable.bt_lesion);
        if (mi_ok) lesion_mano_izq.setBackgroundResource(R.drawable.bt_lesion_terminado);
        else lesion_mano_izq.setBackgroundResource(R.drawable.bt_lesion);

        //Contar las lesiones en pantalla
        int numero_lesiones = 0;
        if (lesion_mano_der.getVisibility() == View.VISIBLE) numero_lesiones++;
        if (lesion_mano_izq.getVisibility() == View.VISIBLE) numero_lesiones++;
        if (lesion_pierna_der.getVisibility() == View.VISIBLE) numero_lesiones++;
        if (lesion_pierna_izq.getVisibility() == View.VISIBLE) numero_lesiones++;
        if (lesion_brazo_der.getVisibility() == View.VISIBLE) numero_lesiones++;
        if (lesion_brazo_izq.getVisibility() == View.VISIBLE) numero_lesiones++;
        if (lesion_tronco.getVisibility() == View.VISIBLE) numero_lesiones++;
        if (lesion_cabeza.getVisibility() == View.VISIBLE) numero_lesiones++;

        int lesiones_terminadas = 0;
        if (lesion_mano_der.getVisibility() == View.VISIBLE && md_ok) lesiones_terminadas++;
        if (lesion_mano_izq.getVisibility() == View.VISIBLE && mi_ok) lesiones_terminadas++;
        if (lesion_pierna_der.getVisibility() == View.VISIBLE && pd_ok) lesiones_terminadas++;
        if (lesion_pierna_izq.getVisibility() == View.VISIBLE && pi_ok) lesiones_terminadas++;
        if (lesion_brazo_der.getVisibility() == View.VISIBLE && bd_ok) lesiones_terminadas++;
        if (lesion_brazo_izq.getVisibility() == View.VISIBLE && bi_ok) lesiones_terminadas++;
        if (lesion_tronco.getVisibility() == View.VISIBLE && t_ok) lesiones_terminadas++;
        if (lesion_cabeza.getVisibility() == View.VISIBLE && c_ok) lesiones_terminadas++;

        if (lesiones_terminadas == numero_lesiones) showButtons();

    }


    //Estas variables controlan el listener onTouch del diagrama
    boolean cabeza_active = false, brazo_der_active = false, brazo_izq_active = false;
    boolean tronco_active = false, pierna_der_active = false, pierna_izq_active = false;
    boolean mano_der_active = false, mano_izq_active = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int parte = getParteDelCuerpo(event.getX() / ancho, event.getY() / alto);
                showPartLabel(parte);
                break;
            case MotionEvent.ACTION_MOVE:
                parte = getParteDelCuerpo(event.getX() / ancho, event.getY() / alto);
                showPartLabel(parte);
                break;
            case MotionEvent.ACTION_UP:
                parte = getParteDelCuerpo(event.getX() / ancho, event.getY() / alto);
                if (parte == CABEZA && cabeza_active) {
                    Intent i = new Intent(this, CabezaActivity.class);
                    startActivity(i);
                } else if (parte == TRONCO && tronco_active) {
                    Intent i = new Intent(this, TroncoActivity.class);
                    startActivity(i);
                } else if (parte == BRAZO_DERECHO && brazo_der_active) {
                    Intent i = new Intent(this, BrazoDerechoActivity.class);
                    startActivity(i);
                } else if (parte == BRAZO_IZQUIERDO && brazo_izq_active) {
                    Intent i = new Intent(this, BrazoIzquierdoActvity.class);
                    startActivity(i);
                } else if (parte == PIERNA_DERECHA && pierna_der_active) {
                    Intent i = new Intent(this, PiernaDerechaActivity.class);
                    startActivity(i);
                } else if (parte == PIERNA_IZQUIERDA && pierna_izq_active) {
                    Intent i = new Intent(this, PiernaIzquierdaActivity.class);
                    startActivity(i);
                } else if (parte == MANO_DERECHA && mano_der_active) {
                    Intent i = new Intent(this, ManoDerechaActivity.class);
                    startActivity(i);
                } else if (parte == MANO_IZQUIERDA && mano_izq_active) {
                    Intent i = new Intent(this, ManoIzquierdaActivity.class);
                    startActivity(i);
                }
                return false;
        }
        return true;
    }

    private void showPartLabel(int parte) {
        if (parte <= 7) {
            labels[parte].setTextSize(20);
            labels[parte].setTypeface(null, Typeface.BOLD_ITALIC);
            for (int i = 0; i < labels.length; i++) {
                if (i != parte) {
                    labels[i].setTextSize(16);
                    labels[parte].setTypeface(null, Typeface.ITALIC);
                }
            }
        }
    }

    private int getParteDelCuerpo(float x, float y) {
        if (x < 0.25 && x > 0 && y < 0.5 && y > 0.185) {
            return BRAZO_DERECHO;
        } else if (x < 0.7 && x > 0.3 && y < 0.18 && y >= 0) {
            return CABEZA;
        } else if (x <= 1 && x > 0.75 && y < 0.5 && y > 0.185) {
            return BRAZO_IZQUIERDO;
        } else if (x <= 0.75 && x >= 0.25 && y <= 0.55 && y >= 0.18) {
            return TRONCO;
        } else if (x <= 0.5 && x >= 0.2 && y <= 1 && y > 0.55) {
            return PIERNA_DERECHA;
        } else if (x <= 0.8 && x > 0.5 && y <= 1 && y > 0.55) {
            return PIERNA_IZQUIERDA;
        } else if (x < 0.25 && x > 0 && y <= 0.6 && y > 0.5) {
            return MANO_DERECHA;
        } else if (x <= 1 && x > 0.75 && y <= 0.6 && y > 0.5) {
            return MANO_IZQUIERDA;
        }
        return 8;
    }

    private void adjustSticks() {

        LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(subtitulo_cuerpo.getWidth(), subtitulo_cuerpo.getHeight());
        par.setMargins(0, 0, 0, 40);
        subtitulo_cuerpo.setLayoutParams(par);

        stick1.setX(cuerpo.getX() + (int) (0.6 * cuerpo.getWidth()));
        stick1.setY(cuerpo.getY() + (int) (0.08 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams((int) (0.5 * cuerpo.getWidth()), 2);
        stick1.setLayoutParams(p1);

        stick2.setX(cuerpo.getX() + (int) (0.5 * cuerpo.getWidth()));
        stick2.setY(cuerpo.getY() + (int) (0.23 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams((int) (0.60 * cuerpo.getWidth()), 2);
        stick2.setLayoutParams(p2);

        stick3.setX(cuerpo.getX() + (int) (0.9 * cuerpo.getWidth()));
        stick3.setY(cuerpo.getY() + (int) (0.38 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p3 = new RelativeLayout.LayoutParams((int) (0.34 * cuerpo.getWidth()), 2);
        stick3.setLayoutParams(p3);

        stick4.setX(cuerpo.getX() - (int) (0.1 * cuerpo.getWidth()));
        stick4.setY(cuerpo.getY() + (int) (0.34 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p4 = new RelativeLayout.LayoutParams((int) (0.25 * cuerpo.getWidth()), 2);
        stick4.setLayoutParams(p4);

        stick5.setX(cuerpo.getX() - (int) (0.07 * cuerpo.getWidth()));
        stick5.setY(cuerpo.getY() + (int) (0.83 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p5 = new RelativeLayout.LayoutParams((int) (0.4 * cuerpo.getWidth()), 2);
        stick5.setLayoutParams(p5);

        stick6.setX(cuerpo.getX() + (int) (0.65 * cuerpo.getWidth()));
        stick6.setY(cuerpo.getY() + (int) (0.73 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p6 = new RelativeLayout.LayoutParams((int) (0.36 * cuerpo.getWidth()), 2);
        stick6.setLayoutParams(p6);

        stick7.setX(cuerpo.getX() - (int) (0.25 * cuerpo.getWidth()));
        stick7.setY(cuerpo.getY() + (int) (0.54 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p7 = new RelativeLayout.LayoutParams((int) (0.36 * cuerpo.getWidth()), 2);
        stick7.setLayoutParams(p7);

        stick8.setX(cuerpo.getX() + (int) (0.89 * cuerpo.getWidth()));
        stick8.setY(cuerpo.getY() + (int) (0.54 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p8 = new RelativeLayout.LayoutParams((int) (0.36 * cuerpo.getWidth()), 2);
        stick8.setLayoutParams(p8);
    }

    private void adjustLabels() {
        label_cabeza.setX(cuerpo.getX() + (int) (1.15 * cuerpo.getWidth()));
        label_cabeza.setY(cuerpo.getY() + (int) (0.05 * cuerpo.getHeight()));

        label_tronco.setX(cuerpo.getX() + (int) (1.15 * cuerpo.getWidth()));
        label_tronco.setY(cuerpo.getY() + (int) (0.2 * cuerpo.getHeight()));

        label_brazo_izquierdo.setX(cuerpo.getX() + (int) (1.28 * cuerpo.getWidth()));
        label_brazo_izquierdo.setY(cuerpo.getY() + (int) (0.35 * cuerpo.getHeight()));

        label_brazo_derecho.setX(cuerpo.getX() - (int) (1 * cuerpo.getWidth()));
        label_brazo_derecho.setY(cuerpo.getY() + (int) (0.3 * cuerpo.getHeight()));

        label_pierna_izquierda.setX(cuerpo.getX() + (int) (1.05 * cuerpo.getWidth()));
        label_pierna_izquierda.setY(cuerpo.getY() + (int) (0.7 * cuerpo.getHeight()));

        label_pierna_derecha.setX(cuerpo.getX() - (int) (0.8 * cuerpo.getWidth()));
        label_pierna_derecha.setY(cuerpo.getY() + (int) (0.8 * cuerpo.getHeight()));

        label_mano_izquierda.setX(cuerpo.getX() + (int) (1.2 * cuerpo.getWidth()));
        label_mano_izquierda.setY(cuerpo.getY() + (int) (0.5 * cuerpo.getHeight()));

        label_mano_derecha.setX(cuerpo.getX() - (int) (1 * cuerpo.getWidth()));
        label_mano_derecha.setY(cuerpo.getY() + (int) (0.5 * cuerpo.getHeight()));
    }

    private void setListernerToLabels(int parte) {
        switch (parte) {
            case CABEZA:
                label_cabeza.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_cabeza.setScaleX(1.2f);
                            label_cabeza.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_cabeza.setScaleX(1);
                            label_cabeza.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), CabezaActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case TRONCO:
                label_tronco.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_tronco.setScaleX(1.2f);
                            label_tronco.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_tronco.setScaleX(1);
                            label_tronco.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), TroncoActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case BRAZO_DERECHO:
                label_brazo_derecho.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_brazo_derecho.setScaleX(1.2f);
                            label_brazo_derecho.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_brazo_derecho.setScaleX(1);
                            label_brazo_derecho.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), BrazoDerechoActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case BRAZO_IZQUIERDO:
                label_brazo_izquierdo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_brazo_izquierdo.setScaleX(1.2f);
                            label_brazo_izquierdo.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_brazo_izquierdo.setScaleX(1);
                            label_brazo_izquierdo.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), BrazoIzquierdoActvity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case PIERNA_DERECHA:
                label_pierna_derecha.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_pierna_derecha.setScaleX(1.2f);
                            label_pierna_derecha.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_pierna_derecha.setScaleX(1);
                            label_pierna_derecha.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), PiernaDerechaActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case PIERNA_IZQUIERDA:
                label_pierna_izquierda.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_pierna_izquierda.setScaleX(1.2f);
                            label_pierna_izquierda.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_pierna_izquierda.setScaleX(1);
                            label_pierna_izquierda.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), PiernaIzquierdaActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case MANO_DERECHA:
                label_mano_derecha.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_mano_derecha.setScaleX(1.2f);
                            label_mano_derecha.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_mano_derecha.setScaleX(1);
                            label_mano_derecha.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), ManoDerechaActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

            case MANO_IZQUIERDA:
                label_mano_izquierda.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            label_mano_izquierda.setScaleX(1.2f);
                            label_mano_izquierda.setScaleY(1.2f);
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            label_mano_izquierda.setScaleX(1);
                            label_mano_izquierda.setScaleY(1);
                            Intent i = new Intent(getApplicationContext(), ManoIzquierdaActivity.class);
                            startActivity(i);
                        }
                        return false;
                    }
                });
                break;

        }
    }

    private void initLesiones() {
        lesion_cabeza.setX(cuerpo.getX() + (int) (0.42 * cuerpo.getWidth()));
        lesion_cabeza.setY(cuerpo.getY() + (int) (0.05 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_cabeza.setLayoutParams(p1);

        lesion_tronco.setX(cuerpo.getX() + (int) (0.42 * cuerpo.getWidth()));
        lesion_tronco.setY(cuerpo.getY() + (int) (0.21 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_tronco.setLayoutParams(p2);

        lesion_brazo_der.setX(cuerpo.getX() + (int) (0.05 * cuerpo.getWidth()));
        lesion_brazo_der.setY(cuerpo.getY() + (int) (0.36 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p3 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_brazo_der.setLayoutParams(p3);

        lesion_brazo_izq.setX(cuerpo.getX() + (int) (0.79 * cuerpo.getWidth()));
        lesion_brazo_izq.setY(cuerpo.getY() + (int) (0.36 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p4 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_brazo_izq.setLayoutParams(p4);

        lesion_pierna_der.setX(cuerpo.getX() + (int) (0.27 * cuerpo.getWidth()));
        lesion_pierna_der.setY(cuerpo.getY() + (int) (0.71 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p5 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_pierna_der.setLayoutParams(p5);

        lesion_pierna_izq.setX(cuerpo.getX() + (int) (0.58 * cuerpo.getWidth()));
        lesion_pierna_izq.setY(cuerpo.getY() + (int) (0.71 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p6 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_pierna_izq.setLayoutParams(p6);

        lesion_mano_der.setX(cuerpo.getX() + (int) (0.02 * cuerpo.getWidth()));
        lesion_mano_der.setY(cuerpo.getY() + (int) (0.53 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p7 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_mano_der.setLayoutParams(p7);

        lesion_mano_izq.setX(cuerpo.getX() + (int) (0.82 * cuerpo.getWidth()));
        lesion_mano_izq.setY(cuerpo.getY() + (int) (0.53 * cuerpo.getHeight()));
        RelativeLayout.LayoutParams p8 = new RelativeLayout.LayoutParams((int) (ancho * 0.15), (int) (ancho * 0.15));
        lesion_mano_izq.setLayoutParams(p8);


    }

    boolean lesion_presente_bd = false, lesion_presente_bi = false, lesion_presente_c = false;
    boolean lesion_presente_t = false, lesion_presente_md = false, lesion_presente_mi = false;
    boolean lesion_presente_pd = false, lesion_presente_pi = false;

    private void showLesion(int parte) {
        switch (parte) {
            case BRAZO_DERECHO:
                lesion_brazo_der.setVisibility(View.VISIBLE);
                setListernerToLabels(BRAZO_DERECHO);
                brazo_der_active = true;
                lesion_presente_bd = true;
                break;
            case BRAZO_IZQUIERDO:
                lesion_brazo_izq.setVisibility(View.VISIBLE);
                setListernerToLabels(BRAZO_IZQUIERDO);
                brazo_izq_active = true;
                lesion_presente_bi = true;
                break;
            case CABEZA:
                lesion_cabeza.setVisibility(View.VISIBLE);
                setListernerToLabels(CABEZA);
                cabeza_active = true;
                lesion_presente_c = true;
                break;
            case TRONCO:
                lesion_tronco.setVisibility(View.VISIBLE);
                setListernerToLabels(TRONCO);
                tronco_active = true;
                lesion_presente_t = true;
                break;
            case PIERNA_DERECHA:
                lesion_pierna_der.setVisibility(View.VISIBLE);
                setListernerToLabels(PIERNA_DERECHA);
                pierna_der_active = true;
                lesion_presente_pd = true;
                break;
            case PIERNA_IZQUIERDA:
                lesion_pierna_izq.setVisibility(View.VISIBLE);
                setListernerToLabels(PIERNA_IZQUIERDA);
                pierna_izq_active = true;
                lesion_presente_pi = true;
                break;
            case MANO_DERECHA:
                lesion_mano_der.setVisibility(View.VISIBLE);
                setListernerToLabels(MANO_DERECHA);
                mano_der_active = true;
                lesion_presente_md = true;
                break;
            case MANO_IZQUIERDA:
                lesion_mano_izq.setVisibility(View.VISIBLE);
                setListernerToLabels(MANO_IZQUIERDA);
                mano_izq_active = true;
                lesion_presente_mi = true;
                break;
        }
    }

    private void showButtons() {
        LinearLayout botones_cuerpo = (LinearLayout) findViewById(R.id.botones_cuerpo);
        botones_cuerpo.setVisibility(View.VISIBLE);
    }

    private void hideButtons() {
        LinearLayout botones_cuerpo = (LinearLayout) findViewById(R.id.botones_cuerpo);
        botones_cuerpo.setVisibility(View.GONE);
    }


    public void addNewLesion(View v) {
        activar_modo_nueva_lesion();
    }

    private void backNewLesion() {

        desactivar_modo_nueva_lesion();
    }

    @Override
    public void onBackPressed() {
        if (!modo_nueva_lesion) {
            finish();
            borrarPreferences();
        } else backNewLesion();
    }

    public void activar_modo_nueva_lesion() {
        hideButtons();
        if (lesion_presente_c)
            lesion_cabeza.setVisibility(View.GONE);
        if (lesion_presente_t)
            lesion_tronco.setVisibility(View.GONE);
        if (lesion_presente_md)
            lesion_mano_der.setVisibility(View.GONE);
        if (lesion_presente_mi)
            lesion_mano_izq.setVisibility(View.GONE);
        if (lesion_presente_bd)
            lesion_brazo_der.setVisibility(View.GONE);
        if (lesion_presente_bi)
            lesion_brazo_izq.setVisibility(View.GONE);
        if (lesion_presente_pd)
            lesion_pierna_der.setVisibility(View.GONE);
        if (lesion_presente_pi)
            lesion_pierna_izq.setVisibility(View.GONE);

        cabeza_active = true;
        setListernerToLabels(CABEZA);

        tronco_active = true;
        setListernerToLabels(TRONCO);

        mano_der_active = true;
        setListernerToLabels(MANO_DERECHA);

        mano_izq_active = true;
        setListernerToLabels(MANO_IZQUIERDA);

        brazo_der_active = true;
        setListernerToLabels(BRAZO_DERECHO);

        brazo_izq_active = true;
        setListernerToLabels(BRAZO_IZQUIERDO);

        pierna_der_active = true;
        setListernerToLabels(PIERNA_DERECHA);

        pierna_izq_active = true;
        setListernerToLabels(PIERNA_IZQUIERDA);

        subtitulo_cuerpo.setText("Seleccione el lugar donde se encuentra la nueva lesión");
        modo_nueva_lesion = true;

        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("modo_nueva_lesion", true).commit();
    }

    public void desactivar_modo_nueva_lesion() {
        if (modo_nueva_lesion) showButtons();

        if (lesion_presente_c) lesion_cabeza.setVisibility(View.VISIBLE);
        if (lesion_presente_t) lesion_tronco.setVisibility(View.VISIBLE);
        if (lesion_presente_md) lesion_mano_der.setVisibility(View.VISIBLE);
        if (lesion_presente_mi) lesion_mano_izq.setVisibility(View.VISIBLE);
        if (lesion_presente_bd) lesion_brazo_der.setVisibility(View.VISIBLE);
        if (lesion_presente_bi) lesion_brazo_izq.setVisibility(View.VISIBLE);
        if (lesion_presente_pd) lesion_pierna_der.setVisibility(View.VISIBLE);
        if (lesion_presente_pi) lesion_pierna_izq.setVisibility(View.VISIBLE);

        //Inhabilitar zonas en el diagrama
        if (!lesion_presente_c) {
            cabeza_active = false;
            label_cabeza.setOnTouchListener(null);
        }
        if (!lesion_presente_t) {
            tronco_active = false;
            label_tronco.setOnTouchListener(null);
        }
        if (!lesion_presente_md) {
            mano_der_active = false;
            label_mano_derecha.setOnTouchListener(null);
        }
        if (!lesion_presente_mi) {
            mano_izq_active = false;
            label_mano_izquierda.setOnTouchListener(null);
        }
        if (!lesion_presente_bd) {
            brazo_der_active = false;
            label_brazo_derecho.setOnTouchListener(null);
        }
        if (!lesion_presente_bi) {
            brazo_izq_active = false;
            label_brazo_izquierdo.setOnTouchListener(null);
        }
        if (!lesion_presente_pd) {
            pierna_der_active = false;
            label_pierna_derecha.setOnTouchListener(null);
        }
        if (!lesion_presente_pi) {
            pierna_izq_active = false;
            label_pierna_izquierda.setOnTouchListener(null);
        }

        subtitulo_cuerpo.setText("Seleccione la zona del cuerpo donde se encuentran ubicadas las lesiones para fotografiarla.");
        modo_nueva_lesion = false;
        PreferenceManager.getDefaultSharedPreferences(this).edit().remove("modo_nueva_lesion").commit();
    }

    public void terminarToma(View v) {
        Intent intent = new Intent(getApplicationContext(), Evaluacion.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        borrarPreferences();

        String id = PreferenceManager.getDefaultSharedPreferences(this).getString("paciente_id", "");
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("toma_fotos_terminada", true)
                .putBoolean("F" + id, true)
                .commit();



        String path = Environment.getExternalStorageDirectory()+"/REMODEL.json";

        try (
                Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(path), "UTF8"))
                ) {

            Schema schema = db.buscarSchemaActivoDelPaciente(paciente);
            DailySchema ds = db.buscarDailySchema(schema.getUuid(), Calendar.getInstance().getTime());
            if(ds == null){
                //Agregar DailySchema completo y finalizarlo en null
                db.agregarDailySchema(CuerpoHumanoActivity.dailySchema);
                db.agregarUIcerForm( CuerpoHumanoActivity.dailySchema.getImagenes().getUlcerForms().get(0) );
                for(int i=0 ; i<CuerpoHumanoActivity.dailySchema.getImagenes().getUlcerForms().get(0).getUlcerImages().getUlcerImages().size() ; i++){
                    db.agregarUIcerImg( CuerpoHumanoActivity.dailySchema.getImagenes().getUlcerForms().get(0).getUlcerImages().getUlcerImages().get(i) );
                }
            }else{
                //Editar el dailySchema
                db.editarDailySchema(CuerpoHumanoActivity.dailySchema);
                db.editarUIcerForm( ds.getImagenes().getUlcerForms().get(0) );
                for(int i=0 ; i<CuerpoHumanoActivity.dailySchema.getImagenes().getUlcerForms().get(0).getUlcerImages().getUlcerImages().size() ; i++){
                    db.agregarUIcerImg( CuerpoHumanoActivity.dailySchema.getImagenes().getUlcerForms().get(0).getUlcerImages().getUlcerImages().get(i) );
                }
            }

            CuerpoHumanoActivity.dailySchema.getImagenes().getUlcerForms().get(0).getUlcerImages().getUlcerImages().clear();
            dailySchema = null;


            Evaluador rater_db = db.getRaterByUUID(paciente.getEvaluadorId());
            Evaluador fullRater = db.getFullRater(rater_db);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
            String json = gson.toJson(fullRater);
            Log.e("REMODELACION",""+json);
            writer.write(json);


        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    private void borrarPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();


        edit.remove("modo_nueva_lesion");
        edit.remove("last_foto");
        edit.remove("id_zona");
        edit.remove("parte_actual");
        edit.remove("foto_path");
        edit.remove("prueba_c");
        edit.remove("prueba_t");
        edit.remove("prueba_bd");
        edit.remove("prueba_bi");
        edit.remove("prueba_mi");
        edit.remove("prueba_md");
        edit.remove("prueba_pd");
        edit.remove("prueba_pi");
        edit.remove("c_ok");
        edit.remove("t_ok");
        edit.remove("bd_ok");
        edit.remove("bi_ok");
        edit.remove("pd_ok");
        edit.remove("pi_ok");
        edit.remove("md_ok");
        edit.remove("mi_ok");
        edit.remove("body_name");
        edit.remove("foto_path");
        edit.remove("modo_eliminar_foto");
        for (int i = 1; i <= 134; i++) {
            edit.remove("BP" + i);
        }
        edit.commit();
    }

    public static void eliminarFotosNoGuardadas() {
        /*
        if(CuerpoHumanoActivity.dailySchema == null) return;
        //Eliminar las fotos que quedaron en la variable statica
        for (int j = 0; j < CuerpoHumanoActivity.dailySchema.getImagenes().getUlcerForms().get(0).getUlcerImages().getUlcerImages().size(); j++) {
            String foto_name = CuerpoHumanoActivity.dailySchema.getImagenes().getUlcerForms().get(0).getUlcerImages().getUlcerImages().get(j).getImgUUID();
            String foto_path = Environment.getExternalStorageDirectory()+"/LeishST/"+foto_name+".jpg";
            File f = new File(foto_path);
            f.delete();
        }
        CuerpoHumanoActivity.dailySchema.getImagenes().getUlcerForms().get(0).getUlcerImages().getUlcerImages().clear();
        */
    }

    @Override
    protected void onDestroy() {
        eliminarFotosNoGuardadas();
        super.onDestroy();
    }
}
