import re

def generated(word, alphabet, sep="_"):
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
    count = 0
    for a in alpha:
        for c in alphabet:
            if c == a:
                count = count + 1
    return count == len(alpha)