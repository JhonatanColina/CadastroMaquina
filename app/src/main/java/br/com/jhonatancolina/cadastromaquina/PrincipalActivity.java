package br.com.jhonatancolina.cadastromaquina;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.jhonatancolina.cadastromaquina.service.MaquinaAPI;
import br.com.jhonatancolina.cadastromaquina.service.SessionRepository;
import br.com.jhonatancolina.cadastromaquina.service.UsuarioAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Referencias
    private SessionRepository sr = new SessionRepository();
    private TextView tvNomeUsuario,tvNivelAdm;
    private ImageView imagemMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        verificaNavigation(navigationView);

        changeFragment(new DashBoardFragment());
        setTitle(R.string.inicio);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.dashBoard:
                changeFragment(new DashBoardFragment());
                setTitle(R.string.inicio);
                break;
                case R.id.novoUsuario:
                changeFragment(new NovoUsuarioFragment());
                setTitle(R.string.menu_novoUsuario);
                break;
                case R.id.listaUsuario:
                changeFragment(new ListaUsuarioFragment());
                setTitle(R.string.menu_listarUsuario);
                break;
            case R.id.novaMaquina:
                changeFragment(new NovaMaquinaFragment());
                setTitle(R.string.menu_novaMaquina);
                break;
                case R.id.listaMaquina:
                changeFragment(new ListaMaquinaFragment());
                setTitle(R.string.menu_listaMaquina);
                break;
            case R.id.sobre:
                changeFragment(new SobreFragment());
                setTitle(R.string.sobre);
                break;
            case R.id.fazerLogoff:
                fazLogoff();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeFragment(Fragment fragment) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_principal, fragment);
        ft.commit();
    }

    public void verificaNavigation(NavigationView nv)
    {
        //Dados da navigation
        tvNomeUsuario = (TextView) nv.getHeaderView(0).findViewById(R.id.tvNomeUsuario);
        tvNivelAdm = (TextView) nv.getHeaderView(0).findViewById(R.id.tvNivelAdm);
        imagemMenu = (ImageView) nv.getHeaderView(0).findViewById(R.id.imagemMenu);
        imagemMenu.setImageResource(sr.getUsuario().isAdmin()?R.drawable.ti:R.drawable.user_orange);
        preencheDadosUsuario();
        //se nao for adm nao existe a opção de usuarios
        if(!sr.getUsuario().isAdmin())
        {
            nv.getMenu().getItem(1).setVisible(false);
        }
    }

    public void preencheDadosUsuario()
    {
        tvNivelAdm.setText(sr.getUsuario().isAdmin()?getString(R.string.usuarioAdmin):getString(R.string.usuarioComum));
        tvNomeUsuario.setText(sr.getUsuario().getUsuario().toString());
    }

    public void abreConfiguracao(MenuItem item)
    {
        Intent intent = new Intent(PrincipalActivity.this,
                ConfiguracoesActivity.class);
        startActivity(intent);
    }

    private void fazLogoff()
    {
        sr.setUsuario(null);
        Intent intent = new Intent(this,LoginActivity.class);
        this.finish();
        startActivity(intent);
    }
}
