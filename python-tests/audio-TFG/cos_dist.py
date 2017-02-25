import numpy as np
import scipy.spatial as sp


def cos_cdist_1(matrix, vector):
    v = vector.reshape(1, -1)
    return sp.distance.cdist(matrix, v, 'cosine').reshape(-1)


def cos_cdist_2(matrix1, matrix2):
    return sp.distance.cdist(matrix1, matrix2, 'cosine').reshape(-1)

list1 = [[1,1,1],[1,2,1]]
list2 = [[1,1,1],[1,2,1]]

matrix1 = np.asarray(list1)
matrix2 = np.asarray(list2)

results = []
for vector in matrix2:
    distance = cos_cdist_1(matrix1,vector)
    distance = np.asarray(distance)
    similarity = (1-distance).tolist()
    results.append(similarity)
print '1 - results', results

results4 = []
for vector in matrix2:
    distance = sp.distance.cdist(matrix1, vector.reshape(1, -1), 'cosine')
    results4.append(distance)
results4 = np.asarray(results4)
results4 = 1 - results4
print '4 - results4', results4

results2 = 1 - sp.distance.cdist(matrix1, matrix2, 'cosine')
print '2 - results2', results2

dist_all = cos_cdist_2(matrix1, matrix2)
results3 = []
for item in dist_all:
    distance_result = np.asarray(item)
    similarity_result = (1-distance_result).tolist()
    results3.append(similarity_result)

"""Could you normalize the matrix columns and 
then AB' would be the similarity matrix. 
Use np.dot(A,B.T) """