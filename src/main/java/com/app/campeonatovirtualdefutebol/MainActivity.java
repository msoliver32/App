package com.app.campeonatovirtualdefutebol;


import java.util.ArrayList;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import com.app.campeonatovirtualdefutebol.dao.Time;
import com.app.campeonatovirtualdefutebol.pojo.Competidor;

public class MainActivity extends Activity {

    Button btn_competidoresMenu, btn_adicionarCompetidores, btn_alterarCompetidores, btn_adicionarCompetidor, btn_cancelarCompetidor;
    ImageButton imgbtn_voltaMenuPrincipalCompetidores, imgbtn_voltaMenuCompetidoresCompetidor;
    AutoCompleteTextView edt_nomeApelidoCompetidores,  edt_timeCompetidor;
    EditText edt_nomeApelidoCompetidor;
    GridView grv_listaCompetidores;

    BancoDados banco;

    ArrayList<Competidor> competidores = new ArrayList<Competidor>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chamaMenuPrincipal();
    }

    public void chamaMenuPrincipal() {

        setContentView(R.layout.activity_main);

        banco = new BancoDados(this);

        //banco.onUpgrade(banco.teste(), 0, 0);
        //banco.onCreate(banco.teste());

        btn_competidoresMenu = (Button) findViewById(R.id.btn_competidoresMenu);

        btn_competidoresMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                chamaTelaCompetidores();
            }
        });

    }

    public void chamaTelaCompetidores() {

        setContentView(R.layout.competidores);

        btn_adicionarCompetidores = (Button) findViewById(R.id.btn_adicionarCompetidores);
        btn_alterarCompetidores = (Button) findViewById(R.id.btn_alterarCompetidores);

        edt_nomeApelidoCompetidores = (AutoCompleteTextView) findViewById(R.id.edt_nomeApelidoCompetidores);

        imgbtn_voltaMenuPrincipalCompetidores = (ImageButton) findViewById(R.id.imgbtn_voltaMenuPrincipalCompetidores);

        grv_listaCompetidores = (GridView) findViewById(R.id.gridview);

        setAdapterCompetidores(new Competidor(), true);


        edt_nomeApelidoCompetidores.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


                Competidor competidor = new Competidor();
                competidor.setNomeApelido(edt_nomeApelidoCompetidores.getText().toString());
                setAdapterCompetidores(competidor, false);
            }

        });

        imgbtn_voltaMenuPrincipalCompetidores
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        chamaMenuPrincipal();

                    }
                });

        btn_adicionarCompetidores.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String nome = edt_nomeApelidoCompetidores.getText().toString();


                if (nome.equals("")) {

                    chamaTelaCompetidor(new Competidor());
                }else{

                    Competidor competidor = new Competidor();
                    competidor.setNomeApelido(nome);
                    competidor = banco.buscarCompetidor(competidor);
                    if(competidor.equals(null)){

                        competidor.setNomeApelido("");
                    }

                    chamaTelaCompetidor(competidor);

                    //banco.adicionarCompetidor("");
                    setAdapterCompetidores(new Competidor(), false);
                }
            }
        });


        btn_alterarCompetidores.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //chamaTeste();
            }
         });

    }

    public void chamaTelaCompetidor(Competidor competidor){

        setContentView(R.layout.competidor);

        edt_nomeApelidoCompetidor = (EditText) findViewById(R.id.edt_nomeApelidoCompetidor);
        edt_timeCompetidor = (AutoCompleteTextView) findViewById(R.id.edt_timeCompetidor);

        btn_adicionarCompetidor = (Button) findViewById(R.id.btn_adicionarCompetidor);
        btn_cancelarCompetidor = (Button) findViewById(R.id.btn_cancelarCompetidor);

        if(competidor.getNomeApelido().equals("")){

            edt_nomeApelidoCompetidor.setText("");
            edt_timeCompetidor.setText("");
        }else {

            Time time =  new Time();
            time.setId(competidor.getId());
            time = banco.buscarTime(time);

            edt_nomeApelidoCompetidor.setText(competidor.getNomeApelido());
            edt_timeCompetidor.setText(time.getNome());
        }

        btn_cancelarCompetidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Competidor competidor = new Competidor();
                competidor.setNomeApelido(edt_nomeApelidoCompetidor.getText().toString());
                Time time = new Time();
                time.setNome(edt_timeCompetidor.getText().toString());
                time = banco.buscarTime(time);
                if(time.getId() == 0){

                    exibirMensagem("Info Banco", "Time não existe no banco: "+time.getNome());
                }else{
                    exibirMensagem("TESTES", "Time TESTE: "+time.getId());
                    competidor.setTime(time.getId());
                    banco.adicionarCompetidor(competidor);
                }
            }
        });
        /*
        if(competidores.indexOf(nome) == -1){

            edt_nomeApelidoCompetidor.setText("");
            edt_timeCompetidor.setText("");
        }else{

            banco.buscarCompetidor(new Competidor());
            edt_nomeApelidoCompetidor.setText("");
            edt_timeCompetidor.setText("");
        }
        */
    }

    public void chamaTeste(){

        // Chamar outra activity
        Intent minhaTela = new Intent(this, ListaAdapter.class);
        this.startActivity(minhaTela);
    }

    public void setAdapterCompetidores(Competidor competidor, boolean primeira) {


        competidores = (ArrayList<Competidor>) banco.buscarCompetidores(competidor);

        if((competidores.size() < 1) && (primeira == true)) {

            exibirMensagem("Info Banco", "Não há competidores registrados!");
        }
        else {

            ArrayList<String> lista = new ArrayList<String>();
            for (Competidor c : competidores) {
                lista.add(c.getNomeApelido());
            }

            if ((competidores.size() >= 1) && (competidor.getNomeApelido().length() > 0)) {

                grv_listaCompetidores.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lista));

                //exibirMensagem("teste","foi so grid n:"+competidores.size()+" string:"+nome);
            } else {

                edt_nomeApelidoCompetidores.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lista));
                grv_listaCompetidores.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lista));
            }
        }
    }


    public void exibirMensagem(String titulo, String texto) {

        AlertDialog.Builder mensagem = new AlertDialog.Builder(MainActivity.this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);
        mensagem.setNeutralButton("OK", null);
        mensagem.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
