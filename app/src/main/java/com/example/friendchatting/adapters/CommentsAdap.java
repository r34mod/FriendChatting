package com.example.friendchatting.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendchatting.Modelo.ReviewModel;
import com.example.friendchatting.R;

import java.util.List;

public class CommentsAdap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    public List<ReviewModel> models;
    CommentListener viewModel;

    public CommentsAdap(Context context, CommentListener viewModel) {
        this.context = context;
        this.viewModel = viewModel;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.edit_text, parent, false);
            return new EditTextHolder(view);
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.comment_item, parent, false);
            return new CommentHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentHolder) {
            CommentHolder commentHolder = (CommentHolder) holder;
            ReviewModel restaurantModel = models.get(position-1);
            commentHolder.textView.setText(restaurantModel.comment);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        if (models != null) {
            return models.size() + 1;
        }
        return 1;
    }

    public void setComments(List<ReviewModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    public class EditTextHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        Button button;

        public EditTextHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.EditText);
            button = itemView.findViewById(R.id.Button);
            button.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            viewModel.onComment(textView.getText().toString());
            textView.setText("");
        }
    }

    public class CommentHolder extends RecyclerView.ViewHolder  {

        TextView textView;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.Comment);

        }


    }


    public interface CommentListener {
        void onComment(String comment);
    }
}
