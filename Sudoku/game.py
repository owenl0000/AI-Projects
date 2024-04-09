from sudoku import Sudoku
import queue
import copy
import time
import numpy as np #pip install numpy
import matplotlib.pyplot as plt #pip install matplotlib
import math
from multiprocessing import Process, Queue


'''
Parameters: Takes as input the curr_board state and the puzzle
Returns: True if the current board state is the goal and False if not
Note: Existing version solves the puzzle everytime you test for goal
      feel free to change the implementation to save time
'''
def test_goal(curr_board,puzzle):
    puzzle_solution=puzzle.solve()
    try:
        solution_board=puzzle_solution.board
        for i in range(len(solution_board)):
            for j in range(len(solution_board[i])):
                assert(curr_board[i][j]==solution_board[i][j])
        return True
    except Exception as e:
        return False

'''
Parameters: Takes as input a puzzle board and puzzle size
Returns: True if the puzzle board is valid and False if not
'''    
def valid_puzzle(puzzle_size,puzzle_board):
    puzzle=Sudoku(puzzle_size,board=puzzle_board)
    return puzzle.validate()

'''
Parameters: Takes as input a puzzle board
Returns: Returns all the cells in the grid that are empty
'''
def empty_cells(puzzle_board):
    empty_cell_list=[]
    for i in range(len(puzzle_board)):
        for j in range(len(puzzle_board[i])):
            if puzzle_board[i][j] is None:
                empty_cell_list.append([i,j])
    return empty_cell_list

'''
params: Takes the current puzzle as input
Return: The puzzle board corresponding to the goal
Note: You can modify the function definition as you see fit
'''
def bfs(puzzle):

    q = queue.Queue()
    initialState = {'board': puzzle.board, 'empty_cells': empty_cells(puzzle.board)}
    q.put(initialState)
    explored = set()

    while not q.empty():
        currentState = q.get()
        current_board = currentState['board']
        board_tuple = tuple(tuple(row) for row in current_board)

        if board_tuple in explored:
            continue

        explored.add(board_tuple)

        if test_goal(current_board, puzzle):
            return current_board
        
        for i, j in currentState['empty_cells']:
            for value in range(1, puzzle.size + 1):
                new_board = copy.deepcopy(current_board)
                new_board[i][j] = value
                if valid_puzzle(int(math.sqrt(puzzle.size)), new_board):
                    
                    new_empty_cells = copy.deepcopy(currentState['empty_cells'])
                    new_empty_cells.remove([i, j])
                    q.put({'board': new_board, 'empty_cells': new_empty_cells})

    return None

'''
params: Takes the current puzzle as input
Return: The puzzle board corresponding to the goal
Note: You can modify the function definition as you see fit
'''
def dfs(puzzle):
    
    q = queue.LifoQueue()
    initialState = {'board': puzzle.board, 'empty_cells': empty_cells(puzzle.board), 'path': []}
    q.put(initialState)

    while not q.empty():
        currentState = q.get()  
        current_board = currentState['board']

        if test_goal(current_board, puzzle):
            return current_board  
        
        for i, j in currentState['empty_cells']:
            for value in range(1, puzzle.size + 1):
                new_board = copy.deepcopy(current_board)
                new_board[i][j] = value  
                
                if valid_puzzle(int(math.sqrt(puzzle.size)), new_board):  
                    new_empty_cells = copy.deepcopy(currentState['empty_cells'])
                    new_empty_cells.remove([i, j])
                    q.put({'board': new_board, 'empty_cells': new_empty_cells})

    return None

'''
params: Takes the current puzzle as input
Return: The puzzle board corresponding to the goal
Note: You can modify the function definition as you see fit
'''
def bfs_with_pruning(puzzle):
    q = queue.Queue()
    initialState = {'board': copy.deepcopy(puzzle.board), 'empty_cells': empty_cells(puzzle.board)}
    q.put(initialState)

    while not q.empty():
        currentState = q.get()
        current_board = currentState['board']

        if test_goal(current_board, puzzle):
            return current_board

        if not currentState['empty_cells']:
            continue

        i, j = currentState['empty_cells'][0]  

        for value in range(1, puzzle.size + 1):
            new_board = copy.deepcopy(current_board)
            new_board[i][j] = value
            if valid_puzzle(int(math.sqrt(puzzle.size)), new_board):
                new_empty_cells = currentState['empty_cells'][1:]  
                q.put({'board': new_board, 'empty_cells': new_empty_cells})

    return None
'''
params: Takes the current puzzle as input
Return: The puzzle board corresponding to the goal
Note: You can modify the function definition as you see fit
'''
def dfs_with_pruning(puzzle):
    q = queue.LifoQueue()
    initialState = {'board': copy.deepcopy(puzzle.board), 'empty_cells': empty_cells(puzzle.board)}
    q.put(initialState)

    while not q.empty():
        currentState = q.get()
        current_board = currentState['board']

        if test_goal(current_board, puzzle):
            return current_board

        if not currentState['empty_cells']:
            continue

        i, j = currentState['empty_cells'][0] 

        for value in range(1, puzzle.size + 1):
            new_board = copy.deepcopy(current_board)
            new_board[i][j] = value
            if valid_puzzle(int(math.sqrt(puzzle.size)), new_board):
                new_empty_cells = currentState['empty_cells'][1:] 
                q.put({'board': new_board, 'empty_cells': new_empty_cells})
    
    return None

def strategy_runner(strategy_name, puzzle, result_queue):
    solution = globals()[strategy_name](puzzle)  
    result_queue.put(solution)

def execute_strategy(strategy_name, puzzle, timeout=30): #after 30 seconds I lose patience
    result_queue = Queue()
    strategy_process = Process(target=strategy_runner, args=(strategy_name, puzzle, result_queue))
    strategy_process.start()
    strategy_process.join(timeout=timeout)
    if strategy_process.is_alive():
        strategy_process.terminate()
        strategy_process.join()
        print(f"\n{strategy_name} exceeded the timeout.")
        return None 
    else:
        solution = result_queue.get() if not result_queue.empty() else None
        if solution:
            print(f"\n{strategy_name} Solved Puzzle Board:")
            #for row in solution:
                #print(row)
            return solution
        else:
            print(f"\n{strategy_name}: No solution found.")
            return None
        
def plot_runtime_vs_difficulty(strategy_name):
    difficulties = np.arange(0.2, 1.0, 0.1)  # Difficulties from 0.2 to 0.9
    num_strategies = len(strategies)
    cols = 2  
    rows = num_strategies // cols + (num_strategies % cols > 0)  
    
    fig, axs = plt.subplots(rows, cols, figsize=(10, 5*rows))  
    fig.subplots_adjust(hspace=0.4, wspace=0.4)  

    for i, strategy_name in enumerate(strategies):
        runtimes = []
        for difficulty in difficulties:
            puzzle = Sudoku(2, 2).difficulty(difficulty)  
            print(f"Executing {strategy_name} at difficulty {difficulty:.1f}")
            start_time = time.time()
            result = execute_strategy(strategy_name, puzzle)
            if result is None:  
                runtimes.append(np.nan)  
            else:
                runtime = time.time() - start_time
                runtimes.append(runtime)

        # Determine the subplot index
        if num_strategies <= cols:
            ax = axs[i]
        else:
            ax = axs[i // cols, i % cols]

        ax.plot(difficulties, runtimes, marker='o', linestyle='-', label=strategy_name)
        ax.set_title(f'Strategy: {strategy_name}')
        ax.set_xlabel('Puzzle Difficulty')
        ax.set_ylabel('Runtime (seconds)')
        ax.set_xticks(difficulties)
        ax.grid(True)
        ax.legend()

    for i in range(num_strategies, rows*cols):
        fig.delaxes(axs.flatten()[i])

    plt.show()
        

if __name__ == "__main__":

    
    puzzle=Sudoku(2,2).difficulty(0.2) # Constructs a 2 x 2 puzzle
    puzzle.show() # Pretty prints the puzzle

    #print(valid_puzzle(2,puzzle.board)) # Checks if the puzzle is valid
    #(test_goal(puzzle.board,puzzle)) # Checks if the given puzzle board is the goal for the puzzle
    #print(empty_cells(puzzle.board)) # Prints the empty cells as row and column values in a list for the current puzzle board
    

    print("Runtime")
    strategies = ['dfs', 'bfs', 'dfs_with_pruning', 'bfs_with_pruning']
    runtimes = []

    longest_strategy_name = max(strategies, key=len)

    for strategy in strategies:
        print("----------------------------------------------")
        print(f"Executing {strategy}")
        print("----------------------------------------------")
        result = execute_strategy(strategy, puzzle)  
        if result is None:
            runtimes.append("NA")  
            print(f"{strategy} Runtime: NA")
        else:
            start_time = time.time()
            solution = globals()[strategy](puzzle)  
            end_time = time.time()
            runtime = end_time - start_time
            runtimes.append(runtime)
            print(f"{strategy} Runtime: {runtime} seconds")
    
    print(strategies)
    print(runtimes)

    header = f"{'Strategy':<{len(longest_strategy_name)}} | Runtime (seconds) |"
    print("\n" + "-" * len(header))
    print(header)
    print("-" * len(header))

    for strategy, runtime in zip(strategies, runtimes):
        runtime_display = f"{runtime:.6f} seconds  |" if isinstance(runtime, float) else "NA                |"
        print(f"{strategy:<{len(longest_strategy_name)}} | {runtime_display}")

    print("-" * len(header))

    print("\nStrategies and Runtimes:")
    for strategy, runtime in zip(strategies, runtimes):
        if runtime == "NA":
            print(f"{strategy} Runtime: NA")
        else:
            print(f"{strategy} Runtime: {runtime} seconds")

    """
    #1
    plt.figure(figsize=(10, 6))

    adjusted_runtimes = []
    labels = []
    for i, runtime in enumerate(runtimes):
        if runtime == "NA":  
            adjusted_runtimes.append(0)  
            labels.append(f"{strategies[i]} (Timeout)")
        else:
            adjusted_runtimes.append(runtime)
            labels.append(strategies[i])

    bars = plt.bar(labels, adjusted_runtimes, color=['lightgray' if runtime == "NA" else 'skyblue' for runtime in runtimes])

    for bar, runtime in zip(bars, runtimes):
        if runtime == "NA":
            text = '> 30 seconds'
        else:
            text = f'{runtime:.4f}'  
        y_pos = bar.get_height() + max(adjusted_runtimes) * 0.02  
        plt.text(bar.get_x() + bar.get_width() / 2.0, y_pos, text, ha='center', va='bottom')

    plt.xlabel('Strategy')
    plt.ylabel('Runtime (seconds)')
    plt.title('Runtime for Each Strategy on a Puzzle')

    max_runtime = max(adjusted_runtimes, default=0)  
    plt.ylim(0, max_runtime * 1.2)  

    plt.grid(axis='y', linestyle='--', alpha=0.7)
    plt.show()
    """
    #2
    print("#2: 2x2 size different difficulty")
    strategy_names = ['dfs', 'bfs', 'dfs_with_pruning', 'bfs_with_pruning']  
    plot_runtime_vs_difficulty(strategies)

    
    
