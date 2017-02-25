import blobDetection.*;

PGraphics img;

// ==================================================
// setup()
// ==================================================
void setup()
{
  // Works with Processing 1.5
  // img = createGraphics(640, 480,P2D);

  // Works with Processing 2.0b3
  img = createGraphics(640, 480);

  img.beginDraw();
  img.background(255);
  img.noStroke();
  img.fill(0);
  for (int i=0;i<20;i++) {
    float r = random(50);
    img.ellipse(random(img.width), random(img.height), r, r);
  }
  img.endDraw();

  // Size of applet
  //size(img.width, img.height);
  size(660, 500);

}

// ==================================================
// draw()
// ==================================================
void draw()
{
  image(img, 0, 0, width, height);
  //drawBlobsAndEdges(true, true);
}

// ==================================================
// drawBlobsAndEdges()
// ==================================================
void drawBlobsAndEdges(boolean drawBlobs, boolean drawEdges)
{
  noFill();
  BlobDetection theBlobDetection = new BlobDetection(640,480);
  Blob b;
  EdgeVertex eA, eB;
  for (int n=0 ; n<theBlobDetection.getBlobNb() ; n++)
  {
    b=theBlobDetection.getBlob(n);
    if (b!=null)
    {
      // Edges
      if (drawEdges)
      {
        strokeWeight(2);
        stroke(0, 255, 0);
        for (int m=0;m<b.getEdgeNb();m++)
        {
          eA = b.getEdgeVertexA(m);
          eB = b.getEdgeVertexB(m);
          if (eA !=null && eB !=null)
            line(
              eA.x*width, eA.y*height, 
              eB.x*width, eB.y*height
              );
        }
      }

      // Blobs
      if (drawBlobs)
      {
        strokeWeight(1);
        stroke(255, 0, 0);
        rect(
          b.xMin*width, b.yMin*height, 
          b.w*width, b.h*height
          );
      }
    }
  }
}