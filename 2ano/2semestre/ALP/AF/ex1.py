def controls(delta):
    aux = set()
    for c in delta:
        if c[0] not in aux:
            aux.add(c[0])
    return aux