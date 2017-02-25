from scipy.io.wavfile import read, write
from scipy.signal.filter_design import butter, buttord
from scipy.signal import lfilter, lfiltic
import numpy as np
from math import log
import matplotlib.pyplot as plt


''' R E A D I N G '''
name = 'itadakimasu_A'
rate, sound_samples = read(name+'.wav')
sound_samples = np.float64(sound_samples / 32768.0) # 2^15 = 64*512
for i in range(0,10):
	print i, sound_samples[i], sound_samples[i]
print '--- rate: ', rate
print '--- sound samples', sound_samples
print 'len muestras: ', len(sound_samples)
print '---             after: (/ 32768.0)   ||   before: '
print 'max muestras: ', max(sound_samples),'||', max(sound_samples)*32768
print 'min muestras: ', min(sound_samples),'||', min(sound_samples)*32768


''' F I L T E R I N G '''
pass_freq = 0.2
stop_freq = 0.3
pass_gain = 0.5 # permissible loss (ripple) in passband (dB)
stop_gain = 10.0 # attenuation required in stopband (dB)
ord, wn = buttord(pass_freq, stop_freq, pass_gain, stop_gain)


''' W R I T I N G '''
print '--- orden: ', ord
print '--- wn corte?: ', wn
b, a = butter(ord, wn, btype = 'low')
filtered = lfilter(b, a, sound_samples)
filtered = np.int16(filtered * 32768 * 10) # 16bit integer 
write(name+'-filtered.wav', rate, filtered)


''' P L O T I N G '''
# PLOT 
a = 0.05
f0 = 1000.0 # Hz
T = 0.15
t = np.linspace(0, T, len(sound_samples))#, endpoint=False)
plt.figure(1)
plt.clf()

plt.subplot(211)
plt.plot(t, sound_samples, label='Noisy signal')
plt.xlabel('time (seconds)')
plt.hlines([-a, a], 0, T, linestyles='--')
plt.grid(True)
plt.axis('tight')
plt.legend(loc='upper left')

plt.subplot(212)
plt.plot(t, filtered, label='Filtered signal (%g Hz)' % f0)
plt.xlabel('time (seconds)')
plt.hlines([-a, a], 0, T, linestyles='--')
plt.grid(True)
plt.axis('tight')
plt.legend(loc='upper left')

plt.show()