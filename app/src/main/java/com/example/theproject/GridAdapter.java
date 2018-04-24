package com.example.theproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theproject.GalleryActivity;

import java.io.File;
import java.util.ArrayList;


public class GridAdapter extends BaseAdapter{

    public File startingDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

    ArrayList<String> result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    public GridAdapter(GalleryActivity mainActivity, ArrayList<String> prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result = prgmNameList;
        context = mainActivity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        final View rowView;


        rowView = inflater.inflate(R.layout.gridviewlayout, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

        holder.tv.setText(result.get(position));
        holder.img.setImageResource(imageId[position % imageId.length]);

        rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ((GridView) parent).performItemClick(v, position, 0);

            }
        });
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Pytanie")
                        .setMessage("Czy chcesz usunąć galerię?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String home = startingDir.getPath() + "/PhotoMgr/" + result.get(position) + "/";
                                File file = new File(home);
                                File[] files = file.listFiles();
                                for (File inFile : files) {
                                    inFile.delete();
                                }
                                Toast.makeText(context, "Usunięto: " + result.get(position), Toast.LENGTH_LONG).show();
                                file.delete();

                                // getFileList();
                            }

                        })
                        .setNegativeButton("Nie", null)
                        .show();

                return false;
            }
        });

        return rowView;
    }

}