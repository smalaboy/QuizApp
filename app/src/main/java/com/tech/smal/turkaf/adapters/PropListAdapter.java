package com.tech.smal.turkaf.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tech.smal.turkaf.R;
import com.tech.smal.turkaf.data.GradesReport;
import com.tech.smal.turkaf.data.QuestionDetails;
import com.tech.smal.turkaf.data.models.Question_;

import java.util.ArrayList;

/**
 * Created by smoct on 22/03/2019.
 */

public class PropListAdapter extends RecyclerView.Adapter<PropListAdapter.VH>  {

    public final static String TAG = PropListAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<Question_> mDataSet;
    private ClickListener listener;

    public PropListAdapter(Context mContext, ArrayList<Question_> mDataSet) {
        this.mContext = mContext;
        this.mDataSet = mDataSet;
    }

    @NonNull
    @Override
    public PropListAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.prop_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropListAdapter.VH holder, int position) {
        Question_ qst = mDataSet.get(position);
        //GradesReport report = new GradesReport(qst.getUser().getId(), qst.getGradesList());
        //report.makeReport();
        holder.tvQst.setText(qst.getQuestion());
        holder.tvPosRating.setText(20+"");
        holder.tvNegRating.setText(5+"");
    }

    public Question_ getItem(int position) {
        if (mDataSet == null)
            return null;
        return mDataSet.get(position);
    }

    public void setItem(int position, Question_ newQst) {
        mDataSet.set(position, newQst);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null)
            return 0;
        return mDataSet.size();
    }

    public void setData(ArrayList<Question_> dataSet) {
        if (mDataSet != null)
            mDataSet.clear();
        mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }


    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvQst;
        TextView tvPosRating;
        TextView tvNegRating;
        public VH(View itemView) {
            super(itemView);
            tvQst = (TextView)itemView.findViewById(R.id.tv_question);
            tvPosRating = (TextView)itemView.findViewById(R.id.tv_pos_rating);
            tvNegRating = (TextView)itemView.findViewById(R.id.tv_neg_rating);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(this.getAdapterPosition());
        }
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public interface ClickListener {
        public void onItemClick(int position);
    }
}
