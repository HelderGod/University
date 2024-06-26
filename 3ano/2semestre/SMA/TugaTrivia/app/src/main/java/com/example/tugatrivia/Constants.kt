package com.example.tugatrivia

object Constants {

    const val USER_NAME: String = "user_name"
    const val TOTAL_QUESTIONS: String = "total_questions"
    const val CORRECT_ANSWERS: String = "correct_answers"
    const val SELECTED_DIFFICULTY: String = "selected_dificulty"

    fun getEasyQuestions(): ArrayList<Question> {

        val allQuestionsList = ArrayList<Question>()
        if (allQuestionsList.isEmpty()) {
            val que1 = Question(
                id = 1, question = "Quem foi o primeiro rei de Portugal?",
                R.drawable.ic_flag_portugal,
                optionOne = "Afonso I", optionTwo = "Dinis I",
                optionThree = "João I", optionFour = "Pedro I", correctAnswer = 1, tema = "HISTÓRIA"
            )
            allQuestionsList.add(que1)

            val que2 = Question(
                2, "Qual é o ponto mais alto de Portugal Continental?",
                R.drawable.ic_montanha,
                "Serra do Gerês", "Serra da Estrela",
                "Serra do Marão", "Ilha do Pico", 2, tema = "GEOGRAFIA"
            )
            allQuestionsList.add(que2)

            val que3 = Question(
                3, "Qual é o autor da obra \"Os Lusíadas\"?",
                R.drawable.ic_lusiadas,
                "José Saramago", "Eça de Queirós",
                "Luís de Camões", "Fernando Pessoa", 3, tema = "CULTURA"
            )
            allQuestionsList.add(que3)

            val que4 = Question(
                4, "Quantos campeonatos europeus de futebol a seleção portuguesa venceu até 2024?",
                R.drawable.ic_selecao,
                "0", "1",
                "2", "3", 2, tema = "DESPORTO"
            )
            allQuestionsList.add(que4)

            val que5 = Question(
                5, "Portugal venceu o Festival Eurovisão da Canção em que ano?",
                R.drawable.ic_salvador,
                "2005", "2010",
                "2017", "2021", 3, tema = "CULTURA"
            )
            allQuestionsList.add(que5)

            val que6 = Question(
                6, "Qual cidade portuguesa é conhecida pelo seu vinho?",
                R.drawable.ic_wine,
                "Braga", "Porto",
                "Lisboa", "Évora", 2, tema = "CULTURA"
            )
            allQuestionsList.add(que6)

            val que7 = Question(
                7, "Qual é o nome do tratado que dividiu o mundo entre Portugal e Espanha em 1494?",
                R.drawable.ic_tordesilhas,
                "Tratado de Tordesilhas", "Tratado de Windsor",
                "Tratado de Lisboa", "Tratado de Paris", 1, tema = "HISTÓRIA"
            )
            allQuestionsList.add(que7)

            val que8 = Question(
                8, "Qual foi a cidade portuguesa eleita a Capital Europeia da Cultura em 2027?",
                R.drawable.ic_europa,
                "Coimbra", "Braga",
                "Lisboa", "Évora", 4, tema = "CULTURA"
            )
            allQuestionsList.add(que8)

            val que9 = Question(
                9, "Em que ano se deu a Revolução dos Cravos?",
                R.drawable.ic_cravos,
                "1973", "1974",
                "1975", "1976", 2, tema = "HISTÓRIA"
            )
            allQuestionsList.add(que9)

            val que10 = Question(
                10, "Qual é o maior arquipélago de Portugal?",
                R.drawable.ic_arquipelagos,
                "Arquipélago dos Açores ", "Arquipélago da Madeira",
                "Ilhas Selvagens", "Ilhas Berlengas", 1, tema = "GEOGRAFIA"
            )
            allQuestionsList.add(que10)
        }
        return allQuestionsList
    }

    fun getHardQuestions(): ArrayList<Question> {

        val allQuestionsList = ArrayList<Question>()

        val que1 = Question(
            id = 1,
            question = "Qual é a estátua mais antiga de Portugal, datando da Idade do Bronze?",
            R.drawable.ic_flag_portugal,
            optionOne = "Estátua de D. Sebastião",
            optionTwo = "Estátua de Vímara Peres",
            optionThree = "Estátua de Guimarães",
            optionFour = "Estátua de Osório",
            correctAnswer = 2,
            tema = "CULTURA"
        )
        allQuestionsList.add(que1)

        val que2 = Question(
            2, "Em que ano terminou a monarquia em Portugal?",
            R.drawable.ic_monarquia,
            "1890", "1900",
            "1910", "1920", 3, tema = "HISTÓRIA"
        )
        allQuestionsList.add(que2)

        val que3 = Question(
            3,
            "Quem foi o navegador que liderou a primeira circum-navegação ao serviço de Espanha?",
            R.drawable.ic_navegacao,
            "Bartolomeu Dias",
            "Pedro Álvares Cabral",
            " Fernão de Magalhães",
            "Vasco da Gama",
            1,
            tema = "HISTÓRIA"
        )
        allQuestionsList.add(que3)

        val que4 = Question(
            4, "Que rio é o mais longo que corre inteiramente dentro do território de Portugal?",
            R.drawable.ic_rio,
            "Rio Douro", "Rio Tejo",
            "Rio Guadiana", "Rio Mondego", 4, tema = "GEOGRAFIA"
        )
        allQuestionsList.add(que4)

        val que5 = Question(
            5, "Quem foi o poeta português conhecido como \"o príncipe dos poetas\"?",
            R.drawable.ic_poetas,
            "Luís de Camões", "Fernando Pessoa",
            "Antero de Quental", "Florbela Espanca", 1, tema = "CULTURA"
        )
        allQuestionsList.add(que5)

        val que6 = Question(
            6, "Qual é o único parque nacional de Portugal continental?",
            R.drawable.ic_geres,
            "Serra da Estrela", "Peneda-Gerês",
            "Arrábida", "Mata de Albergaria", 2, tema = "GEOGRAFIA"
        )
        allQuestionsList.add(que6)

        val que7 = Question(
            7, "Que cidade portuguesa é famosa pelas suas tradicionais casas de cerâmica colorida?",
            R.drawable.ic_costa,
            "Aveiro", "Óbidos",
            "Sintra", "Costa Nova", 1, tema = "CULTURA"
        )
        allQuestionsList.add(que7)

        val que8 = Question(
            8, "Em que ano a seleção nacional de portugal de futsal se sagrou campeâ mundial?",
            R.drawable.ic_futsal,
            "2010", "2015",
            "2019", "2021", 4, tema = "DESPORTO"
        )
        allQuestionsList.add(que8)

        val que9 = Question(
            9,
            "Qual foi o primeiro clube português a ganhar uma competição europeia de clubes da UEFA?",
            R.drawable.ic_uefa,
            "FC Porto",
            "Sporting CP",
            "SL Benfica",
            "Boavista FC",
            3,
            tema = "DESPORTO"
        )
        allQuestionsList.add(que9)

        val que10 = Question(
            10,
            "Qual foi a batalha decisiva que consolidou a independência de Portugal no século XII?",
            R.drawable.ic_batalha,
            "Batalha de Aljubarrota",
            "Batalha de Ourique",
            "Batalha do Salado",
            "Batalha de Toro",
            2,
            tema = "HISTÓRIA"
        )
        allQuestionsList.add(que10)

        return allQuestionsList
    }
}
