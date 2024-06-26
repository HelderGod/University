import re

def alphabetFor(word, sep="_"):
    aux = []
    if sep is "":
        aux = list(word)
    else:
        aux = word.split(sep)
    alpha = []
    aux = sorted(aux)
    for char in aux:
        if char not in alpha:
            alpha.append(char)
    return alpha