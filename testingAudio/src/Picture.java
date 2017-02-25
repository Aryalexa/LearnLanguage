import org.apache.commons.math3.complex.Complex;


public class Picture {

	int width;
	int height;
	int nComp;
	
	int[] integers;
	Complex[] complex;
	int[] colors;
	
	public void setComplex(int height, int width, Complex[][] complex){
		setSize(height, width);
		setComplexValues(complex);
		updateIntegerValues();
		updateIntegerValues255();
		updateColor();
		// now you can use colors[] as pixels[]
	}
	
	public void setInteger(int height, int width, int[] ints){
		setSize(height, width);
		setIntegerValues(ints);
		//updateIntegerValues255(); // activar cuando usemos sonidos de verdad y no matrices inventadas
		updateColor();
		// now you can use colors[] as pixels[]
	}
	
	public void setSize(int height, int width){
		this.height = height;
		this.width = width;
		this.nComp = this.height * this.width;
	}
	
	public void setComplexValues(Complex[][] complex){
		// necesario: H and W
		// 2D-array de complejos -> 1D-array de complejos
		
		this.complex = new Complex[nComp];
		
		for (int h=0; h<this.height; h++){
			for (int w=0; w<this.width; w++){
				this.complex[h*this.height + w] = complex[h][w];
			}
		}
	}
	
	public void updateIntegerValues(){
		// necesario: tener this.complex inicializado.
		// 1D-array complex -> 1D-array integer
		integers = new int[this.nComp];
		for (int i=0; i<this.nComp; i++){
			integers[i] = conver_1(this.complex[i]);
			//integers[i] = conver_2(this.complex[i]);
		}
	}
	
	private int conver_1(Complex z){
		// complex -> int (1)
		double x = Math.abs( (z.getReal() + z.getImaginary()) / 2);
		return (int) Math.floor(x);
	}
	
	private int conver_2(Complex z){
		// complex -> int (2)
		double x = z.getArgument();
		return (int) Math.floor(x);
	}
	
	public void setIntegerValues(int[] ints){
		// necesario: H and W
		integers = new int[this.nComp];
		for (int i=0; i<this.nComp; i++){
			integers[i] = ints[i];
		}
	}
	
	private void updateIntegerValues255(){
		// necesario: tener this.integers inicializado.
		// 1D-array integers -> 1D-array integers [0,255]
		int max = 0;
		for (int i=0; i<this.nComp; i++){
			max = Math.max(max, integers[i]);
		}
		if (max==0)System.out.println("max es 0!!!!");
		
		for (int i=0; i<this.nComp; i++){
			integers[i] = integers[i]*max/255;
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
		// necesario: tener this.integers inicializado.
		// 1D-array integers [0,255] --> 1D-array grey colors
		colors = new int[this.nComp];
		for (int i=0; i<this.nComp; i++){
			colors[i] = getColorAto0xAAA(integers[i]);
		}
		addAlphaColor();
	}
	
	private void addAlphaColor(){
		int alphaFF = 0xff000000;
		for (int i=0; i<(height);i++){
			for (int j=0; j<(width);j++){
				colors[i*width+j] = colors[i*width+j] + alphaFF;
			}
		}
	
	}
	
	
	public static void main(String[] args) {
		//set();
		int a,e,i,o,u;
		String s,t;
		
		//convertAto0xA
		// 15 (b16)--> 21 (b10)
		e = Integer.valueOf(String.valueOf(15), 16);
		
		System.out.println("-----------------------");
		a = 0 ; 
		//250, 5921370, 6579300, 5263440, -16777216 = ff000000
		System.out.println("color :"+a+" B10, "+Integer.toHexString(a)+" B16");
		// 250 B10, fa B16
		a = getColorAto0xAAA(a);
		a = 0xff000000 + a;
		System.out.println("color :"+a+" B10, "+Integer.toHexString(a)+" B16");
		//16448250 B10, fafafa B16
		
		//-16777216 = 0xff000000 >> 0xffffffff=-1
		
	}

}
