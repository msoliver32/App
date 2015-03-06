package com.app.campeonatovirtualdefutebol;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;


public class ListaAdapter  extends Activity{

    GridView grv_listaCompetidores;
    ArrayList<String> competidores;
    BancoDados banco;
	
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


		chamaPrincipal();
	}

    public void chamaPrincipal(){

        banco = new BancoDados(this);
        //competidores = (ArrayList<String>) banco.buscarCompetidores("");

        setContentView(R.layout.teste);

        grv_listaCompetidores = (GridView) findViewById(R.id.gridview);
        setAdapterCompetidores();
    }



    public void setAdapterCompetidores() {

        grv_listaCompetidores.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, competidores));
    }

}
