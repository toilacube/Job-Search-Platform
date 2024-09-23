CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       "passwordHash" VARCHAR(255) NOT NULL,  -- Storing the hash of the password
                       name VARCHAR(255) NOT NULL,
                       "contactInfo" VARCHAR(255)
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



