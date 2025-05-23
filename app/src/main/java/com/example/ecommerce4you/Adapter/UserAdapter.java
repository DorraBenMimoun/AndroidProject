package com.example.ecommerce4you.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerce4you.R;
import com.example.ecommerce4you.databinding.ItemUserBinding;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<QueryDocumentSnapshot> users;

    public UserAdapter(ArrayList<QueryDocumentSnapshot> users) {
        this.users = users;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ItemUserBinding binding;

        public UserViewHolder(@NonNull ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        QueryDocumentSnapshot doc = users.get(position);
        String username = doc.getString("name");
        String email = doc.getString("email");
        String role = doc.getString("role");

        // Affichage texte
        holder.binding.username.setText(username != null ? username : "—");
        holder.binding.email.setText(email != null ? email : "—");
        holder.binding.role.setText(role != null ? role : "—");

        // Génération de la photo
        String safeName = (username != null && !username.isEmpty()) ? username : "User";
        String capitalized = safeName.substring(0, 1).toUpperCase() + safeName.substring(1).toLowerCase();
        String avatarUrl = "https://ui-avatars.com/api/?name=" + capitalized + "&rounded=true&size=128";

        // Chargement avec Glide
        Glide.with(holder.itemView.getContext())
                .load(avatarUrl)
                .placeholder(R.drawable.profile_default) // image par défaut
                .into(holder.binding.profileImage);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
