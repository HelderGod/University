def symbols(delta):
    aux = set()
    for c in delta:
        aux.add(c[-2])
    return aux