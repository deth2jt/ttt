package com.google.example.games.ttt;

import org.json.JSONArray;
import org.json.JSONObject;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class BoardView extends View {
  private int _color = 1;
  public static final String TAG = "BVIEWED";
  boolean isTouched = false;
  BaseGameActivity act;
  
  public int[][] positions = new int[][] { 
	      { 0, 0, 0 },
	      { 0, 0, 0 },
	      { 0, 0, 0 }
	      
	  };
  
  /*
  private void updatePositions( Document doc ) {
	    for( int x = 0; x < 3; x++ ) {
	      for( int y = 0; y < 3; y++ ) {
	        positions[x][y] = 0;
	      }
	    }
	    
	    for (int i=0;i<items.getLength();i++){
	    	 / positions[x][y] = color;
	    }
  }
  */
  
  
  public void setColor( int c ) {
    _color = c;
    
  }
  
  /*
  public BoardView(Context context) {
      super(context);
      //GameService.getInstance().startGame(0);
  }
  */
  
  public BoardView(Context context, int[][]  positions) {
      super(context);
      this.positions =positions;
      //GameService.getInstance().startGame(0);
  }
  
  public BoardView(Context context, AttributeSet attrs) {
      super(context,attrs);
      //GameService.getInstance().startGame(0);
  }
  
  public BoardView(Context context, AttributeSet attrs, int defStyle) {
      super(context,attrs,defStyle);
      //GameService.getInstance().startGame(0);
  }

  public void setPosition(int uhh, int xval, int yval, int colour)
  {
	  positions[xval][yval] = colour;
  }
  
  
  public void setPos(int[][] array)
  {
	  ///
	  setWillNotDraw(false);
	  for( int x = 0; x < 3; x++ ) {
		  //JSONObject item = array.;
	      for( int y = 0; y < 3; y++ )
	    	  positions[x][y] = array[x][y];
	  }
	  invalidate();
  }
  
  
  public int[][] getPos()
  {
	  return positions;
  }
  
  
  public boolean onTouchEvent( MotionEvent event ) {
    if ( event.getAction() != MotionEvent.ACTION_UP )
      return true;
    int offsetX = getOffsetX();
    int offsetY = getOffsetY();
    int lineSize = getLineSize();
    for( int x = 0; x < 3; x++ ) {
      for( int y = 0; y < 3; y++ ) {
        Rect r = new Rect( ( offsetX + ( x * lineSize ) ),
            ( offsetY + ( y * lineSize ) ),
            ( ( offsetX + ( x * lineSize ) ) + lineSize ),
            ( ( offsetY + ( y * lineSize ) ) + lineSize ) );
        if ( r.contains( (int)event.getX(), (int)event.getY() ) ) {
          if(positions[ x ][ y ] == 0 && ((SkeletonActivity) act).isDoingTurn)
          {
        	  
        	  setPosition(0, x, y, _color);
        	  Log.w(TAG, "x: "+  x + " y: " + y);
        	  isTouched = true;
        	  
        	  if(checkWin(positions, 1))
              {
              	Log.w(TAG, "winner: OOOOO");
              	//mTurnTextView.setText(" won");
              	Toast.makeText(getContext(), "OOOOO", Toast.LENGTH_SHORT).show(); 
                ((SkeletonActivity) act).onFinishClicked( this);
              }
              else if(checkWin(positions, 2))
              {
              	Log.w(TAG, "winner: XXXXXX");
              	

              	Toast.makeText(getContext(), "XXXXXX", Toast.LENGTH_SHORT).show();  
                ((SkeletonActivity) act).onFinishClicked( this);
              }
              else
            	  ((SkeletonActivity) act).onDoneClicked( this);
          }
          invalidate();
          
          
          ///Log.w(TAG, "GameService.getInstance(): " + GameService.getInstance());
          
          
          
          
          
          return true;
        }
      }
    }
   
    return true;
  }

  
  private boolean checkWin(int[][] board, int play)
  {
	//boolean returnVal = false;
	//boolean diagLUD = board[0][0] == play && board[1][1] == play && board[2][2] == play;
	//boolean diagRUD = board[0][2] == play && board[1][1] == play && board[2][0] == play;
	//boolean diag = board[0][2] == play && board[1][1] == play && board[2][0] == play;
	//return (diagLUD || diagRUD);
	
	boolean v = true;
	for(int x = 0; x < board.length; x++)
	{
		v = v && (play == board[x][x]);
		
	}
	if(v)
		return v;
	else
		v = true;
	
	
	for(int x = 0; x < board.length; x++)
	{
		v = v && (play == board[x][(board.length - 1)  - x]);
		
	}
	if(v)
		return v;
	else
		v = true;
	
	
	
	for(int x = 0; x < board.length; x++)
	{
		v = v && (play == board[x][0]);
		
	}
	if(v)
		return v;
	else
		v = true;
	
	
	for(int x = 0; x < board.length; x++)
	{
		v = v && (play == board[x][1]);
		Log.d(TAG, " board[x][1]:" + board[x][1]);
		Log.d(TAG, "(play :" + play );
		Log.d(TAG, "(play == board[x][1]:" + (play == board[x][1]));
		
	}
	
	if(v)
		return v;
	else
		v = true;
	
	
	for(int x = 0; x < board.length; x++)
	{
		v = v && (play == board[x][2]);
		
	}
	if(v)
		return v;
	else
		v = true;
	
	for(int x = 0; x < board.length; x++)
	{
		v = v && (play == board[x][1]);
		
	}
	if(v)
		return v;
	else
		v = true;
	
	
	for(int x = 0; x < board.length; x++)
	{
		v = v && (play == board[x][0]);
		
	}
	if(v)
		return v;
	else
		v = true;
	
	for(int x = 0; x < board.length; x++)
	{
		v = v && (play == board[0][x]);
		
	}
	if(v)
		return v;
	else
		v = true;
	
	
	for(int x = 0; x < board.length; x++)
	{
		v = v && (play == board[1][x]);
		
	}
	if(v)
		return v;
	else
		v = true;
	
	
	for(int x = 0; x < board.length; x++)
	{
		v = v && (play == board[2][x]);
		
	}
	if(v)
		return v;
	else
		v = true;
	
	
	
	//return returnVal;
	return false;
  }
  
  
  
  private int getSize() {
    return (int) ( (float) 
    ( ( getWidth() < getHeight() ) ? getWidth() : getHeight() ) * 0.8 );
  }

  private int getOffsetX() {
    return ( getWidth() / 2 ) - ( getSize( ) / 2 );
  }
  
  private int getOffsetY() {
    return ( getHeight() / 2 ) - ( getSize() / 2 );
  }
  
  private int getLineSize() {
    return ( getSize() / 3 );
  }

  protected void onDraw(Canvas canvas) {
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setColor(Color.BLACK);
    canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(), paint);
    
    int size = getSize();
    int offsetX = getOffsetX();
    int offsetY = getOffsetY();
    int lineSize = getLineSize();
    
    paint.setColor(Color.DKGRAY);
    paint.setStrokeWidth( 5 );
    for( int col = 0; col < 2; col++ ) {
      int cx = offsetX + ( ( col + 1 ) * lineSize );
      canvas.drawLine(cx, offsetY, cx, offsetY + size, paint);
    }
    for( int row = 0; row < 2; row++ ) {
      int cy = offsetY + ( ( row + 1 ) * lineSize );
      canvas.drawLine(offsetX, cy, offsetX + size, cy, paint);
    }
    int inset = (int) ( (float)lineSize * 0.1 );
    
    paint.setColor(Color.WHITE);
    paint.setStyle(Paint.Style.STROKE); 
    paint.setStrokeWidth( 10 );
    for( int x = 0; x < 3; x++ ) {
      for( int y = 0; y < 3; y++ ) {
        Rect r = new Rect( ( offsetX + ( x * lineSize ) ) + inset,
            ( offsetY + ( y * lineSize ) ) + inset,
            ( ( offsetX + ( x * lineSize ) ) + lineSize ) - inset,
            ( ( offsetY + ( y * lineSize ) ) + lineSize ) - inset );
        
        if ( positions[ x ][ y ] == 1 ) {
          canvas.drawCircle( ( r.right + r.left ) / 2, 
                  ( r.bottom + r.top ) / 2, 
                  ( r.right - r.left ) / 2, paint);
        }
        if ( positions[ x ][ y ] == 2 ) {
          canvas.drawLine( r.left, r.top, r.right, r.bottom, paint);
          canvas.drawLine( r.left, r.bottom, r.right, r.top, paint);
        }
        
      }
    }
  }





}