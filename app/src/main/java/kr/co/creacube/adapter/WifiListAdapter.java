package kr.co.creacube.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.creacube.R;

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.WifiListViewHolder> {

    Context context;
    private ArrayList<String> wifiList;
    private int selectedPosition = -1;

    public ArrayList<String> getWifiList() {
        return wifiList;
    }

    public void setWifiList(ArrayList<String> wifiList) {
        this.wifiList = wifiList;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @NonNull
    @Override
    public WifiListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_list_popup, parent, false);
        WifiListViewHolder viewHolder = new WifiListViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WifiListViewHolder holder, int position) {
        String ssid = wifiList.get(position);

        holder.tvWifiSSID.setText(ssid);

        if (selectedPosition == position) {
            holder.tvWifiSSID.setTextColor(Color.parseColor("#FFFF00"));

        } else {
            holder.tvWifiSSID.setTextColor(Color.parseColor("#FFFFFF"));
        }

        holder.wifiItem.setTag(""+position);
        holder.wifiItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = Integer.parseInt((String)view.getTag());
                selectedPosition = pos;
                (WifiListAdapter.this).notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (wifiList != null ? wifiList.size() : 0);
    }

    public class WifiListViewHolder extends RecyclerView.ViewHolder {

        LinearLayout wifiItem;
        TextView tvWifiSSID;

        public WifiListViewHolder(@NonNull View itemView) {
            super(itemView);

            wifiItem = itemView.findViewById(R.id.wifi_item);
            tvWifiSSID = itemView.findViewById(R.id.tv_wifi_ssid);
        }
    }
}
