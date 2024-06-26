import string

def is_symbol(x):
    return len(x) > 0 and (' ' not in x)
    
def is_variable(x):
    return is_symbol(x) and x.isupper()

def is_terminal(x):
    return is_symbol(x) and not is_variable(x)
    
def findInitial(rules):
    if len(rules) == 0:
        return 'S'
    count = 0
    for c in rules:
        for char in c:
            if char == 'S':
                count = count + 1
    if count > 0:
        return 'S'
    else:
        for c in rules:
            for char in c:
                if char.isupper() and is_variable(char):
                    return char              

#print(findInitial(rules = [['S', 'a', 'S', 'b'], ['S']]))
	
rules = [
  ['A', 'a', 'B', 'b'],
  ['B', 'A'],
  ['B'],
  ['S', 'A']
]

print(findInitial(rules))