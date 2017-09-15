int[] data; //<>//

void setup() {
  size(200, 200);
  // Load text file as a String
  String[] stuff = loadStrings("data.csv");
  // Convert string into an array of integers using ',' as a delimiter
  data = int(split(stuff[0], ','));
}

void draw() {
  background(255);
  stroke(0);
  for (int i = 0; i<data.length; i++) { 
    // Use array of ints to set the color and height of each rectangle.
    rect(i*20, 0, 20, data[i]);
  }
  noLoop();
}