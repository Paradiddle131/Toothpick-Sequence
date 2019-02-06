import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class toothPicks extends PApplet {

int len = 69;
int minX;
int maxX;
int count = 0;

ArrayList<Toothpick> picks;

public void setup(){
  
//  fullScreen(P2D);
  minX = -width/2;
  maxX = width/2;
  picks = new ArrayList<Toothpick>();
  picks.add(new Toothpick(0,0,1));
//  noLoop();

//  println(count);
}

public void mousePressed(){ 
  redraw(); // so that we can click every frame
}

public void draw(){
  background(255);
  translate(width/2, height/2); // changes the origin to the center
  float factor = PApplet.parseFloat(width) / (maxX-minX);
  scale(factor);
  for(Toothpick t: picks){
    t.show(factor);
    minX = min(t.ax, minX);
    maxX = max(t.ax, maxX);
    
  }
  
  ArrayList<Toothpick> next = new ArrayList<Toothpick>();
    for(Toothpick t: picks){
      if(t.newPick){
    Toothpick nextA = t.createA(picks);
    Toothpick nextB = t.createB(picks);
    if(nextA != null){
    next.add(nextA);
    }
    if(nextB != null){
    next.add(nextB);
    }
    t.newPick = false;
    count++;
 //   println("count: " + count);
    println(picks.size());
  }  
    frameRate(3+picks.size()*0.041f);
    println("frameRate: " + frameRate);
}
  picks.addAll(next);

}
class Toothpick{
  int ax, ay, bx, by;
  int dir; // orientation / direction
  boolean newPick = true;
  
  Toothpick(int x, int y, int d) {
    dir = d;
    if(dir == 1){ // horizontal
      ax = x - len/2;
      bx = x + len/2;
      ay = y;
      by = y;      
    } else {      // vertical
      ax =x;
      bx = x;
      ay= y - len/2;
      by= y + len/2;
       
    }
  }
  
  public boolean intersects(int x, int y){
    if(ax == x && ay == y) {
      return true;
    } else if(bx == x && by == y) {
      return true;
    } else {
      return false;
    }
  }
  
  public Toothpick createA(ArrayList<Toothpick> others) {
    boolean available = true;
    for(Toothpick other: others){
      if(other != this && other.intersects(ax,ay)) {
      available = false;
      }
    }
    
    if(available) {
      return new Toothpick(ax,ay,dir*-1);
    } else {
      return null;
    }
  }
  public Toothpick createB(ArrayList<Toothpick> others) {
    boolean available = true;
    for(Toothpick other: others){
      if(other != this && other.intersects(bx,by)) {
      available = false;
      }
    }
    
    if(available) {
      return new Toothpick(bx,by,dir*-1);
    } else {
      return null;
    }
  }
  
  public void show(float factor){
    stroke(10/factor);
    if(newPick){
      stroke(135,224,237);
    }
    strokeWeight(3);  // thick enough so that we can see it
    line(ax,ay,bx,by);
//    newPick = false;
  }
  
}
  public void settings() {  size(768,768); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "toothPicks" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
