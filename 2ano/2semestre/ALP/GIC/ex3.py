def is_symbol(x):
    return len(x) > 0 and (' ' not in x)
    
def is_variable(x):
    return is_symbol(x) and x.isupper()
    
def is_terminal(x):
    return is_symbol(x) and not is_variable(x)
    
def is_word(x):
    return all(is_symbol(c) for c in x)
    
def is_production(x):
    if len(x) != 2:
        return False
    (v, p) = x
    return is_variable(v) and is_word(p)
    
def is_nil(rule):
    if not is_production(rule):
        return False
    (v, p) = rule
    return not p