/*
 * Copyright (C) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.example.games.ttt;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.example.games.ttt.BoardView;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.android.gms.games.multiplayer.Multiplayer;
//import com.google.example.games.basegameutils.BaseGameActivity;
//import com.google.example.games.tbmpskel.R;
//import basegameutils.BaseGameActivity;
import com.google.example.games.ttt.R;



/**
 * keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
 * AndroidManifest.xml: package name
 * TBMPSkeleton: A minimalistic "game" that shows turn-based
 * multiplayer features for Play Games Services.  In this game, you
 * can invite a variable number of players and take turns editing a
 * shared state, which consists of single string.  You can also select
 * automatch players; all known players play before automatch slots
 * are filled.
 *
 * INSTRUCTIONS: To run this sample, please set up
 * a project in the Developer Console. Then, place your app ID on
 * res/values/ids.xml. Also, change the package name to the package name you
 * used to create the client ID in Developer Console. Make sure you sign the
 * APK with the certificate whose fingerprint you entered in Developer Console
 * when creating your Client Id.
 *
 * @author Wolff (wolff@google.com), 2013
 */
public class SkeletonActivity extends BaseGameActivity implements OnInvitationReceivedListener,
            OnTurnBasedMatchUpdateReceivedListener {
    public static final String TAG = "DrawingActivity";

    public BoardView boardView;
    // Local convenience pointers
    public TextView mDataView;
    public TextView mTurnTextView;

    private AlertDialog mAlertDialog;

    // For our intents
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_LOOK_AT_MATCHES = 10001;

    // How long to show toasts.
    final static int TOAST_DELAY = 2000;

    // Should I be showing the turn API?
    public boolean isDoingTurn = false;
    public boolean isFinish = false;

    // This is the current match we're in; null if not loaded
    public TurnBasedMatch mMatch;

    // This is the current match data after being unpersisted.
    // Do not retain references to match data once you have
    // taken an action on the match, such as takeTurn()
    public SkeletonTurn mTurnData;
    boolean isPlaying = false;
    GameHelper gameHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	//mHelper.getApiClient().registerInvitationListener(this);
    	//gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES | GameHelper.CLIENT_PLUS);
    	  //gameHelper.setup(this, GameHelper.CLIENT_GAMES | GameHelper.CLIENT_PLUS);
    	  //gameHelper.getApiClient().registerConnectionCallbacks(this);
    	//Games.Invitations.registerInvitationListener(getApiClient(), this);
    	  
    	Log.i(TAG, "XXXBBB = ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "XXXAAA = ");
        // Setup signin button
        findViewById(R.id.sign_out_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signOut();
                        setViewVisibility();
                    }
                });
        Log.d(TAG, "XXXVVV = ");
        findViewById(R.id.sign_in_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // start the asynchronous sign in flow
                    	Log.d(TAG, "XXXVVV-1 = ");
                        beginUserInitiatedSignIn();
                        Log.d(TAG, "XXXVVV1 = ");
                        //findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                        Log.d(TAG, "XXXVVV2 = ");
                    }
                });
        Log.d(TAG, "XXXVVV3 = ");
        //mDataView = ((TextView) findViewById(R.id.data_view));
        Log.d(TAG, "XXXVVV4 = ");
        
        Log.d(TAG, "XXXVVV5 = ");
    }
    
    /*
    public void onClick(View v) {
    	
        boardView = (BoardView)this.findViewById(R.id.bview);
        if ( v.getId() == R.id.playx ) {
        	boardView.setColor( 2 );
        }
        if ( v.getId() == R.id.playo ) {
        	boardView.setColor( 1 );
        }
        
      }
    */
    
    
    //Once a player has signed in to your game, the player may receive invitations to join a turn-based match created by another player. 
    //To save coding time and provide users with a consistent UI for responding to match invitations across applications, 
    //you can use the default match inbox UI provided by the SDK. 
    //To launch the default match inbox UI, call getInboxIntent() to get an Intent; then call startActivityForResult() and pass in that Intent.
    // Displays your inbox. You will get back onActivityResult where
    // you will need to figure out what you clicked on.
    public void onCheckGamesClicked(View view) {
    	Log.d(TAG, "onCheckGamesClicked");
        Intent intent = Games.TurnBasedMultiplayer.getInboxIntent(getApiClient());
        startActivityForResult(intent, RC_LOOK_AT_MATCHES);
    }

    
    //Optional. Returns an Intent for launching the default player selection UI. 
    //From this UI, the user can select other Google+ users to invite to the user's match, 
    //or request to be auto-matched with random Google+ users.
    //setup invatin screen
    // Open the create-game UI. You will get back an onActivityResult
    // and figure out what to do.
    public void onStartMatchClicked(View view) {
    	
        Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(getApiClient(),
                1, 7, true);
        startActivityForResult(intent, RC_SELECT_PLAYERS);
        Log.w(TAG, "onStartMatchClicked");
    }

    // Create a one-on-one automatch game.
    public void onQuickMatchClicked(View view) {
    	Log.d(TAG, "onQuickMatchClicked");

        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                1, 1, 0);

        TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
                .setAutoMatchCriteria(autoMatchCriteria).build();

        showSpinner();

        // Start the match
        ResultCallback<TurnBasedMultiplayer.InitiateMatchResult> cb = new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
            @Override
            public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
                processResult(result);
            }
        };
        Games.TurnBasedMultiplayer.createMatch(getApiClient(), tbmc).setResultCallback(cb);
    }

    // In-game controls

    // Cancel the game. Should possibly wait until the game is canceled before
    // giving up on the view.
    public void onCancelClicked(View view) {
    	Log.d(TAG, "onCancelClicked");
        showSpinner();
        Games.TurnBasedMultiplayer.cancelMatch(getApiClient(), mMatch.getMatchId())
                .setResultCallback(new ResultCallback<TurnBasedMultiplayer.CancelMatchResult>() {
                    @Override
                    public void onResult(TurnBasedMultiplayer.CancelMatchResult result) {
                        processResult(result);
                    }
                });
        isDoingTurn = false;
        setViewVisibility();
    }

    // Leave the game during your turn. Note that there is a separate
    // Games.TurnBasedMultiplayer.leaveMatch() if you want to leave NOT on your turn.
    public void onLeaveClicked(View view) {
    	Log.d(TAG, "onLeaveClicked");
        showSpinner();
        String nextParticipantId = getNextParticipantId();

        Games.TurnBasedMultiplayer.leaveMatchDuringTurn(getApiClient(), mMatch.getMatchId(),
                nextParticipantId).setResultCallback(
                    new ResultCallback<TurnBasedMultiplayer.LeaveMatchResult>() {
            @Override
            public void onResult(TurnBasedMultiplayer.LeaveMatchResult result) {
                processResult(result);
            }
        });
        setViewVisibility();
    }

    // Finish the game. Sometimes, this is your only choice.
    public void onFinishClicked(View view) {
    	Log.d(TAG, "onFinishClicked");
        showSpinner();
        Games.TurnBasedMultiplayer.finishMatch(getApiClient(), mMatch.getMatchId())
                .setResultCallback(new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
                    @Override
                    public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
                        processResult(result);
                    }
                });

        isDoingTurn = false;
        setViewVisibility();
    }


    public JSONArray createJSONObjArray(int[][] array)
    {
    	JSONArray retVal = new JSONArray();
    			
    			//try
    			//{
	    			for( int x = 0; x < array.length; x++ )
	    			{
	    				JSONArray list = new JSONArray();
	
	    				for( int z = 0; z < array[x].length; z++ )
	    				{		
	    		      				//JSONObject jsonObj = new JSONObject();
	    							//jsonObj.put("z", array[x][z]);
	    							list.put(array[x][z]);
	
	    		      
	    				}
	    				retVal.put(list);
	    			}
	    		return retVal;
    	  
    }
    
    public int[][] jsonObjectToArray(JSONArray retVal)
    {
        			int[][] array = new int [3][ 3];
        			try
        			{
    	    			for( int x = 0; x < retVal.length(); x++ )
    	    			{
    	    				JSONArray list = (JSONArray) retVal.get(x);
    	
    	    				for( int z = 0; z < list.length(); z++ )
    	    				{		
    	    		      		array[x][z] = list.getInt(z);
    	    		      		
    	    		      
    	    				}
    	    				
    	    			}
        			}
        			
        			catch (JSONException e) {
        	            // TODO Auto-generated catch block
        	            e.printStackTrace();
        	        }
        	  return array;
    }
    
    
    // Upload your new gamestate, then take a turn, and pass it on to the next
    // player.
    public void onDoneClicked(View view) {
    	Log.d(TAG, "onDoneClicked");
        showSpinner();

        String nextParticipantId = getNextParticipantId();
        int[][] array = boardView.getPos();
        // Create the next turn
        isDoingTurn = false;
        mTurnData.array 
        = createJSONObjArray(array);
        //mTurnData.turnCounter += 1;
        //mTurnData.data = mDataView.getText().toString();
        //Log.w(TAG, "mTurnData.data: "+ mTurnData.data);
        Log.w(TAG, "mTurnData.array: " + mTurnData.array);
        //Log.w(TAG, "mTurnData.isChoosingWord: " + mTurnData.isChoosingWord);
        //Log.w(TAG, "mTurnData.data: " + mTurnData.data);
        //Log.w(TAG, "mDataView.getText().toString()): " + mDataView.getText().toString());
        //Log.w(TAG, "mTurnData.data.equals(mDataView.getText().toString()): " + mTurnData.data.equals(mDataView.getText().toString()));
        
        /*
        if(!mTurnData.isChoosingWord)
        {
        	if(mTurnData.data.equals(mDataView.getText().toString()))
        	{
        		Log.w(TAG, "true");
        		mTurnData.pts = mTurnData.pts + 1;
        	}
        }
        else
        {
        	mTurnData.data = mDataView.getText().toString();
        }
        mTurnData.isChoosingWord = !mTurnData.isChoosingWord;
        */		
        		
        showSpinner();
        
        //Log.w(TAG, "mMatch.getMatchId(): "+ mMatch.getMatchId());
        //Log.w(TAG, "mTurnData.persist(): "+ mTurnData.persist());
        //Log.w(TAG, "nextParticipantId: "+ nextParticipantId);
        
        
        
        Games.TurnBasedMultiplayer.takeTurn(getApiClient(), mMatch.getMatchId(),
                mTurnData.persist(), nextParticipantId).setResultCallback(
                new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
            @Override
            public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
                processResult(result);
            }
        });
        
        
     
        
        

        mTurnData = null;
    }

    // Sign-in, Sign out behavior

    // Update the visibility based on what state we're in.
    public void setViewVisibility() {
    	Log.d(TAG, "setViewVisibilityy");
        if (!isSignedIn()) {
        	
            findViewById(R.id.login_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.matchup_layout).setVisibility(View.GONE);
            findViewById(R.id.gameplay_layout).setVisibility(View.GONE);

            if (mAlertDialog != null) {
                mAlertDialog.dismiss();
            }
            
            return;
        }


        ((TextView) findViewById(R.id.name_field)).setText(Games.Players.getCurrentPlayer(
                getApiClient()).getDisplayName() + " is playing ");
        findViewById(R.id.login_layout).setVisibility(View.GONE);

        if (isPlaying || isDoingTurn) {
            findViewById(R.id.matchup_layout).setVisibility(View.GONE);
            findViewById(R.id.gameplay_layout).setVisibility(View.VISIBLE);
        }
        else if(mMatch != null)
        {
        	if(mMatch.getStatus() == TurnBasedMatch.MATCH_STATUS_COMPLETE)
        	{	
	        	findViewById(R.id.finish_layout).setVisibility(View.VISIBLE);
	            findViewById(R.id.gameplay_layout).setVisibility(View.GONE);
        	}
        }
        
        else
        {
            findViewById(R.id.matchup_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.gameplay_layout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onSignInFailed() {
    	Log.d(TAG, "onSignInFailed");
        setViewVisibility();
    }

    @Override
    public void onSignInSucceeded() {
    	Log.d(TAG, "onSignInSucceededdd");
    	Log.d(TAG, "XXXVVV0 = ");
    	Log.d(TAG, "XXX = " + mHelper.getTurnBasedMatch() );
        if (mHelper.getTurnBasedMatch() != null) {
            // GameHelper will cache any connection hint it gets. In this case,
            // it can cache a TurnBasedMatch that it got from choosing a turn-based
            // game notification. If that's the case, you should go straight into
            // the game.
            updateMatch(mHelper.getTurnBasedMatch());
            return;
        }
        Log.d(TAG, "XXXYYYY = " );
        setViewVisibility();

        // As a demonstration, we are registering this activity as a handler for
        // invitation and match events.
        Log.d(TAG, "XXXQQQ = " );
        // This is *NOT* required; if you do not register a handler for
        // invitation events, you will get standard notifications instead.
        // Standard notifications may be preferable behavior in many cases.
        
        
        Games.Invitations.registerInvitationListener(getApiClient(), this);
        
        
        Log.d(TAG, "XXXZZZ = ");
        // Likewise, we are registering the optional MatchUpdateListener, which
        // will replace notifications you would get otherwise. You do *NOT* have
        // to register a MatchUpdateListener.
        Games.TurnBasedMultiplayer.registerMatchUpdateListener(getApiClient(), this);
    }

    
    /*
    // create click listener
    OnClickListener doneAction = new OnClickListener() {
    	
    	
      @Override
      
      public void onClick(View v) {
        // change text of the TextView (tvOut)
        if(boardView.isTouched)
        {
        	onDoneClicked(v);
        	Log.d(TAG, "doneAction2");
        }
        
      }
       
    };
    */
    
    
    
    // Switch to gameplay view.
    public void setGameplayUI() {
    	Log.d(TAG, "setGameplayUI");
        isDoingTurn = true;
        isPlaying = true;
        setViewVisibility();
        boardView = (BoardView)this.findViewById(R.id.bview);
        boardView.setWillNotDraw(false);
        boardView.setPos(jsonObjectToArray(mTurnData.array));
        //boardView.invalidate();
        //boardView.performClick();
        //boardView.setOnClickListener(doneAction);
        boardView.act = this;
        
        
        ArrayList<String> participantIds = mMatch.getParticipantIds();
        String playerId = Games.Players.getCurrentPlayerId(getApiClient());
        String myParticipantId = mMatch.getParticipantId(playerId);
        
        Log.d(TAG, "(myParticipantId):" + myParticipantId);
        Log.d(TAG, "(participantIds):" + participantIds);
        Log.d(TAG, "(playerId):" + playerId);
        Log.d(TAG, "participantIds.indexOf(playerId):" + participantIds.indexOf(myParticipantId));
        Log.d(TAG, "Games.Players.getCurrentPlayer( getApiClient(): " + Games.Players.getCurrentPlayer( getApiClient()));
        	
        if ( 	participantIds.indexOf(myParticipantId)   == 0) {
        	Log.d(TAG, "PLAYER1");
        	boardView.setColor(2);
        }
        if ( participantIds.indexOf(myParticipantId)   ==  1 ) {
        	Log.d(TAG, "PLAYER2");
        	boardView.setColor(1);
        }

        
        
        //boardView = (BoardView)this.findViewById(R.id.bview);
        
        //mDataView.setText("");
        
        //mTurnTextView.setText("Turn " + mTurnData.turnCounter + "\nPoints "+ mTurnData.pts);
    }

    
    // Helpful dialogs

    public void showSpinner() {
    	Log.d(TAG, "showSpinner");
        findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
    }

    public void dismissSpinner() {
    	Log.d(TAG, "dismissSpinnerr");
        findViewById(R.id.progressLayout).setVisibility(View.GONE);
    }

    // Generic warning/info dialog
    public void showWarning(String title, String message) {
    	Log.d(TAG, "showWarning");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(title).setMessage(message);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                    }
                });

        // create alert dialog
        mAlertDialog = alertDialogBuilder.create();

        // show it
        mAlertDialog.show();
    }

    // Rematch dialog
    public void askForRematch() {
    	Log.d(TAG, "askForRematc"
    			+ "");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Do you want a rematch?");

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Sure, rematch!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                rematch();
                            }
                        })
                .setNegativeButton("No.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

        alertDialogBuilder.show();
    }

    // This function is what gets called when you return from either the Play
    // Games built-in inbox, or else the create game built-in interface.
    @Override
    public void onActivityResult(int request, int response, Intent data) {
    	Log.d(TAG, "onActivityResult");
        // It's VERY IMPORTANT for you to remember to call your superclass.
        // BaseGameActivity will not work otherwise.
        super.onActivityResult(request, response, data);
        Log.d(TAG, "onActivityResult1");
        if (request == RC_LOOK_AT_MATCHES) {
        	Log.d(TAG, "request == RC_LOOK_AT_MATCHES");
            // Returning from the 'Select Match' dialog

            if (response != Activity.RESULT_OK) {
                // user canceled
            	Log.d(TAG, "response != Activity.RESULT_OK");
                return;
            }

            TurnBasedMatch match = data
                    .getParcelableExtra(Multiplayer.EXTRA_TURN_BASED_MATCH);

            if (match != null) {
            	Log.d(TAG, "match != null");
                updateMatch(match);
            }

            Log.d(TAG, "Match = " + match);
        } 
        
        
        
        
        
        else if (request == RC_SELECT_PLAYERS) {
        	Log.d(TAG, "request == RC_SELECT_PLAYERS");
            // Returned from 'Select players to Invite' dialog

            if (response != Activity.RESULT_OK) {
                // user canceled
            	Log.d(TAG, "response != Activity.RESULT_OK");
                return;
            }

            // get the invitee list
            final ArrayList<String> invitees = data
                    .getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

            // get automatch criteria
            Bundle autoMatchCriteria = null;

            int minAutoMatchPlayers = data.getIntExtra(
                    Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
            int maxAutoMatchPlayers = data.getIntExtra(
                    Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

            if (minAutoMatchPlayers > 0) {
            	Log.d(TAG, "rminAutoMatchPlayers > 0 " + minAutoMatchPlayers + " " + maxAutoMatchPlayers);
                autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                        minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            } else {
            	Log.d(TAG, "elseeeeeeeeeee");
                autoMatchCriteria = null;
            }
            Log.d(TAG, "onActivityResult2");
            TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
                    .addInvitedPlayers(invitees)
                    .setAutoMatchCriteria(autoMatchCriteria).build();
            Log.d(TAG, "onActivityResult3");
            // Start the match
            Games.TurnBasedMultiplayer.createMatch(getApiClient(), tbmc).setResultCallback(
                    new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
                @Override
                public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
                	Log.d(TAG, "onResult(TurnBasedMultiplayer.InitiateMatchResult result)");
                    processResult(result);
                }
            });
            Log.d(TAG, "onActivityResult6");
            showSpinner();
        }
        
        Log.d(TAG, "onActivityResult76");
        
        
        
    }

    // startMatch() happens in response to the createTurnBasedMatch()
    // above. This is only called on success, so we should have a
    // valid match object. We're taking this opportunity to setup the
    // game, saving our initial state. Calling takeTurn() will
    // callback to 7(), which will show the game
    // UI.
    public void startMatch(TurnBasedMatch match) {
    	Log.d(TAG, "startMatch");
        mTurnData = new SkeletonTurn();
        // Some basic turn data
        mTurnData.winner = "ss";
        //mTurnData.data = "";
        
        int[][] positions = new int[][] { 
      	      { 0, 0, 0 },
      	      { 0, 0, 0 },
      	      { 0, 0, 0 }
      	  };
        mTurnData.array = createJSONObjArray(positions);
        mMatch = match;

        String playerId = Games.Players.getCurrentPlayerId(getApiClient());
        String myParticipantId = mMatch.getParticipantId(playerId);

        showSpinner();
        isDoingTurn = true;
        Games.TurnBasedMultiplayer.takeTurn(getApiClient(), match.getMatchId(),
                mTurnData.persist(), myParticipantId).setResultCallback(
                new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
            @Override
            public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
                processResult(result);
            }
        });
    }

    // If you choose to rematch, then call it and wait for a response.
    public void rematch() {
    	Log.d(TAG, "rematch");
        showSpinner();
        Games.TurnBasedMultiplayer.rematch(getApiClient(), mMatch.getMatchId()).setResultCallback(
                new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
            @Override
            public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
                processResult(result);
            }
        });
        mMatch = null;
        isDoingTurn = false;
    }

    /**
     * Get the next participant. In this function, we assume that we are
     * round-robin, with all known players going before all automatch players.
     * This is not a requirement; players can go in any order. However, you can
     * take turns in any order.
     *
     * @return participantId of next player, or null if automatching
     */
    public String getNextParticipantId() {
    	Log.d(TAG, "getNextParticipantId");

        String playerId = Games.Players.getCurrentPlayerId(getApiClient());
        String myParticipantId = mMatch.getParticipantId(playerId);

        ArrayList<String> participantIds = mMatch.getParticipantIds();

        int desiredIndex = -1;

        for (int i = 0; i < participantIds.size(); i++) {
            if (participantIds.get(i).equals(myParticipantId)) {
                desiredIndex = i + 1;
            }
        }

        if (desiredIndex < participantIds.size()) {
            return participantIds.get(desiredIndex);
        }

        if (mMatch.getAvailableAutoMatchSlots() <= 0) {
            // You've run out of automatch slots, so we start over.
            return participantIds.get(0);
        } else {
            // You have not yet fully automatched, so null will find a new
            // person to play against.
            return null;
        }
    }

    // This is the main function that gets called when players choose a match
    // from the inbox, or else create a match and want to start it.
    public void updateMatch(TurnBasedMatch match) {
    	Log.d(TAG, "updateMatch");
        mMatch = match;
        
       
        
        
        int status = match.getStatus();
        int turnStatus = match.getTurnStatus();
        
        
        Log.d(TAG, "status:" + status);
        Log.d(TAG, "turnStatus:" + turnStatus);
        //match.turnStatus = TurnBasedMatch.MATCH_TURN_STATUS_COMPLETE;
        switch (status) {
            case TurnBasedMatch.MATCH_STATUS_CANCELED:
                showWarning("Canceled!", "This game was canceled!");
                return;
            case TurnBasedMatch.MATCH_STATUS_EXPIRED:
                showWarning("Expired!", "This game is expired.  So sad!");
                return;
            case TurnBasedMatch.MATCH_STATUS_AUTO_MATCHING:
                showWarning("Waiting for auto-match...",
                        "We're still waiting for an automatch partner.");
                return;
            case TurnBasedMatch.MATCH_STATUS_COMPLETE:
                if (turnStatus == TurnBasedMatch.MATCH_TURN_STATUS_COMPLETE) {
                    showWarning(
                            "Complete!",
                            "This game is over; someone finished it, and so did you!  There is nothing to be done.");
                    break;
                }

                // Note that in this state, you must still call "Finish" yourself,
                // so we allow this to continue.
                showWarning("Complete!",
                        "This game is over; someone finished it!  You can only finish it now.");
        }
        
        
	        // OK, it's active. Check on turn status.
	        switch (turnStatus) {
	        
	            case TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN:
	                mTurnData = SkeletonTurn.unpersist(mMatch.getData());
	                setGameplayUI();
	                return;
	            case TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN:
	                // Should return results.
	                showWarning("Alas...", "It's not your turn.");
	                break;
	            case TurnBasedMatch.MATCH_TURN_STATUS_INVITED:
	                showWarning("Good inititative!",
	                        "Still waiting for invitations.\n\nBe patient!");
	        }
        
        
        //else if(!!mTurnData.isFinish)
        	//mTurnData = null;

        setViewVisibility();
    }

    private void processResult(TurnBasedMultiplayer.CancelMatchResult result) {
    	Log.d(TAG, "processResult");
        dismissSpinner();

        if (!checkStatusCode(null, result.getStatus().getStatusCode())) {
            return;
        }

        isDoingTurn = false;

        showWarning("Match",
                "This match is canceled.  All other players will have their game ended.");
    }

    private void processResult(TurnBasedMultiplayer.InitiateMatchResult result) {
    	Log.d(TAG, "processResult2");
        TurnBasedMatch match = result.getMatch();
        dismissSpinner();

        if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
            return;
        }

        if (match.getData() != null) {
            // This is a game that has already started, so I'll just start
            updateMatch(match);
            return;
        }

        startMatch(match);
    }


    private void processResult(TurnBasedMultiplayer.LeaveMatchResult result) {
    	Log.d(TAG, "processResult3");
        TurnBasedMatch match = result.getMatch();
        dismissSpinner();
        if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
            return;
        }
        isDoingTurn = (match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);
        showWarning("Left", "You've left this match.");
    }


    public void processResult(TurnBasedMultiplayer.UpdateMatchResult result) {
    	Log.d(TAG, "processResult13");
        TurnBasedMatch match = result.getMatch();
        dismissSpinner();
        if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
            return;
        }
        if (match.canRematch()) {
            askForRematch();
        }
        
        
        Log.d(TAG, "match.getTurnStatus(): " + match.getTurnStatus());
        Log.d(TAG, "TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN: " + TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);
        
        
        
         if (isDoingTurn || isFinish) {
            updateMatch(match);
            return;
        }

        setViewVisibility();
    }

    // Handle notification events.
    @Override
    public void onInvitationReceived(Invitation invitation) {
    	Log.d(TAG, "onInvitationReceived");
        Toast.makeText(
                this,
                "An invitation has arrived from "
                        + invitation.getInviter().getDisplayName(), TOAST_DELAY)
                .show();
    }

    @Override
    public void onInvitationRemoved(String invitationId) {
    	Log.d(TAG, "onInvitationRemoved");
        Toast.makeText(this, "An invitation was removed.", TOAST_DELAY).show();
    }

    @Override
    public void onTurnBasedMatchReceived(TurnBasedMatch match) {
    	Log.d(TAG, "onTurnBasedMatchReceived");
        //Toast.makeText(this, "A match was updated.", TOAST_DELAY).show();
        ///onCheckGamesClicked(this.findViewById(R.id.secret_layout));
    	
    		updateMatch(match);
    	
    	
    	
    }

    @Override
    public void onTurnBasedMatchRemoved(String matchId) {
    	Log.d(TAG, "onTurnBasedMatchRemoved");
        Toast.makeText(this, "A match was removed.", TOAST_DELAY).show();

    }

    public void showErrorMessage(TurnBasedMatch match, int statusCode,
            int stringId) {
    	Log.d(TAG, "showErrorMessage");
        showWarning("Warning", getResources().getString(stringId));
    }

    // Returns false if something went wrong, probably. This should handle
    // more cases, and probably report more accurate results.
    private boolean checkStatusCode(TurnBasedMatch match, int statusCode) {
    	Log.d(TAG, "checkStatusCode");
        switch (statusCode) {
            case GamesStatusCodes.STATUS_OK:
                return true;
            case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_DEFERRED:
                // This is OK; the action is stored by Google Play Services and will
                // be dealt with later.
                Toast.makeText(
                        this,
                        "Stored action for later.  (Please remove this toast before release.)",
                        TOAST_DELAY).show();
                // NOTE: This toast is for informative reasons only; please remove
                // it from your final application.
                return true;
            case GamesStatusCodes.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER:
                showErrorMessage(match, statusCode,
                        R.string.status_multiplayer_error_not_trusted_tester);
                break;
            case GamesStatusCodes.STATUS_MATCH_ERROR_ALREADY_REMATCHED:
                showErrorMessage(match, statusCode,
                        R.string.match_error_already_rematched);
                break;
            case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_FAILED:
                showErrorMessage(match, statusCode,
                        R.string.network_error_operation_failed);
                break;
            case GamesStatusCodes.STATUS_CLIENT_RECONNECT_REQUIRED:
                showErrorMessage(match, statusCode,
                        R.string.client_reconnect_required);
                break;
            case GamesStatusCodes.STATUS_INTERNAL_ERROR:
                showErrorMessage(match, statusCode, R.string.internal_error);
                break;
            case GamesStatusCodes.STATUS_MATCH_ERROR_INACTIVE_MATCH:
                showErrorMessage(match, statusCode,
                        R.string.match_error_inactive_match);
                break;
            case GamesStatusCodes.STATUS_MATCH_ERROR_LOCALLY_MODIFIED:
                showErrorMessage(match, statusCode,
                        R.string.match_error_locally_modified);
                break;
            default:
                showErrorMessage(match, statusCode, R.string.unexpected_status);
                Log.d(TAG, "Did not have warning or string to deal with: "
                        + statusCode);
        }

        return false;
    }
}
