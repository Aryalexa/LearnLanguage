from scipy.io.wavfile import read, write
import matplotlib.pyplot as plt
import numpy as np
#import pandas as pd

#''' R E A D I N G '''
name = 'kor3'
rate, sound_samples = read(name+'.wav')
sound_samples0 = sound_samples
sound_samples = np.float64(sound_samples / 32768.0) # 2^15 = 64*512
print 'rate', rate

T=0.001
t = np.linspace(0, T, len(sound_samples))
NFFT = 1024 #4096 #200     # the length of the windowing segments

ax1 = plt.subplot(311)
plt.plot(t,sound_samples)   # for this one has to either undersample or zoom in 
#.xlim([0,15])
plt.subplot(312)  # don't share the axis
Pxx, freqs, bins, im = plt.specgram(sound_samples, NFFT=NFFT, Fs=rate, noverlap=100, cmap=plt.cm.gist_heat)
print 'num vals', Pxx.size

NFFT = 512
plt.subplot(313)  # don't share the axis
Pxx, freqs, bins, im = plt.specgram(sound_samples, NFFT=NFFT, Fs=rate, noverlap=0, cmap=plt.cm.gist_heat)
print 'num vals', Pxx.size


plt.show() 

'''
time1 = np.arange(0,5,0.0001)
time = np.arange(0,15,0.0001)
data1=np.sin(2*np.pi*300*time1)
data2=np.sin(2*np.pi*600*time1)
data3=np.sin(2*np.pi*900*time1)
data=np.append(data1,data2 )
data=np.append(data,data3)
print len(time)
print len(data)

NFFT = 200     # the length of the windowing segments
Fs = 500  # the sampling rate

# plot signal and spectrogram

ax1 = plt.subplot(211)
plt.plot(time,data)   # for this one has to either undersample or zoom in 
plt.xlim([0,15])
plt.subplot(212 )  # don't share the axis
Pxx, freqs, bins, im = plt.specgram(data, NFFT=NFFT,   Fs=Fs,noverlap=100, cmap=plt.cm.gist_heat)
plt.show() 
'''