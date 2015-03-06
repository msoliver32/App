package com.app.campeonatovirtualdefutebol;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.app.campeonatovirtualdefutebol.dao.Time;
import com.app.campeonatovirtualdefutebol.pojo.Competidor;

public class BancoDados extends SQLiteOpenHelper {

	
	
	private static String nomedb = "campeonato_virtual";
	
	private static final String LOG = nomedb;

    private final String tblTimes = "times";

	private final String tblCompetidores = "competidores";

    private final String criarTabelaTimes = ("CREATE TABLE IF NOT EXISTS times "
            + " (id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR(32) NOT NULL, time INTEGER, liga VARCHAR(32) NULL); ");
	
	private final String criarTabelaCompetidores = ("CREATE TABLE IF NOT EXISTS competidores "
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT , nome VARCHAR(32) NOT NULL, time INTEGER, FOREIGN KEY(time) REFERENCES times(id)); ");
	
	private Context ct;
    private Competidor competidor = new Competidor();

	public BancoDados(Context context) {

		
		super(context, nomedb, null, 1);
		this.ct = context;

	}


	@Override
	public void onCreate(SQLiteDatabase db) {

        db.execSQL(criarTabelaTimes);
		db.execSQL(criarTabelaCompetidores);
        Time time = new Time();
        time.setNome("Flamengo");
        time.setLiga("Campeonato Carioca");
        adicionarTime(time);


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS "+tblCompetidores);
        db.execSQL("DROP TABLE IF EXISTS "+tblTimes);
	}

    public SQLiteDatabase teste(){

        return this.getWritableDatabase();
    }
	
	public void adicionarCompetidor(Competidor competidor) {
		
		try{


            if(competidor.getTime() == 0){

                exibirMensagem("Erro Banco", "Time não Encontrado!");
            }else {

                SQLiteDatabase db = this.getWritableDatabase();


                ContentValues values = new ContentValues();
                values.put("nome", competidor.getNomeApelido());
                values.put("time", competidor.getId());

                db.insert(tblCompetidores, null, values);

                exibirMensagem("Info Banco", "Competidor registrado com sucesso!");
            }
		} catch (Exception erro) {
			
			exibirMensagem("Erro Banco", "Erro ao gravar competidor no banco: "
					+ erro);
		}
	    
	}
	
	public List<Competidor> buscarCompetidores(Competidor competidor) {
		
	    List<Competidor> lista = new ArrayList<Competidor>();
	    String query = "SELECT id, nome, time FROM " + tblCompetidores + " WHERE nome LIKE '%"+competidor.getNomeApelido()+"%' ORDER BY nome";
	 

	    try{
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(query, null);
        //Log.e(LOG, query);
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	    	
	        do {

                competidor.setId(c.getInt(c.getColumnIndex("id")));
	            competidor.setNomeApelido(c.getString(c.getColumnIndex("nome")));
                competidor.setId(c.getInt(c.getColumnIndex("time")));
	            lista.add(competidor);
                competidor = new Competidor();
	        } while (c.moveToNext());
	    }


	    return lista;

        } catch (Exception erro) {

            exibirMensagem("Erro Banco", "Erro ao buscar competidor no banco: "
                    + erro);
            return new ArrayList<Competidor>();
        }
	}


    public Competidor buscarCompetidor(Competidor competidor) {

        String query = "SELECT id, nome, time FROM " + tblCompetidores + " WHERE nome = '"+competidor.getNomeApelido()+"'";

        try{
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);


        if (c.moveToFirst()) {


            competidor.setId(c.getInt(c.getColumnIndex("id")));
            competidor.setNomeApelido(c.getString(c.getColumnIndex("nome")));
            competidor.setTime(c.getInt(c.getColumnIndex("time")));
        }


        exibirMensagem("TESTE", "ID: "+competidor.getId());


        //if(competidor.getId() <= 0){

          //  exibirMensagem("Info Banco", "Não há competidor registrado!");
          //  return null;
        //}else {

        return competidor;
        } catch (Exception erro) {

            exibirMensagem("Erro Banco", "Erro ao buscar competidor no banco: "
                    + erro);
            return new Competidor();
        }
        //}
    }

    public void adicionarTime(Time time) {

        try{

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("nome", time.getNome());

            values.put("liga", time.getLiga());


            db.insert(tblTimes, null, values);


            exibirMensagem("Info Banco", "Time registrado com sucesso!");

        } catch (Exception erro) {


            exibirMensagem("Erro Banco", "Erro ao gravar time no banco: "
                    + erro);
        }

    }

    public Time buscarTime(Time time) {

        String query;
        if((time.getId() == 0) && (time.getNome().equals(""))){

            return new Time();
        }else if(time.getId() > 0){

            query = "SELECT id, nome, liga FROM " + tblTimes+ " WHERE id = "+time.getId();
        }else {

            query = "SELECT id, nome, liga FROM " + tblTimes + " WHERE nome = '" + time.getNome()+"'";
        }

        try {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {


            time.setId(c.getInt(c.getColumnIndex("id")));
            time.setNome(c.getString(c.getColumnIndex("nome")));
            time.setLiga(c.getString(c.getColumnIndex("liga")));
        }

        //exibirMensagem("TESTE", "ID: "+competidor.getId());

        return time;

        }catch (Exception erro) {

            exibirMensagem("Erro Banco", "Erro ao buscar time no banco: "
                + erro);
            return new Time();
        }
    }

	public void exibirMensagem(String titulo, String texto) {

		AlertDialog.Builder mensagem = new AlertDialog.Builder(ct);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("OK", null);
		mensagem.show();
	}
}
