# Tokoro 🌟

 **Tokoro** is a web application designed to help users find interesting places to visit based on personalized queries. From cozy cafés to vibrant events, Tokoro ensures you'll never be bored in your city.

> ## 🔧 Tech Stack
>
> - **Backend**: Spring Framework (Java), MongoDB, REST API
> - **Frontend**: Next.js, TypeScript, Tailwind CSS
> - **Additional Tools**: Auth0 (authentication), AI integrations for personalized recommendations

[//]: # (> ## 🚀 Getting Started)

[//]: # (>)

[//]: # (> ### **Prerequisites**)

[//]: # (> 1. Install **Node.js** &#40;v16+&#41;, **npm** &#40;v7+&#41;, and **Java JDK** &#40;v17+&#41;.)

[//]: # (> 2. Set up a **MongoDB** database and an **Auth0 tenant**.)

[//]: # (>)

[//]: # (> ### **Installation**)

[//]: # (> - Clone the repository:)

[//]: # (>   ```bash)

[//]: # (>   git clone https://github.com/tokoro-koshi/tokoro.git)

[//]: # (>   cd tokoro)

[//]: # (>   ```)

[//]: # (> - Set up environment variables:)

[//]: # (>   - **Frontend &#40;`frontend/.env.local`&#41;**:)

[//]: # (>     ```env)

[//]: # (>     NEXT_PUBLIC_AUTH0_DOMAIN=your-auth0-domain)

[//]: # (>     NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api)

[//]: # (>     ```)

[//]: # (>   - **Backend &#40;`backend/.env`&#41;**:)

[//]: # (>     ```env)

[//]: # (>     SPRING_DATASOURCE_URL=mongodb://localhost:27017/tokoro)

[//]: # (>     AUTH0_DOMAIN=your-auth0-domain)

[//]: # (>     AUTH0_AUDIENCE=your-api-audience)

[//]: # (>     ```)

[//]: # (> - Install dependencies:)

[//]: # (>   - **Frontend**:)

[//]: # (>     ```bash)

[//]: # (>     cd frontend)

[//]: # (>     npm install)

[//]: # (>     ```)

[//]: # (>   - **Backend**:)

[//]: # (>     ```bash)

[//]: # (>     cd backend)

[//]: # (>     ./mvnw clean install)

[//]: # (>     ```)

[//]: # (>)

[//]: # (> ### **Running the Project**)

[//]: # (> - **Frontend**:)

[//]: # (>   ```bash)

[//]: # (>   cd frontend)

[//]: # (>   npm run dev)

[//]: # (>   ```)

[//]: # (>   Accessible at `http://localhost:3000`.)

[//]: # (> - **Backend**:)

[//]: # (>   ```bash)

[//]: # (>   cd backend)

[//]: # (>   ./mvnw spring-boot:run)

[//]: # (>   ```)

[//]: # (>   Accessible at `http://localhost:8080`.)

> ## 🗂 Project Structure
>
> ```
> tokoro/
> ├── backend/        # Spring Boot backend code
> ├── frontend/       # Next.js frontend code
> └── README.md       # Repository overview
> ```

> ## 📜 Roadmap
>
> ### Current Focus
> - **Frontend**: Landing Page, About Us, Best Places, AI Prompt Page
> - **Backend**: Database setup, AI functionality, Hashtag and User REST API
>
> ### Future Plans
> - REST APIs for articles and places management
> - Pages for hashtags, places, blogs, and saved prompts

> ## 📜 License
>
> This project is licensed under MIT License.

Thank you for exploring Tokoro! 🌍✨
