package connectfour

import kotlin.system.exitProcess

fun main() {
    println("Connect Four")
    println("First player's name:")
    val firstPlayer = readln()
    println("Second player's name:")
    val secondPlayer = readln()
    var rows = 6
    var columns = 7
    val boardSize = 5..9
    do {
        var inputIsValid = false
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        val inputString = readln()
        if (inputString.isEmpty()) {
            inputIsValid = true
            continue
        }
        try {
            val inputList = inputString.lowercase().split(Regex(" ?x ?")).map { it.trim() }
            rows = inputList.first().toInt()
            columns = inputList.last().toInt()
        } catch (e: Exception) {
            println("Invalid input")
            continue
        }
        if (rows !in boardSize) {
            println("Board rows should be from 5 to 9")
            continue
        }
        if (columns !in boardSize) {
            println("Board columns should be from 5 to 9")
            continue
        }
        inputIsValid = true
    } while (!inputIsValid)
    
    var numberOfGames = 1
    do {
        var inputIsOK = false
        println("Do you want to play single or multiple games?")
        println("For a single game, input 1 or press Enter")
        println("Input a number of games:")
        val input = readln()
        if (input.isEmpty()) {
            numberOfGames = 1
            inputIsOK = true
            continue
        }
        try {
            numberOfGames = input.toInt()
            if (numberOfGames < 1) {
                println("Invalid input")
            } else inputIsOK = true
        } catch (e: Exception) {
            println("Invalid input")
        }
    } while (!inputIsOK)

    val singleGame = (numberOfGames == 1)
    println("$firstPlayer VS $secondPlayer")
    println("$rows X $columns board")
    println(
        if (singleGame) "Single game" else "Total $numberOfGames games"
    )

    var scorePlayer1 = 0
    var scorePlayer2 = 0
    var turnIsFirst = true

    for (gameNumber in 1..numberOfGames) {
        val board = MutableList(rows) { MutableList(columns) { 0 } }
        if (!singleGame) println("Game #$gameNumber")
        printBoard(board)
        do {
            println(
                "${if (turnIsFirst) firstPlayer else secondPlayer}'s turn:"
            )
            val inputColNumber = readln()
            var end = (inputColNumber == "end")
            if (end) {
                print("Game over!")
                exitProcess(0)
            }
            var columnNumber: Int
            try {
                columnNumber = inputColNumber.toInt()
            } catch (e: NumberFormatException) {
                println("Incorrect column number")
                continue
            }
            if (columnNumber - 1 !in board[0].indices) {
                println("The column number is out of range (1 - $columns)")
                continue
            }

            var i = 0
            var nextTurn = true
            do {
                var stop = false
                if (board[i][columnNumber - 1] != 0) {
                    i++
                    if (i == rows) {
                        println("Column $columnNumber is full")
                        nextTurn = false
                        stop = true
                    }
                } else {
                    board[i][columnNumber - 1] = if (turnIsFirst) 1 else -1
                    stop = true
                }
            } while (!stop)
            if (nextTurn) {
                printBoard(board)
                if (checkWin(board, i, columnNumber - 1)) {
                    println("Player ${if (turnIsFirst) firstPlayer else secondPlayer} won")
                    if (turnIsFirst) scorePlayer1 += 2 else scorePlayer2 += 2
                    end = true
                } else if (draw(board)) {
                    println("It is a draw")
                    scorePlayer1++
                    scorePlayer2++
                    end = true
                }
                turnIsFirst = !turnIsFirst
            }
        } while (!end)
        if (!singleGame) {
            println("Score")
            println("$firstPlayer: $scorePlayer1 $secondPlayer: $scorePlayer2")
        }
    }
    print("Game over!")
}

fun printBoard(board: MutableList<MutableList<Int>>) {
    for (i in 1..board[0].size) {
        print(" $i")
    }
    println()
    for (r in board.size - 1 downTo 0) {
        for (c in board[r].indices) {
            print("║")
            print(
                when (board[r][c]) {
                    1 -> 'o'
                    -1 -> '*'
                    else -> ' '
                }
            )
        }
        println("║")
    }
    print('╚')
    repeat(board[0].size - 1) {
        print("═╩")
    }
    println("═╝")
}

fun checkWin(board: MutableList<MutableList<Int>>, row: Int, column: Int): Boolean {
    var sum = 1
    var i = 1
    while (row - i >= 0 && board[row - i][column] == board[row][column]) {
        sum++
        i++
    }
    i = 1
    while (row + i < board.size - 1 && board[row + i][column] == board[row][column]) {
        sum++
        i++
    }
    if (sum > 3) return true

    sum = 1
    i = 1
    while (column - i >= 0 && board[row][column - i] == board[row][column]) {
        sum++
        i++
    }
    i = 1
    while (column + i <= board[0].size - 1 && board[row][column + i] == board[row][column]) {
        sum++
        i++
    }
    if (sum > 3) return true

    sum = 1
    i = 1
    while (column - i >= 0 && row - i >= 0 && board[row - i][column - i] == board[row][column]) {
        sum++
        i++
    }
    i = 1
    while (column + i <= board[0].size - 1 && row + i < board.size - 1 && board[row + i][column + i] == board[row][column]) {
        sum++
        i++
    }
    if (sum > 3) return true

    sum = 1
    i = 1
    while (column - i >= 0 && row + i < board.size - 1 && board[row + i][column - i] == board[row][column]) {
        sum++
        i++
    }
    i = 1
    while (column + i < board[0].size - 1 && row - i >= 0 && board[row - i][column + i] == board[row][column]) {
        sum++
        i++
    }
    if (sum > 3) return true

    return false
}

fun draw(board: MutableList<MutableList<Int>>): Boolean {
    for (row in board) {
        if (0 in row) return false
    }
    return true
}