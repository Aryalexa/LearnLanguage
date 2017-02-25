import matplotlib.pyplot as plt
import numpy as np
import wave
import sys

spf = wave.open('itadakimasu_A.wav','r')

#Extract Raw Audio from Wav File
signal = spf.readframes(-1)
signal = np.fromstring(signal, 'Int16')

print 'SIGNAL'
for i in range(0,10):
	print i, signal[i]
print '--- sound samples', signal
print 'len muestras: ', len(signal)
print 'max muestras: ', max(signal)
print 'min muestras: ', min(signal)


plt.figure(1)
plt.title('Signal Wave...')
plt.plot(signal)
plt.xlabel('samples')
plt.ylabel('Gain')

plt.show()