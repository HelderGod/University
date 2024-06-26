%Condições:

%    sobre(X, Y): bloco X está sobre o bloco Y.
%    livre(X): bloco X não tem nenhum bloco sobre ele.
%    no_chao(X): bloco X está no chão.
%    no_braço(X, B): bloco X está no braço B.
%    braço_livre(B): braço B está livre.
%    pequeno(X): bloco X é pequeno.
%    grande(X): bloco X é grande.

%Estado inicial:
estado_inicial([
    grande(a),
    grande(d),
    pequeno(e),
    pequeno(c),
    pequeno(b),
    no_chao(a),
    sobre(d, a),
    sobre(e, d),
    no_chao(b),
    sobre(c, b),
    livre(e),
    livre(c),
    braco_livre(1),
    braco_livre(2)
]).

estado_final([
    grande(a),
    grande(d),
    pequeno(e),
    pequeno(c),
    pequeno(b),
    no_chao(c),
    sobre(b, c),
    sobre(e, b),
    no_chao(d),
    sobre(a, d),
    livre(e),
    livre(a),
    braco_livre(1),
    braco_livre(2)
]).

%Definição das ações:
accao(agarrar_pequeno(X, B), 
[pequeno(X), livre(X), braco_livre(B)],
[no_braco(X, B)],
[livre(X), braco_livre(B)]
).

accao(agarrar_grande(X),
[grande(X), livre(X), braco_livre(1), braco_livre(2)],
[no_braco(X, 1), no_braco(X, 2)],
[livre(X), braco_livre(1), braco_livre(2)]
).

accao(peq_sobre_peq(X, Y),
[pequeno(X), pequeno(Y), livre(Y), no_braco(X, B)],
[sobre(X, Y), livre(X), braco_livre(B)],
[no_braco(X, B), livre(Y)]
).

accao(peq_sobre_grande(X, Y),
[pequeno(X), grande(Y), livre(Y), no_braco(X, B)],
[sobre(X, Y), livre(X), braco_livre(B)],
[no_braco(X, B), livre(Y)]
).

accao(grande_sobre_grande(X, Y),
[grande(X), grande(Y), livre(Y), no_braco(X, 1), no_braco(X, 2)],
[sobre(X, Y), livre(X), braco_livre(1), braco_livre(2)],
[no_braco(X, 1), no_braco(X, 2), livre(Y)]
).

accao(por_no_chao_grande(X),
[grande(X), no_braco(X, 1), no_braco(X, 2)],
[no_chao(X), braco_livre(1), braco_livre(2), livre(X)],
[no_braco(X, 1), no_braco(X, 2)]
).

accao(por_no_chao_pequeno(X),
[pequeno(X), no_braco(X, B)],
[no_chao(X), braco_livre(B), livre(X)],
[no_braco(X, B)]
).