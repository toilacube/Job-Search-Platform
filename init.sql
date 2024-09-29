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
                       "resumeUrl" VARCHAR(255)
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
    (1, 'Software Engineer', 'Develop and maintain web applications', 'Tech Corp', 'New York, NY', 120000.00, 'Full-time', false),
    (1, 'Data Analyst', 'Analyze data and provide insights for business decisions', 'Data Solutions', 'San Francisco, CA', 90000.00, 'Full-time', false),
    (1, 'Project Manager', 'Manage project timelines and ensure client satisfaction', 'Consulting Inc', 'Chicago, IL', 110000.00, 'Full-time', false);


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
