package com.example.studyapp.data

import com.example.studyapp.models.LibrarySubject
import com.example.studyapp.models.Topic

object LibraryData {
    val secondarySubjects = listOf(
        LibrarySubject(
            name = "Mathematics",
            icon = "➗",
            description = "Algebra, Geometry, Calculus and more.",
            topics = listOf(
                Topic(
                    "Numbers",
                    "Notes on Numbers:\n\n1. Natural Numbers: 1, 2, 3...\n2. Whole Numbers: 0, 1, 2...\n3. Integers: ...-2, -1, 0, 1, 2...\n4. Rational Numbers: Expressed as p/q where q ≠ 0.\n5. Irrational Numbers: Cannot be expressed as p/q (e.g., √2, π).\n\n--- Workout: HCF & LCM ---\nExample: Find HCF and LCM of 12 and 18.\n1. Prime Factorization:\n   12 = 2² × 3\n   18 = 2 × 3²\n2. HCF (Lowest powers): 2¹ × 3¹ = 6\n3. LCM (Highest powers): 2² × 3² = 36\n\n--- Diagram: Number Sets ---\n[ Real Numbers [ Rational [ Integers [ Whole [ Natural ] ] ] ] ]",
                    listOf("Number Classification", "BODMAS", "HCF & LCM")
                ),
                Topic(
                    "Algebra",
                    "Algebra Fundamentals:\n\nQuadratic Equations: ax² + bx + c = 0\nQuadratic Formula: x = [-b ± √(b² - 4ac)] / 2a\n\n--- Workout: Quadratic Equation ---\nSolve: x² - 5x + 6 = 0\na=1, b=-5, c=6\n1. Discriminant (D) = b² - 4ac = (-5)² - 4(1)(6) = 25 - 24 = 1\n2. x = [-(-5) ± √1] / 2(1)\n3. x = (5 + 1)/2 = 3 OR x = (5 - 1)/2 = 2\nRoots are {2, 3}",
                    listOf("Linear Equations", "Expansion", "Factorization")
                ),
                Topic(
                    "Geometry",
                    "Pythagoras Theorem: a² + b² = c² (for right-angled triangles)\n\n--- Diagram: Right Triangle ---\n      /|\n   c / | a\n    /  |\n   /___|\n     b\n\n--- Workout: Area of Circle ---\nFind area of circle with radius 7cm (π = 22/7).\nArea = πr²\nArea = (22/7) × 7 × 7\nArea = 22 × 7 = 154 cm²",
                    listOf("Angles", "Triangles", "Circles")
                ),
                Topic(
                    "Statistics",
                    "Measures of Central Tendency:\n- Mean: Sum of values / Number of values\n- Median: Middle value when arranged in order\n- Mode: Most frequent value\n\n--- Workout: Mean & Median ---\nData: 4, 8, 6, 10, 12\n1. Mean = (4+8+6+10+12) / 5 = 40 / 5 = 8\n2. Median: Order -> 4, 6, 8, 10, 12. Middle = 8\n3. Mode: None (all values appear once)",
                    listOf("Mean", "Median", "Mode")
                )
            )
        ),
        LibrarySubject(
            name = "Biology",
            icon = "🧬",
            description = "Study of living organisms and life processes.",
            topics = listOf(
                Topic(
                    "The Cell",
                    "Cell Biology:\n\n--- Diagram: Animal Cell ---\n   _______ \n  /       \\  <- Cell Membrane\n |   ( )   | <- Nucleus\n |    ~    | <- Mitochondria\n  \\_______/ \n\nKey Organelles:\n1. Nucleus: Contains genetic material (DNA).\n2. Mitochondria: Site of ATP (energy) production.\n3. Ribosomes: Protein synthesis.\n4. Vacuole: Storage of water/food (large in plants).",
                    listOf("Cell Structure", "Organelles", "Plant vs Animal Cells")
                ),
                Topic(
                    "Nutrition",
                    "Photosynthesis Equation:\n6CO₂ + 6H₂O + Light → C₆H₁₂O₆ + 6O₂\n\n--- Diagram: Leaf Structure ---\n  _________________ <- Upper Epidermis\n |  |  |  |  |  |  | <- Palisade Mesophyll (Chloroplasts)\n |_________________| \n |  o   o   o   o  | <- Spongy Mesophyll\n |_________________| <- Lower Epidermis",
                    listOf("Photosynthesis", "Digestion", "Enzymes")
                ),
                Topic(
                    "Classification",
                    "Grouping organisms based on shared characteristics.\n\n--- Diagram: Dichotomous Key ---\n1. (a) Plant has flowers ....... Go to 2\n   (b) Plant has no flowers .... Go to 3\n2. (a) Leaves are broad ........ Hibiscus\n   (b) Leaves are narrow ....... Grass",
                    listOf("Taxonomy", "Kingdoms", "Keys")
                )
            )
        ),
        LibrarySubject(
            name = "Physics",
            icon = "🍎",
            description = "Matter, energy, and the fundamental forces of nature.",
            topics = listOf(
                Topic(
                    "Force",
                    "Newton's Second Law: F = ma\n(Force = mass × acceleration)\n\n--- Workout: Force calculation ---\nA car of mass 1000kg accelerates at 2m/s². Find the force.\nF = 1000 × 2 = 2000 Newtons (N).\n\n--- Diagram: Resultant Force ---\n     [ 500kg ] ---> 10N\n  2N <--- [       ] \n  Resultant = 10N - 2N = 8N to the right.",
                    listOf("Types of Force", "Newton's Laws", "Weight & Mass")
                ),
                Topic(
                    "Pressure",
                    "Pressure in Liquids: P = hρg\nh = depth, ρ = density, g = gravity\n\n--- Diagram: Pressure with Depth ---\n |   .   | \n |  ...  | <- Low Pressure\n | ..... | \n |.......| <- High Pressure\n --------",
                    listOf("Pressure in Solids", "Liquid Pressure", "Atmospheric Pressure")
                ),
                Topic(
                    "Electricity",
                    "Ohm's Law: V = IR\nV = Voltage (V), I = Current (A), R = Resistance (Ω)\n\n--- Diagram: Simple Circuit ---\n  [ Battery ]----( Switch )----\n       |                  |\n       -------[ Lamp ]----- \n\n--- Workout: Current Calculation ---\nA 12V battery is connected to a 4Ω resistor. Find the current.\nI = V / R = 12 / 4 = 3 Amperes (A).",
                    listOf("Current", "Voltage", "Resistance")
                )
            )
        ),
        LibrarySubject(
            name = "Chemistry",
            icon = "🧪",
            description = "Properties, composition, and structure of substances.",
            topics = listOf(
                Topic(
                    "Structure of Atom",
                    "Atomic Structure:\n- Protons (+): In nucleus\n- Neutrons (0): In nucleus\n- Electrons (-): In shells\n\n--- Diagram: Helium Atom ---\n      _--_   (Electrons in orbit)\n    /      \\\n   |  (pn)  |  (Protons & Neutrons in Nucleus)\n    \\      /\n      ----",
                    listOf("Atomic Number", "Mass Number", "Isotopes")
                ),
                Topic(
                    "The Periodic Table",
                    "Arrangement of elements by atomic number.\nGroups: Vertical columns (similar chemical properties)\nPeriods: Horizontal rows (increasing electron shells)\n\n--- Diagram: Group 1 (Alkali Metals) ---\n [ Li ] \n [ Na ] \n [ K  ] ",
                    listOf("Groups", "Periods", "Metals")
                ),
                Topic(
                    "Acids and Bases",
                    "The pH Scale:\n0 --- 7 --- 14\nAcid  Neutral Base\n\n--- Workout: Neutralization ---\nHCl + NaOH → NaCl + H₂O\n(Acid + Base → Salt + Water)",
                    listOf("Properties", "pH Scale", "Neutralization")
                )
            )
        ),
        LibrarySubject(
            name = "English",
            icon = "📚",
            description = "Grammar, literature, and communication skills.",
            topics = listOf(
                Topic(
                    "Parts of Speech",
                    "Nouns: Common (dog), Proper (London), Abstract (love).\nVerbs: Action (run), Stative (believe).\nAdjectives: Describe nouns (blue, large).",
                    listOf("Nouns", "Verbs", "Adjectives & Adverbs")
                )
            )
        ),
        LibrarySubject(
            name = "Kiswahili",
            icon = "🇰🇪",
            description = "Sarufi, fasihi na insha.",
            topics = listOf(
                Topic(
                    "Ngeli",
                    "A-WA: Mtu - Watu, Mwalimu - Walimu\nKI-VI: Kiti - Viti, Kitabu - Vitabu\nLI-YA: Jicho - Macho, Tunda - Matunda",
                    listOf("Ngeli", "Aina za Maneno", "Viambishi")
                )
            )
        ),
        LibrarySubject(
            name = "History",
            icon = "🏛️",
            description = "Past events and the evolution of human societies.",
            topics = listOf(
                Topic(
                    "Early Man",
                    "Stages of Evolution:\n1. Aegyptopithecus (earliest)\n2. Dryopithecus\n3. Ramapithecus\n4. Australopithecus (Southern Ape)\n5. Homo Habilis (Handy Man)\n6. Homo Erectus (Upright Man)\n7. Homo Sapiens (Modern Man)",
                    listOf("Evolution", "Stone Age", "Discoveries")
                )
            )
        ),
        LibrarySubject(
            name = "Geography",
            icon = "🌍",
            description = "The Earth's landscapes, peoples, and environments.",
            topics = listOf(
                Topic(
                    "Solar System",
                    "The Planets (Order from Sun):\n1. Mercury\n2. Venus\n3. Earth\n4. Mars\n5. Jupiter\n6. Saturn\n7. Uranus\n8. Neptune\n\n--- Diagram: Orbit ---\n     O (Sun)\n      )  . (Planet)",
                    listOf("Solar System", "Movements", "Structure")
                ),
                Topic(
                    "Earth Structure",
                    "Internal structure of the Earth:\n\n--- Diagram: Earth Cross-section ---\n ( ( ( @ ) ) )\n   ^   ^   ^ \n   |   |   Core (Inner & Outer)\n   |   Mantle\n   Crust",
                    listOf("Crust", "Mantle", "Core")
                )
            )
        ),
        LibrarySubject(
            name = "CRE",
            icon = "🙏",
            description = "Christian Religious Education and values.",
            topics = listOf(
                Topic(
                    "Creation",
                    "Genesis 1: Six days of creation.\nDay 1: Light and Darkness\nDay 2: Sky/Firmament\nDay 3: Land, Sea, Vegetation\nDay 4: Sun, Moon, Stars\nDay 5: Birds and Fish\nDay 6: Animals and Man",
                    listOf("Creation", "Stewardship", "The Fall")
                )
            )
        ),
        LibrarySubject(
            name = "Business Studies",
            icon = "💼",
            description = "Economics, accounting, and commerce.",
            topics = listOf(
                Topic(
                    "Business Environment",
                    "SWOT Analysis:\n- Strengths (Internal)\n- Weaknesses (Internal)\n- Opportunities (External)\n- Threats (External)",
                    listOf("Internal Factors", "External Factors", "SWOT")
                )
            )
        )
    )
}
