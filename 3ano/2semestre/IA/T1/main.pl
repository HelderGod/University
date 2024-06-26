%e(Agente, Maquina, CasasBloqueadas, Objetos)

estado_inicial(e(a(1,2), m(2,2), [(6,1), (7, 4), (6, 3), (7,4), (4, 4), (3, 4), (2, 4), (6, 7)], [(4,2)])).
%estado_inicial(e(a(1,2), m(2,2), [(6,1), (7, 3), (6, 3), (3,4), (4, 3), (3, 4), (2, 4), (6, 7)], [(5,5)])).
%estado_inicial(e(a(1,2), m(2,2), [(6,1), (7, 4), (7, 3), (6,3), (4, 4), (3, 4), (2, 4), (6, 7)], [(4,2), (6,2)])).

estado_final(e(_, m(7, 2), _, [])).
%estado_final(e(_, m(7,5), _, [])).
%estado_final(e(_, m(7,5), _, [])).

%move possiveis
move([(0,1), (0, -1), (1, 0), (-1, 0)]).

%operaçao mover agente
op(e(a(X, Y), m(Xm, Ym), CB, Os), (L, C), e(a(X1, Y1), m(Xm1, Ym1), CB, Os), 1) :- 
    move(M),
    member((L, C), M),
    X1 is X + L,
    Y1 is Y + C,
    X1 > 0, X1 < 8,
    Y1 > 0, Y1 < 8,
    \+ member((X1, Y1), CB),
    (Xm = X1, Ym = Y1 -> (Xm1 is Xm + L, Ym1 is Ym + C); Xm1 = Xm, Ym1 = Ym).
    

op(e(a(X, Y), m(Xm, Ym), CB, Os), ativa_alavanca, e(a(X, Y), m(Xm, Ym), CB, Os1), 1) :-
    member((Xm, Ym), Os),            % Verifica se a máquina está na mesma posição de um objeto
    delete(Os, (Xm, Ym), Os1).       % Remove o objeto da lista de objetos




% Busca em largura (BFS)
bfs(Estado, Caminho, NVisitados) :-
    bfs([[Estado]], [], Caminho, 0, NVisitados).

bfs([[Estado|Caminho]|_], _, [Estado|Caminho], NVisitados, NVisitados) :-
    estado_final(Estado).

bfs([[Estado|Caminho]|OutrosCaminhos], Visitados, Solucao, NVisAnt, NVisitados) :-
    findall([NovoEstado, Estado|Caminho],
            (op(Estado, _, NovoEstado, _), \+ member(NovoEstado, Visitados)),
            NovosCaminhos),
    append(OutrosCaminhos, NovosCaminhos, TodosCaminhos),
    length(OutrosCaminhos, N),
    NVisSeg is NVisAnt + N,
    bfs(TodosCaminhos, [Estado|Visitados], Solucao, NVisSeg, NVisitados).

% Predicado para iniciar a busca em largura
pesquisa_largura(EstadoInicial, Solucao, NVisitados) :-
    bfs(EstadoInicial, Solucao, NVisitados).


% Busca em profundidade (DFS)
pesquisa_profundidade(EstadoInicial, Solucao) :-
    dfs([EstadoInicial], [], Solucao).

dfs([Estado|Caminho], _, [Estado|Caminho]) :-
    estado_final(Estado).

dfs([Estado|Caminho], Visitados, Solucao) :-
    op(Estado, _, NovoEstado, _),
    \+ member(NovoEstado, Visitados),
    dfs([NovoEstado|Caminho], [NovoEstado|Visitados], Solucao).

% Calcula a distância de Manhattan entre duas posições (X1, Y1) e (X2, Y2)
manhattan_distance((X1, Y1), (X2, Y2), Dist) :-
    Dist is abs(X1 - X2) + abs(Y1 - Y2).

% Calcula a distância Euclidiana entre duas posições (X1, Y1) e (X2, Y2)
euclidean_distance((X1, Y1), (X2, Y2), Dist) :-
    Dist is sqrt((X1 - X2)^2 + (Y1 - Y2)^2).

% Calcula a heurística da Distância de Manhattan entre o estado atual e o estado final
h(e(_, m(Xm, Ym), _, _), H) :-
    estado_final(e(_, m(Xf, Yf), _, _)),
    manhattan_distance((Xm, Ym), (Xf, Yf), H).

% Calcula a heurística da Distância Euclidiana entre o estado atual e o estado final
%h(e(_, m(Xm, Ym), _, _), H) :-
 %   estado_final(e(_, m(Xf, Yf), _, _)),
  %  euclidean_distance((Xm, Ym), (Xf, Yf), H).

