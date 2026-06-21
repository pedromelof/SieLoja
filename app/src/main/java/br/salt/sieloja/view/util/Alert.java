package br.salt.sieloja.view.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.view.View;

import br.salt.sieloja.R;

public class Alert {

    public static final int IMAGEM = R.mipmap.ic_logo;
    public static final int NOME = R.string.app_name;

    public static void dialog(Context context, String mensagem){
        dialog(context, mensagem, null);
    }

    public static void dialog(Context context, String mensagem, OnClickListener onClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(IMAGEM);
        builder.setTitle(context.getResources().getString(NOME));
        builder.setMessage(mensagem);
        builder.setNeutralButton(R.string.confirmar, onClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void dialogValidation(Context context, String mensagem, OnClickListener onClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(IMAGEM);
        builder.setTitle(context.getResources().getString(NOME));
        builder.setMessage(mensagem);
        builder.setNegativeButton(R.string.cancelar, null);
        builder.setPositiveButton(R.string.confirmar, onClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void dialogPersonalizado(Context context, OnClickListener onClickListener, View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setIcon(IMAGEM);
        builder.setTitle(context.getResources().getString(NOME));
        builder.setNegativeButton(R.string.cancelar, null);
        builder.setPositiveButton(R.string.confirmar, onClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void dialogListItems(Context context, OnClickListener onClickListener, String[] idiomas){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(IMAGEM);
        builder.setTitle(context.getResources().getString(NOME));
        builder.setItems(idiomas, onClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void dialogMultiChoiceItems(Context context, OnClickListener onClickListener, OnMultiChoiceClickListener listener, String[] grupos){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(IMAGEM);
        builder.setTitle(context.getResources().getString(NOME));
        builder.setMultiChoiceItems(grupos, null, listener);
        builder.setNegativeButton(R.string.cancelar, null);
        builder.setPositiveButton(R.string.confirmar, onClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void startProgressDialog(Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIcon(IMAGEM);
        progressDialog.setTitle(context.getString(NOME));
        progressDialog.setMessage(context.getString(R.string.aguarde));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}

