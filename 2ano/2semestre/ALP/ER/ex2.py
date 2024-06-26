import re

def symbolsIn(word, sep=None):
    symbols_list = []
    if sep is None:
        symbols_list.append(word.split("_"))
    elif sep is "":
        symbols_list.append(list(word))
    else:
        symbols_list.append(word.split(sep))
    for char in symbols_list:
        return char