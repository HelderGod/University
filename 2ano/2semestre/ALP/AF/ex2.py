def leadsTo(x, delta):
    aux = set()
    for c in delta:
        if x in c:
            aux.add(c[-1])
    return aux