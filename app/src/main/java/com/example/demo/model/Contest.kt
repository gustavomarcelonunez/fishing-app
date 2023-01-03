package com.example.demo.model

import com.example.demo.R

class Contest(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val featuredImage: Int,
    val termsAndConditions: String,
) {
    companion object {
        val data
            get() = listOf(
                Contest(
                    1,
                    "Pesca de Tiburones",
                    "Increíbles premios a la captura del tiburón más grande.",
                    "05/11/2025",
                    R.drawable.hilux,
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                ),
                Contest(
                    2,
                    "¡Gran premio en dólares!",
                    "Participe del torneo de pesca de cornalitos.",
                    "17/05/2024",
                    R.drawable.dolares,
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                ),
                Contest(
                    3,
                    "UNA TIRA DE ASADO!!!",
                    "Sorteamos una tira de asado entre los participantes que atrapen un pez con patas.",
                    "07/12/2023",
                    R.drawable.asado,
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                ),
            )
    }
}