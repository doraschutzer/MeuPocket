package br.pro.ednilsonrossi.meupocket.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import br.pro.ednilsonrossi.meupocket.R;
import br.pro.ednilsonrossi.meupocket.dao.SiteDao;
import br.pro.ednilsonrossi.meupocket.model.Site;

public class NovoSiteActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor mEditor;
    private EditText editTextNome;
    private EditText editTextSite;
    private CheckBox checkBoxFavorito;
    private Button buttonSalvar;
    private Site site;
    private List<Site> siteLista = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_site);

        editTextNome = findViewById(R.id.edittext_nome);
        editTextSite = findViewById(R.id.edittext_site);
        checkBoxFavorito = findViewById(R.id.checkbox_favoritar);
        buttonSalvar = findViewById(R.id.button_salvar);

        buttonSalvar.setOnClickListener(this);

        sharedPreferences = this.getSharedPreferences(getString(R.string.site ), MODE_PRIVATE);
        mEditor = sharedPreferences.edit();
    }

    private void cadastrarSite() {
        if(validaSite()){
            adicionar();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSalvar){
            cadastrarSite();
        }
    }

    private Boolean validaSite(){
        String nome = editTextNome.getText().toString();
        String siteEndereco = editTextSite.getText().toString();

        if ( !nome.isEmpty() && !siteEndereco.isEmpty() ){
            site = new Site(nome, siteEndereco);
            if(checkBoxFavorito.isChecked()){
                site.doFavotite();
            }else{
                site.undoFavorite();
            }
            return true;
        } else {
            Toast.makeText(this, R.string.erro_cadastro, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void adicionar(){
        SiteDao.inserir(getApplicationContext(), site);
    }
}
