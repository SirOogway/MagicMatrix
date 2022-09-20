package com.innova.magicmatrix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.children

var rb3x3: RadioButton? = null
var rb5x5: RadioButton? = null
var rb7x7: RadioButton? = null
var sizeMatrix: Int? = null

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var tvResutl = findViewById<TextView>(R.id.tvResult)
        var btnCalculate = findViewById<Button>(R.id.button)
        var radioGroup = findViewById<RadioGroup?>(R.id.radioGroup)
        rb3x3 = findViewById(R.id.rb3x3)
        rb5x5 = findViewById(R.id.rb5x5)
        rb7x7 = findViewById(R.id.rb7x7)
        btnCalculate.setOnClickListener {
            tvResutl.text = ""
            when (radioGroup?.checkedRadioButtonId) {
                rb5x5?.id -> sizeMatrix = 5
                rb7x7?.id -> sizeMatrix = 7
                rb3x3?.id -> sizeMatrix = 3
                else -> Toast.makeText(this, "Choose a Matriz Size", Toast.LENGTH_SHORT).show()
            }

            if (sizeMatrix != null) {
                var sizeHelperMatrix = sizeMatrix!! * 2 - 1
                var helperMatrix =
                    Array(sizeHelperMatrix) { Array<Int?>(sizeHelperMatrix) { null } }
                fillHelperMatrix(helperMatrix)
                for (row in helperMatrix.indices) {
                    tvResutl.text = "${tvResutl.text} \n"
                    for (arg in helperMatrix[row]) {
                        if (arg != 0 && arg != null && arg < 10) tvResutl.text =
                            "${tvResutl.text}   $arg  "
                        if (arg != 0 && arg != null && arg >= 10) tvResutl.text =
                            "${tvResutl.text} $arg  "
                    }
                }
            }
        }
    }

    fun fillHelperMatrix(matrix: Array<Array<Int?>>) {
        activeCells(matrix)
        fillDiagonal(matrix)
        fillInside(matrix)
        organiceValues(matrix)
    }

    fun activeCells(matrix: Array<Array<Int?>>) {
        val halfIndex = matrix.lastIndex / 2
        var startCell = halfIndex
        var endCell = halfIndex

        for (row in matrix.indices) {
            matrix[row].mapIndexed { column, _ ->
                matrix[row][column] = if (column in startCell..endCell) 0 else null
            }
            if (row < halfIndex) {
                startCell--
                endCell++
            } else {
                startCell++
                endCell--
            }
        }
    }

    fun fillDiagonal(matrix: Array<Array<Int?>>) {
        var diagonalValue = 1
        val matrixSize = (matrix.size + 1) / 2
        var prevColumnPos = matrix.lastIndex / 2
        var nextColumPos = 1
        val originalMatrixLength = matrixSize - 1

//    println("matrix Size $matrixSize")
//    println("steps $matrixSize")

        for (row in matrix.indices) {
            if (row < matrixSize) {
                matrix[row][prevColumnPos] = diagonalValue
                prevColumnPos--
            } else {
                diagonalValue += originalMatrixLength
                matrix[row][nextColumPos] = diagonalValue
                nextColumPos++
            }
            diagonalValue++
        }
    }

    fun fillInside(matrix: Array<Array<Int?>>) {
        val steps = matrix.lastIndex / 2
        var value = 0
        for (row in matrix.indices) { // .filter {row%2 == 0}.mapIndexded { llenado}
            for ((column, arg) in matrix[row].withIndex()) {
                if (arg != 0 && arg != null) value = arg
                if (arg == 0 && column % 2 == 0 && row % 2 == 0) {
                    value += steps
                    matrix[row][column] = value
                } else if (arg == 0 && column % 2 != 0 && row % 2 != 0) {
                    value += steps
                    matrix[row][column] = value
                }
            }
        }
    }

    fun organiceValues(matrix: Array<Array<Int?>>) {
        val startCell = Math.floorDiv(((matrix.size + 1) / 2), 2)
        val endCell = matrix.lastIndex - startCell

//    println("startCell $startCell endCell $endCell")
        for (row in matrix.indices) {

            for ((column, arg) in matrix[row].withIndex()) {
//           izquierda a derecha
                if (column !in startCell..endCell && arg != 0 &&
                    arg != null && column < matrix.lastIndex / 2
                ) {
                    if (matrix[row][endCell]!! == 0) matrix[row][endCell] = arg
                    else {
                        if (matrix[row][endCell - 1]!! == 0) matrix[row][endCell - 1] = arg
                        if (matrix[row][endCell - 2]!! == 0) matrix[row][endCell - 2] = arg
                    }
                    matrix[row][column] = null
                }
//             Derecha a izquierda
                if (column !in startCell..endCell && arg != 0 &&
                    arg != null && column > matrix.lastIndex / 2
                ) {
                    if (matrix[row][startCell]!! == 0) matrix[row][startCell] = arg
                    else {
                        if (matrix[row][startCell + 1]!! == 0) matrix[row][startCell + 1] = arg
                        if (matrix[row][startCell + 2]!! == 0) matrix[row][startCell + 2] = arg
                    }
                    matrix[row][column] = null
                }
//            Arriba hacia abajo
                if (row !in startCell..endCell && arg != 0 &&
                    arg != null && row < matrix.lastIndex / 2
                ) {
                    var nextRow: Int = matrix.lastIndex / 2 + 1
                    var asignado: Boolean = false
                    if (row == 2) {
                        matrix[endCell][column] = arg
                    } else {
                        while (!asignado) {
                            if (matrix[nextRow][column] == 0) {
                                matrix[nextRow][column] = arg
                                asignado = true
                            } else nextRow++
                        }
                    }
                    matrix[row][column] = null
                }
                //Abajo hacia arriba
                if (row !in startCell..endCell && arg != 0 &&
                    arg != null && row > matrix.lastIndex / 2
                ) {
                    var prevRow: Int = matrix.lastIndex / 2 - 1
                    var asignado: Boolean = false
                    if (row == endCell + 1) {
                        matrix[startCell][column] = arg
                    } else {
                        while (!asignado) {
                            if (matrix[prevRow][column] == 0) {
                                matrix[prevRow][column] = arg
                                asignado = true
                            } else prevRow--
                        }
                    }
                    matrix[row][column] = null
                }
                if (column !in startCell..endCell && arg == 0) matrix[row][column] = null
            }
        }
    }
}