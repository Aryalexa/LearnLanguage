package blobDetection;
/**
 * documentation: http://www.v3ga.net/processing/BlobDetection/index-page-documentation.html
 * @author aryalexa
 *
 */
//==================================================
//class Blob
//==================================================
/* 
 * float x, float y
 * Normalized coordinates of the blob's center ( range [0;1] )
 * 
 * float w, float h
 * Normalized dimensions of the blob ( range [0;1] )
 * 
 * float xMin, float yMin, float xMax, float yMax
 * Normalized min/max coordinates of the blob ( range [0;1] )
*/
public class Blob
{
	public BlobDetection parent;
	
	public int   	id;
	public float 	x,y;   // position of its center
	public float 	w,h;   // width & height
	public float 	xMin, xMax, yMin, yMax;

	public int[] 	line;  
	public int 		nbLine;
	
	public static int MAX_NBLINE = 4000;    

	public Blob(BlobDetection parent)
	{
		this.parent = parent;
		line = new int[MAX_NBLINE];  // stack of index
		nbLine = 0;
	}

	/**
	 * Each detected blob maintains a list of edges. 
	 * An edge is made of two points A and B (called edgeVertex). 
	 * For a given index, these two functions enable you to access them. 
	 * Both of them have normalized coordinates (in the range [0;1]). 
	 * If the given index iEdge is not in the range [0;lineNumber-1], 
	 * then the function returns null.
	 * @param iEdge
	 * @return
	 */
	public EdgeVertex getEdgeVertexA(int iEdge)
	{
		if (iEdge*2<parent.nbLineToDraw*2)
			return parent.getEdgeVertex(line[iEdge*2]);
		else
			return null;
	}
	public EdgeVertex getEdgeVertexB(int iEdge)
	{
		if ((iEdge*2+1)<parent.nbLineToDraw*2)
			return parent.getEdgeVertex(line[iEdge*2+1]);
		else
			return null;
	}

	/**
	 * Returns the number of edges for the blob.
	 * @return int number of edges for the blob
	 */
	public int getEdgeNb()
	{
		return nbLine;
	}

	public void update()
	{
		w = (xMax-xMin);
		h = (yMax-yMin);
		x = 0.5f*(xMax+xMin);
		y = 0.5f*(yMax+yMin);
	
		nbLine /= 2;  
	}


};