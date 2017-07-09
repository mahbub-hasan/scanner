package com.muktolab.imagetopdf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muktolab.imagetopdf.R;
import com.muktolab.imagetopdf.model.PDFAdapter;
import com.muktolab.imagetopdf.model.PDFAdapterRoot;
import com.muktolab.imagetopdf.model.PDFFileInfo;
import com.muktolab.imagetopdf.model.PdfImageFile;

import java.util.ArrayList;

/**
 * Created by mahbubhasan on 6/17/17.
 */

public class PDFContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    PDFAdapterRoot roots;
    PDFAdapter pdfAdapter;

    int HEADER = 0;
    int ITEM = 1;

    public PDFContentAdapter(Context _context, PDFAdapterRoot object){
        context = _context;
        roots = object;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pdf_content, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if(holder instanceof ItemViewHolder){
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                pdfAdapter = roots.pdfAdaptersRoot.get(position);
                itemViewHolder.pdfFileName.setText(pdfAdapter.pdfFileName);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return roots.pdfAdaptersRoot.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pdfFileName;
        public ItemViewHolder(View itemView) {
            super(itemView);
            pdfFileName = itemView.findViewById(R.id.pdfFileName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder{

        TextView date, count;
        HeaderViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.headerDate);
            count = itemView.findViewById(R.id.headerNumberOfPDF);
        }
    }
}
