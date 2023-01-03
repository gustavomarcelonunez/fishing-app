package com.example.demo.model

class Regulation (
    val id: Int,
    val title: String,
    val zone:  String,
    val description: String,
    ) {
        companion object {
            val data
                get() = listOf(
                    Regulation(
                        1,
                        "Pesca deportiva",
                        "Puerto Madryn",
                        "REGLAMENTO DE PESCA DEPORTIVA CONTINENTAL PATAGONICO 2021/2022",
                    ),
                    Regulation(
                        2,
                        "Pesca comercial",
                        "Comodoro Rivadavia",
                        "REGLAMENTO DE PESCA DEPORTIVA CONTINENTAL PATAGONICO 2021/2022",
                    ),
                    Regulation(
                        3,
                        "Pesca comercial",
                        "Trelew",
                        "REGLAMENTO DE PESCA DEPORTIVA CONTINENTAL PATAGONICO 2021/2022",
                    ),
                    Regulation(
                        4,
                        "Pesca turística",
                        "Cholila",
                        "REGLAMENTO DE PESCA DEPORTIVA CONTINENTAL PATAGONICO 2021/2022",
                    ),
                )
        }
    }