import re

def listWords(file):
    aux=[]

    file = open(file,"r")
    word = file.read()
    for string in re.sub(r'[^\w]', ' ', word).split():
        if'"' in string:
            lower_words = string.lower()
            aux.append(lower_words.replace('"', ''))
        else:
            aux.append(string.lower())
    return aux