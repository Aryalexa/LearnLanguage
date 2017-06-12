"""
==================================
Modifying the coordinate formatter
==================================

Show how to modify the coordinate formatter to report the image "z"
value of the nearest pixel given x and y
"""
import numpy as np
import matplotlib.pyplot as plt



with open('kor3colores2F3') as f:
	X = [int(line) for line in f]

#print X
nX = np.asarray(X)
print nX.size


w = 256
h = nX.size/w
mX = np.reshape(nX,(h,w))

print nX.shape, mX.shape

plt.imshow(mX)

plt.show()
