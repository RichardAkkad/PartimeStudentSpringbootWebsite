Website for Part-Time Students

HTML | CSS | Spring Boot | AWS EC2 & S3 | PostgreSQL

As a part-time evening student at Birkbeck University studying a Bsc in Maths and Statistics, I felt disconnected from the traditional university experience, particularly when it came to finding accommodation and meeting people on the same course. To solve this, I independently designed and built a full-stack web application to help students find compatible housemates and accommodation.
The core features allow students to search free availability slots, find others with similar budgets and courses they could potentially live with, with results filtered to surface the most compatible matches. Students can then contact those people directly to arrange viewings, as well as track their own progress through the platform.
The application includes full CRUD functionality — students can add (including uploading photos), update, and delete their profiles. It also includes strong Spring Security protocols with custom authentication, role-based access control to restrict sensitive operations, and OAuth2 login for Google and GitHub. Student profile images and data are stored using AWS S3, with the project deployed on an AWS EC2 instance.

Live Demo: http://ec2-13-40-31-169.eu-west-2.compute.amazonaws.com:5000
Login Credentials:

Student access — Username: tony Password: tony

Employee access — Username: james Password: james

Or login directly via Google or GitHub for Student access

What I Learnt & Next Steps

It was a massive challenge deploying a website as I had to first learn a second language being Java then learn Spring Boot following by AWS, PostgreSql, Html, Css, Linux and Github. Building this project gave me a much deeper understanding of full-stack development end to end — from designing a relational database in PostgreSQL, to securing an application with Spring Security, to deploying on AWS. My primary interest has always been back-end development, and this project reinforced that — I found the most satisfaction in building the authentication system, managing server-side logic, and configuring the AWS infrastructure.

On the front end, the application was built with plain HTML and CSS which gets the job done but is limited visually. A natural next step would be to rebuild the front end using React, which would make the application far more dynamic and visually appealing — for example, filtering and displaying compatible student matches in real time without page reloads. However, as my focus and passion lies firmly in back-end development, this would be more of a nice-to-have improvement rather than a core priority. In a future a project I would implement JWT (JSON Web Token) based authentication rather than session-based authentication. JWT is stateless and more scalable, making it better suited for REST APIs and modern applications where the front end and back end are decoupled, however session-based was a good starter for me in this project.

This was by far the most challenging project I have undertaken. It required me to learn Java and Spring Boot from scratch, alongside a range of AWS concepts including EC2, S3, Elastic Beanstalk and security groups. I also had to develop a working knowledge of Linux and Git in order to manage and deploy the project effectively.
Although my background is in mathematics and statistics, I wanted to push myself beyond theoretical knowledge and learn how to actually build, deploy and put a full application together end to end. Taking a project from an idea all the way through to a live deployed application accessible on the web was something I was determined to achieve, and this project proved that I could do that.
The most demanding aspect was Spring Security — implementing custom authentication, role-based access control and OAuth2 login required a significant amount of research and problem solving. Despite the difficulty, it was the part I found most rewarding, as it gave me a deep understanding of how real-world authentication systems are built and secured.
