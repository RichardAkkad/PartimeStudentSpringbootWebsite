
# Website for part time students
Html | Css | Spring Boot | AWS EC2 S3 | PostGre

As a part-time evening student at Birkbeck University, I felt disconnected from the traditional university experience, particularly when it came to finding accommodation and meeting people on the same course. To solve this, I independently designed and built a full-stack web application to help students find compatible housemates and accommodation.

The core features allows students to search free availability slots, search for others with similar budgets, courses that they can potentially live with, with results filtered to surface the most compatible matches. Students can then contact those people directly to arrange viewings of available accommodation, as well as allowing students to track their progress.

The application includes full CRUD functionality — students can add (including uploading photos), update, and delete their profiles — strong Spring Security protocols including custom authentication and role-based access control to restrict sensitive operations as well as oauth2 login. Student profile images and data are stored using AWS, with the project deployed on an AWS EC2 instance.

Live Demo: http://ec2-13-40-31-169.eu-west-2.compute.amazonaws.com:5000

Login credentials — for Student access Username: tony and Password: tony and for Employee access Username: james and Password: james, or can just login using google or github.
