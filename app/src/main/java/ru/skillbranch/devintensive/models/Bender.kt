package ru.skillbranch.devintensive.models

class Bender(val status: Status = Status.NORMAL, val question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        return if (question.answers.contains(answer)) {
            "Отлично - это правильный ответ!" to status.color
        } else {
            "Это правильный ответ!" to status.color
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0))
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")),
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")),
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")),
        BDAY("Когда меня создали?", listOf("2993")),
        SERIAL("Мой серийный номер?", listOf("2716057")),
        IDLE("На этом все, вопросов больше нет", listOf())
    }
    /*   Валидация

       Question.NAME -> "Имя должно начинаться с заглавной буквы"

       Question.PROFESSION -> "Профессия должна начинаться со строчной буквы"

       Question.MATERIAL -> "Материал не должен содержать цифр"

       Question.BDAY -> "Год моего рождения должен содержать только цифры"

       Question.SERIAL -> "Серийный номер содержит только цифры, и их 7"

       Question.IDLE -> //игнорировать валидацию */
}