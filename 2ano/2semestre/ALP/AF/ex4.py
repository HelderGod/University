def areNeighbours(i, f, delta):
    aux = set()
    for c in delta:
        if {c[0], c[-1]} == {i, f}:
            return True
    return False