from scipy.io.wavfile import read, write
from scipy.signal.filter_design import butter, buttord
from scipy.signal import lfilter, lfiltic
import numpy as np
from math import log
import matplotlib.pyplot as plt


''' R E A D I N G '''
name = 'itadakimasu_A'
rate, sound_samples = read(name+'.wav')
# leach sample is a short (2 bytes)
sound_samples_2 = np.float64(sound_samples / 32768.0) # 2^15 = 64*512
sound_samples_3 = np.float64(sound_samples) # 2^15 = 64*512
sound_samples_4 = np.float32(sound_samples)
lenn = len(sound_samples)


for i in range(0,10):
		print i, sound_samples[i], sound_samples_2[i], sound_samples_3[i], sound_samples_4[i]

print '--- rate: ', rate
#print '--- sound samples', sound_samples
print 'len muestras: ', len(sound_samples)
print '---            before:   ||   after: (/ 32768.0) '
print 'max muestras: ', max(sound_samples),'||', max(sound_samples_2),'||', max(sound_samples_3),'||', max(sound_samples_4)
print 'min muestras: ', min(sound_samples),'||', min(sound_samples_2),'||', min(sound_samples_3),'||', min(sound_samples_4)

''' W R I T I N G '''
filtered_2 = sound_samples_2
filtered = np.int16(filtered_2 * 32768 ) # 16bit integer 
write(name+'-copy.wav', rate, filtered)



#for i in range(0,100):
#	print i, sound_samples[i], sound_samples_2[i]
#	print i, filtered[i], filtered_2[i]

''' P L O T I N G '''
# PLOT 
T = 0.05
t = np.linspace(0, T, lenn)
plt.figure(1)
plt.clf()
plt.plot(t, sound_samples, label='read signal')
plt.plot(t, filtered, label='writen signal')
plt.xlabel('time (seconds)')
plt.grid(True)
plt.axis('tight')
plt.legend(loc='upper left')

plt.show()
