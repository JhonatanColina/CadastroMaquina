package br.com.jhonatancolina.cadastromaquina.adapter;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.jhonatancolina.cadastromaquina.R;
import br.com.jhonatancolina.cadastromaquina.core.ClickRecyclerView_Interface;
import br.com.jhonatancolina.cadastromaquina.model.Usuario;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>
{
    public static ClickRecyclerView_Interface clickRecyclerViewInterface;
    Context mctx;
    private List<Usuario> users;

    public UsuarioAdapter(Context ctx, List<Usuario> list, ClickRecyclerView_Interface clickRecyclerViewInterface)
    {
        this.mctx = ctx;
        this.users = list;
        this.clickRecyclerViewInterface = clickRecyclerViewInterface;
    }

    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.linha_lista_usuario, viewGroup, false);
        return new UsuarioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UsuarioViewHolder viewHolder, int i)
    {
        Usuario user = users.get(i);

        viewHolder.setIsRecyclable(false);

        viewHolder.nomeUsuarioView.setText(viewHolder.nomeUsuarioView.getText().toString()+": "+user.getUsuario());
        viewHolder.nivelAdmView.setText(viewHolder.nivelAdmView.getText().toString()+": "+(user.isAdmin()?"Administrator":"Common User"));
        viewHolder.tvIDListaUsuario.setText(viewHolder.tvIDListaUsuario.getText().toString()+": "+user.getId());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount()
    {
        return users.size();
    }

    protected class UsuarioViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView nomeUsuarioView,nivelAdmView,tvIDListaUsuario;

        protected FloatingActionButton btnRemoverUserListaUsuario;

        public UsuarioViewHolder(final View itemView)
        {
            super(itemView);

            nomeUsuarioView = (TextView) itemView.findViewById(R.id.tvNomeListaUsuario);
            nivelAdmView = (TextView) itemView.findViewById(R.id.tvNivelAdmListaUsuario);
            tvIDListaUsuario = (TextView) itemView.findViewById(R.id.tvIDListaUsuario);
            btnRemoverUserListaUsuario = (FloatingActionButton) itemView.findViewById(R.id.btnRemoverUserListaUsuario);

            //Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    clickRecyclerViewInterface.onCustomClick(users.get(getLayoutPosition()));
                }
            });

            //Setup the click listener
            btnRemoverUserListaUsuario.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    clickRecyclerViewInterface.onCloseButton(users.get(getLayoutPosition()));
                }
            });
        }
    }
}
