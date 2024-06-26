type peca =
  | I
  | I1
  | S
  | S1
  | O
  | T
  | T1
  | T2
  | T3

  let rotate_piece peca nrot =
    match peca with
    | I | I1 -> 
      (match nrot mod 2 with
      | 0 -> I
      | 1 -> I1
      | _ -> failwith "impossible")
    | S | S1 -> 
      (match nrot mod 2 with
      | 0 -> S
      | 1 -> S1
      | _ -> failwith "impossible")
    | O -> O
    | T | T1 | T2 | T3 -> 
      (match nrot mod 4 with
      | 0 -> T
      | 1 -> T1
      | 2 -> T2
      | 3 -> T3
      | _ -> failwith "impossible")
  

let rec check_fit tabuleiro peca pos =
  let rec check_cell x y =
    x >= 0 && x < Array.length tabuleiro &&
    y >= 0 && y < Array.length tabuleiro.(0) &&
    tabuleiro.(x).(y) = 0
  in

  let rec piece_cells x_offset y_offset =
    match peca with
    | I -> [(0, 0); (1, 0); (2, 0); (3, 0)]
    | I1 -> [(0, 0); (0, 1); (0, 2); (0, 3)]
    | S -> [(0, 0); (1, 0); (1, 1); (2, 1)] 
    | S1 -> [(1, 0); (1, 1); (0, 1); (0, 2)]
    | O -> [(0, 0); (0, 1); (1, 0); (1, 1)]
    | T -> [(0, 1); (1, 1); (2, 1); (1, 0)]
    | T1 -> [(0, 1); (1, 0); (1, 1); (1, 2)]
    | T2 -> [(0, 0); (1, 0); (2, 0); (1, 1)]
    | T3 -> [(0, 0); (0, 1); (0, 2); (1, 1)]
  in

  let can_place_piece x_offset y_offset =
    List.for_all (fun (x, y) -> check_cell (pos + x + x_offset) (y + y_offset)) (piece_cells x_offset y_offset)
  in

  let rec try_place_piece x_offset y_offset =
    if can_place_piece x_offset y_offset then begin
      List.iter (fun (x, y) -> tabuleiro.(pos + x + x_offset).(y + y_offset) <- 1) (piece_cells x_offset y_offset);
      true
    end else if y_offset + 1 < Array.length tabuleiro.(0) then
      try_place_piece x_offset (y_offset + 1)
    else
      false
  in

  try_place_piece 0 0

  let imprimir_tabuleiro tabuleiro =
    let n = Array.length tabuleiro in
    let m = Array.length tabuleiro.(0) in
    for j = m - 1 downto 0 do
      for i = 0 to n - 1 do
        print_int tabuleiro.(i).(j);
        print_string " ";
      done;
      print_newline ();
    done
  

let paktris jogadas =
  let n = 4 in
  let m = 4 in
  let tabuleiro = Array.make_matrix n m 0 in

  let rec loop jogadas =
    match jogadas with
    | [] -> true
    | (peca, nrot, ndir) :: tail ->
        let rotated_piece = rotate_piece peca nrot in
        if check_fit tabuleiro rotated_piece ndir then
          loop tail
        else
          false
  in

  let result = loop jogadas in
  imprimir_tabuleiro tabuleiro;
  print_endline (string_of_bool result)



  
