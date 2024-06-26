% Estado Inicial
estado_inicial(e(1, 2, 2, 5)).

% Máximo
maximo(7).

% Estado Terminal
terminal(e(0, 0, 0, 0)).

% Função de Valor
valor(E, -1, P):- 
    terminal(E),
    R is P mod 2,
    R = 1.

valor(E, 1, P):- 
    terminal(E),
    R is P mod 2,
    R = 0.

% Ações Possíveis
op1(e(N1, N2, N3, N4), ret(1, N), e(N11, N2, N3, N4)):- 
    numero(1, N),
    N11 is N1 - N,
    N11 >= 0.

op1(e(N1, N2, N3, N4), ret(2, N), e(N1, N22, N3, N4)):- 
    numero(1, N),
    N22 is N2 - N,
    N22 >= 0.

op1(e(N1, N2, N3, N4), ret(3, N), e(N1, N2, N33, N4)):- 
    numero(1, N),
    N33 is N3 - N,
    N33 >= 0.

op1(e(N1, N2, N3, N4), ret(4, N), e(N1, N2, N3, N44)):- 
    numero(1, N),
    N44 is N4 - N,
    N44 >= 0.

% Números
numero(N, N).
numero(L, N1):- 
    maximo(M),
    L < M,
    L1 is L + 1,
    numero(L1, N1).

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
