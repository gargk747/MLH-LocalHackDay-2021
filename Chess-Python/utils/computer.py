from modules.board import *

def matrix_to_tuple(array, empty_array):
    for i in range(8):
        empty_array[i] = tuple(array[i])
    return tuple(empty_array)

def check_castling(board,c,side):
    castleLeft = False
    castleRight = False

    if c == "w":
        king = board.white_king
        leftRook = board.white_rook_left
        rightRook =  board.white_rook_right
        attacked = move_gen(board, "b", True)
        row = 7
    elif c == "b":
        king = board.black_king
        leftRook = board.black_rook_left
        rightRook =  board.black_rook_right
        attacked = move_gen(board, "w", True)
        row = 0

    squares = set()

    if king.moved == False: 
        
        if board.array[row][0] == leftRook and leftRook.moved == False:
            
            squares = {(row,1),(row,2),(row,3)}
            if not board.array[row][1] and not board.array[row][2] and not board.array[row][3]:
                if not attacked.intersection(squares):
                    castleLeft = True
        
        if board.array[row][7] == rightRook and rightRook.moved == False:
            
            squares = {(row,6),(row,5)}
            if not board.array[row][6] and not board.array[row][5]:
                if not attacked.intersection(squares):
                    castleRight = True

    if side == "r":
        return castleRight
    elif side == "l":
        return castleLeft

def special_move_gen(board,color,moves = None):
    if moves == None:
        moves = dict()
    if color == "w":
        x = 7
    elif color == "b":
        x = 0
    rightCastle = check_castling(board,color,"r")
    leftCastle = check_castling(board,color,"l")

    if rightCastle:
        moves[(x,6)] = "CR"
    if leftCastle:
        moves[(x,2)] = "CL"

    return moves


def move_gen(board, color, attc = False):
    if attc:
        moves = set()
    else:
        moves = dict()

    
    for j in range(8):
        for i in range(8):
            piece = board.array[i][j]
            if piece != None and piece.color == color:
                legal_moves = piece.gen_legal_moves(board)
                if legal_moves and not attc:
                    moves[(i,j)] = legal_moves
                elif legal_moves and attc:
                    moves = moves.union(legal_moves)

    return moves



def minimax(board, depth, alpha, beta, maximizing, memo):
    tuple_mat = matrix_to_tuple(board.array, board.empty)

    if tuple_mat in memo and depth != 3: 
        return memo[tuple_mat], 0

    if depth == 0: 
        memo[tuple_mat] = board.score
        return board.score, 0

    if maximizing:
        bestValue = float("-inf")
        black_moves = move_gen(board,"b")

        
        for start, move_set in black_moves.items():
            for end in move_set:

                
                
                
                piece = board.array[start[0]][start[1]]
                dest = board.array[end[0]][end[1]]

                
                pawn_promotion = board.move_piece(piece,end[0],end[1],False)

                
                attacked = move_gen(board,"w",True) 
                if (board.black_king.y,board.black_king.x) in attacked:
                    
                    board.move_piece(piece,start[0],start[1],False, True)
                    board.array[end[0]][end[1]] = dest
                    if pawn_promotion:
                        board.score -= 9 
                    continue 

                
                if dest != None:
                    board.score += board.pvalue_dict[type(dest)]

                
                
                v, __ = minimax(board, depth - 1,alpha,beta, False, memo)

                
                board.move_piece(piece,start[0],start[1],False, True)
                board.array[end[0]][end[1]] = dest
                if pawn_promotion:
                    board.score -= 9
                if dest != None:
                    board.score -= board.pvalue_dict[type(dest)]

                if v >= bestValue: 
                    move = (start, (end[0],end[1]))

                bestValue = max(bestValue, v)
                alpha = max(alpha, bestValue)

                if beta <= alpha:
                    return bestValue, move
        try:
            return bestValue, move
        except:
            return bestValue, 0 


    else:    
        bestValue = float("inf")
        white_moves = move_gen(board,"w")

        
        for start, move_set in white_moves.items():
            for end in move_set:

                
                piece = board.array[start[0]][start[1]]
                dest = board.array[end[0]][end[1]]
                pawn_promotion = board.move_piece(piece,end[0],end[1],False)

                
                attacked = move_gen(board,"b",True) 
                if (board.white_king.y,board.white_king.x) in attacked:
                    board.move_piece(piece,start[0],start[1],False,True)
                    board.array[end[0]][end[1]] = dest
                    if pawn_promotion:
                        board.score += 9
                    continue 

                
                if dest != None:
                    board.score -= board.pvalue_dict[type(dest)]

                v, __ = minimax(board, depth - 1,alpha,beta, True, memo)
                
                bestValue = min(v, bestValue)
                beta = min(beta,bestValue)

                
                board.move_piece(piece,start[0],start[1],False,True)
                board.array[end[0]][end[1]] = dest
                if pawn_promotion:
                    board.score += 9
                if dest != None:
                    board.score += board.pvalue_dict[type(dest)]

                if beta <= alpha:
                    return bestValue, 0

        return bestValue, 0

if __name__ == "__main__":

    pygame.init()
    screen = pygame.display.set_mode((800, 60 * 8))
    b = Board()
    sprites = []

    trans_table = dict()
    value, move = minimax(b,3,float("-inf"),float("inf"), True, trans_table)
    print(len(trans_table))
    print(" ")
    b.print_to_terminal()
    print(value)
    print(move)
