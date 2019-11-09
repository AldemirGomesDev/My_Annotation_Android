package com.android.myannotations.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.myannotations.MainActivity;
import com.android.myannotations.R;
import com.android.myannotations.retrofit.controllers.AnnotationController;
import com.android.myannotations.models.Annotation;
import com.android.myannotations.retrofit.models.api.AnnotationResult;
import com.android.myannotations.retrofit.theards.IThreadResult;

import java.io.IOException;
import java.util.List;

public class AnnotationAdapter  extends RecyclerView.Adapter<AnnotationAdapter.ViewHolder> {

        private List<Annotation> annotations;
        private Activity context;
        private static final String TAG ="AnnotationAdapter";
        private MainActivity mainActivity;
        private Dialog dialog;

        public AnnotationAdapter(List<Annotation> annotations, Activity context){
            this.context = context;
            this.annotations = annotations;

            this.dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
            this.dialog.setContentView(R.layout.loading_custom);
        }

        @Override
        public AnnotationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.main_line_view, parent, false);

            return new ViewHolder(view);
        }


    @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.title.setText(annotations.get(position).getTitulo());
            holder.date.setText(annotations.get(position).getCreatedAt());
            holder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog(position);
                }
            });
            holder.card_view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.setContentView(R.layout.dialog_excluir);
                    final Button confirmar = (Button) dialog.findViewById(R.id.btn_del_sim);
                    final Button cancelar = (Button) dialog.findViewById(R.id.btn_del_nao);

                    confirmar.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            DeleteAnnotationThread getRoutersThread = new DeleteAnnotationThread(annotations.get(position).getId());
                            getRoutersThread.setOnResult(new IThreadResult<AnnotationResult>() {
                                @Override
                                public void onResult(AnnotationResult routerResult) {
                                    if(routerResult != null || routerResult.getAnnotations() != null){
                                        annotations = routerResult.getAnnotations();
                                        notifyDataSetChanged();
                                    }
                                }
                            });
                            getRoutersThread.execute();
                            dialog.dismiss();
                        }
                    });

                    cancelar.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(context,  "Cancelado com sucesso! ", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return true;
                }
            });


        }

        @Override
        public int getItemCount() {
            return annotations != null ? annotations.size() : 0;
        }

    private class UpdateAnnotationThread extends AsyncTask<Void, Void, AnnotationResult> {

        private IThreadResult<AnnotationResult> iThreadResult;
        private int id;
        private String titulo;
        private String message;
        private ProgressDialog progressBar;
        Annotation annotation;

        private UpdateAnnotationThread(int id, String titulo, String message, Annotation annotation) {
            this.id = id;
            this.titulo = titulo;
            this.message = message;
            this.annotation = annotation;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected AnnotationResult doInBackground(Void... voids) {
            try {
                Log.w("RouterSaver", "updateAnnotation: " + annotation.toString());
                return new AnnotationController().updateAnnotation(id, annotation);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(AnnotationResult routerResult) {
            dialog.dismiss();
            if (iThreadResult != null)
                iThreadResult.onResult(routerResult);
            annotations = routerResult.getAnnotations();
            Toast.makeText(context,  "Atualizado com sucesso! ", Toast.LENGTH_SHORT).show();

        }

        public void setOnResult(IThreadResult<AnnotationResult> iThreadResult) {
            this.iThreadResult = iThreadResult;
        }
    }

    private class DeleteAnnotationThread extends AsyncTask<Void, Void, AnnotationResult> {

        private IThreadResult<AnnotationResult> iThreadResult;
        private int id;
        private ProgressDialog progressBar;

        private DeleteAnnotationThread(int id) {
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected AnnotationResult doInBackground(Void... voids) {
            try {
                return new AnnotationController().deleteAnnotation(id);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(AnnotationResult routerResult) {
            dialog.dismiss();
            if (iThreadResult != null)
                iThreadResult.onResult(routerResult);
                Toast.makeText(context,  "Excluido com sucesso! ", Toast.LENGTH_SHORT).show();

        }

        public void setOnResult(IThreadResult<AnnotationResult> iThreadResult) {
            this.iThreadResult = iThreadResult;
        }
    }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView date;
            CardView card_view;

            public ViewHolder(View view) {
                super(view);
                card_view = (CardView) itemView.findViewById(R.id.card_view);
                title = (TextView) itemView.findViewById(R.id.tvNameRecycleView);
                date = (TextView) itemView.findViewById(R.id.tvDate);
            }
        }

        private void dialog(final int position){
            final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(R.layout.dialog_edit);
            final Button confirmar = (Button) dialog.findViewById(R.id.btn_update);
            final Button cancelar = (Button) dialog.findViewById(R.id.btn_cancel);
            final EditText edtTitulo = (EditText) dialog.findViewById(R.id.text_titulo);
            final EditText edtMessage = (EditText) dialog.findViewById(R.id.text_annotation);

            edtTitulo.setText(annotations.get(position).getTitulo());
            edtMessage.setText(annotations.get(position).getMessage());

            confirmar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String titulo = edtTitulo.getText().toString();
                    String message = edtMessage.getText().toString();
                    Annotation annotation = new Annotation();
                    annotation.setTitulo(titulo);
                    annotation.setMessage(message);

                    UpdateAnnotationThread getRoutersThread = new UpdateAnnotationThread(annotations.get(position).getId(), titulo, message, annotation);
                    getRoutersThread.setOnResult(new IThreadResult<AnnotationResult>() {
                        @Override
                        public void onResult(AnnotationResult routerResult) {
                            if(routerResult != null){
                                annotations = routerResult.getAnnotations();
                                notifyDataSetChanged();
                                Log.w(TAG, "dialog: " + annotations.toString());
                            }
                        }
                    });
                    getRoutersThread.execute();
                    dialog.dismiss();
                }
            });

            cancelar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toast.makeText(context,  "Cancelado com sucesso! ", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
