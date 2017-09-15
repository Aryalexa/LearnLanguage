# LearnLanguage 

This is a Java application dedicated to processing voices for pronunciation improvement. 

It takes two WAV audio files, executes a filter on them and creates a spectrogram applying the Fourier transform. 
Then we run a blob detection algorithm on each spectrogram interpreted as an image, 
this will extract valueable features from the image and with that information we simplify the spectrogram data.
Now we enterprete the simplified each spectrogram as a matrix and compare them with each other.

## Running the Java application

To run LearnLanguage download the whole Java project and import it in Eclipse.
Or just download the `src` folder and then include the `commons-math3-3.6.1` and `org.apache.commons.io` libraries.


The `main` process of the project in within the default package.

## Things you can find in this project

- A bandpass filter written in Java. 
The code was based on the Buttherworth bandpass filter algorithm from 
[Digital Signal Processing](http://www.exstrom.com/journal/sigproc/).

- A spectrogram construction using Fourier transform from [MusicG](https://github.com/jooink/musicg) GitHub project.

- A blob detection algorithm written in Java using the Processing library 
[BlobDetection](http://www.v3ga.net/processing/BlobDetection/).
