package com.example.studyapp.data

import com.example.studyapp.models.Quiz
import com.example.studyapp.models.Question

object QuizData {
    val subjectQuizzes = mapOf(
        "Mathematics" to Quiz(
            "Mathematics",
            listOf(
                Question("What is the LCM of 12 and 18?", listOf("6", "24", "36", "72"), 2, "12=2²x3, 18=2x3². LCM is 2²x3²=36."),
                Question("Solve x² - 5x + 6 = 0 for x.", listOf("2, 3", "-2, -3", "1, 6", "-1, -6"), 0, "Factoring gives (x-2)(x-3)=0, so x=2 or x=3."),
                Question("What is the area of a circle with radius 7cm? (Use π=22/7)", listOf("44 cm²", "154 cm²", "49 cm²", "14 cm²"), 1, "Area = πr² = 22/7 * 7 * 7 = 154.")
            )
        ),
        "Biology" to Quiz(
            "Biology",
            listOf(
                Question("Which organelle is known as the powerhouse of the cell?", listOf("Nucleus", "Ribosome", "Mitochondria", "Vacuole"), 2, "Mitochondria produce ATP, the energy currency of the cell."),
                Question("In photosynthesis, what gas is released as a byproduct?", listOf("Carbon Dioxide", "Oxygen", "Nitrogen", "Hydrogen"), 1, "6CO₂ + 6H₂O + light -> C₆H₁₂O₆ + 6O₂."),
                Question("Which kingdom do mushrooms belong to?", listOf("Plantae", "Animalia", "Fungi", "Protista"), 2, "Mushrooms are fungi.")
            )
        ),
        "Physics" to Quiz(
            "Physics",
            listOf(
                Question("Calculate the force needed to accelerate a 1000kg car at 2m/s².", listOf("500 N", "1000 N", "2000 N", "200 N"), 2, "F = ma = 1000 * 2 = 2000 N."),
                Question("What is Ohm's Law formula?", listOf("V = I/R", "V = IR", "P = IV", "F = ma"), 1, "V = IR relates Voltage, Current, and Resistance."),
                Question("What is the unit of power?", listOf("Joule", "Watt", "Newton", "Pascal"), 1, "The SI unit of power is the Watt (W).")
            )
        ),
        "Chemistry" to Quiz(
            "Chemistry",
            listOf(
                Question("What are the subatomic particles found in the nucleus?", listOf("Electrons and Protons", "Protons and Neutrons", "Electrons and Neutrons", "Only Protons"), 1, "The nucleus contains protons and neutrons."),
                Question("What is the product of Acid + Base?", listOf("Salt + Water", "Gas + Water", "Salt + Gas", "Only Water"), 0, "This is a neutralization reaction."),
                Question("Which group in the periodic table contains Alkali Metals?", listOf("Group 1", "Group 2", "Group 17", "Group 18"), 0, "Group 1 elements (except H) are alkali metals.")
            )
        ),
        "English" to Quiz(
            "English",
            listOf(
                Question("Identify the abstract noun in: 'Her courage saved the day.'", listOf("Her", "courage", "saved", "day"), 1, "Courage is an abstract noun as it refers to a quality."),
                Question("What is the plural form of 'Child'?", listOf("Childs", "Childrens", "Children", "Childes"), 2, "Children is the irregular plural form of child."),
                Question("Which of these is a formal letter closing?", listOf("Cheers", "Yours faithfully", "Best", "Later"), 1, "'Yours faithfully' is used when the recipient's name is unknown.")
            )
        ),
        "Kiswahili" to Quiz(
            "Kiswahili",
            listOf(
                Question("Neno 'Mtu' liko katika ngeli gani?", listOf("KI-VI", "A-WA", "LI-YA", "U-I"), 1, "Majina ya watu na wanyama huwa katika ngeli ya A-WA."),
                Question("Uwingo wa neno 'Kiti' ni nini?", listOf("Viti", "Mkiti", "Kiti", "Makiti"), 0, "Kiti (umoja) -> Viti (uwingo) katika ngeli ya KI-VI."),
                Question("Kamilisha methali: 'Haba na haba...'", listOf("...hujaza ndoo", "...hujaza kibaba", "...huisha upesi", "...hukua sana"), 1, "Methali kamili ni 'Haba na haba hujaza kibaba'.")
            )
        ),
        "History" to Quiz(
            "History",
            listOf(
                Question("Who is often referred to as 'Handy Man' in human evolution?", listOf("Homo Erectus", "Homo Sapiens", "Homo Habilis", "Australopithecus"), 2, "Homo Habilis means 'Handy Man' because they were the first to make tools."),
                Question("Which theory suggests that humans originated from a single creator?", listOf("Evolution Theory", "Creation Theory", "Big Bang Theory", "Migration Theory"), 1, "Creation theory is based on religious beliefs of a divine creator."),
                Question("How can one become a citizen of Kenya by birth?", listOf("If one parent is Kenyan", "By living in Kenya for 10 years", "By applying", "By marrying a Kenyan"), 0, "Citizenship by birth is granted if at least one parent is a citizen.")
            )
        ),
        "Geography" to Quiz(
            "Geography",
            listOf(
                Question("Which is the largest planet in our solar system?", listOf("Earth", "Mars", "Jupiter", "Saturn"), 2, "Jupiter is the largest planet."),
                Question("What instrument is used to measure atmospheric pressure?", listOf("Thermometer", "Hygrometer", "Barometer", "Anemometer"), 2, "A Barometer measures atmospheric pressure."),
                Question("What causes day and night on Earth?", listOf("Revolution", "Rotation", "Orbit", "Gravity"), 1, "The rotation of the Earth on its axis causes day and night.")
            )
        ),
        "CRE" to Quiz(
            "CRE",
            listOf(
                Question("On which day did God create light?", listOf("Day 1", "Day 2", "Day 3", "Day 4"), 0, "Genesis 1:3-5 says God created light on the first day."),
                Question("To whom did God give the Ten Commandments?", listOf("Abraham", "Moses", "Isaac", "Noah"), 1, "Moses received the Ten Commandments on Mount Sinai."),
                Question("What was the sign of the covenant between God and Abraham?", listOf("Rain", "Circumcision", "Sacrifice", "Bread"), 1, "Circumcision was the physical sign of the covenant with Abraham.")
            )
        ),
        "Business Studies" to Quiz(
            "Business Studies",
            listOf(
                Question("What is a basic human need?", listOf("Car", "Food", "Computer", "Television"), 1, "Food, shelter, and clothing are basic needs."),
                Question("What does 'S' stand for in SWOT analysis?", listOf("Sales", "Strategy", "Strengths", "Security"), 2, "SWOT stands for Strengths, Weaknesses, Opportunities, and Threats."),
                Question("Which of these is an example of an intangible service?", listOf("Bread", "Laptop", "Insurance", "Phone"), 2, "Insurance is an intangible service, unlike physical goods.")
            )
        )
    )
}
