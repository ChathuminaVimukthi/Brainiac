package com.example.chathumina.brainiac;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MainFragment extends Fragment {
    private AlertDialog mDialog;
    private String[] levelNames = {"Novice", "Easy", "Medium", "Guru"};


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container,false);


        // Handle buttons here ...
        View aboutButton = rootView.findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.about_title);
                builder.setMessage(R.string.about_text);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        // nothing
                    }
                });
                mDialog = builder.show();
            }
        });

        View newgameButton = rootView.findViewById(R.id.new_button);
        newgameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder levelAlertbox = new AlertDialog.Builder(getActivity());
                levelAlertbox.setTitle("Choose a level")
                        .setSingleChoiceItems(levelNames, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //start gameplay
                                startPlay(which);
                            }
                        });
                AlertDialog ad = levelAlertbox.create();
                ad.show();
            }
        });
        View exit = rootView.findViewById(R.id.exit_button);
        exit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Save Game");
                builder.setMessage("Do you want to save the game?");
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        SharedPreferences.Editor save = getActivity().getSharedPreferences("GameStats", Context.MODE_PRIVATE).edit();
                        boolean saveGame = true;
                        save.putBoolean("save",saveGame);
                        save.commit();
                        System.exit(0);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences.Editor save = getActivity().getSharedPreferences("GameStats", Context.MODE_PRIVATE).edit();
                        boolean saveGame = false;
                        save.putBoolean("save",saveGame);
                        save.commit();
                        System.exit(0);
                    }
                });
                mDialog = builder.show();
            }
        });
        View settings = rootView.findViewById(R.id.settings_btn);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSettings();
            }
        });
        View continueBtn = rootView.findViewById(R.id.continue_button);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences continueG = getActivity().getSharedPreferences("GameStats", Context.MODE_PRIVATE);
                boolean isPlayed = continueG.getBoolean("gamePlayed",false);
                if (isPlayed){
                    int value = 102;
                    continueGame(value);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.continue_head);
                    builder.setMessage(R.string.continue_txt);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                            // nothing
                        }
                    });
                    mDialog = builder.show();
                }

            }
        });
        return rootView;
    }

    public void onPause() {
        super.onPause();
        // Get rid of the about dialog if it ’s still up
        if (mDialog!=null)
            mDialog.dismiss();
    }


    private void startPlay(int chosenLevel) {
        //start gameplay
        Intent playIntent = new Intent(this.getActivity(), MainGame.class);
        playIntent.putExtra("level", chosenLevel);
        this.startActivity(playIntent);
    }

    private void startSettings(){
        Intent playIntent = new Intent(this.getActivity(), Settings.class);
        this.startActivity(playIntent);
    }

    private void continueGame(int value){
        Intent playIntent = new Intent(this.getActivity(), MainGame.class);
        playIntent.putExtra("continue",value);
        this.startActivity(playIntent);
    }
}
