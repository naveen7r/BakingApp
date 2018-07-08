package com.naveen.backingapp;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.naveen.backingapp.dto.IngredientsItem;
import com.naveen.backingapp.dto.Recipes;
import com.naveen.backingapp.dto.StepsItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ItemDetailFragment extends Fragment implements Player.EventListener {

    public static final String ARG_ITEM_ID = "item_id";
    private static final String TAG = ItemDetailFragment.class.getCanonicalName();

    private Recipes recipes;
    private StepsItem step;
    private boolean isTwoPane;

    private View rootView;

    SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;


    @BindView(R.id.exoPlayer)
    PlayerView exoPlayerView;



    @Nullable
    @BindView(R.id.tvRecipieInstruction)
    TextView tvRecipieInstruction;

    @BindView(R.id.ivImage)
    ImageView ivImage;



    Unbinder unbinder;
    private long playPosition = 0;


    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bn = getArguments();

        if (bn != null) {
            if (bn.containsKey(getString(R.string.ingredi))) {
                recipes = bn.getParcelable(getString(R.string.ingredi));
            }

            if (bn.containsKey(getString(R.string.step))) {
                step = bn.getParcelable(getString(R.string.step));
            }

            if (bn.containsKey(getString(R.string.isTwoPane))) {
                isTwoPane = bn.getBoolean(getString(R.string.isTwoPane));
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (recipes != null) {
            if (tvRecipieInstruction != null)
                tvRecipieInstruction.setText(getRecipies());
        }


        int orientation = getResources().getConfiguration().orientation;
        String video = null;
        String thumbnailUrl = null;
        if (step != null) {
            video = step.getVideoURL();
            if (tvRecipieInstruction != null)
                tvRecipieInstruction.setText(step.getDescription());
            thumbnailUrl = step.getThumbnailURL();
            if(ivImage != null && !TextUtils.isEmpty(thumbnailUrl)){
                Picasso.get().load(thumbnailUrl).placeholder(R.mipmap.ic_place_holder).into(ivImage);
                ivImage.setVisibility(View.VISIBLE);
            } else {
                if(ivImage != null) {
                    ivImage.setVisibility(View.GONE);
                }
            }
        }
        if (!TextUtils.isEmpty(video)) {

            setViewVisibility(exoPlayerView, true);
            initializeMediaSession();
            initializePlayer(Uri.parse(video));


            if (orientation == Configuration.ORIENTATION_LANDSCAPE && !isTwoPane) {

                if (tvRecipieInstruction != null)
                    setViewVisibility(tvRecipieInstruction, false);
            }

        } else {
            setViewVisibility(exoPlayerView, false);
        }

    }

    private String getRecipies() {
        if (recipes != null) {
            if (recipes.getIngredients() != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (IngredientsItem ingredientsItem : recipes.getIngredients()) {
                    stringBuilder.append(ingredientsItem.getIngredient());
                    stringBuilder.append(" ");
                    stringBuilder.append(ingredientsItem.getQuantity());
                    stringBuilder.append(" ");
                    stringBuilder.append(ingredientsItem.getMeasure());
                    stringBuilder.append("\n");
                }
                return stringBuilder.toString();
            }
        }
        return "";
    }


    @Override
    public void onResume() {
        super.onResume();
        String video = null;
        if (step != null) {
            video = step.getVideoURL();
            initializeMediaSession();
            initializePlayer(Uri.parse(video));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(exoPlayer != null){
            playPosition = exoPlayer.getCurrentPosition();
        }
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        step = null;
        recipes = null;

    }

    private void setViewVisibility(View view, boolean show) {
        if (show) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        if (outState != null) {
            outState.putParcelable(getString(R.string.recipe_ingredients), recipes);
            outState.putParcelable(getString(R.string.step), step);
            outState.putBoolean(getString(R.string.isTwoPane), isTwoPane);
            outState.putLong(getString(R.string.play_pos), playPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            recipes = savedInstanceState.getParcelable(getString(R.string.recipe_ingredients));
            step = savedInstanceState.getParcelable(getString(R.string.step));
            isTwoPane = savedInstanceState.getBoolean(getString(R.string.isTwoPane));
            playPosition = savedInstanceState.getLong(getString(R.string.play_pos));
            if(exoPlayer != null) {
                exoPlayer.prepare(mediaSource, false, true);
                exoPlayer.seekTo(playPosition);
                exoPlayer.setPlayWhenReady(true);
            }
        }
    }


    private void initializeMediaSession() {

        mediaSession = new MediaSessionCompat(getContext(), "RecipeStepSinglePageFragment");

        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                exoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                exoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                exoPlayer.seekTo(0);
            }
        });
        mediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "StepVideo");
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), userAgent), bandwidthMeter);
            mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            exoPlayer.prepare(mediaSource, false, true);
            exoPlayer.seekTo(playPosition);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    MediaSource mediaSource;

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, exoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == Player.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, exoPlayer.getCurrentPosition(), 1f);
        }
        if(mediaSession != null) {
            mediaSession.setPlaybackState(stateBuilder.build());
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        String video = null;
        if(step != null){
            video = step.getVideoURL();
        }
        if(video != null && exoPlayer != null && mediaSource != null) {
            exoPlayer.prepare(mediaSource, false, true);
            exoPlayer.seekTo(playPosition);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }


    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


}
