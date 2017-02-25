import blobDetection.*;
import peasy.*;
PeasyCam cam;

PImage img;

float levels = 20;                    // number of contours
float factor = 1;                     // scale factor
float elevation = 25;                 // total height of the 3d model

float colorStart =  45;               // Starting dregee of color range in HSB Mode (0-360)
float colorRange =  160;             // color range / can also be negative

// Array of BlobDetection Instances
BlobDetection[] theBlobDetection = new BlobDetection[int(levels)];

void setup() {
  size(1000,800,P3D);  
  img = loadImage("cosa2.gif");          // heightmap (about 250x250px)
  cam = new PeasyCam(this,200);
  colorMode(HSB,360,100,100);  
  float thr = 0;
  //Computing Blobs with different thresholds 
  for (int i=0 ; i<levels ; i++) {
    thr = (i+1)/levels;
    theBlobDetection[i] = new BlobDetection(img.width, img.height);
    theBlobDetection[i].setThreshold(thr); 
    theBlobDetection[i].computeBlobs(img.pixels); //<>// //<>//
    
  }
  
  
  // show blobdetection's number of blobs 
  for (int l=0 ; l<levels; l++){
    System.out.println("blob det "+l+": "+theBlobDetection[l].getBlobNb());
  }
  
  // choose (blob detection) and show max, min, centre, edges..
  int nblob = 11;
  System.out.println("Blob detection "+nblob);
  for (int n=0 ; n<theBlobDetection[nblob].getBlobNb() ; n++) {
    System.out.println("Blob "+n+".-----------------------");
    printMaxMinCentre(nblob, n);
    //printAllEdges(nblob, n);
  }
  
  // print values of the image
  printPixelsValue();

  
}

void draw() { 
  background(0);
  translate(-img.width*factor/2,-img.height*factor/2);
  
  // DRAW countours -- drawContours(11);
  // DRAW rect -- drawRect(11);
  // DRAW centre -- drawCentre(11);
  
  for (int i=0 ; i<levels ; i++) {
    translate(0,0,elevation/levels);  
    drawContours(i);
    drawRect(i);
  }
  
}


void drawContours(int i) {
  Blob b;
  EdgeVertex eA,eB;
  for (int n=0 ; n<theBlobDetection[i].getBlobNb() ; n++) {
    b=theBlobDetection[i].getBlob(n);
    if (b!=null) {
      stroke((i/levels*colorRange)+colorStart,100,100);
      for (int m=0; m<b.getEdgeNb(); m++) {
        eA = b.getEdgeVertexA(m);
        eB = b.getEdgeVertexB(m);
        if (eA !=null && eB !=null)
          line(
          eA.x*img.width*factor, eA.y*img.height*factor, 
          eB.x*img.width*factor, eB.y*img.height*factor 
            );
      }
    }
  }
}

void drawRect(int i) {
  Blob b;
  for (int n=0 ; n<theBlobDetection[i].getBlobNb() ; n++) {
    b=theBlobDetection[i].getBlob(n);
    if (b!=null) {
      stroke((i/levels*colorRange)+colorStart,100,100);
                              
      //line( // diagonal
      //b.xMin*img.width*factor,+b.yMin*img.height*factor,
      //b.xMax*img.width*factor,+b.yMax*img.height*factor
      //  );
      line( // | lft
        b.xMin*img.width*factor,+b.yMin*img.height*factor,
        b.xMin*img.width*factor,+b.yMax*img.height*factor
      );
      line( // | rgt
        b.xMax*img.width*factor,+b.yMin*img.height*factor,
        b.xMax*img.width*factor,+b.yMax*img.height*factor
      );
      line( // - up
        b.xMin*img.width*factor,+b.yMin*img.height*factor,
        b.xMax*img.width*factor,+b.yMin*img.height*factor
      );
      line( // - down
        b.xMin*img.width*factor,+b.yMax*img.height*factor,
        b.xMax*img.width*factor,+b.yMax*img.height*factor
      );
        
      
    }// endif
  }
}

void drawCentre(int i) {
  Blob b;
  EdgeVertex eA,eB;
  for (int n=0 ; n<theBlobDetection[i].getBlobNb() ; n++) {
    b=theBlobDetection[i].getBlob(n);
    if (b!=null) {
      stroke((i/levels*colorRange)+colorStart,100,100);
      
        
      for (int m=0; m<b.getEdgeNb(); m++) {
        eA = b.getEdgeVertexA(m);
        eB = b.getEdgeVertexB(m);
        if (eA !=null && eB !=null){
          line(
            b.x*img.width*factor,+b.y*img.height*factor, // centre
            eA.x*img.width*factor, eA.y*img.height*factor 
              );
          
          }
      }// endfor
        
      
    }// endif
  }
}

void printPixelsValue(){
  //imgWidth, imgHeight,pixels
  int wi = 0;// ancho ini
  int wf = 500;// ancho fin
  int h = 125;// altura
  
  System.out.println("pixels width: 0<"  +wi+"<"+wf+  "<"+theBlobDetection[0].imgWidth);
  System.out.println("pixels height: 0<"     +h+      "<"+theBlobDetection[0].imgHeight);
  for (int w=wi; w<wf; w++){
    //System.out.println(""+theBlobDetection[0].pixels[h*theBlobDetection[0].imgWidth + w]);
    System.out.println(""+img.pixels[h*theBlobDetection[0].imgWidth + w]);
  }
}

void printMaxMinCentre(int nb, int n){
  Blob b;
  b = theBlobDetection[nb].getBlob(n); // get blob
  if (b!=null) {
    for (int m=0;m<b.getEdgeNb();m++) {
      
      System.out.print("edge:"+m);
      System.out.print(" -- (x0:"+b.x*100+",y0:"+b.y*100+")");
      System.out.println(" -- (xmin:"+b.xMin*100+",ymin:"+b.yMin*100+")"+
                             "(xmax:"+b.xMax*100+",ymax:"+b.yMax*100+")");
      
    }
  }
}

void printAllEdges(int nb, int n){
  Blob b;
  EdgeVertex eA,eB;
  float ax,ay,bx,by;
  
  b = theBlobDetection[nb].getBlob(n); // get blob
  if (b!=null) {
    for (int m=0;m<b.getEdgeNb();m++) {
      eA = b.getEdgeVertexA(m);
      eB = b.getEdgeVertexB(m);
      
      if (eA !=null && eB !=null){
        
        ax = eA.x*img.width*factor; 
        ay = eA.y*img.height*factor; 
        bx = eB.x*img.width*factor; 
        by = eB.y*img.height*factor;
        //System.out.println( " -- (ax:"+eA.x*100+",ay:"+eA.y*100+")(bx:"+eB.x*100+",by:"+eB.y*100+")");
        System.out.println( " -- (ax:"+ax+",ay:"+ay+")(bx:"+bx+",by:"+by+")");
      }  
    }
  }
}