package br.pro.ednilsonrossi.meupocket.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.pro.ednilsonrossi.meupocket.R;
import br.pro.ednilsonrossi.meupocket.model.Site;

public class SiteDao {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String TAG = "SITE DAO";

    public static final List<Site> recuperateAll(Context context) {

        JSONObject jsonObject;
        JSONArray jsonArray;

        sharedPreferences    = context.getSharedPreferences(context.getString(R.string.key_sites), context.MODE_PRIVATE);
        String sites         = sharedPreferences.getString(context.getString(R.string.key_sites), "");
        List<Site> siteLista = new ArrayList<>();

        try{
            jsonArray = new JSONArray(sites);

            for(int i = 0; i < jsonArray.length(); i++){

                jsonObject = jsonArray.getJSONObject(i);
                Site site = new Site(jsonObject.getString("nome"), jsonObject.getString("site"), jsonObject.getBoolean("favoritar"));
                // Adiciona o site a nossa lista.
                siteLista.add(site);

            }

        } catch (JSONException ex){
            Log.e(TAG,"Não foi possível recuperar o site.");
            siteLista.clear();
        }
        return siteLista;
    }

    public static final void inserir(Context context, Site site) {
        List<Site> siteList = recuperateAll(context);
        siteList.add(site);
        inserirLista(context, siteList);
    }

    public static void update(Context context, Site site) {
        List<Site> siteList = recuperateAll(context);
        for (Site s : siteList) {
            if(s.equals(site)) {
                if(site.isFavorito()){
                    s.doFavotite();
                }else{
                    s.undoFavorite();
                }
            }
        }
        inserirLista(context, siteList);
    }

    private static void inserirLista(Context context, List<Site> siteList){

        sharedPreferences = context.getSharedPreferences(context.getString(R.string.key_sites), context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();

        for(Site s : siteList){
            jsonObject = new JSONObject();
            try {
                jsonObject.put("nome", s.getTitulo());
                jsonObject.put("site", s.getEndereco());
                jsonObject.put("favoritar", s.isFavorito());
                jsonArray.put(jsonObject);
            }catch (JSONException e){
                Log.e(TAG,context.getString(R.string.erro_busca));
            }
        }

        String sites = jsonArray.toString();
        editor.putString(context.getString(R.string.key_sites), sites);
        editor.commit();
    }

}