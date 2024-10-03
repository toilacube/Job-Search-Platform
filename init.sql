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
                        "expiryDate" timestamp,
                       "isDeleted" boolean DEFAULT false,
                       FOREIGN KEY ("recruiterId") REFERENCES users(id) ON DELETE CASCADE
);

-- Create savedJobs table
create sequence savedJobs_id_seq start with 1 increment by 1;
CREATE table "savedJobs" (
    "id"INT PRIMARY KEY DEFAULT nextval('savedJobs_id_seq'),
    "userId" int,
    "jobId" int,
    "isApplied" boolean default false,  
    foreign key ("userId") references users(id) on delete cascade,
    foreign key ("jobId") references "job"(id) on delete cascade
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
INSERT INTO "job" ("recruiterId", "jobTitle", description, "companyName", location, salary, "jobType", "isDeleted", "expiryDate")
VALUES
    (1, 'Software Engineer', 'Current students pursuing Bachelor ''s degree program or above who are in 3rd or 4th year or fresh graduates. Had excellent knowledge of Software Engineering fundamentals (Data Structure, Algorithms). Experience with one or more programming languages (e.g., Java, C/C++, C#, Objective-C, Python, JavaScript, Go, etc.).', 'Tech Corp', 'New York, NY', 120000.00, 'Full-time', false, '2024-11-01 00:00:00'),
    (1, 'Data Analyst', 'Analyze data and provide insights for business decisions', 'Data Solutions', 'San Francisco, CA', 90000.00, 'Full-time', false, '2024-10-31 00:00:00'),
    (1, 'Project Manager', 'Manage project timelines and ensure client satisfaction', 'Consulting Inc', 'Chicago, IL', 110000.00, 'Full-time', false, '2024-11-15 00:00:00');

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


CREATE SEQUENCE company_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE company (
    id INT PRIMARY KEY DEFAULT nextval('company_id_seq'),
    name VARCHAR(255) UNIQUE NOT NULL
);
CREATE SEQUENCE location_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE location (
    id INT PRIMARY KEY DEFAULT nextval('location_id_seq'),
    province VARCHAR(255) UNIQUE NOT NULL
);
CREATE SEQUENCE jobTag_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE jobTags (
    id INT PRIMARY KEY DEFAULT nextval('jobTag_id_seq'),
    tag VARCHAR(50) UNIQUE NOT NULL
);
-- First, drop the old columns
ALTER TABLE "job" 
    DROP COLUMN "companyName",
    DROP COLUMN location,
    DROP COLUMN "jobType";

-- Then, add foreign key references to the new tables
ALTER TABLE "job" 
    ADD COLUMN "companyId" INT,
    ADD COLUMN "locationId" INT,
    ADD COLUMN "jobTagId" INT,
    ADD FOREIGN KEY ("companyId") REFERENCES company(id) ON DELETE CASCADE,
    ADD FOREIGN KEY ("locationId") REFERENCES location(id) ON DELETE SET NULL,
    ADD FOREIGN KEY ("jobTagId") REFERENCES jobTags(id) ON DELETE SET NULL;
INSERT INTO company (name) VALUES
    ('Tech Corp'),
    ('Data Solutions'),
    ('Consulting Inc');
INSERT INTO location (province) VALUES
    ('New York'),
    ('California'),
    ('Illinois');
INSERT INTO jobTags (tag) VALUES
    ('FE'),   -- Front-end
    ('BE'),   -- Back-end
    ('QC');   -- Quality Control
-- Example update queries for existing jobs

UPDATE "job" 
SET "companyId" = (SELECT id FROM company WHERE name = 'Tech Corp'), 
    "locationId" = (SELECT id FROM location WHERE province = 'New York'), 
    "jobTagId" = (SELECT id FROM jobTags WHERE tag = 'FE')
WHERE id = 1;  -- For job 1

UPDATE "job" 
SET "companyId" = (SELECT id FROM company WHERE name = 'Data Solutions'), 
    "locationId" = (SELECT id FROM location WHERE province = 'California'), 
    "jobTagId" = (SELECT id FROM jobTags WHERE tag = 'BE')
WHERE id = 2;  -- For job 2

UPDATE "job" 
SET "companyId" = (SELECT id FROM company WHERE name = 'Consulting Inc'), 
    "locationId" = (SELECT id FROM location WHERE province = 'Illinois'), 
    "jobTagId" = (SELECT id FROM jobTags WHERE tag = 'QC')
WHERE id = 3;  -- For job 3
CREATE SEQUENCE jobSubscription_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE jobSubscriptions (
    id INT PRIMARY KEY DEFAULT nextval('jobSubscription_id_seq'),
    "userId" INT NOT NULL,
    "locationIds" INT[] DEFAULT '{}',  -- Array of location IDs
    "jobTagIds" INT[] DEFAULT '{}',    -- Array of job tag IDs
    "companyIds" INT[] DEFAULT '{}',   -- Array of company IDs
    FOREIGN KEY ("userId") REFERENCES users(id) ON DELETE CASCADE
);
INSERT INTO jobSubscriptions ("userId", "locationIds", "jobTagIds", "companyIds")
VALUES (1, ARRAY[1, 2], ARRAY[1, 2], ARRAY[1, 2]);
CREATE SEQUENCE jobNotification_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE jobNotifications (
    id INT PRIMARY KEY DEFAULT nextval('jobNotification_id_seq'),
    "userId" INT NOT NULL,
    "jobId" INT NOT NULL,
    FOREIGN KEY ("userId") REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY ("jobId") REFERENCES "job"(id) ON DELETE CASCADE
);