from scipy.io.wavfile import read, write
from scipy.signal.filter_design import butter, buttord
from scipy.signal import lfilter, lfiltic
import numpy as np
from math import log
import matplotlib.pyplot as plt


from scipy.signal import butter, lfilter


def butter_bandpass(lowcut, highcut, fs, order=5):
    nyq = 0.5 * fs
    low = lowcut / nyq
    high = highcut / nyq
    b, a = butter(order, [low, high], btype='band')
    return b, a


def butter_bandpass_filter(data, lowcut, highcut, fs, order=5):
    b, a = butter_bandpass(lowcut, highcut, fs, order=order)
    y = lfilter(b, a, data)
    return y

def butter_print_coeffs(lowcut, highcut, fs, order=5):
	b, a = butter_bandpass(lowcut, highcut, fs, order=order)
	print '--- B: '
	for i in range(0,len(b)):
		print i, b[i]
	print '--- A: '
	for i in range(0,len(a)):
		print i, a[i]


if __name__ == "__main__":

	#''' R E A D I N G '''
	name = 'itadakimasu_A'
	rate, sound_samples = read(name+'.wav')
	sound_samples = np.float64(sound_samples / 32768.0) # 2^15 = 64*512

	for i in range(0,10):
		print i, sound_samples[i], sound_samples[i]
	print '--- rate: ', rate
	print 'len muestras: ', len(sound_samples)
	print '---             : (/ 32768.0)   ||   normal: '
	print 'max muestras: ', max(sound_samples),'||', max(sound_samples)*32768
	print 'min muestras: ', min(sound_samples),'||', min(sound_samples)*32768
	#''' F I L T E R I N G '''
	# Sample rate and desired cutoff frequencies (in Hz).
	#fs = rate
	lowcut = 500.0
	highcut = 3500.0
	order = 6
	filtered = butter_bandpass_filter(sound_samples, lowcut, highcut, rate, order)
	butter_print_coeffs(lowcut, highcut, rate, order)

	
	#''' P L O T I N G '''
	# PLOT 
	a = 0.02
	f0 = 1000.0 # Hz
	T = 2.2675
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


	for i in range(0,100):
		print i, sound_samples[i], filtered[i]
	
	# PLOT - compare
	T = 0.05
	t = np.linspace(0, T, len(sound_samples))
	plt.figure(2)
	plt.clf()
	plt.plot(t, sound_samples, label='read signal')
	plt.plot(t, filtered, label='writen signal')
	plt.xlabel('time (seconds)')
	plt.grid(True)
	plt.axis('tight')
	plt.legend(loc='upper left')


	#''' W R I T I N G '''
	print '--- orden: ', order
	#print '--- wn corte?: ', wn
	filtered = np.int16(filtered * 32768 ) # 16bit integer 
	write(name+'-filtered.wav', rate, filtered)



	print 'len fmuestras: ', len(filtered)
	print '---             :  normal  ||   (/ 32768.0): '
	print 'max fmuestras: ', max(filtered),'||', max(filtered)/32768.0
	print 'min fmuestras: ', min(filtered),'||', min(filtered)/32768.0


	plt.show()