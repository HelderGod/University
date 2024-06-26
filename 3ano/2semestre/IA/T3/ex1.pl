% Estado inicial 
estado_inicial(e([ 	b,w,b,b,w,b,w,
					w,b,w,w,w,b,w, 
					v,v,v,v,b,w,b, 
					w,b,v,w,w,b,w,  
					w,w,b,w,b,w,b, 
					w,b,w,b,w,b,w], w)).

% Valor de um estado
valor(e(L,_),0,_):-  \+ member(v,L), !.
valor(E,V,P):-
        terminal(E),
        X is P mod 2,
        ( X == 1, V = 1 ; X == 0, V = -1 ).

% Verifica se um estado é terminal
terminal(E):- dividir_em_linhas(E,SL),E = e(L,J),(linhas(e(SL),J);
             colunas(e(SL),J); cheio(L)).

cheio(L):- \+member(v,L).

linhas(e([]),_):- fail.
linhas(e([H|T]),X) :- linhas(H,X); linhas(e(T),X).
linhas([X,X,X,X|_],X):- X \= v.
linhas([_|T],X):- linhas(T,X).

colunas(e([]),_):- fail.
colunas(e([_,_,_,[]]),_):- fail.
colunas(e([A,B,C,D|T]),X) :- colunas(A,B,C,D,X); colunas(e([B,C,D|T]),X).
colunas([X|_],[X|_],[X|_],[X|_],X):- X \= v.
colunas([_|A],[_|B],[_|C],[_|D],X):- colunas(A,B,C,D,X).
colunas([],[],[],[],_):- fail.

diagonal(e([]),_):- fail.
diagonal(e([_,_,_,[]]),_):- fail.
diagonal(e([A,B,C,D|T]),X) :- diagonal(A,B,C,D,X); diagonal(e([B,C,D|T]),X).
diagonal([X|_],[_,X|_],[_,_,X|_],[_,_,_,X|_],X):- X \= v.
diagonal([_,_,_,X|_],[_,_,X|_],[_,X|_],[X|_],X):- X \= v.
diagonal([_|A],[_|B],[_|C],[_|D],X):- diagonal(A,B,C,D,X).
diagonal([],[],[],[],_):- fail.

dividir_em_linhas(e(L,_), S) :- divide(L,S).
divide([],[]).
divide([V1,V2,V3,V4,V5,V6,V7|T],[[V1,V2,V3,V4,V5,V6,V7]|T1]):- divide(T,T1).

inv(w,b).
inv(b,w).

op1(e(L,J),(C),e(L1,J1)) :-
    inv(J,J1),
    subs(v,J,L,L1,1,C),C > 0, C < 43.

subs(A,J,[A|R],[J|R],C,C).
subs(A,J,[B|R],[B|S],N,C) :-
        M is N+1,
        subs(A,J,R,S,M,C).

% Pesquisa Minmax
minmax_decidir(Ei,terminou):- terminal(Ei).
minmax_decidir(Ei,Opf):- 
    findall(Es-Op, op1(Ei,Op,Es),L),
    findall(Vc-Op,(member(E-Op,L), minmax_valor(E,Vc,1)),L1),
    escolhe_max(L1,Opf).

minmax_valor(Ei,Val,P):- terminal(Ei), valor(Ei,Val,P).
minmax_valor(Ei,Val,P):- 
    findall(Es,op1(Ei,_,Es),L),
    P1 is P+1,
    findall(Val1,(member(E,L),minmax_valor(E,Val1,P1)),V),
    seleciona_valor(V,P,Val).

seleciona_valor(V,P,Val):- X is P mod 2, X=0,!, maximo(V,Val).
seleciona_valor(V,_,Val):- minimo(V,Val).

maximo([A|R],Val):- maximo(R,A,Val).
maximo([],A,A).
maximo([A|R],X,Val):- A < X,!, maximo(R,X,Val).
maximo([A|R],_,Val):- maximo(R,A,Val).

escolhe_max([A|R],Val):- escolhe_max(R,A,Val).
escolhe_max([],_-Op,Op).
escolhe_max([A-_|R],X-Op,Val):- A < X,!, escolhe_max(R,X-Op,Val).
escolhe_max([A|R],_,Val):- escolhe_max(R,A,Val).

minimo([A|R],Val):- minimo(R,A,Val).
minimo([],A,A).
minimo([A|R],X,Val):- A > X,!, minimo(R,X,Val).
minimo([A|R],_,Val):- minimo(R,A,Val).

% Pesquisa Alfa-Beta
alfabeta_decidir(Ei, MelhorJogada) :- 
    findall(Vc-Op, (op1(Ei,Op,Es), alfabeta_min(Es, Vc, 1, -10000, 10000)), L),
    escolhe_max(L, MelhorJogada).

alfabeta_min(Ei, Val, _, _, _) :- 
    terminal(Ei), 
    valor(Ei, Val, _), !.

alfabeta_min(Ei, Val, P, Alfa, Beta) :- 
    findall(Es, op1(Ei, _, Es), L),
    P1 is P + 1,
    processa_lista_min(L, P1, 10000, Alfa, Beta, Val), !.

processa_lista_min([], _, Val, _, _, Val).
processa_lista_min([H|T], P, V, A, B, Val) :-
    alfabeta_max(H, V2, P, A, B),
    min(V, V2, V3),
    (V3 =< A -> Val = V3 ; min(B, V3, B1), processa_lista_min(T, P, V3, A, B1, Val)).

alfabeta_max(Ei, Val, _, _, _) :- 
    terminal(Ei), 
    valor(Ei, Val, _), !.

alfabeta_max(Ei, Val, P, Alfa, Beta) :- 
    findall(Es, op1(Ei, _, Es), L),
    P1 is P + 1,
    processa_lista_max(L, P1, -10000, Alfa, Beta, Val), !.

processa_lista_max([], _, Val, _, _, Val).
processa_lista_max([H|T], P, V, A, B, Val) :-
    alfabeta_min(H, V2, P, A, B),
    max(V, V2, V3),
    (V3 >= B -> Val = V3 ; max(A, V3, A1), processa_lista_max(T, P, V3, A1, B, Val)).

max(A, B, B) :- A < B, !.
max(A, _, A).

min(A, B, A) :- A < B, !.
min(_, B, B).

% Teste de desempenho para Minmax
testar_minmax(Ei, MelhorJogada, Tempo) :-
    statistics(runtime, [Start|_]),
    minmax_decidir(Ei, MelhorJogada),
    statistics(runtime, [End|_]),
    Tempo is End - Start.

% Teste de desempenho para Alfa-Beta
testar_alfabeta(Ei, MelhorJogada, Tempo) :-
    statistics(runtime, [Start|_]),
    alfabeta_decidir(Ei, MelhorJogada),
    statistics(runtime, [End|_]),
    Tempo is End - Start.

% Exemplo de uso
executar_teste :-
    estado_inicial(Ei),
    testar_minmax(Ei, MelhorJogadaMinmax, TempoMinmax),
    write('Melhor Jogada Minmax: '), write(MelhorJogadaMinmax), nl,
    write('Tempo de Execução Minmax: '), write(TempoMinmax), write(' ms'), nl,
    testar_alfabeta(Ei, MelhorJogadaAlfabeta, TempoAlfabeta),
    write('Melhor Jogada Alfa-Beta: '), write(MelhorJogadaAlfabeta), nl,
    write('Tempo de Execução Alfa-Beta: '), write(TempoAlfabeta), write(' ms'), nl.
