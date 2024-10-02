-- Create sequences for tables

-- Sequence for users table
CREATE SEQUENCE users_id_seq START WITH 1 INCREMENT BY 1;

-- Sequence for roles table
CREATE SEQUENCE roles_id_seq START WITH 1 INCREMENT BY 1;

-- Sequence for usersRoles table
CREATE SEQUENCE usersRoles_id_seq START WITH 1 INCREMENT BY 1;

-- Sequence for job table
CREATE SEQUENCE job_id_seq START WITH 1 INCREMENT BY 1;

-- Sequence for jobApplications table
CREATE SEQUENCE jobApplications_id_seq START WITH 1 INCREMENT BY 1;

-- Create users table
CREATE TABLE users (
                       id INT PRIMARY KEY DEFAULT nextval('users_id_seq'),
                       email VARCHAR(255) UNIQUE NOT NULL,
                       "passwordHash" VARCHAR(255) NOT NULL,  -- Storing the hash of the password
                       name VARCHAR(255) NOT NULL,
                       "contactInfo" VARCHAR(255),
                       "resumeUrl" VARCHAR(255),
                        "is2FA" boolean default false
);

-- Create roles table
CREATE TABLE roles (
                       id INT PRIMARY KEY DEFAULT nextval('roles_id_seq'),
                       name VARCHAR(255)
);

-- Create usersRoles table
CREATE TABLE "usersRoles" (
                              id INT PRIMARY KEY DEFAULT nextval('usersRoles_id_seq'),
                              "userId" INT NOT NULL,
                              "roleId" INT NOT NULL,
                              FOREIGN KEY ("userId") REFERENCES users(id) ON DELETE CASCADE,
                              FOREIGN KEY ("roleId") REFERENCES roles(id) ON DELETE CASCADE
);

-- CREATE SEQUENCE skills_id_seq START WITH 1 INCREMENT BY 1;
create table skills (
    id serial primary key,
    name varchar(255)
);

CREATE SEQUENCE usersSkills_id_seq START WITH 1 INCREMENT BY 1;

create table "usersSkills" (
    id int primary key default nextval('usersSkills_id_seq'),
    "userId" int,
    "skillId" int,
    FOREIGN KEY ("userId") REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY ("skillId") REFERENCES skills(id) ON DELETE CASCADE
);

-- Create job table
CREATE TABLE "job" (
                       id INT PRIMARY KEY DEFAULT nextval('job_id_seq'),
                       "recruiterId" INT NOT NULL,  -- Recruiter who posted the job
                       "jobTitle" VARCHAR(255) NOT NULL,
                       description TEXT NOT NULL,
                       "companyName" VARCHAR(255) NOT NULL,
                       location VARCHAR(255),
                       salary DECIMAL(15, 2),
                       "jobType" VARCHAR(50),  -- Full-time or part-time
                       "isDeleted" boolean DEFAULT false,
                       FOREIGN KEY ("recruiterId") REFERENCES users(id) ON DELETE CASCADE
);

-- Create jobApplications table
CREATE TABLE "jobApplications" (
                                   id INT PRIMARY KEY DEFAULT nextval('jobApplications_id_seq'),
                                   "userId" INT NOT NULL,  -- Applicant's user ID
                                   "jobId" INT NOT NULL,  -- ID of the job listing
                                   "coverLetter" TEXT,  -- Optional cover letter
                                   "resumeUrl" VARCHAR(255),  -- URL to the resume that was uploaded
                                   "appliedAt" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY ("userId") REFERENCES users(id) ON DELETE CASCADE,
                                   FOREIGN KEY ("jobId") REFERENCES "job"(id) ON DELETE CASCADE
);

CREATE SEQUENCE appCmt_id_seq START WITH 1 INCREMENT BY 1;

create table "appCmt" (
    id int primary key default nextval('appCmt_id_seq'),
    "applicationId" int,
    "comment" text,
    foreign key ("applicationId") references "jobApplications"(id)
);

-- Insert initial users
INSERT INTO users (email, "passwordHash", name, "contactInfo")
VALUES
    ('admin@gmail.com', '$2a$10$i4KuMgCDm2AjfBYF9hoAJ.sc59m93IUU6Ic/VwccIOFBch3hx2k9y', 'aaaaaaaaa', '66669999'),
    ('user@gmail.com', '$2a$10$i4KuMgCDm2AjfBYF9hoAJ.sc59m93IUU6Ic/VwccIOFBch3hx2k9y', 'zzzzzzz', '66669999');

-- Insert initial roles
INSERT INTO roles (name) VALUES ('ADMIN'), ('USER');

-- Insert initial user roles
INSERT INTO "usersRoles" ("userId", "roleId") VALUES (1, 1), (2, 2), (1, 2);

-- Insert initial jobs
INSERT INTO "job" ("recruiterId", "jobTitle", description, "companyName", location, salary, "jobType", "isDeleted")
VALUES
    (1, 'Software Engineer', 'Current students pursuing Bachelor ''s degree program or above who are in 3rd or 4th year or fresh graduates. Had excellent knowledge of Software Engineering fundamentals (Data Structure, Algorithms). Experience with one or more programming languages (e.g., Java, C/C++, C#, Objective-C, Python, JavaScript, Go, etc.).', 'Tech Corp', 'New York, NY', 120000.00, 'Full-time', false),
    (1, 'Data Analyst', 'Analyze data and provide insights for business decisions', 'Data Solutions', 'San Francisco, CA', 90000.00, 'Full-time', false),
    (1, 'Project Manager', 'Manage project timelines and ensure client satisfaction', 'Consulting Inc', 'Chicago, IL', 110000.00, 'Full-time', false);

-- Insert some skills
INSERT INTO skills (name) VALUES
                              ('Java'),
                              ('Python'),
                              ('SQL'),
                              ('JavaScript'),
                              ('Backend'),
                              ('OOP'),
                              ('Web development'),
                              ('React');

-- Insert data into usersSkills for userId 1
INSERT INTO "usersSkills" ("userId", "skillId") VALUES
                                                    (1, 1),  -- User 1 knows Java
                                                    (1, 2),  -- User 1 knows Python
                                                    (1, 3);  -- User 1 knows SQL

-- Insert data into usersSkills for userId 2
INSERT INTO "usersSkills" ("userId", "skillId") VALUES
                                                    (2, 2),  -- User 2 knows Python
                                                    (2, 4),  -- User 2 knows JavaScript
                                                    (2, 5);  -- User 2 knows React


CREATE INDEX idx_job_description ON job USING GIN (to_tsvector('english', description));


-- Update sequence for users table
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

-- Update sequence for roles table
SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles));

-- Update sequence for usersRoles table
SELECT setval('usersRoles_id_seq', (SELECT MAX(id) FROM "usersRoles"));

-- Update sequence for job table
SELECT setval('"job_id_seq"', (SELECT MAX(id) FROM "job"));

-- Update sequence for jobApplications table
SELECT setval('jobApplications_id_seq', (SELECT MAX(id) FROM "jobApplications"));

SELECT setval('usersSkills_id_seq', (SELECT MAX(id) FROM "usersSkills"));
