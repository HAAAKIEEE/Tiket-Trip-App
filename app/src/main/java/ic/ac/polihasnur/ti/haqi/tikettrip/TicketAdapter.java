package ic.ac.polihasnur.ti.haqi.tikettrip;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyTicket> myTicket;

    public TicketAdapter(Context c, ArrayList<MyTicket> p) {
        context = c;
        myTicket = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MyViewHolder(LayoutInflater
                .from(context).inflate(R.layout.item_myticket,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myt_nama_wisata.setText(myTicket.get(position).getNama_wisata());
        holder.myt_lokasi.setText(myTicket.get(position).getLokasi());
        holder.myt_jumlahtiket.setText(myTicket.get(position).getJumlah_tiket() + "  Tiket");

        final String getNamaWisata = myTicket.get(position).getNama_wisata();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gomydetailtick = new Intent(context, MyTicketDetailAct.class);
                gomydetailtick.putExtra("nama_wisata" , getNamaWisata );
                context.startActivity(gomydetailtick);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myTicket.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
TextView myt_jumlahtiket,myt_nama_wisata,myt_lokasi;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myt_lokasi = itemView.findViewById(R.id.myt_lokasi);
            myt_jumlahtiket = itemView.findViewById(R.id.myt_jumlahtiket);
            myt_nama_wisata = itemView.findViewById(R.id.myt_nama_wisata);
        }
    }
}
