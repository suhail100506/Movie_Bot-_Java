<img width="749" height="583" alt="image" src="https://github.com/user-attachments/assets/4557a3ea-f899-4876-b6af-6661bf45f1c9" />🚀Movie_Bot_Java

Design and develop a movie recommendation system in Python that allows users to discover movies based on their preferences such as genre, language, actor, or ratings. The system should analyze user inputs using recommendation algorithms (content-based, collaborative, or hybrid filtering) and suggest personalized movies. Importantly, it must provide only legal streaming or download links from authorized platforms using APIs.

🔴Functional Requirements
1. User Interaction
        ->Allow users to enter movie title, language, and release year.
        ->Support user registration and login to personalize recommendations.

2. Data Management
        ->Use open and legal movie datasets.
        ->Store user preferences, ratings, and history securely.

3. Recommendation Engine
        ->Implement collaborative filtering, content-based filtering, or hybrid recommendation algorithms.
        ->Generate personalized movie lists based on the analysis of user data.

4. Legal Movie Access
        ->Provide metadata with title, cast, genre, rating, and year.
        ->Display only legal streaming/download sources for movies.

5. System Interface
        ->Create a user-friendly interface to display recommendations and links.

🟠Tech Stack:
        ->Programming Language: Java JDK 21+ 
        ->Frontend (UI):  JavaFX 
        ->API Integration: TMDb API/ OMDb API
        ->HTTP Client: HttpURLConnection
        ->Database : MySQL for storing favorites & history
        ->Version Control: Git & GitHub

🔵OOP Concept:
        ->Encapsulation: Securely stores movie details in classes.
        ->Abstraction: Hides API integration details behind interfaces.
        ->Inheritance: Reuses code by extending Movie for specific types.
        ->Polymorphism: Displays different output depending on movie type.
        ->Composition: MovieSearchManager contains and uses MovieService.

🟡Setup & Installation:
Prerequisites:
        ->Java JDK 21+
        ->Maven
        ->API key from TMDb API.




