package com.example.oollan.androidexam.hero;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oollan.androidexam.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.oollan.androidexam.utils.AnimationUtils.*;
import static com.example.oollan.androidexam.utils.Base64Utils.encodeTobase64;
import static com.example.oollan.androidexam.utils.Constants.*;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.NewsViewHolder> {

    private ImageView lastFavHero;
    private List<Hero> heroList;
    private HeroInterface heroInterface;
    private SharedPreferences preferences;

    public void setHeroAdapter(List<Hero> heroList) {
        this.heroList = heroList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        preferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        if (!preferences.contains(SIZE_KEY)) {
            preferences.edit().putInt(SIZE_KEY, heroList.size()).apply();
        }
        return new NewsViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.heroName.setText(heroList.get(position).getTitle());
        holder.heroAbilities.setText(heroList.get(position).getAbilities());
        holder.heroImage.setImageBitmap(heroList.get(position).getImage());
        if (preferences.contains(FAV_KEY) && position == preferences.getInt(FAV_KEY, 0)) {
            lastFavHero = holder.favHero;
            lastFavHero.setVisibility(View.VISIBLE);
        }
        if (!preferences.contains(NAME_KEY + position)) {
            preferences.edit()
                    .putString(NAME_KEY + position, heroList.get(position).getTitle())
                    .putString(ABILITIES_KEY + position, heroList.get(position).getAbilities())
                    .putString(IMAGE_KEY + position, encodeTobase64
                            (heroList.get(position).getImage())).apply();
        }
    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }

    public void clearData() {
        heroList.clear();
        notifyDataSetChanged();
    }

    public void setHeroInterface(HeroInterface heroInterface) {
        this.heroInterface = heroInterface;
    }


    class NewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.fav_hero)
        ImageView favHero;
        @BindView(R.id.hero_name)
        TextView heroName;
        @BindView(R.id.hero_abilities)
        TextView heroAbilities;
        @BindView(R.id.hero_image)
        CircleImageView heroImage;

        NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick
        void onClick() {
            if (preferences.contains(FAV_KEY)) {
                fadeOutFav(lastFavHero);
            }
            favHero.setVisibility(View.VISIBLE);
            fadeInFav(favHero);
            lastFavHero = favHero;
            heroInterface.setHeader(heroList.get(getAdapterPosition()).getImage(),
                    heroList.get(getAdapterPosition()).getTitle());
            preferences.edit().putInt(FAV_KEY, getAdapterPosition())
                    .putString(NAME_KEY, heroList.get(getAdapterPosition()).getTitle())
                    .putString(IMAGE_KEY, encodeTobase64(heroList.get
                            (getAdapterPosition()).getImage())).apply();
        }
    }
}