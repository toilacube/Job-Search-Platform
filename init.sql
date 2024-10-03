-- Sequence creation
CREATE SEQUENCE users_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE roles_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE usersRoles_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE job_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE jobApplications_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE usersSkills_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE savedJobs_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE appCmt_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE company_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE location_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE jobTag_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE jobSubscription_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE jobNotification_id_seq START WITH 1 INCREMENT BY 1;

-- Create users table
CREATE TABLE users (
                       id INT PRIMARY KEY DEFAULT nextval('users_id_seq'),
                       email VARCHAR(255) UNIQUE NOT NULL,
                       "passwordHash" VARCHAR(255) NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       "contactInfo" VARCHAR(255),
                       "resumeUrl" VARCHAR(255),
                       "is2FA" BOOLEAN DEFAULT FALSE
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

-- Create skills table
CREATE TABLE skills (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255)
);

-- Create usersSkills table
CREATE TABLE "usersSkills" (
                               id INT PRIMARY KEY DEFAULT nextval('usersSkills_id_seq'),
                               "userId" INT,
                               "skillId" INT,
                               FOREIGN KEY ("userId") REFERENCES users(id) ON DELETE CASCADE,
                               FOREIGN KEY ("skillId") REFERENCES skills(id) ON DELETE CASCADE
);


-- Create company table
CREATE TABLE company (
                         id INT PRIMARY KEY DEFAULT nextval('company_id_seq'),
                         name VARCHAR(255) UNIQUE NOT NULL
);

-- Create location table
CREATE TABLE location (
                          id INT PRIMARY KEY DEFAULT nextval('location_id_seq'),
                          province VARCHAR(255) UNIQUE NOT NULL
);


-- Create jobTags table
CREATE TABLE jobTags (
                         id INT PRIMARY KEY DEFAULT nextval('jobTag_id_seq'),
                         tag VARCHAR(50) UNIQUE NOT NULL
);


-- Create job table
CREATE TABLE "job" (
                       id INT PRIMARY KEY DEFAULT nextval('job_id_seq'),
                       "recruiterId" INT NOT NULL,
                       "jobTitle" VARCHAR(255) NOT NULL,
                       description TEXT NOT NULL,
                       "companyId" INT,
                        "location" varchar(255), -- this should be address but im lazy
                       "locationId" INT,
                       salary DECIMAL(15, 2),
                       "jobType" VARCHAR(50),
                       "expiryDate" TIMESTAMP,
                       "isDeleted" BOOLEAN DEFAULT FALSE,
                       "jobTags" INT[] DEFAULT '{}',
                       FOREIGN KEY ("recruiterId") REFERENCES users(id) ON DELETE CASCADE,
                       FOREIGN KEY ("companyId") REFERENCES company(id) ON DELETE CASCADE,
                       FOREIGN KEY ("locationId") REFERENCES location(id) ON DELETE SET NULL
);

-- Create savedJobs table
CREATE TABLE "savedJobs" (
                             id INT PRIMARY KEY DEFAULT nextval('savedJobs_id_seq'),
                             "userId" INT,
                             "jobId" INT,
                             "isApplied" BOOLEAN DEFAULT FALSE,
                             FOREIGN KEY ("userId") REFERENCES users(id) ON DELETE CASCADE,
                             FOREIGN KEY ("jobId") REFERENCES "job"(id) ON DELETE CASCADE
);

-- Create jobApplications table
CREATE TABLE "jobApplications" (
                                   id INT PRIMARY KEY DEFAULT nextval('jobApplications_id_seq'),
                                   "userId" INT NOT NULL,
                                   "jobId" INT NOT NULL,
                                   "coverLetter" TEXT,
                                   "resumeUrl" VARCHAR(255),
                                   "appliedAt" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY ("userId") REFERENCES users(id) ON DELETE CASCADE,
                                   FOREIGN KEY ("jobId") REFERENCES "job"(id) ON DELETE CASCADE
);

-- Create appCmt table
CREATE TABLE "appCmt" (
                          id INT PRIMARY KEY DEFAULT nextval('appCmt_id_seq'),
                          "applicationId" INT,
                          "comment" TEXT,
                          FOREIGN KEY ("applicationId") REFERENCES "jobApplications"(id)
);


-- Create jobSubscriptions table
CREATE TABLE jobSubscriptions (
                                  id INT PRIMARY KEY DEFAULT nextval('jobSubscription_id_seq'),
                                  "userId" INT NOT NULL,
                                  "locationIds" INT[] DEFAULT '{}',
                                  "jobTagIds" INT[] DEFAULT '{}',
                                  "companyIds" INT[] DEFAULT '{}',
                                  FOREIGN KEY ("userId") REFERENCES users(id) ON DELETE CASCADE
);

-- Create jobNotifications table
CREATE TABLE jobNotifications (
                                  id INT PRIMARY KEY DEFAULT nextval('jobNotification_id_seq'),
                                  "userId" INT NOT NULL,
                                  "jobIds" INT[] NOT NULL,
                                  FOREIGN KEY ("userId") REFERENCES users(id) ON DELETE CASCADE
);

-- Insert initial data into users table
INSERT INTO users (email, "passwordHash", name, "contactInfo") VALUES
                                                                   ('admin@gmail.com', '$2a$10$i4KuMgCDm2AjfBYF9hoAJ.sc59m93IUU6Ic/VwccIOFBch3hx2k9y', 'aaaaaaaaa', '66669999'),
                                                                   ('user@gmail.com', '$2a$10$i4KuMgCDm2AjfBYF9hoAJ.sc59m93IUU6Ic/VwccIOFBch3hx2k9y', 'zzzzzzz', '66669999'),
                                                                   ('nguyensylehoang@gmail.com', '$2a$10$i4KuMgCDm2AjfBYF9hoAJ.sc59m93IUU6Ic/VwccIOFBch3hx2k9y', 'zzzzzzz', '66669999');

-- Insert initial data into roles table
INSERT INTO roles (name) VALUES ('ADMIN'), ('USER');

-- Insert initial data into usersRoles table
INSERT INTO "usersRoles" ("userId", "roleId") VALUES (1, 1), (2, 2), (1, 2);

-- Insert additional companies
INSERT INTO company (name)
VALUES
    ('Innovative Tech Solutions'),
    ('Global Enterprises'),
    ('FinTech World'),
    ('Creative Design Studio'),
    ('Logistics Masters');
-- Insert additional locations
INSERT INTO location (province)
VALUES
    ('Texas'),
    ('Florida'),
    ('Washington'),
    ('Massachusetts'),
    ('Nevada');
-- Insert additional job tags
INSERT INTO jobTags (tag)
VALUES
    ('DevOps'),
    ('UI/UX'),
    ('Data Science'),
    ('Cloud Computing'),
    ('Cybersecurity');


-- Insert new jobs using the newly added companies, locations, and jobTags, with the updated location (address)
INSERT INTO "job" ("recruiterId", "jobTitle", description, "companyId", "location", "locationId", salary, "jobType", "isDeleted", "expiryDate", "jobTags")
VALUES
    (1, 'DevOps Engineer',
     'Responsible for managing cloud infrastructure and automating software deployment pipelines.',
     (SELECT id FROM company WHERE name = 'Innovative Tech Solutions'),
     '1234 Cloud St, Austin, TX',  -- Address for job
     (SELECT id FROM location WHERE province = 'Texas'),
     130000.00, 'Full-time', FALSE, '2024-12-01 00:00:00',
     ARRAY[(SELECT id FROM jobTags WHERE tag = 'DevOps'), (SELECT id FROM jobTags WHERE tag = 'Cloud Computing')]
    ),
    (1, 'UI/UX Designer',
     'Design intuitive and visually appealing interfaces for mobile and web applications.',
     (SELECT id FROM company WHERE name = 'Global Enterprises'),
     '5678 Design Rd, Miami, FL',  -- Address for job
     (SELECT id FROM location WHERE province = 'Florida'),
     85000.00, 'Full-time', FALSE, '2024-11-20 00:00:00',
     ARRAY[(SELECT id FROM jobTags WHERE tag = 'UI/UX')]
    ),
    (1, 'Data Scientist',
     'Analyze large datasets to uncover insights and build predictive models.',
     (SELECT id FROM company WHERE name = 'FinTech World'),
     '9876 Analytics Blvd, Seattle, WA',  -- Address for job
     (SELECT id FROM location WHERE province = 'Washington'),
     145000.00, 'Full-time', FALSE, '2024-12-10 00:00:00',
     ARRAY[(SELECT id FROM jobTags WHERE tag = 'Data Science')]
    ),
    (1, 'Cloud Architect',
     'Design and implement scalable cloud solutions for enterprise clients.',
     (SELECT id FROM company WHERE name = 'Creative Design Studio'),
     '5432 Cloud St, Boston, MA',  -- Address for job
     (SELECT id FROM location WHERE province = 'Massachusetts'),
     160000.00, 'Full-time', FALSE, '2024-11-30 00:00:00',
     ARRAY[(SELECT id FROM jobTags WHERE tag = 'Cloud Computing'), (SELECT id FROM jobTags WHERE tag = 'Cybersecurity')]
    ),
    (1, 'Cybersecurity Analyst',
     'Monitor and secure network systems to protect sensitive data from cyber threats.',
     (SELECT id FROM company WHERE name = 'Logistics Masters'),
     '4321 Security Ln, Las Vegas, NV',  -- Address for job
     (SELECT id FROM location WHERE province = 'Nevada'),
     115000.00, 'Full-time', FALSE, '2024-12-15 00:00:00',
     ARRAY[(SELECT id FROM jobTags WHERE tag = 'Cybersecurity')]
    );


-- Insert initial skills
INSERT INTO skills (name) VALUES ('Java'), ('Python'), ('SQL'), ('JavaScript'), ('Backend'), ('OOP'), ('Web development'), ('React');

-- Insert data into usersSkills
INSERT INTO "usersSkills" ("userId", "skillId") VALUES (1, 1), (1, 2), (1, 3), (2, 2), (2, 4), (2, 5);

-- Update sequence for users table
SELECT setval('users_id_seq', (SELECT COALESCE(MAX(id), 1) FROM users));

-- Update sequence for roles table
SELECT setval('roles_id_seq', (SELECT COALESCE(MAX(id), 1) FROM roles));

-- Update sequence for usersRoles table
SELECT setval('usersRoles_id_seq', (SELECT COALESCE(MAX(id), 1) FROM "usersRoles"));

-- Update sequence for skills table (handled automatically since it's serial)

-- Update sequence for usersSkills table
SELECT setval('usersSkills_id_seq', (SELECT COALESCE(MAX(id), 1) FROM "usersSkills"));

-- Update sequence for job table
SELECT setval('job_id_seq', (SELECT COALESCE(MAX(id), 1) FROM "job"));

-- Update sequence for jobApplications table
SELECT setval('jobApplications_id_seq', (SELECT COALESCE(MAX(id), 1) FROM "jobApplications"));

-- Update sequence for appCmt table
SELECT setval('appCmt_id_seq', (SELECT COALESCE(MAX(id), 1) FROM "appCmt"));

-- Update sequence for company table
SELECT setval('company_id_seq', (SELECT COALESCE(MAX(id), 1) FROM company));

-- Update sequence for location table
SELECT setval('location_id_seq', (SELECT COALESCE(MAX(id), 1) FROM location));

-- Update sequence for jobTags table
SELECT setval('jobTag_id_seq', (SELECT COALESCE(MAX(id), 1) FROM jobTags));

-- Update sequence for jobSubscriptions table
SELECT setval('jobSubscription_id_seq', (SELECT COALESCE(MAX(id), 1) FROM jobSubscriptions));

-- Update sequence for jobNotifications table
SELECT setval('jobNotification_id_seq', (SELECT COALESCE(MAX(id), 1) FROM jobNotifications));

-- Update sequence for savedJobs table
SELECT setval('savedJobs_id_seq', (SELECT COALESCE(MAX(id), 1) FROM "savedJobs"));
