import java.io.IOException;

import org.apache.commons.math3.complex.Complex;

import testingAudio.ReadWriteRaw;


public class Picture {

	int width;
	int height;
	private int nComp;
	
	private int[] integers;
	private Complex[] complex;
	int[] colors;

	
	
	private boolean DEBUG = false;
	
	/**
	 * Constructor a partir de complejos. 
	 * Construye el atributo colors[] al que podemos tratar como un array de pixeles
	 * @param height
	 * @param width
	 * @param complex
	 */
	public Picture(int height, int width, Complex[][] complex){
		setSize(height, width);
		setComplexValues(complex);
		if (DEBUG) printMatriz(this.complex,"complex");
		updateIntegerValues();
		if (DEBUG) printMatriz(integers, "colores-enteros");
		updateIntegerValues255();
		if (DEBUG) printMatriz(integers, "colores-255");
		updateColor();
		if (DEBUG) printMatriz(colors, "colores");
		ensanchar();
		// now you can use colors[] as pixels[]
	}
	
	/**
	 * Constructor a partir de enteros. 
	 * Construye el atributo colors[] al que podemos tratar como un array de pixeles
	 * @param height
	 * @param width
	 * @param ints
	 */
	public Picture(int height, int width, int[] ints){
		setSize(height, width);
		setIntegerValues(ints);
		//updateIntegerValues255(); // activar cuando usemos sonidos de verdad y no matrices inventadas
		updateColor();
		// now you can use colors[] as pixels[]
	}
	
	/**
	 * Constructor a partir de enteros. 
	 * Construye el atributo colors[] al que podemos tratar como un array de pixeles
	 * @param height
	 * @param width
	 */
	private void setSize(int height, int width){
		this.height = height;
		this.width = width;
		this.nComp = this.height * this.width;
	}
	
	private void setComplexValues(Complex[][] complex){
		// necesario: H and W
		// 2D-array de complejos -> 1D-array de complejos
		
		this.complex = new Complex[nComp];
		
		for (int h=0; h<this.height; h++){
			for (int w=0; w<this.width; w++){
				this.complex[h*this.width + w] = complex[h][w];
			}
		}
	}
	
	private void setIntegerValues(int[] ints){
		// necesario: H and W
		integers = new int[nComp];
		for (int i=0; i<nComp; i++){
			integers[i] = ints[i];
		}
	}
	
	private void updateIntegerValues(){
		// necesario: tener this.complex inicializado.
		// 1D-array complex -> 1D-array integer
		integers = new int[nComp];
		for (int i=0; i<nComp; i++){
			integers[i] = conver_1(complex[i]);
			//integers[i] = conver_2(this.complex[i]);
		}
	}
	
	private int conver_1(Complex z){
		// complex -> int (1)
		double x = Math.abs( (z.getReal() + z.getImaginary()) / 0.002);
		return (int) Math.floor(x);
	}
	
	private int conver_2(Complex z){
		// complex -> int (2)
		double x = z.multiply(100).getArgument();
		return (int) Math.floor(x);
	}
	
	private void updateIntegerValues255(){
		// necesario: tener this.integers inicializado.
		// 1D-array integers -> 1D-array integers [0,255]
		int max = 0;
		for (int i=0; i<nComp; i++){
			max = Math.max(max, integers[i]);
		}
		if (max==0)System.out.println("max es 0!!!!");
		System.out.println("max --> "+max);
		for (int i=0; i<nComp; i++){
			integers[i] = integers[i]*255/max;
		}
	}
	
	public static int convertAto0xA(int a){
		// 15 (b16)--> 21 (b10)
		return Integer.valueOf(String.valueOf(a), 16);
	}
	
	public static int getColorAto0xAAA(int a){
		int a1;// = convertAto0xA(a);
		a1 = a*256*256 + a*256 + a;
		return a1;
	}
	
	
	
	private void updateColor(){
		//0xaaRRGGBB (alpha,red,green,blue) (int-4bytes)
		// necesario: tener this.integers inicializado.
		// 1D-array integers [0,255] --> 1D-array grey colors
		colors = new int[this.nComp];
		for (int i=0; i<this.nComp; i++){
			colors[i] = getColorAto0xAAA(integers[i]);
			colors[i] = addAlphaColor(colors[i]);
		}
	}
	
	private int addAlphaColor(int color){
		int alphaFF = 0xff000000;
		return color + alphaFF;	
	}
	
	public void printMatriz(int[] data,String name){
		int ini=0;
		int num = 100;
		for(int i=ini; i<ini+num; i++){
			System.out.println(name+" "+data[i]+" \t\t "+Integer.toHexString(data[i])+" (Base16)");
		}
	}
	public void printMatriz(Complex[] data,String name){
		int ini=0;
		int num = 100;
		for(int i=ini; i<ini+num; i++){
			System.out.println(name+" "+data[i]);
		}
	}
	
	void ensanchar(){
		int veces=3;
		colors = Matrix.duplicarEjeY(veces, height, width, colors);
	}

	
	
	public static void main(String[] args) {
		//set();
		int a,e;
		String s,t;
		
		//convertAto0xA
		// 15 (b16)--> 21 (b10)
		e = Integer.valueOf(String.valueOf(15), 16);
		
		System.out.println("----------shigakhaeyo-------------");
		a = 255 ; // 0-255
		System.out.println("color :"+a+" B10, "+Integer.toHexString(a)+" B16");
		// 250 B10, fa B16
		a = getColorAto0xAAA(a);
		System.out.println("color :"+a+" B10, "+Integer.toHexString(a)+" B16");
		a = 0xff000000 + a;
		System.out.println("color :"+a+" B10, "+Integer.toHexString(a)+" B16");
		//16448250 B10, fafafa B16
		
		//-16777216 = 0xff000000 >> 0xffffffff=-1
		
	}
}
