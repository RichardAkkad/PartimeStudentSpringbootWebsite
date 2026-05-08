Website for Part-Time Students
HTML | CSS | Spring Boot | AWS EC2 & S3 | PostgreSQL
As a part-time evening student at Birkbeck University, I felt disconnected from the traditional university experience, particularly when it came to finding accommodation and meeting people on the same course. To solve this, I independently designed and built a full-stack web application to help students find compatible housemates and accommodation.
The core features allow students to search free availability slots, find others with similar budgets and courses they could potentially live with, with results filtered to surface the most compatible matches. Students can then contact those people directly to arrange viewings, as well as track their own progress through the platform.
The application includes full CRUD functionality — students can add (including uploading photos), update, and delete their profiles. It also includes strong Spring Security protocols with custom authentication, role-based access control to restrict sensitive operations, and OAuth2 login for Google and GitHub. Student profile images and data are stored using AWS S3, with the project deployed on an AWS EC2 instance.
Live Demo: http://ec2-13-40-31-169.eu-west-2.compute.amazonaws.com:5000
Login Credentials:

Student access — Username: tony Password: tony
Employee access — Username: james Password: james
Or login directly via Google or GitHub for Student access

What I Learnt & Next Steps
Building this project gave me a much deeper understanding of full-stack development end to end — from designing a relational database in PostgreSQL, to securing an application with Spring Security, to deploying on AWS. My primary interest has always been back-end development, and this project reinforced that — I found the most satisfaction in building the authentication system, managing server-side logic, and configuring the AWS infrastructure.
On the front end, the application was built with plain HTML and CSS which gets the job done but is limited visually. A natural next step would be to rebuild the front end using React, which would make the application far more dynamic and visually appealing — for example, filtering and displaying compatible student matches in real time without page reloads. However, as my focus and passion lies firmly in back-end development, this would be more of a nice-to-have improvement rather than a core priority.
