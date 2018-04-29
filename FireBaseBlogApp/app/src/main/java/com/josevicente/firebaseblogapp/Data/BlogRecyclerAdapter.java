package com.josevicente.firebaseblogapp.Data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.josevicente.firebaseblogapp.Model.Blog;
import com.josevicente.firebaseblogapp.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


//5.-Creamos el adaptador del recyclerview
public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>{
    private Context context;
    private List<Blog> blogList;

    public BlogRecyclerAdapter(Context context, List<Blog> blogList){
        this.context=context;
        this.blogList=blogList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Instanciamos las Varialbes en onBindViewHolder
        Blog blog = blogList.get(position);
        String imageUrl = null;

        holder.title.setText(blog.getTitle());
        holder.desc.setText(blog.getDesc());

        //Formateamos el tiempo
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());
        holder.timestamp.setText(formattedDate);

        imageUrl = blog.getImage();

        //Añadimos librería Picasso para cargar las imágenes en el recyclerView
        //File>>Project Structure>>app>>Dependencies>>+>>library dependencie>>Picasso
        Picasso.with(context).load(imageUrl).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Variable declaradas que referencian a row.xml
        public TextView title;
        public TextView desc;
        public TextView timestamp;
        public ImageView image;
        String userid;

        public ViewHolder(View view,Context ctx) {
            super(view);

            context = ctx;
            //Instanciamos las variables
            title = view.findViewById(R.id.postTitleList);
            desc = view.findViewById(R.id.postTextList);
            image = view.findViewById(R.id.postImageList);
            timestamp = view.findViewById(R.id.dateText);

            userid = null;

            //Listener del Adapter
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }



}
