
import org.apache.commons.math3.complex.Complex;


public class Picture {

	int width;
	int height;
	private int nComp;
	
	private int[] integers;
	private Complex[] complex;
	private double[] doubleValues;
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
		
		updateIntegerValues("complex");
		if (DEBUG) printMatriz(integers, "colores-enteros");
		
		updateValues255("integer");
		if (DEBUG) printMatriz(integers, "colores-255");
		
		//invertIntegerValues255();
		if (DEBUG) printMatriz(integers, "colores-255-inv");
		
		updateColor();
		if (DEBUG) printMatriz(colors, "colores");
		
		//ensanchar();
		
		// now you can use colors[] as pixels
	}
	
	/**
	 * Constructor a partir de doubles. 
	 * Construye el atributo colors[] al que podemos tratar como un array de pixeles 
	 * @param height
	 * @param width
	 * @param values
	 */
	public Picture(int height, int width, double[][] values){
		setSize(height, width);
		
		setDoubleValues(values);
		if (DEBUG) printMatriz(this.doubleValues,"doubleValues");
		
		updateValues255("double");
		if (DEBUG) printMatriz(doubleValues, "colores-255");
		
		updateIntegerValues("double");
		if (DEBUG) printMatriz(integers, "colores enteros-255");

		
		//invertIntegerValues255();
		if (DEBUG) printMatriz(integers, "colores-255-inv");
		
		updateColor();
		if (DEBUG) printMatriz(colors, "colores");
		
		//ensanchar();
		
		// now you can use colors[] as pixels
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
	
	private void setDoubleValues(double[][] doubles){
		// necesario: H and W
		// 2D-array de doubles -> 1D-array de complejos
		
		this.doubleValues = new double[nComp];
		
		for (int h=0; h<this.height; h++){
			for (int w=0; w<this.width; w++){
				this.doubleValues[h*this.width + w] = doubles[h][w];
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
	
	private void updateIntegerValues(String tipo_origen){
		// 1D-array complex/double -> 1D-array integer

		// necesario: tener this.doubleValues inicializado.
		if (tipo_origen == "double"){
			integers = new int[nComp];
			for (int i=0; i<nComp; i++){
				integers[i] = (int) Math.floor(doubleValues[i]);
				
			}
		}
		
		// necesario: tener this.complex inicializado.
		else if (tipo_origen == "complex"){
			int neg =0;
			doubleValues = new double[nComp];
			for (int i=0; i<nComp; i++){
				doubleValues[i] = module(complex[i]);
				//integers[i] = conver_2(this.complex[i]);
				if (doubleValues[i] <0) {
					neg++;
				}
			}
			if (DEBUG) System.out.println("argumentos negativos "+neg);
			
			if (DEBUG) printMaxMin("argumentos positivos","doubleValues");
			if (DEBUG) logarithmicScale();
			if (DEBUG) printMaxMin("logaritmo*1000","doubleValues");

			integers = new int[nComp];
			if (DEBUG)
			for (int i=0; i<nComp; i++){
				integers[i] = (int) ( doubleValues[i]<0 ? 0 : doubleValues[i]*1000 );
			}
			if (DEBUG) printMaxMin("enteros","integers");
			
		}
		else {
			System.out.println("argumento no valido");

		}
	}
	
	private double conver_1(Complex z){
		// complex -> int (la media entre parte real e imaginaria * 10000)
		double x = Math.abs( z.getReal() + z.getImaginary()) / 2 * 10000;
		return x;
	}
	
	private double abs_argument(Complex z){
		// complex -> int ( argummento(angulo) del numero complejo * 10000)
		double x = Math.abs(z.getArgument()) * 10000;
		return x;
	}
	
	private double module(Complex z){
		// complex -> int ( modulo del numero complejo * 10000)
		double x = Math.sqrt(z.getReal()*z.getReal() + z.getImaginary()*z.getImaginary()) * 10000;
		return x;
	}
	
	private void updateValues255(String tipo){
		// necesario: tener this.integers inicializado.
		// 1D-array integers [min,max] -> 1D-array integers [0,255]
		
		if (tipo == "integer"){
			if (DEBUG) printMaxMin("antes de 255","integers");
			integers = Matrix.normalizeVal(height, width, integers, 255);
			if (DEBUG) printMaxMin("despues de 255","integers");
		}
		else if (tipo == "double"){
			if (DEBUG) printMaxMin("antes de 255","doubleValues");
			doubleValues = Matrix.normalizeVal(height, width, doubleValues, 255);
			if (DEBUG) printMaxMin("despues de 255","doubleValues");
		}
		else {
			System.out.println("argumento no valido");

		}
	}
	private void logarithmicScale(){
		doubleValues = Matrix.scaleLog(height, width, doubleValues);
	}
	
	
	private void invertIntegerValues255(){
		// necesario: tener this.integers inicializado.
		// 1D-array integers [0,255] -> invertir( 1D-array integers [0,255] )
		for (int i=0; i<nComp; i++){
			if (DEBUG) System.out.println("1:"+integers[i]+" 2:"+(255-integers[i]));
			integers[i] = 255-integers[i];
		}
	}
	
	public static int convertAto0xA(int a){
		// 15 (b16)--> 21 (b10)
		return Integer.valueOf(String.valueOf(a), 16);
	}
	
	public static int getColorAto0xAAA(int a){
		int a1;// = convertAto0xA(a);
		//a = (0xff << 24 )+ (a<<16) + (a<<8) + a;
		a1 = (a<<16) + (a<<8) + a;
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
		int alphaFF = 0xff << 24; // 0xff000000
		return color + alphaFF;	
	}
	
	public void printMatriz(int[] data,String name){
		int ini=0;
		int num = 100;
		for(int i=ini; i<ini+num; i++){
			System.out.println(name+" "+data[i]+" \t\t "+Integer.toHexString(data[i])+" (Base16)");
		}
	}
	public void printMatriz(double[] data,String name){
		int ini=0;
		int num = 100;
		for(int i=ini; i<ini+num; i++){
			System.out.println(name+" "+data[i]);
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

	private void printMaxMin(String titulo, String arrayName){
		if (arrayName=="integers") {
			int max = 0;
			int min = 0;
			for (int i=0; i<nComp; i++){
				max = Math.max(max, integers[i]);
				min = Math.min(min, integers[i]);
			}
			System.out.println(titulo+" -\t max --> "+max+ " / min --> "+min);
		}
		if (arrayName=="doubleValues") {
			double max = 0;
			double min = 0;
			for (int i=0; i<nComp; i++){
				max = Math.max(max, doubleValues[i]);
				min = Math.min(min, doubleValues[i]);
			}
			System.out.println(titulo+" -\t max --> "+max+ " / min --> "+min);
		}
	}
	
	public static void main(String[] args) {
		//set();
		int a;
		
		//convertAto0xA
		// 15 (b16) --> 21 (b10)
		
		System.out.println("----------shijakhaeyo-------------");
		a = 0xfa ; // 0-255
		System.out.println("color :"+a+" B10, "+Integer.toHexString(a)+" B16");
		// 250 B10, fa B16
		a = getColorAto0xAAA(a);
		System.out.println("color :"+a+" B10, "+Integer.toHexString(a)+" B16");
		//16448250 B10, fafafa B16
		a = 0xff000000 + a;
		System.out.println("color :"+a+" B10, "+Integer.toHexString(a)+" B16");
		//-328966 B10, fffafafa B16
		
		//-16777216 = 0xff000000 >> 0xffffffff=-1
		
		
		
	}
}
