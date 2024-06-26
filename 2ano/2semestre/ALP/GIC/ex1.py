import string

def is_symbol(x):
    return (' ' not in x) and ('_' not in x)

def is_terminal(x):
    return is_symbol(x) and x.islower()

def is_variable(x):
    return is_symbol(x) and x.isupper()
    
def wellFormed(rules):
    if len(rules) == 0:
        return False
    for c in rules:
        if len(c) == 0:
            return False
        if is_terminal(c[0]):
            return False
        for char in c:
            if not is_symbol(char):
                return False
    return True