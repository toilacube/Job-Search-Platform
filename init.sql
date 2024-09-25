CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       "passwordHash" VARCHAR(255) NOT NULL,  -- Storing the hash of the password
                       name VARCHAR(255) NOT NULL,
                       "contactInfo" VARCHAR(255),
                        "resumeUrl" VARCHAR(255)
);

-- create table resumes(
--     id SERIAL PRIMARY KEY,
--     url VARCHAR(255)
-- )

create table roles (
    id serial primary key,
    name varchar(255)
);

create table "usersRoles"(
    id serial primary key,
    "userId" int not null,
    "roleId" int not null,
    foreign key ("userId") references users(id) on delete cascade,
    foreign key ("roleId") references roles(id) on delete cascade
);


CREATE TABLE "jobListings" (
                             id SERIAL PRIMARY KEY,
                             "recruiterId" INT NOT NULL,  -- Recruiter who posted the job
                             "jobTitle" VARCHAR(255) NOT NULL,
                             description TEXT NOT NULL,
                             "companyName" VARCHAR(255) NOT NULL,
                             location VARCHAR(255),
                             salary DECIMAL(15, 2),
                             "jobType" VARCHAR(50),  -- Full-time or part-time
                             "isDeleted" boolean,
                             FOREIGN KEY ("recruiterId") REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE "jobApplications" (
                                 id SERIAL PRIMARY KEY,
                                 "userId" INT NOT NULL,  -- Applicant's user ID
                                 "jobId" INT NOT NULL,  -- ID of the job listing
                                 "coverLetter" TEXT,  -- Optional cover letter
                                 "resumeUrl" VARCHAR(255),  -- URL to the resume that was uploaded
                                 "appliedAt" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY ("userId") REFERENCES users(id) ON DELETE CASCADE,
                                 FOREIGN KEY ("jobId") REFERENCES "jobListings"(id) ON DELETE CASCADE
);

insert into users (id, email, "passwordHash", name, "contactInfo")
values (1, 'string@gmail.com', '$2a$10$i4KuMgCDm2AjfBYF9hoAJ.sc59m93IUU6Ic/VwccIOFBch3hx2k9y', 'toilacube', '66669999');



