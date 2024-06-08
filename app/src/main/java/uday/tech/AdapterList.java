package uday.tech;

import android.view.LayoutInflater;
import  android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> {
    private List<ItemList> itemLists;

    public AdapterList(List<ItemList> itemLists){
        this.itemLists = itemLists;
    }

    @NonNull
    @Override
    public AdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterList.ViewHolder holder, int position) {
        ItemList item = itemLists.get(position);
        holder.judul.setText(item.getJudul());
        holder.deskripsi.setText(item.getDeskripsi());
        Glide.with(holder.imageView.getContext()).load(item.getImgUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView judul, deskripsi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_img);
            judul= itemView.findViewById(R.id.judul);
            deskripsi= itemView.findViewById(R.id.deskripsi);

        }
    }
}
