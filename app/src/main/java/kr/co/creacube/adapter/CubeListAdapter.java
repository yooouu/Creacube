package kr.co.creacube.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.co.creacube.ConnectActivity;
import kr.co.creacube.R;

public class CubeListAdapter extends RecyclerView.Adapter<CubeListAdapter.CubeHolder> {

    private ConnectActivity activity;
    private List<String> list;

    public CubeListAdapter(ConnectActivity activity, List<String> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public CubeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cube_list, parent, false);
        return new CubeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CubeHolder holder, final int position) {
        holder.onBind(list.get(position), position);
    }

    @Override
    public void onBindViewHolder(@NonNull CubeHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class CubeHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView tvCubeName;

        CubeHolder(View view) {
            super(view);
            this.view = view;
            this.tvCubeName = view.findViewById(R.id.tv_cube_name);
        }

        void onBind(String data, final int position) {
            tvCubeName.setText(data);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO
                }
            });
        }
    }
}
