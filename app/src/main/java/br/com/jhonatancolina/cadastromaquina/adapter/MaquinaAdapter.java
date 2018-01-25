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
import br.com.jhonatancolina.cadastromaquina.model.Maquina;

public class MaquinaAdapter extends RecyclerView.Adapter<MaquinaAdapter.MaquinaViewHolder>
{
    public static ClickRecyclerView_Interface clickRecyclerViewInterface;
    Context mctx;
    private List<Maquina> maquinas;

    public MaquinaAdapter(Context ctx, List<Maquina> list, ClickRecyclerView_Interface clickRecyclerViewInterface)
    {
        this.mctx = ctx;
        this.maquinas = list;
        this.clickRecyclerViewInterface = clickRecyclerViewInterface;
    }

    @Override
    public MaquinaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.linha_lista_maquina, viewGroup, false);
        return new MaquinaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MaquinaViewHolder viewHolder, int i)
    {
        Maquina maquina = maquinas.get(i);

        viewHolder.setIsRecyclable(false);

        viewHolder.nomeMaquinaView.setText(viewHolder.nomeMaquinaView.getText().toString()+": "+maquina.getHostname());
        viewHolder.localMaquinaView.setText(viewHolder.localMaquinaView.getText().toString()+": "+maquina.getLocal());
        viewHolder.tvIDListaMaquina.setText(viewHolder.tvIDListaMaquina.getText().toString()+": "+maquina.getId());
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
        return maquinas.size();
    }

    protected class MaquinaViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView nomeMaquinaView,localMaquinaView,tvIDListaMaquina;
        protected FloatingActionButton btnRemoverMaquinaListaMaquina;

        public MaquinaViewHolder(final View itemView)
        {
            super(itemView);

            nomeMaquinaView = (TextView) itemView.findViewById(R.id.tvHostanameListaMaquina);
            localMaquinaView = (TextView) itemView.findViewById(R.id.tvLocalListaMaquina);
            tvIDListaMaquina = (TextView) itemView.findViewById(R.id.tvIDListaMaquina);
            btnRemoverMaquinaListaMaquina = (FloatingActionButton) itemView.findViewById(R.id.btnRemoverMaquinaListaMaquina);

            //Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    clickRecyclerViewInterface.onCustomClick(maquinas.get(getLayoutPosition()));
                }
            });

            //Setup the click listener
            btnRemoverMaquinaListaMaquina.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    clickRecyclerViewInterface.onCloseButton(maquinas.get(getLayoutPosition()));
                }
            });
        }
    }
}